package com.bird.connector.file.source;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.common.utils.AppInfoParser;
import org.apache.kafka.connect.connector.Task;
import org.apache.kafka.connect.errors.ConnectException;
import org.apache.kafka.connect.source.SourceConnector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author 李璞
 * @Date 2022/1/20 15:33
 * @Description 自定义文件Source插件
 */
@Slf4j
public class BirdFileSourceConnector extends SourceConnector {

    //定义加载的配置文件的K-V

    /**
     * source 抽取数据的文件的路径
     */
    public static final String FILE_URL_KEY = "file.url";
    /**
     * source 抽取数据后存放kafka的topic地址
     */
    public static final String TOPIC_KEY="topic";

    private String fileUrl;
    private String topic;

    /**
     * @Author 李璞
     * @Date 2022/1/21 10:23
     * @Description 初始化方法 读取配置文件
     */
    @Override
    public void start(Map<String, String> map) {
        //connector的配置文件会被自动读取到此map中
        fileUrl = map.get(FILE_URL_KEY);
        topic = map.get(TOPIC_KEY);
        if (fileUrl == null || fileUrl.isEmpty()) {
            throw new ConnectException("配置文件加载失败,请检查你的配置文件 file.url");
        }
        if (topic==null || topic.isEmpty()){
            throw new ConnectException("配置文件加载失败,请检查你的配置文件 topic");
        }

    }

    /**
     * @Author 李璞
     * @Date 2022/1/20 16:26
     * @Description 设置执行的Task的类
     */
    @Override
    public Class<? extends Task> taskClass() {
        return BirdFileSourceTask.class;
    }

    /**
     * @Author 李璞
     * @Date 2022/1/21 10:24
     * @Description 传递给Task配置信息
     */
    @Override
    public List<Map<String, String>> taskConfigs(int i) {
        ArrayList<Map<String, String>> configs = new ArrayList<>();
        Map<String,String> config=new HashMap<>();
        config.put(FILE_URL_KEY,fileUrl);
        config.put(TOPIC_KEY,topic);
        configs.add(config);
        return configs;
    }

    /**
     * @Author 李璞
     * @Date 2022/1/21 10:47
     * @Description 销毁方法
     */
    @Override
    public void stop() {
       //TODO
    }

    /**
     * @Author 李璞
     * @Date 2022/1/21 10:48
     * @Description 配置校验 可以定义配置文件的信息 可以提供有关错误和建议值的反馈
     */
    @Override
    public ConfigDef config() {
        ConfigDef configDef=new ConfigDef();
        configDef.define(FILE_URL_KEY, ConfigDef.Type.STRING, ConfigDef.Importance.HIGH,"抽取数据的文件的路径");
        configDef.define(TOPIC_KEY,ConfigDef.Type.STRING, ConfigDef.Importance.HIGH,"存入kafka的主题名称");
        return configDef;
    }

    /**
     * @Author 李璞
     * @Date 2022/1/21 10:25
     * @Description 设置版本 可以随意控制 直接return 1.0字符串都可以
     */
    @Override
    public String version() {
        return AppInfoParser.getVersion();
    }
}
