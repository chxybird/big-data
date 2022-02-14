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
 * @Date 2022/2/12 9:08
 * @Description
 */
public class Topology {
    public static void main(String[] args) throws Exception {
        LinearDRPCTopologyBuilder builder = new LinearDRPCTopologyBuilder("bird");
        builder.addBolt(new Bolt(), 1);
        Config config = new Config();
        config.setNumWorkers(2);
        //集群提交
        StormSubmitter.submitTopology("dprc", config, builder.createRemoteTopology());


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
