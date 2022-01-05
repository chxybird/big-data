package com.bird.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import java.io.IOException;
import java.net.URI;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @Author 李璞
 * @Date 2022/1/5 14:23
 * @Description Hadoop-HDFS 工具类
 */
@Slf4j
public class HdfsUtils {

    /**
     * NameNode地址
     */
    private static final String URL = "hdfs://192.168.78.134:8020";
    /**
     * 认证账号
     */
    private static final String AUTH_USER = "root";

    private static final Configuration CONFIGURATION = new Configuration();


    /**
     * @Author 李璞
     * @Date 2022/1/5 15:31
     * @Description 初始化配置
     */
    static {
        try {
            //设置配置参数
            CONFIGURATION.set("fs.defaultFS", URL);
            CONFIGURATION.set("HADOOP_USER_NAME", AUTH_USER);
        } catch (Exception e) {
            log.error("HDFS初始化失败!!!");
            e.printStackTrace();
        }
    }

    /**
     * @Author 李璞
     * @Date 2022/1/5 15:10
     * @Description 获取连接
     */
    private FileSystem getClient() throws Exception {
        return FileSystem.get(CONFIGURATION);
    }

    /**
     * @Author 李璞
     * @Date 2022/1/5 15:35
     * @Description 归还连接
     */
    private void closeClient(FileSystem fileSystem) throws Exception {
        fileSystem.close();
    }

    /**
     * @Author 李璞
     * @Date 2022/1/5 16:16
     * @Description 判断文件或者目录是否存在
     */
    public boolean exist(String path) throws Exception {
        FileSystem client = getClient();
        boolean exists = client.exists(new Path(path));
        closeClient(client);
        return exists;
    }

    /**
     * @Author 李璞
     * @Date 2022/1/5 16:25
     * @Description 创建文件夹
     */
    public boolean mkdir(String path) throws Exception {
        FileSystem client = getClient();
        boolean flag = client.mkdirs(new Path(path));
        closeClient(client);
        return flag;
    }

    /**
     * @Author 李璞
     * @Date 2022/1/5 16:25
     * @Description 删除文件夹或者文件
     */
    public boolean rm(String path) {
        return true;
    }


}
