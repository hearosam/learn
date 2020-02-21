package com.sam.cn.tools;

import java.util.Random;
import java.util.concurrent.CountDownLatch;

/**
 * 演示CountDownLatch使用
 * 模拟王者荣耀游戏开始之前的过程（所有玩家都确定了游戏才能开始）
 * @author sam.liang
 */
public class CountDownLatchTest {

    public static void main(String[] args) throws InterruptedException {
        CountDownLatch start = new CountDownLatch(10);
        for (int i = 0; i < start.getCount(); i++) {
            new Thread(new Player(start),"玩家"+i).start();
        }
        start.await();
        System.out.println("所有玩家准备完毕，开始游戏");
    }

    private static class Player implements Runnable {
        private CountDownLatch start;

        public Player(CountDownLatch start) {
            this.start = start;
        }
        @Override
        public void run() {
            try {
                Random random = new Random();
                //准备时间
                int readyTime = random.nextInt(1000);
                Thread.sleep(readyTime);
                System.out.println(Thread.currentThread().getName()+"准备好了，耗时(毫秒)"+readyTime);
                this.start.countDown();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
