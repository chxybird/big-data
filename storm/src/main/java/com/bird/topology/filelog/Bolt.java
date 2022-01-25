package com.bird.topology.filelog;

import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Tuple;

import java.util.Map;

/**
 * @Author 李璞
 * @Date 2022/1/14 15:52
 * @Description
 */
public class Bolt extends BaseRichBolt {

    private OutputCollector collector;

    /**
     * @Author 李璞
     * @Date 2022/1/14 15:55
     * @Description Bolt启动前运行的代码
     */
//    @Override
//    public void prepare(Map<String, Object> map, TopologyContext topologyContext, OutputCollector outputCollector) {
//        this.collector=outputCollector;
//    }

    @Override
    public void prepare(Map map, TopologyContext topologyContext, OutputCollector outputCollector) {
        this.collector=outputCollector;
    }

    /**
     * @Author 李璞
     * @Date 2022/1/14 15:56
     * @Description 处理从Spout传输过来的Tuple
     */
    @Override
    public void execute(Tuple tuple) {
        //获取tuple的数据
        String log = tuple.getStringByField("log");
        //切割字符提取姓名
//        String[] split = log.split("\\|");
//        String name = split[1];
        //TODO 可以将处理的数据进行数据输出或者传输到下一个Bolt中 这里方便演示直接打印
//        System.out.println(name);
        System.out.println(log);
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {

    }
}
