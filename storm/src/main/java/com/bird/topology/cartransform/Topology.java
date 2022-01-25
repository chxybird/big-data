package com.bird.topology.cartransform;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.StormSubmitter;
import org.apache.storm.generated.StormTopology;
import org.apache.storm.kafka.spout.ByTopicRecordTranslator;
import org.apache.storm.kafka.spout.KafkaSpout;
import org.apache.storm.kafka.spout.KafkaSpoutConfig;
import org.apache.storm.topology.TopologyBuilder;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;


/**
 * @Author 李璞
 * @Date 2022/1/17 17:04
 * @Description
 */
public class Topology {
    public static void main(String[] args) throws Exception {
        //将Kafka获取的信息转换为内部的tuple
        ByTopicRecordTranslator<String, String> translator = new ByTopicRecordTranslator<>((record) -> new Values(record.topic(), record.key(), record.value()),
                new Fields("topic", "key", "value"), "stream-kafka-record");
        //创建Spout
        KafkaSpoutConfig<String, String> kafkaSpoutConfig = KafkaSpoutConfig
                //设置Kafka集群地址,多个使用,号隔开以及消费的的topic
                .builder("192.168.78.134:9092", "topic-storm")
                //设置消费者的消费参数
                .setProp(ConsumerConfig.GROUP_ID_CONFIG, "storm-group")
                .setRecordTranslator(translator)
                .build();
        KafkaSpout<String, String> kafkaSpout = new KafkaSpout<>(kafkaSpoutConfig);
        TopologyBuilder builder = new TopologyBuilder();
        //设置Spout 每个worker一个executor运行 每个executor一个task
        builder.setSpout("car-spout", kafkaSpout, 1)
                .setNumTasks(1);
        //设置Bolt 每个worker一个executor运行 每个executor一个task
        builder.setBolt("car-bolt", new Bolt(), 1)
                .setNumTasks(1)
                //这里必须指定streamId，因为默认是default，而我们在下面设置了kafkaSpout的streamId
                .shuffleGrouping("car-spout","stream-kafka-record");
        StormTopology topology = builder.createTopology();
        Config config = new Config();
        //配置worker的数量
        config.setNumWorkers(2);
        //集群提交
        StormSubmitter.submitTopology("car-transform-topology", config, topology);
        //本地提交
//        LocalCluster local = new LocalCluster();
//        local.submitTopology("car-transform-topology", config, topology);
    }
}
