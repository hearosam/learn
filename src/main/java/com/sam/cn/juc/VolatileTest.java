package com.sam.cn.juc;

/**
 * volatile 变量自增运算测试
 * @author sam.liang
 */
public class VolatileTest {

    private static volatile int race = 0;

    public static void incre() {
        race++;
    }
    private static final int THREADS_COUNT = 20;

    public static void main(String[] args) {
        Thread[] threads = new Thread[THREADS_COUNT];
        for(int i=0;i<THREADS_COUNT;i++) {
            threads[i] = new Thread(new Runnable() {
                @Override
                public void run() {
                    for (int i = 0; i < 1000; i++) {
                        incre();
                    }
                }
            });
            threads[i].start();
        }
        //等待所有累加线程都结束
        while (Thread.activeCount()>1) {
            Thread.yield();
            System.out.println(race);
        }
    }
}
