package com.bird.webservice.config;

import com.bird.webservice.service.impl.TestServiceImpl;
import org.apache.cxf.Bus;
import org.apache.cxf.jaxws.EndpointImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import javax.xml.ws.Endpoint;

/**
 * @Author 小鸟程序员
 * @Date 2022/2/4 14:01
 * @Description 基于CXF框架发布WebService服务
 */
@Configuration
public class CxfConfig {

    @Resource
    private Bus bus;
    @Resource
    private TestServiceImpl testService;

    /**
     * @Author 小鸟程序员
     * @Date 2022/2/4 14:05
     * @Description 服务发布
     */
    @Bean
    public Endpoint test(){
        EndpointImpl endpoint=new EndpointImpl(bus,testService);
        endpoint.publish("/get-info");
        return endpoint;
    }
}
