package com.bird.demo.demo1;


import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.generated.StormTopology;
import org.apache.storm.topology.TopologyBuilder;

/**
 * @Author 李璞
 * @Date 2022/2/16 9:17
 * @Description
 */
public class Topology {
    public static void main(String[] args) throws Exception {
        //创建Topology
        TopologyBuilder builder = new TopologyBuilder();
        //设置Spout 每个worker一个executor运行 每个executor一个task
        builder.setSpout("spout", new Spout(), 1).setNumTasks(1);
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

