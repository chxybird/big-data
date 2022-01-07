package com.bird.mr.wordcount;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

/**
 * @Author 李璞
 * @Date 2022/1/6 20:14
 * @Description
 */
public class DriverWordCount {
    public static void main(String[] args) throws Exception{
        System.setProperty("HADOOP_USER_NAME","root");
        //获取Job
        Configuration configuration=new Configuration();
        Job job = Job.getInstance(configuration);
        //设置jar包路径
        job.setJarByClass(DriverWordCount.class);
        //关联mapper与reducer
        job.setMapperClass(MapWordCount.class);
        job.setReducerClass(ReduceWordCount.class);
        //设置map输出的kv类型
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(IntWritable.class);
        //设置reduce输出kv类型
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);
        //设置输入路径和输出路径
        FileInputFormat.setInputPaths(job,new Path("F:\\BigData\\input\\word-count.txt"));
        FileOutputFormat.setOutputPath(job,new Path("F:\\BigData\\output"));
        //提交Job
        boolean result = job.waitForCompletion(true);
        System.out.println(result ? "运行成功" : "运行失败");
        //0正常退出 1非正常退出
        System.exit(result ? 0 : 1);
    }
}
