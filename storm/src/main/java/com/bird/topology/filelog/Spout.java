package com.bird.topology.filelog;

import org.apache.storm.spout.SpoutOutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichSpout;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;

import java.io.*;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @Author 李璞
 * @Date 2022/1/14 14:18
 * @Description Storm中Spout将数据导入并给系统内的Bolt处理
 */
public class Spout extends BaseRichSpout {
    private SpoutOutputCollector collector;
    private InputStream inputStream;
    private BufferedReader bufferedReader;
    private String temp;
    /**
     * @Author 李璞
     * @Date 2022/1/14 14:54
     * @Description 初始化方法 用于找水源
     */
    @Override
    public void open(Map<String, Object> map, TopologyContext topologyContext, SpoutOutputCollector spoutOutputCollector) {
        this.collector = spoutOutputCollector;
        //设置数据源
        try {
//            inputStream = new FileInputStream(new File("/home/bird/storm", "log.txt"));
            inputStream = new FileInputStream(new File("F:\\log", "log.txt"));
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    /**
     * @Author 李璞
     * @Date 2022/1/14 14:54
     * @Description 从水源将水抽取并将水导入到系统
     */
    @Override
    public void nextTuple() {
        try {
            while ((temp = bufferedReader.readLine()) != null) {
                collector.emit(new Values(temp));
                TimeUnit.SECONDS.sleep(2);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * @Author 李璞
     * @Date 2022/1/14 14:54
     * @Description 定义导出水到系统的格式
     */
    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
        outputFieldsDeclarer.declare(new Fields("log"));
    }
}
