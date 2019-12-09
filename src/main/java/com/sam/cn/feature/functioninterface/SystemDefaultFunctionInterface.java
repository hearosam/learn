package com.sam.cn.feature.functioninterface;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * 系统自带的函数式接口
 * @author sam.liang
 */
public class SystemDefaultFunctionInterface {

    public static void main(String[] args) {
        //------------------------Function接口
        /**
         * andThen:doubleFun.apply(3)-->结果a，plusFun.apply(结果a)-->return 最终结果
         * compose:plusFun.apply(3)-->结果a,doubleFun.apply(结果a)-->return 最终结果
         * identity:调用当前正在运行的方法跟直接doubleFun.apply(3)没区别
         */
        Function<Integer,Integer> doubleFun = (n)-> 2 * n;
        Function<Integer, Integer> plusFun = (n)-> 2+n;
        //2*3
        System.out.println(doubleFun.apply(3));
        //2+3
        System.out.println(plusFun.apply(3));
        //2*3+2
        System.out.println(doubleFun.andThen(plusFun).apply(3));
        //(2+3)*2
        System.out.println(doubleFun.compose(plusFun).apply(3));
        //2*3
        System.out.println(Function.identity().compose(doubleFun).apply(3));

        //----------------------supplier生产者接口
        Supplier<Person> person = Person::new;
        Person person1 = person.get();

        //----------------------consumer消费者接口
        //指定消费方式
        Consumer<Person> greeter = (p)->System.out.println("hello----->"+ p.name);
        //接受入参
        greeter.accept(new Person("tomcat",11));

        //---------------------Comparator比较接口
        /**
         * compare方法
         * reversed方法 逆转
         * thenComparing二次排序方法
         */
        Comparator<Integer> comparator = (o1,o2)->o1-o2;
        List<Integer> numbers = Arrays.asList(11, 22, 3, 6);
        numbers.sort(comparator);
        System.out.println(numbers);
        System.out.println("------------");
        numbers.sort(comparator.reversed());
        System.out.println(numbers);
        //对Person列表进行排序先按照年龄的大小进行排序，然后按照名称的字符长度排序
        Comparator<Person> comparaName = (o1, o2) ->o1.name.length() - o2.name.length();
        Comparator<Person> comparaAge = (o1,o2)->o1.age-o2.age;

        List<Person> personList = new ArrayList<>();
        personList.add(new Person("tom",10));
        personList.add(new Person("tomas",10));
        personList.add(new Person("tomasLee",13));
        personList.add(new Person("jerry",16));

        //按照年龄的大小进行排序
        personList.sort(comparaName);
        System.out.println(personList);
        //先按照年龄的大小进行排序，然后按照名称的字符长度排序
        personList.sort(comparaAge.thenComparing(comparaName));
        System.out.println(personList);



    }
}
