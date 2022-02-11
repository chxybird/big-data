package com.bird.topology.trident.api;

import org.apache.storm.trident.operation.BaseFunction;
import org.apache.storm.trident.operation.TridentCollector;
import org.apache.storm.trident.tuple.TridentTuple;

/**
 * @Author 李璞
 * @Date 2022/2/11 15:16
 * @Description Trident的each方法
 */
public class ExampleEach extends BaseFunction {
    /**
     * @Author 李璞
     * @Date 2022/2/11 15:16
     * @Description each方法传入此类实例会调用execute方法对数据流中的每个tuple进行操作 功能类似于map
     */
    @Override
    public void execute(TridentTuple tuple, TridentCollector collector) {

    }
}
