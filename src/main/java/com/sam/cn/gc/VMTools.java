package com.sam.cn.gc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * JCONSOLE工具使用案例
 */
public class VMTools {

    /**
     * 内存占位符对象，一个OOMObject大约占64k
     */
    static class OOMObject{
        public byte[] placeholder = new byte[64*1024];
    }

    public static void fillMap(int num) throws InterruptedException {
        List<OOMObject> list = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            //稍作延迟，使得监视曲线变化更加明显
            Thread.sleep(50);
            list.add(new OOMObject());
        }
        System.gc();
    }
    public static void main(String[] args) throws InterruptedException, IOException {
        /**
         * -verbose:gc-Xms100M -Xmx100M -XX:+UseSerialGC
         */
//        fillMap(1000);
        BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
        br.readLine();
        createBusyThread();
        br.readLine();
        Object o = new Object();
        createLockThread(o);
    }

    /**
     * 线程等待演示代码
     */
    /**
     * 线程死循环
     */
    public static void createBusyThread() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true); //死循环
            }
        },"testBusyThread");
        thread.start();
    }
    /**
     * 线程锁等待演示
     */
    public static void createLockThread(Object object){
        new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (object) {
                    try {
                        object.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        },"testLockThread").start();
    }

}
