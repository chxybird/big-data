package com.bird.demo.demo1;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.BasicOutputCollector;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseBasicBolt;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Tuple;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author 李璞
 * @Date 2022/2/16 9:17
 * @Description
 */
public class Bolt extends BaseBasicBolt {
    /**
     * 记录单词的个数
     */
    private Integer count = 0;
    /**
     * 输出hash值判断是否是同一个对象
     */
    private final int[] testHash=new int[20];

    @Override
    public void execute(Tuple input, BasicOutputCollector collector) {
        //该方法每有一个tuple经过都会被调用一次
        //获取tuple的数据
        String sentence = input.getStringByField("sentence");
        //切割句子为单词
        String[] words = sentence.split(" ");
        int length = words.length;
//        synchronized (this){
//            count = count + length;
//        }
        count = count + length;
        System.out.println(Thread.currentThread().getName() + "当前单词总数为:" + count+"---hash为:"+testHash);
        //可以将处理的数据进行数据输出或者传输到下一个Bolt中 这里方便演示直接打印
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
        //如果你需要将数据进行下一个Bolt发送,可以在这里定义格式,原理等同于Spout
    }
}
