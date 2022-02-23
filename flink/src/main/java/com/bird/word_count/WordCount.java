package com.bird.word_count;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.operators.AggregateOperator;
import org.apache.flink.api.java.operators.DataSource;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.util.Collector;

/**
 * @Author 李璞
 * @Date 2022/2/22 19:55
 * @Description 批处理WordCount
 */
public class WordCount {
    public static void main(String[] args) throws Exception {
        //创建执行环境
        ExecutionEnvironment environment = ExecutionEnvironment.getExecutionEnvironment();
        //加载文件数据
        DataSource<String> dataSource = environment.readTextFile("F:\\log\\input.txt");
        //计算数据
        DataSet<Tuple2<String,Integer>> resultSet = dataSource.flatMap(new MyflatMapper()).groupBy(0).sum(1);
        //打印结果
        resultSet.print();
    }

    static class MyflatMapper implements FlatMapFunction<String,Tuple2<String,Integer>>{
        @Override
        public void flatMap(String s, Collector<Tuple2<String, Integer>> collector) throws Exception {
            // 按空格分词
            String[] words = s.split(" ");
            // 遍历所有word，包成二元组输出
            for (String str : words) {
                collector.collect(new Tuple2<>(str, 1));
            }
        }
    }
}
