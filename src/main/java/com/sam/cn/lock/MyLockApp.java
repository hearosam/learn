package com.sam.cn.lock;

import java.util.Date;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MyLockApp {

    public static void main(String[] args) {
        MyLock lock = new MyLock();
        Lock lock1 = new ReentrantLock();

        for (int i = 0; i < 10; i++) {
            new MyThread(lock,"Thread-"+i).start();
        }
    }
   static class MyThread extends Thread{
       Lock lock;
       public MyThread(Lock lock, String name) {
           super(name);
           this.lock = lock;
       }
       @Override
       public void run() {
           lock.lock();
           try {
               System.out.println("Thread " + Thread.currentThread().getName() + " do something thing..." + new Date());
               Thread.sleep(1000);
           } catch (InterruptedException e) {
               e.printStackTrace();
           } finally {
               lock.unlock();
           }
       }
   }
}
