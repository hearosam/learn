package com.sam.cn.feature.java8_demo;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) {
        List<Apple> apples = new ArrayList<>();
        apples.add(new Apple("red",170));
        apples.add(new Apple("red",100));
        apples.add(new Apple("yellow",100));
        apples.add(new Apple("blue",180));
        apples.add(new Apple("red",100));
        apples.add(new Apple("green",100));

//        List<Apple> apples1 = filterApples(apples, (Apple a)->"green".equals(a.getColor()));
        //匿名函数+谓词判定
//        List<Apple> apples1 = filterApples(apples, (Apple a)->a.getHeavy() > 150);
        //lambda 顺序处理
//        List<Apple> apples1 = apples.stream().filter((Apple a) -> a.getHeavy() > 100).collect(Collectors.toList());
        //lambda 并行处理
        List<Apple> apples1 = apples.parallelStream().filter((Apple a) -> a.getHeavy() > 100).collect(Collectors.toList());
        for (Apple a : apples1) {
            System.out.println(a.getColor());
        }
    }

//    static List<Apple> filterApples(List<Apple> apples, Predicate<Apple> p){
//        List<Apple> result = new ArrayList<>();
//        for (Apple apple : apples) {
//            if (p.test(apple)) {
//                result.add(apple);
//            }
//        }
//        return result;
//    }
}
