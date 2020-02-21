package com.sam.cn.wait_notify_lock_condition;

/**
 * 演示Lock和Condition实现等待通知模式
 * @author sam.liang
 */
public class TestExpress {
    private static Express express = new Express(5, Express.CITY_SITE);

    private static class CheckKM implements Runnable{
        @Override
        public void run() {
            express.waitKM();
        }
    }
    private static class CheckSite implements Runnable {

        @Override
        public void run() {
            express.waitSite();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        //checkKM checkSite分别启动三个线程
        for (int i = 0; i < 3; i++) {
            new Thread(new CheckKM()).start();
        }
        for (int i = 0; i < 3; i++) {
            new Thread(new CheckSite()).start();
        }
        Thread.sleep(1000);
        express.changeKM(); //notifyAll
    }
}
