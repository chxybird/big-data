package com.bird.word_count;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.operators.AggregateOperator;
import org.apache.flink.api.java.operators.DataSource;
import org.apache.flink.api.java.tuple.Tuple2;

/**
 * @Author 李璞
 * @Date 2022/2/22 19:55
 * @Description 批处理WordCount
 */
public class WordCount {
    public static void main(String[] args) {
        //创建执行环境
        ExecutionEnvironment environment = ExecutionEnvironment.getExecutionEnvironment();
        //加载文件数据
        DataSource<String> dataSource = environment.readTextFile("F:\\log\\input.txt");
        //计算数据
        AggregateOperator<Tuple2<String, Integer>> sum = dataSource.flatMap((FlatMapFunction<String, Tuple2<String, Integer>>) (s, collector) -> {
            String[] words = s.split(" ");
            for (String word : words) {
                Tuple2<String, Integer> tuple2 = new Tuple2<>(word, 1);
                collector.collect(tuple2);
            }
        }).groupBy(0).sum(1);
    }
}
