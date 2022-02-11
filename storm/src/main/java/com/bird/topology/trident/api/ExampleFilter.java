package com.bird.topology.trident.api;

import org.apache.storm.trident.operation.BaseFilter;
import org.apache.storm.trident.tuple.TridentTuple;

/**
 * @Author 李璞
 * @Date 2022/2/11 15:40
 * @Description Trident的filter方法
 */
public class ExampleFilter extends BaseFilter {
    /**
     * @Author 李璞
     * @Date 2022/2/11 15:41
     * @Description filter方法传入此类实例会调用isKeep方法对数据流中的每个tuple进行过滤操作
     */
    @Override
    public boolean isKeep(TridentTuple tuple) {
        return false;
    }
}
