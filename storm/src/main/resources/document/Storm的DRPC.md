# Storm的DRPC--小鸟程序员

### 一、什么是Storm的DRPC

Storm的DPRC可以理解为Storm计算对外提供的接口服务。开发人员可以编写客户端程序调用Storm的DRPC接口传入响应的参数，Storm开始进行计算并返回结果给客户端。

### 二、DRPC的工作流程

DRPC首先有一个DRPC Server,这个服务是Storm自带的服务，通过storm.yml进行配置(所有节点都需要配置)，客户端发送请求到DRPC Server,请求会被服务转发到Topology上进行计算，并返回结果给客户端。整个服务端由DRPC Server,DPRC Spout,Topology和一个ReturnResults四个部分构成。

 Storm自带了一个称作 LinearDRPCTopologyBuilder 的topology builder, 它把实现DRPC的几乎所有步骤都自动化了。

### 三、如何进行DPRC的开发

1.修改storm.yaml配置文件并启动DRPC

```yml
#增加如下内容，集群的话每台服务都需要配置
#STORM的DRPC服务地址 默认端口3772 可以在Storm-UI界面查看相关信息
drpc.servers: ["192.168.78.134"]
```

```shell
#启动Storm集群的DRPC服务
nohup storm drpc > /dev/null >/dev/null 2>&1 &
```

2.DRPC开发概要

编写DRPC程序实际上和写Storm程序类似，整个DRPC程序其实包含了三部分内容，包含一个DRPCSpout,用来接受客户端传递的参数,自定义Bolt用于处理业务逻辑,ReturnResults用于帮助我们返回结果。对于前两者Storm的开发工具包已经帮助我们处理好并提供给我们使用，我们只需要开发一个自定义的Bolt来处理数据即可。

3.编写DRPC计算逻辑(方式一,基本方式)

Bolt

```java
public class CustomerBolt extends BaseBasicBolt {

    @Override
    public void execute(Tuple input, BasicOutputCollector collector) {
        //DRPCSpout传递过来的字段为 “args”,"return-info"
        String info = input.getStringByField("return-info");
        String args = input.getStringByField("args");
        collector.emit(new Values(args,info));
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        //Bolt如果要返回给ReturnResults必须符合格式 "result","return-info" result设置为你返回的字段 return-info不需要更改,原值传入
        declarer.declare(new Fields("result","return-info"));
    }
}
```

Topology

```java
public class Topology {
    public static void main(String[] args) throws Exception {
        Config config=new Config();
        LocalCluster cluster=new LocalCluster();
        LocalDRPC drpc=new LocalDRPC();
        TopologyBuilder builder = new TopologyBuilder();
        DRPCSpout drpcSpout = new DRPCSpout("bird",drpc);
        builder.setSpout("drpc-spout", drpcSpout);
        builder.setBolt("bolt", new CustomerBolt()).shuffleGrouping("drpc-spout");
        builder.setBolt("return-result",new ReturnResults()).shuffleGrouping("bolt");
        cluster.submitTopology("drpc",config,builder.createTopology());
        String execute = drpc.execute("bird", "信息");
        System.out.println(execute);
    }
}
```

4.编写DRPC计算逻辑(便捷方式)

Bolt

```java
public class Bolt extends BaseBasicBolt {
    @Override
    public void execute(Tuple input, BasicOutputCollector collector) {
        Long request = input.getLongByField("request");
        String args = input.getStringByField("args");
        //返回给ReturnResults的字段的id不要跟更改 原值传入request result为计算处理后的结果
        collector.emit(new Values(request,args+"!!!"));
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        //ReturnResults接收的fields为Fields("id", "result")
        declarer.declare(new Fields("id","result"));
    }
}
```

```java
public class Topology {
    public static void main(String[] args) throws Exception {
        LinearDRPCTopologyBuilder builder = new LinearDRPCTopologyBuilder("bird");
        builder.addBolt(new Bolt(), 1);
        Config config = new Config();
        config.setNumWorkers(2);
        //集群提交
        StormSubmitter.submitTopology("dprc", config, builder.createRemoteTopology());
    }
}
```

5.打包并运行Storm的DRPC Topology

```shell
storm jar jar包 Topology主类路径 [参数可选]
```

6.编写客户端连接StormDRPC接口并测试

```java
public class Client {
    public static void main(String[] args) throws Exception {
        Map<String, Object> config = Utils.readDefaultConfig();
        //drpc服务
        DRPCClient client = new DRPCClient(config, "192.168.78.134", 3772);
        /// 调用drpcTest函数，传递参数为hello
        String result = client.execute("bird", "Storm");
        System.out.println(result);
    }
}
```

