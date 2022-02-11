package com.bird.streaming;

import org.apache.spark.SparkConf;
import org.apache.spark.SparkContext;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaStreamingContext;

/**
 * @Author 李璞
 * @Date 2022/2/11 10:32
 * @Description SparkStreaming的单词统计demo
 */
public class WordCount {
    public static void main(String[] args) {
        SparkConf conf = new SparkConf();
        //local[*]表示本地*个线程启动
        conf.setMaster("local[2]");
        conf.setAppName("spark-streaming-word-count");
        //5秒计算一次
        JavaStreamingContext context = new JavaStreamingContext(conf, Durations.seconds(5));



    }
}
