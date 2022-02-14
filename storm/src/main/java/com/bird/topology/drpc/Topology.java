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
        /**
         * 使用LinearDRPCTopologyBuilder,对于第一个bolt，其输入为Fields("request", "args");
         * 对最后一个bolt要求输出字段为new Fields("id", "result");
         * 对于非最后一个bolt要求输出字段的第一个字段为id,即requestId,方便CoordinatedBolt进行追踪统计,确认bolt是否成功接收上游bolt发送的所有tuple;
         */
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
