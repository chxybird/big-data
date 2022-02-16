# Storm入门案例--小鸟程序员

### 一、前言

在进行案例学习之前，你需要掌握有关Storm相关的基础知识，包含Storm的架构、工作原理、等等知识，否则你将无法开展后续操作。

### 二、需求

读取一个文件，将文件中的每一行的句子进行切分得到若干单词，统计这些单词的的个数。下面为文件input.txt的具体内容。

```
may you have enough happiness to make you sweet
enough trials to make you strong
enough sorrow to keep you human
enough hope to make you happy
always put yourself in others shoes
if you feel that it hurts you
it probably hurts the other person too
```

### 三、案例实现

1.导入maven依赖

```xml
<dependencies>
    <!-- Storm依赖 -->
    <dependency>
        <groupId>org.apache.storm</groupId>
        <artifactId>storm-core</artifactId>
        <version>2.3.0</version>
    </dependency>
    <!-- Storm本地运行需要此依赖 -->
    <dependency>
        <groupId>com.codahale.metrics</groupId>
        <artifactId>metrics-core</artifactId>
        <version>3.0.2</version>
    </dependency>
</dependencies>


<!-- Storm程序使用此插件打包 -->
<build>
    <plugins>
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-shade-plugin</artifactId>
            <version>2.4.1</version>
            <executions>
                <execution>
                    <phase>package</phase>
                    <goals>
                        <goal>shade</goal>
                    </goals>
                </execution>
            </executions>
        </plugin>
    </plugins>
</build>
```

2.Spout编写

```JAVA
public class Spout extends BaseRichSpout {
    private SpoutOutputCollector collector;
    private BufferedReader bufferedReader;

    @Override
    public void open(Map<String, Object> map, TopologyContext topologyContext, SpoutOutputCollector spoutOutputCollector) {
        this.collector = spoutOutputCollector;
        try {
            //读取文件到流中
            InputStream inputStream = new FileInputStream(new File("F:\\log", "input.txt"));
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void nextTuple() {
        try {
            String temp;
            //循环读取 每读取一行都会发射一个元组信息
            while ((temp = bufferedReader.readLine()) != null) {
                //发射的值对应下面定义的字段名称
                collector.emit(new Values(temp));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
        //设置发射元组信息的字段格式
        outputFieldsDeclarer.declare(new Fields("sentence"));
    }
}
```

编写Spout其实就是实现Storm提供的一些接口或者抽象类，在此案例中使用的是BaseRichSpout，其实Storm有提供更上层的Spout，该Spout帮助你忽略了一些他的生命周期方法。使你关注核心的生命周期方法来进行实现。我们需要关注的方法是open、nextTuple、declareOutputFields。open方法是初始化方法，调用且仅调用一次。nextTuple是发射元组的方法，这个方法的理念就是源源不断的发送数据给Bolt进行处理。declareOutputFields方法就是规定发射出的的元组的格式。在该案例中，读取文件每一行进行发送，字段key为sentence，值为读取的文件中的一行的值。

3.Bolt编写

```JAVA
public class Bolt extends BaseBasicBolt {
    /**
     * 记录单词的个数
     */
    private Integer count = 0;
    
    @Override
    public void execute(Tuple input, BasicOutputCollector collector) {
        //该方法每有一个tuple经过都会被调用一次
        //获取tuple的数据
        String sentence = input.getStringByField("sentence");
        //切割句子为单词
        String[] words = sentence.split(" ");
        int length = words.length;
        count = count + length;
        System.out.println("当前单词总数为:" + count);
        //可以将处理的数据进行数据输出或者传输到下一个Bolt中 这里方便演示直接打印
    }
    
    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
        //如果你需要将数据进行下一个Bolt发送,可以在这里定义格式,原理等同于Spout
    }
}
```

Bolt编程和Spout是相同的道理，使用更加方便的BaseBasicBolt来进行编程。该Bolt的逻辑就是接受Spout发送的字段为sentence的元组，将元组中的内容(文件中的一行)进行切割统计数量进行累加。

4.Topology编写

```JAVA
public class Topology {
    public static void main(String[] args) throws Exception {
        //创建Topology
        TopologyBuilder builder = new TopologyBuilder();
        //设置Spout 每个worker一个executor运行 每个executor一个task
        builder.setSpout("spout", new Spout(), 1).setNumTasks(1);
        //设置Bolt 每个worker一个executor运行 每个executor一个task
        builder.setBolt("bolt", new Bolt(), 1).setNumTasks(1)
                .shuffleGrouping("spout");
        StormTopology topology = builder.createTopology();
        Config config = new Config();
        //配置worker的数量
        config.setNumWorkers(2);
        //集群提交
//        StormSubmitter.submitTopology("topology",config,topology);
        //本地提交 用于测试
        LocalCluster local = new LocalCluster();
        local.submitTopology("topology", config, topology);
    }
}
```

Topology就是来设置Spout、Bolt整个拓扑关系的入口。这里使用本地提交方便测试。如果你要提交成集群你需要修改maven的storm-core依赖的scope为provider，打包上传到服务器执行相关Storm命令运行程序。

### 四、思考与补充

上面的案例存在线程问题嘛？就上面的案例来说不会产生线程安全的问题，因为设置的并行度是1，所以不存在线程安全的问题。但是在Storm中并行度是一个重要的概念以及特性。在多并行度会存在线程安全的问题。

### 五、Storm的并行度

接下来看一段代码来解读一下Storm的并行度。在这里你将理解Storm中的worker、executors、task三者之间的关系以及完整的Topolohy下并行运行的过程。

```java
public class Topology {
    public static void main(String[] args) throws Exception {
        //创建Topology
        TopologyBuilder builder = new TopologyBuilder();
        //设置Spout 每个worker一个executor运行 每个executor一个task
        builder.setSpout("spout", new Spout(), 2).setNumTasks(4);
        //设置Bolt 每个worker一个executor运行 每个executor一个task
        builder.setBolt("bolt", new Bolt(), 20).setNumTasks(20)
                .shuffleGrouping("spout");
        StormTopology topology = builder.createTopology();
        Config config = new Config();
        //配置worker的数量
        config.setNumWorkers(2);
        //集群提交
//        StormSubmitter.submitTopology("topology",config,topology);
        //本地提交 用于测试
        LocalCluster local = new LocalCluster();
        local.submitTopology("topology", config, topology);
    }
}
```

