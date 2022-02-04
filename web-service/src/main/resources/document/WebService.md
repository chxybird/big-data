# WebService--小鸟程序员

### 一、概述

WebService是一种异构平台之间相互调用的一种技术。对于远程调用的技术其实有很多。远程调用的技术例如SpringCloud、HttpClient、Dubbo等等。WebService是一种比较老的技术了，但是作为一门技术可以来简单的了解一下。对于性能要求比较高的不建议使用此技术，还有同构系统也不推荐使用。

### 二、WebService原理

WebService通过Soap(简单对象传输协议)协议，传输XML格式的数据。在服务发布的同时自动生成一个WSDL，WSDL可以理解为接口说明书。可以在路径后面加上?wsdl来查看。

所以WebService屏蔽所有的深奥的道理其实就是两步走，第一步就是写好业务执行的代码，第二步就是服务的发布。服务发布的时候会自动生成WSDL服务说明书，客户端调用WebService服务指定这个服务的WSDL说明书地址然后填入参数即可调用服务。

### 三、WebService的实现框架

JAVA实现WebService的框架有很多，例如 Axis2 、 XFire 、 CXF 等等， 现在局势JAVA实现WebService都使用CXF来做。

##### 1.引入Maevn依赖并配置application.yml

```xml
<!-- WebService基于CXF框架-->
<dependency>
    <groupId>org.apache.cxf</groupId>
    <artifactId>cxf-spring-boot-starter-jaxws</artifactId>
    <version>3.5.0</version>
</dependency>
```

```yml
#WebService路径前缀
cxf:
  path: /services
```

##### 2.编写WebService业务接口

(1)接口

```java
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
```

(2)实现类

```java
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

```

##### 3.配置发布服务

```java
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
```

##### 4.所有服务查看以及指定服务的WSDL查看

所有WebService服务查看：http://localhost:8080/services/

指定服务的WSDL查看：http://localhost:8080/services/get-info?wsdl

##### 5.客户端代码调用测试

```java
public class WebServiceTest {

    /**
     * @Author 小鸟程序员
     * @Date 2022/2/4 14:43
     * @Description 客户端调用WebService服务
     */
    @Test
    void getInfo() throws Exception {
        JaxWsDynamicClientFactory factory=JaxWsDynamicClientFactory.newInstance();
        //只需要指定wsdl地址就可以
        Client client = factory.createClient("http://localhost:8080/services/get-info?wsdl");
        //方法名 参数一 参数二 。。。。。
        Object[] result = client.invoke("getInfo", "1");
        System.out.println(result[0]);
    }
}

```

