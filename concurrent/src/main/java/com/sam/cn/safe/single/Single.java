package com.sam.cn.safe.single;

/**
 * 单例双端检测机制(不一定线程安全)
 * @author sam.liang
 */
public class Single {
    private volatile static Single instance;
    /**
     * 此时这个如果有一个初始化很慢的大对象，当SingleDcl对象实例化完毕，BigObject还没有初始化完，
     * 这时候在外面调用SingleDcl.getInstance().bigObject.getName()----》会发生NullPointException
     * 解决方法
     * 1.加volatile关键字
     * 2.懒汉式-类初始化模式
     * 3.饿汉式
     */
    //private BigObject bigObject;
    private Single(){}
    public Single getInstance(){
        if (instance == null) {
            synchronized (Single.class) {
                if (instance == null) {
                    instance = new Single();
                }
            }
        }
        return instance;
    }
}

/**
 * 类初始化模式
 */
class SingleInit{
    private static class SingleHolder{
        public static SingleInit instance2 = new SingleInit();
    }
    public static SingleInit getInstance() {
        return SingleHolder.instance2;
    }
}

/**
 * 饿汉式
 * 在声明的时候就new这个类的实例，因为在JVM中，对类的加载和类初始化，由虚拟机保证线程安全
 */
class SingleEHan{
    public static SingleEHan instance = new SingleEHan();
    private SingleEHan(){}
}
