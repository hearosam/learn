package com.sam.cn.pool;

import java.sql.Connection;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * wait/notify等待超时模式实现数据库连接池演示程序
 */
public class TestPool {

    //初始化一个10个连接的连接池
    static DBPool pool = new DBPool(30);

    static CountDownLatch end ;
    public static void main(String[] args) throws InterruptedException {
        int workThread = 50 ;
        end = new CountDownLatch(workThread);
        int count = 20;
        AtomicInteger got = new AtomicInteger();
        AtomicInteger notGot = new AtomicInteger();
        for (int i = 0; i < workThread; i++) {
            new Thread(new Worker(count,got,notGot),"worker_"+i).start();
        }
        end.await();
        System.out.println("总共尝试次数"+(workThread * count));
        System.out.println("成功获取连接次数"+got.get());
        System.out.println("获取连接失败次数"+notGot.get());
    }

    /**
     * 工作线程
     */
    private static class Worker implements Runnable{
        //尝试获取次数
        int count;
        //获取成功次数
        AtomicInteger got;
        //获取失败次数
        AtomicInteger notGot;

        public Worker(int count, AtomicInteger got, AtomicInteger notGot) {
            this.count = count;
            this.got = got;
            this.notGot = notGot;
        }

        @Override
        public void run() {
             while (count>0) {
                 try {
                     Connection connection = pool.fetchConnection(100);
                     if (connection != null) {
                         try {
                             System.out.println(Thread.currentThread().getName()+"----->获取连接成功");
                             connection.createStatement();
                             connection.commit();
                         }finally {
                             pool.releaseConn(connection);
                             got.incrementAndGet(); //获取成功次数+1
                         }
                     }else {
                         notGot.incrementAndGet();//获取失败次数+1
                         System.out.println(Thread.currentThread().getName()+"------>等待超时！");
                     }
                 }catch (Exception e) {
                     System.out.println(e.getMessage());
                     notGot.incrementAndGet();//获取失败次数+1
                 }finally {
                     count--;
                 }
             }
             end.countDown();
        }
    }
}
