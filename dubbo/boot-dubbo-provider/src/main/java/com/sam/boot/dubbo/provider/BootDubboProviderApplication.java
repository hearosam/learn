package com.sam.boot.dubbo.provider;

import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;
import com.alibaba.dubbo.config.spring.context.annotation.EnableDubboConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * dubbo服务端代码（启用dubbo注解）
 * @author sam.liang
 */
@SpringBootApplication
@EnableDubbo
public class BootDubboProviderApplication {

    public static void main(String[] args) {
        SpringApplication.run(BootDubboProviderApplication.class, args);
    }
}
