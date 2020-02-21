package com.sam.cn.thread_pool;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * 自定义线程池实现
 * @author sam.liang
 */
public class CustomThreadPool {

    //线程池大小,默认值是10
    private static int pool_size = 10;
    //任务队列大小，默认值是20
    private static int queue_size = 20;
    //任务队列
    private static BlockingQueue<Runnable> taskQueue;
    //工作线程数组
    private static Worker[] workers;

    public CustomThreadPool(){
        this(pool_size,queue_size);
    }
    public CustomThreadPool(int pool_size, int queue_size) {

        this.pool_size = pool_size > 0 ? pool_size : this.pool_size;
        this.queue_size = queue_size > 0 ? queue_size : this.queue_size;
        //初始化工作任务队列
        taskQueue = new ArrayBlockingQueue<>(this.queue_size);
        workers = new Worker[this.pool_size];
        //初始化线程池线程数
        for (int i = 0; i < this.pool_size; i++) {
            workers[i] = new Worker();
            workers[i].start();
        }
    }

    /**
     * 提交任务的方法，这个方法直接将任务提交到队列里面就行，任务具体怎么执行，什么时候执行由线程池决定
     * @param task
     */
    public void execute(Runnable task) {
        if(task!=null) {
            try {
                taskQueue.put(task);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    /**
     * 销毁线程池
     */
    public void distroy(){
        System.out.println("distroy thread pool....");
        for (int i = 0 ; i < workers.length;i++) {
            //调用interrupt方法等待线程执行任务完毕后再中断线程，优雅停止线程
            workers[i].stopWorker();
            //help gc
            workers[i] = null;
        }
        //清空任务队列
        taskQueue.clear();
    }

    //worker
    private static class Worker extends Thread{

        @Override
        public void run() {
            while (!isInterrupted()) {
                try {
                    Runnable task = taskQueue.take();
                    /*
                        这里直接是调用Runnable的run方法就可以了，不用新开一个线程(调用start()方法)去执行run的逻辑
                     */
                    if (task!=null) {
                        task.run();
                        System.out.println("execute task  success .....");
                    }
                } catch (InterruptedException e) {
                    //如果程序抛出异常会自动重置停止线程的标志位，需要在catch里面再次调用interrupt方法再次设置标志位停止线程
                    interrupt();
                    e.printStackTrace();
                }
            }
        }

        /**
         * 每一个工作线程都提供一个中断线程的方法
         */
        public void stopWorker() {
            interrupt();
        }
    }

}
