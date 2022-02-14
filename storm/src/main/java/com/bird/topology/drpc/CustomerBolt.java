package com.bird.topology.drpc;

import org.apache.storm.topology.BasicOutputCollector;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseBasicBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;

/**
 * @Author 李璞
 * @Date 2022/2/14 10:38
 * @Description
 */
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
