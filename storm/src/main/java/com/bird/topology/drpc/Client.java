package com.bird.topology.drpc;

import org.apache.storm.utils.DRPCClient;
import org.apache.storm.utils.Utils;

import java.util.Map;

/**
 * @Author 李璞
 * @Date 2022/2/12 13:16
 * @Description
 */
public class Client {
    public static void main(String[] args) throws Exception {
        Map<String, Object> config = Utils.readDefaultConfig();
        //drpc服务
        DRPCClient client = new DRPCClient(config, "192.168.78.134", 3772);
        /// 调用drpcTest函数，传递参数为hello
        String result = client.execute("bird", "Storm");
        System.out.println(result);
    }
}
