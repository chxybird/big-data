package com.bird.topology.drpc;

import org.apache.storm.topology.BasicOutputCollector;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseBasicBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;

/**
 * @Author 李璞
 * @Date 2022/2/12 9:08
 * @Description
 */
public class Bolt extends BaseBasicBolt {
    @Override
    public void execute(Tuple input, BasicOutputCollector collector) {
        Long request = input.getLongByField("request");
        String args = input.getStringByField("args");
        //返回给ReturnResults的字段的id不要跟更改 原值传入request result为计算处理后的结果
        collector.emit(new Values(request,args+"!!!"));
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        //ReturnResults接收的fields为Fields("id", "result")
        declarer.declare(new Fields("id","result"));
    }
}
