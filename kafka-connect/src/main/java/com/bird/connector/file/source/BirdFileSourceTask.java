package com.bird.connector.file.source;

import org.apache.kafka.common.utils.AppInfoParser;
import org.apache.kafka.connect.source.SourceRecord;
import org.apache.kafka.connect.source.SourceTask;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @Author 李璞
 * @Date 2022/1/20 15:35
 * @Description
 */
public class BirdFileSourceTask extends SourceTask {

    public static final String FILENAME_FIELD = "filename";
    public static final String POSITION_FIELD = "position";


    private Long offset;
    private InputStream inputStream;
    private BufferedReader bufferedReader;
    private String temp;

    /**
     * @Author 李璞
     * @Date 2022/1/20 16:05
     * @Description 任务启动方法 获取数据
     */
    @Override
    public void start(Map<String, String> map) {
        try {
            //connector的配置信息会被传递到这个形参中
            //加载数据文件
            inputStream = new FileInputStream(map.get(BirdFileSourceConnector.FILE_URL_KEY));
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @Author 李璞
     * @Date 2022/1/20 16:18
     * @Description 将数据发送到kafka
     */
    @Override
    public List<SourceRecord> poll() {
        try {
            context.offsetStorageReader().offset(Collections.singletonMap(FILENAME_FIELD, ""));
            List<SourceRecord> recordList = new ArrayList<>();
            //读取文件内容
            String line = bufferedReader.readLine();
//            SourceRecord sourceRecord = new SourceRecord(line);
//        while ((temp = bufferedReader.readLine()) != null) {
//            TimeUnit.SECONDS.sleep(20);
//        }
            return null;
        } catch (Exception exception) {
            exception.printStackTrace();
            return null;
        }
    }

    /**
     * @Author 李璞
     * @Date 2022/1/20 16:06
     * @Description 销毁方法
     */
    @Override
    public void stop() {
        //TODO
    }

    /**
     * @Author 李璞
     * @Date 2022/1/21 10:53
     * @Description 设置版本 可以随意控制 直接return 1.0字符串都可以
     */
    @Override
    public String version() {
        return AppInfoParser.getVersion();
    }
}
