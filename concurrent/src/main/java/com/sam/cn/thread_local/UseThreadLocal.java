package com.sam.cn.thread_local;

import java.util.LinkedList;

/**
 * 演示ThreadLocal的使用
 * @author sam.liang
 */
public class UseThreadLocal {

    //声明一个ThreadLocal变量初始值是1
    static ThreadLocal<Integer> threadLocal = new ThreadLocal<Integer>(){
        @Override
        protected Integer initialValue() {
            return 1;
        }
    };

    public void startTreadArray() {
        Thread[] threads = new Thread[3];
        for (int i = 0; i < 3; i++) {
            threads[i] = new Thread(new TestThread(i));
        }
        for (Thread thread : threads){
            thread.start();
        }
    }

    private static class TestThread implements Runnable{
        int id;
        public TestThread(int id) {
            this.id = id;
        }

        @Override
        public void run() {
            System.out.println(Thread.currentThread().getName()+"---started");
            Integer num = threadLocal.get();
            num += id;
            threadLocal.set(num);
            System.out.println(Thread.currentThread().getName()+" ThreadLocal value--->"+threadLocal.get());
        }
    }

    public static void main(String[] args) {
        UseThreadLocal useThreadLocal = new UseThreadLocal();
        useThreadLocal.startTreadArray();
    }
}
