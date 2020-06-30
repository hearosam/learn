package com.sam.springdubbo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * 用户信息实体类 这里的实体类不应该用{@link Component}注解,正常情况这个应该通过mybatis 相关配置交由spring管理
 * 这里为了演示用了{@link Component}注解
 * @author sam.liang
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
public class UserInfo implements Serializable {

    private String userName;
    private String address;
}
