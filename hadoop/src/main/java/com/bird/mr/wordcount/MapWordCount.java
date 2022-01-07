package com.bird.mr.wordcount;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

/**
 * @Author 李璞
 * @Date 2022/1/6 19:44
 * @Description
 */
public class MapWordCount extends Mapper<LongWritable, Text,Text, IntWritable> {
    //Map有默认的读数据的组件 TextInputFormat 一行一行的读数据 返回改行的行号和该行的内容

    private final Text text=new Text();
    private final static IntWritable OUTPUT_VALUE=new IntWritable(1);

    /**
     * @Author 李璞
     * @Date 2022/1/6 19:55
     * @Description 初始化方法 map开始前调用且仅调用一次
     */
    @Override
    protected void setup(Mapper<LongWritable, Text, Text, IntWritable>.Context context) throws IOException, InterruptedException {

    }

    /**
     * @Author 李璞
     * @Date 2022/1/6 19:53
     * @Description map逻辑 每读取数据一次调用一次
     */
    @Override
    protected void map(LongWritable key, Text value, Mapper<LongWritable, Text, Text, IntWritable>.Context context) throws IOException, InterruptedException {
        //获取一行数据
        String line = value.toString();
        //切割 以空格为切割符号
        String[] words = line.split(" ");
        //遍历输出给reduce
        for (String word : words) {
            text.set(word);
            context.write(text,OUTPUT_VALUE);
        }
    }

    /**
     * @Author 李璞
     * @Date 2022/1/6 19:56
     * @Description 结束方法 map开始后调用且仅调用一次
     */
    @Override
    protected void cleanup(Mapper<LongWritable, Text, Text, IntWritable>.Context context) throws IOException, InterruptedException {

    }
}
