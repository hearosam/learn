package com.sam.cn.feature.java8_demo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * test
 * @author sam.liang
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Apple {
    /**
     * 颜色
     */
    private String color;
    /**
     * 重量
     */
    private int heavy;

//    /**
//     * 是否是绿苹果
//     * @param apple 苹果实例对象
//     * @return true/false
//     */
//    public static boolean isGreenApple(Apple apple) {
//        return "green".equals(apple.color);
//    }
//
//    /**
//     * 苹果重量是否超过150
//     * @param apple 苹果实例对象
//     * @return true/false
//     */
//    public static boolean isHeavyApple(Apple apple) {
//        return apple.heavy > 150;
//    }
}
