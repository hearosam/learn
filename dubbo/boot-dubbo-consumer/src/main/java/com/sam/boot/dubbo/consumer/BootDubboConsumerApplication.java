package com.sam.boot.dubbo.consumer;

import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**_@EnableDubbo 启用dubbo注解
 * @author sam.liang
 */
@EnableDubbo
@SpringBootApplication
public class BootDubboConsumerApplication {

    public static void main(String[] args) {
        SpringApplication.run(BootDubboConsumerApplication.class, args);
    }

}
