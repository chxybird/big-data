package com.bird.webservice.service.impl;

import com.bird.webservice.service.intf.TestService;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

/**
 * @Author 小鸟程序员
 * @Date 2022/2/4 13:52
 * @Description WebService业务接口
 */
@Component
@WebService
public class TestServiceImpl implements TestService {

    /**
     * @Author 小鸟程序员
     * @Date 2022/2/4 13:55
     * @Description 获取信息
     */
    @WebMethod
    @WebResult(name = "result")
    @Override
    public String getInfo(@WebParam(name = "id") String id) {
        if ("1".equals(id)){
            return "小鸟程序员";
        }else {
            return "未查询到结果";
        }
    }
}
