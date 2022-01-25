package com.bird.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @Author 李璞
 * @Date 2022/1/24 15:50
 * @Description
 */
@Component
public class SpringContextUtils implements ApplicationContextAware {

    private static ApplicationContext context;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context=applicationContext;
    }

    /**
     * @Author 李璞
     * @Date 2022/1/24 16:00
     * @Description 获取Bean对象
     */
    public static Object getBean(String name){
        return context.getBean(name);
    }

    public static ApplicationContext getContext(){
        return context;
    }
}
