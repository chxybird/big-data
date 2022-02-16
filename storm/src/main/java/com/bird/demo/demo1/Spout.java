package com.bird.demo.demo1;

import org.apache.storm.spout.SpoutOutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichSpout;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;

import java.io.*;
import java.util.Map;

/**
 * @Author 李璞
 * @Date 2022/2/16 9:17
 * @Description
 */
public class Spout extends BaseRichSpout {
    private SpoutOutputCollector collector;
    private BufferedReader bufferedReader;

    @Override
    public void open(Map<String, Object> map, TopologyContext topologyContext, SpoutOutputCollector spoutOutputCollector) {
        this.collector = spoutOutputCollector;
        try {
            //读取文件到流中
            InputStream inputStream = new FileInputStream(new File("F:\\log", "input.txt"));
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void nextTuple() {
        try {
            String temp;
            //循环读取 每读取一行都会发射一个元组信息
            while ((temp = bufferedReader.readLine()) != null) {
                //发射的值对应下面定义的字段名称
                collector.emit(new Values(temp));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
        //设置发射元组信息的字段格式
        outputFieldsDeclarer.declare(new Fields("sentence"));
    }
}
