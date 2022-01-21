package com.bird.topology.cartransform;

import org.apache.storm.topology.BasicOutputCollector;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseBasicBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.springframework.kafka.core.KafkaTemplate;

import javax.annotation.Resource;

/**
 * @Author 李璞
 * @Date 2022/1/17 17:01
 * @Description
 */
public class Spout extends BaseBasicBolt {

    @Resource
    private KafkaTemplate<String,String> kafkaTemplate;


    @Override
    public void execute(Tuple tuple, BasicOutputCollector basicOutputCollector) {
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
        outputFieldsDeclarer.declare(new Fields("brand","speed"));
    }

}
