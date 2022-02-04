package com.bird.webservice.service.intf;

import javax.jws.WebService;

/**
 * @Author 小鸟程序员
 * @Date 2022/2/4 13:51
 * @Description
 */

public interface TestService {

    /**
     * 获取信息
     * @param id 测试ID
     * @return 字符串结果
     */
    String getInfo(String id);
}
