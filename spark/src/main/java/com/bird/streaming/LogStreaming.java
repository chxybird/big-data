package com.bird.streaming;

import org.apache.spark.SparkConf;
import org.apache.spark.streaming.Duration;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaStreamingContext;

/**
 * @Author 李璞
 * @Date 2022/2/11 10:24
 * @Description SparkStreaming与Kafka整合
 */
public class LogStreaming {
    public static void main(String[] args) {
        SparkConf sparkConf = new SparkConf();
        sparkConf.setAppName("spark-kafka-streaming");
        sparkConf.setMaster("local");

        JavaStreamingContext context = new JavaStreamingContext(sparkConf, Durations.seconds(2));

    }
}
