package com.sam.cn.feature.stream;

import com.sun.deploy.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Stream
 *
 * @author sam.liang
 */
public class App {

    public static void main(String[] args) {
        //filter 过滤元素 可以传入一个predicate 返回一个Stream， forEach是一个终端操作 返回值是void
        Comparator<String> comparator = (o1, o2) -> o1.length() - o2.length();
        List<String> list = Arrays.asList("aa", "bb", "cc", "dd", "aa1", "bb1", "cc1", "dd1");
//        list.stream()
//                .filter((s)->s.startsWith("a"))
//                .forEach(System.out::println);
        //sorted 排序，可以传入一个比较器，
//        list.stream()
//                .sorted((o1, o2) ->o2.compareTo(o1))
//                .forEach(System.out::println);
        //distinct(根据元素的hashcode以及equals方法实现)
//        List<Integer> integers = Arrays.asList(1, 2, 3, 4, 3, 2, 5, 6, 2, 8, 9, 0);
//        List<Integer> collect = integers.stream().distinct().collect(Collectors.toList());
//        System.out.println(collect);
        //limit截取，返回一个不超过指定长度的流
//        List<Integer> integers = Arrays.asList(1, 2, 3, 4, 3, 2, 5, 6, 2, 8, 9, 0);
//        List<Integer> collect = integers.stream().limit(3).collect(Collectors.toList());
//        System.out.println(collect);
        //skip 舍弃:舍弃前面n个元素，如果流中元素的个数不足n个，则返回一个流
//        List<Integer> integers = Arrays.asList(1, 2, 3, 4, 3, 2, 5, 6, 2, 8, 9, 0);
//        //舍弃前面3个元素
//        List<Integer> collect = integers.stream().skip(3).collect(Collectors.toList());
//        System.out.println(collect);
        //map 归纳,map接受一个函数，这个函数可以应用于每一个流的元素上
//        List<Integer> integers = Arrays.asList(1, 2, 3, 4, 3, 2, 5, 6, 2, 8, 9, 0);
//        List<Integer> collect = integers.stream().map(s -> s * 2).collect(Collectors.toList());
//        System.out.println(collect);
        //flatMap 压扁 如下代码：将map阶段生成的多个子stream压扁到一个Stream中
//        List<String> strings = Arrays.asList("hello", "world", "tomas");
//        strings.stream()
//                .map(w->w.split(""))
//                .flatMap(Arrays::stream)
//                .distinct()
//                .forEach(w->System.out.print(w+"-"));
        //peek:每个元素恢复运行前插入一个动作
//        List<Integer> integers = Arrays.asList(1, 2, 3, 4, 3, 2, 5, 6, 2, 8, 9, 0);
//        integers.stream()
//                .peek(a->System.out.println("from stream : "+ a))
//                .map(a->a+10)
//                .peek(a->System.out.println("from map : "+a))
//                .limit(5)
//                .peek(a->System.out.println("from limit "+a))
//                .forEach(System.out::println);
        //collect 收集，将stream里面的数据收集起来最终形成一个list map set
//        List<Integer> integers = Arrays.asList(1, 2, 3, 4, 3, 2, 5, 6, 2, 8, 9, 0);
//        List<Integer> collect = integers.stream()
//                .limit(5)
//                .collect(Collectors.toList());
//        System.out.println(collect);
        /**
         * 终止操作
         * 循环：forEach
         * 计算：min max count average
         * 匹配：anyMatch allMatch noneMatch findFirst findAny
         * 聚合：reduce
         * 收集器：Collectors
         */
        List<Integer> integers = Arrays.asList(1, 2, 3, 4, 3, 2, 5, 6, 2, 8, 9, 0);
        //查找和匹配
        //anyMatch:流中是否有一个元素=10，才返回true
//        boolean b = integers.stream().anyMatch(n -> n == 10);
//        System.out.println(b);
        //allMatch:流中必须所有元素都=1，才返回true
//        boolean b = integers.stream().allMatch(n -> n == 1);
//        System.out.println(b);
        //noneMatch:流中必须所有元素都不等于11，才返回true
//        boolean b = integers.stream().noneMatch(n -> n == 11);
//        System.out.println(b);
        //findAny 返回stream中任意一個元素
//        Optional<Integer> any = integers.stream().filter(n -> n > 5).findAny();
//        System.out.println(any.get());
        //findFirst返回过滤后第一个元素
//        Optional<Integer> first = integers.stream().filter(n-> n > 3).findFirst();
//        System.out.println(first.get());
        //reduce归并 將流中的所有元素结合起来最终得到一个值
//        Integer reduce = integers.stream().reduce(0, (a, b) -> a + b);
//        System.out.println(reduce);
//        Integer reduce1 = integers.stream().reduce(0, Integer::sum);
//        System.out.println(reduce1);
        //最小值 最大值
//        Integer reduce1 = integers.stream().reduce(0, Integer::min);
//        Integer reduce1 = integers.stream().reduce(0, Integer::max);
        //Collectors收集器 Collectors.maxBy最大、Collectors.minBy最小值，summingInt汇总、averagingInt平均数、joining连接字符串、counting统计
//        List<String> strings = Arrays.asList("a", "b", "c", "d");
//        String collect = strings.stream().collect(Collectors.joining());
//        System.out.println(collect);
//        String join = StringUtils.join(strings, "");
//        System.out.println(join);

//        Long collect = integers.stream().collect(Collectors.counting());
//        long count = integers.stream().count();
//        System.out.println(count);
        //分组groupingBy
//        Map<String,String> map = new HashMap<>();
//        map.put("a","aa");
//        Map<String,String> map2 = new HashMap<>();
//        map2.put("b","bb");
//        Map<String,String> map3 = new HashMap<>();
//        map3.put("a","aa2");
//        Map<String,String> map4 = new HashMap<>();
//        map4.put("c","cc");
//        List<Map<String,String>> mapList = new ArrayList<>();
//        mapList.add(map);
//        mapList.add(map2);
//        mapList.add(map3);
//        mapList.add(map4);
//        //按照key分组{[a]=[{a=aa}, {a=aa2}], [b]=[{b=bb}], [c]=[{c=cc}]}--->三个key
//        Map<Set<String>, List<Map<String, String>>> collect1 = mapList.stream().collect(Collectors.groupingBy(Map::keySet));
//        System.out.println(collect1);
//
//        //统计分组记录数{[a]=2, [b]=1, [c]=1}
//        Map<Set<String>, Long> collect2 = mapList.stream().collect(Collectors.groupingBy(Map::keySet, Collectors.counting()));
//        System.out.println("------------");
//        System.out.println(collect2);
//        System.out.println("------------");
//
//        //自定义分组key -->{bbbbb=[{b=bb}], ccccc=[{c=cc}], aaaaa=[{a=aa}, {a=aa2}]}
//        Map<String, List<Map<String, String>>> collect = mapList.stream().collect(Collectors.groupingBy(tempMap -> {
//            String key = tempMap.keySet().iterator().next();
//            if ("a".equals(key)) {
//                return "aaaaa";
//            } else if ("b".equals(key)) {
//                return "bbbbb";
//            } else if ("c".equals(key)) {
//                return "ccccc";
//            } else {
//                return "ooooo";
//            }
//        }));
//        System.out.println(collect);
        //多级分组
//        mapList.stream().collect(Collectors.groupingBy(Map::keySet,Collectors.groupingBy(tempMap->{
//            //这里定义多级分组的规则
//            return "";
//        })));
        //并行流 Parallel-Streams(内部实现fork/join)
    }
}
