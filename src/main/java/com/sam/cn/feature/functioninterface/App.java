package com.sam.cn.feature.functioninterface;

public class App {

    public static void main(String[] args) {
        //-----------------函数式接口演示demo-----------------
        //(from)->Integer.parseInt(from):匿名内部类+方法实现
        Convertor<String,Integer> conver = (from)->Integer.parseInt(from);
//        System.out.println(conver.conver("123"));
        //旧代码
//        Convertor<String,Integer> conver2 = new Convertor<String, Integer>() {
//            @Override
//            public Integer conver(String s) {
//                return Integer.parseInt(s);
//            }
//        };
//        System.out.println(conver2.conver("444"));
        //简化
        Convertor<String,Integer> conver2 = Integer::valueOf;
//        System.out.println(conver2.conver("333"));

        //-----------------lambda表达式访问外部变量以及接口默认方法---
        //1.访问外部的局部变量，局部变量必须是final类型的
        //1.1局部变量显式声明final
        final int num = 1;
        Convertor<String,Integer> conver3 = (form)->{
            int tempVal = Integer.parseInt(form);
            return tempVal+num;
        };
//        System.out.println(conver3.conver("99"));

        //1.2局部变量隐式final(何为隐式的 final 呢？就是说到编译期为止，num2 对象是不能被改变的)
        int num2 = 2;
        Convertor<String,Integer> conver4 = (from)->{
          int tempVal = Integer.parseInt(from);
//          num2 = 3; //不能在这里修改num2的值
          return num2 * tempVal;
        };
//        num2 = 3;//也不能在这里修改num2的值
//        System.out.println(conver4.conver("99"));
        //2.访问成员变量和静态变量------>看lambda4
        //3.访问接口的默认方法：带有默认实现的接口方法，是不能在 lambda 表达式中访问的。但是可以在匿名内部类中实现访问

        //-----------------::关键字使用demo-----------------
        //::关键字使用-->可以调用类的构造函数跟方法(静态或者非静态)
        /**
         * Person::new;-->匿名内部类+create抽象方法实现，new关键字会自动根据上下文选中
         * 正确的构造器实现create
         */
        PersonFactory<Person> p = Person::new;
        Person sam = p.create("sam", 18);

    }
}

class lambda4{
    /**
     * 静态变量
     */
    static int outsiteStaticNum;
    /**
     * 成员变量
     */
    int outerNum;

    void testScopes(){
        //对静态变量赋值
        Convertor<String,Integer> convertor1 = (from)->{
            outsiteStaticNum = 3;
          return Integer.parseInt(from);
        };
        System.out.println(convertor1.conver("400"));
        //对成员变量赋值
        Convertor<String,Integer> convertor2 = (from)-> {
            outerNum = 43;
          return Integer.parseInt(from);
        };
        System.out.println(convertor2.conver("111"));
    }
}

