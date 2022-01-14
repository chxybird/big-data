package com.bird.topology.filelog;

import org.apache.storm.Config;
import org.apache.storm.StormSubmitter;
import org.apache.storm.generated.StormTopology;
import org.apache.storm.topology.TopologyBuilder;

/**
 * @Author 李璞
 * @Date 2022/1/14 16:08
 * @Description 一个Topology就是Storm的一个完整的任务
 */
public class Topology {

    public static void main(String[] args) throws Exception {
        //创建Topology
        TopologyBuilder builder=new TopologyBuilder();
        //设置Spout 1个worker运行
        builder.setSpout("file-log-spout",new Spout(),1);
        //设置Bolt 指定Spout、随机分组策略 1个worker运行
        builder.setBolt("file-log-bolt",new Bolt(),1).shuffleGrouping("file-log-spout");
        StormTopology topology = builder.createTopology();
        //配置worker的开启个数
        Config config = new Config();
        config.setNumWorkers(2);
        //集群提交
        StormSubmitter.submitTopology("file-log-topology",config,topology);
    }

}
