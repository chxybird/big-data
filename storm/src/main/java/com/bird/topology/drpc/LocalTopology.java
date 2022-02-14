package com.bird.topology.drpc;

import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.LocalDRPC;
import org.apache.storm.StormSubmitter;
import org.apache.storm.drpc.DRPCSpout;
import org.apache.storm.drpc.LinearDRPCTopologyBuilder;
import org.apache.storm.drpc.ReturnResults;
import org.apache.storm.topology.TopologyBuilder;

/**
 * @Author 李璞
 * @Date 2022/2/14 9:14
 * @Description 本地模式 DRPC 用于调试
 */
public class LocalTopology {
    public static void main(String[] args) throws Exception {
        //方式一
        LinearDRPCTopologyBuilder builder = new LinearDRPCTopologyBuilder("bird");
        builder.addBolt(new Bolt(), 1);
        Config config = new Config();
        config.setNumWorkers(2);
        //本地提交用于测试
        LocalDRPC drpc = new LocalDRPC();
        LocalCluster cluster = new LocalCluster();
        cluster.submitTopology("drpc", config, builder.createLocalTopology(drpc));
        String execute = drpc.execute("bird", "Bird-Storm");
        System.out.println(execute);



        //方式二
//        Config config=new Config();
//        LocalCluster cluster=new LocalCluster();
//        LocalDRPC drpc=new LocalDRPC();
//        TopologyBuilder builder = new TopologyBuilder();
//        DRPCSpout drpcSpout = new DRPCSpout("bird",drpc);
//        builder.setSpout("drpc-spout", drpcSpout);
//        builder.setBolt("bolt", new CustomerBolt()).shuffleGrouping("drpc-spout");
//        builder.setBolt("return-result",new ReturnResults()).shuffleGrouping("bolt");
//        cluster.submitTopology("drpc",config,builder.createTopology());
//        String execute = drpc.execute("bird", "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
//        System.out.println(execute);
    }
}
