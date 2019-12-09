package com.sam.cn.gc;

public class GcCollectDemo {

    private static final int _1MB = 1024*1024;
    public static void main(String[] args) {
//        testAllocation();
        testPretenureSiteThreshold();
//        testTenuringThershold();
    }

    /**
     *  -XX:+UseSerialGC 指定运行当前代码启用的垃圾收集器
     * -verbose:gc-Xms20M -Xmx20M -Xmn10M -XX:+PrintGCDetails -XX:SurvivorRatio=8 -XX:+UseSerialGC -XX:+PrintCommandLineFlags
     */
    public static void testAllocation() {
        byte[] allocation,allocation2,allocation3,allocation4;
        allocation = new byte[2*_1MB];
        allocation2 = new byte[2*_1MB];
        allocation3 = new byte[2*_1MB];
        allocation4 = new byte[4*_1MB];
    }

    /**
     * 演示PretenureSizeThreshold参数使用
     * -XX:PretenureSizeThreshold=3145728 超过3M的对象直接在年老代分配 该参数只针对Serial和ParNew两款收集器有效
     */
    public static void testPretenureSiteThreshold() {
        byte[] allocation4 = new byte[4*_1MB];
    }

    /**
     * 演示MaxTenuringThreshold参数使用
     * -XX:MaxTenuringThreshold=1 当对象在伊甸区经历过1次gc后还存活就将该对象移动到年老区
     */
    public static void testTenuringThershold() {
        byte[] allocation,allocation2,allocation3,allocation4;
        allocation = new byte[_1MB/4];
//        allocation2 = new byte[_1MB/4];
        allocation3 = new byte[4*_1MB];
        allocation4 = new byte[4*_1MB];
        allocation3 = null;
        allocation3 = new byte[4*_1MB];
    }
}
