package com.bird.topology.trident;

import com.bird.topology.filelog.Spout;
import com.bird.topology.trident.filter.CustomerFilter;
import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.trident.TridentTopology;
import org.apache.storm.trident.operation.BaseFunction;
import org.apache.storm.trident.operation.TridentCollector;
import org.apache.storm.trident.operation.TridentOperationContext;
import org.apache.storm.trident.testing.FixedBatchSpout;
import org.apache.storm.trident.tuple.TridentTuple;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;

import java.util.Map;
import java.util.UUID;

/**
 * @Author 李璞
 * @Date 2022/2/15 9:47
 * @Description
 */
public class TestTopology {
    public static void main(String[] args) throws Exception {
//        FixedBatchSpout spout = new FixedBatchSpout(new Fields("info"), 1, new Values("张三"),new Values("李四"));
//        spout.setCycle(false);

        Spout spout = new Spout();

        TridentTopology topology =new TridentTopology();
        topology.newStream("trident-stream",spout)
                .parallelismHint(1)
                .shuffle()
                .each(new Fields("log"),new CustomerFilter())
                .parallelismHint(2)
                .shuffle()
                .each(new Fields("log"),new BaseFunction() {
                    private int partition;
                    @Override
                    public void execute(TridentTuple tuple, TridentCollector collector) {
                        System.err.println(partition+"---"+tuple.get(0));
                    }
                    @Override
                    public void prepare(Map<String, Object> conf, TridentOperationContext context) {
                        this.partition = context.getPartitionIndex();
                    }
                }, new Fields("uuid", "user")).parallelismHint(2)
                .shuffle()
//                .each(new Fields("info"))
                .parallelismHint(5);

        Config config = new Config();
        //配置worker的数量
        config.setNumWorkers(4);
        config.setMaxSpoutPending(20);
        config.setMaxTaskParallelism(200);
        LocalCluster local = new LocalCluster();
        local.submitTopology("trident", config, topology.build());
    }
}
