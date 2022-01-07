package com.bird.mr.wordcount;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

/**
 * @Author 李璞
 * @Date 2022/1/6 20:06
 * @Description
 */
public class ReduceWordCount extends Reducer<Text, IntWritable, Text, IntWritable> {
    private final IntWritable OUTPUT_VALUE = new IntWritable();

    @Override
    protected void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
        int sum = 0;
        //value的值就是map阶段输出的相同KEY值的集合
        for (IntWritable value : values) {
            sum = sum + value.get();
        }
        //写出
        OUTPUT_VALUE.set(sum);
        context.write(key, OUTPUT_VALUE);
    }

}
