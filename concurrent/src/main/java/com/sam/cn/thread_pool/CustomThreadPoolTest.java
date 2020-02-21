package com.sam.cn.thread_pool;

/**
 * 自定义线程池演示
 * @author sam.liang
 */
public class CustomThreadPoolTest {

    public static void main(String[] args) {
        CustomThreadPool pool = new CustomThreadPool();
        pool.execute(new Task());
    }

    private static class Task implements Runnable{
        @Override
        public void run() {
            System.out.println(Thread.currentThread().getName());
        }
    }
}
