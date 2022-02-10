package com.bird.spark;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.MapFunction;
import scala.Tuple2;

import java.util.List;

/**
 * @Author 李璞
 * @Date 2022/2/10 14:10
 * @Description 单词统计入门案例
 */
public class WordCount {
    public static void main(String[] args) {
        //创建配置并设置任务名称
        SparkConf sparkConf=new SparkConf();
        sparkConf.setAppName("WordCount");
        //如果想本地调试设置为local
        sparkConf.setMaster("local");
        //创建入口
        JavaSparkContext context= new JavaSparkContext(sparkConf);
        //初始化RDD(弹性分布式数据集) 从本地中加载 当然可以加载HDFS中的文件
//        JavaRDD<String> rdd= context.textFile("/home/bird/storm/log.txt");
        JavaRDD<String> rdd = context.textFile("F:\\log\\log.txt");

        //Transformation算子
        //将数据切分获取想要的关键词(姓名)
        JavaRDD<String> mapRdd = rdd.map((Function<String, String>) s -> {
            String[] words = s.split("\\|");
            //获取姓名并返回
            return words[1];
        });
        //将关键词转化为<KEY 1>的形式
        JavaRDD<Tuple2<String, Integer>> tupleRdd = mapRdd.map((Function<String, Tuple2<String, Integer>>) s -> new Tuple2<>(s, 1));
        //对每个<KEY 1>的数据进行按照KEY分组并计算KEY的数量
        JavaPairRDD<String, Iterable<Tuple2<String, Integer>>> groupRdd = tupleRdd
                .groupBy((Function<Tuple2<String, Integer>, String>) stringIntegerTuple2 -> stringIntegerTuple2._1);
        //统计KEY的个数
        JavaPairRDD<String, Integer> valuesRdd = groupRdd.mapValues((Function<Iterable<Tuple2<String, Integer>>, Integer>) tuple2s -> {
            int num = 0;
            for (Tuple2<String, Integer> t : tuple2s) {
                num = num + t._2;
            }
            return num;
        });
        //Action算子
        List<Tuple2<String, Integer>> collect = valuesRdd.collect();
        collect.forEach(System.out::println);
    }
}
