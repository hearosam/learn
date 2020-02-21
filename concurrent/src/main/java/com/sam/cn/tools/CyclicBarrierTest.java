package com.sam.cn.tools;

import java.util.Random;
import java.util.concurrent.CyclicBarrier;

/**
 * CyclicBarrier演示
 * 假设公司举行团建活动，活动内容：跨栏比赛计算总时间
 * 规则：一个小组所有队员跨过上一个栏才能夸下一个栏（跨完栏后必须等待队友跨完了才能跨下一个）
 * @author sam.liang
 */
public class CyclicBarrierTest {

    public static void main(String[] args) {

        //parties:表示屏障拦截的线程数量
        CyclicBarrier barrier = new CyclicBarrier(3);
        for (int i = 1; i <= barrier.getParties(); i++) {
            new Thread(new Player(barrier),"队友"+i).start();
        }
    }
    private static class Player implements Runnable{

        private CyclicBarrier barrier;
        public Player(CyclicBarrier barrier) {
            this.barrier = barrier;
        }

        @Override
        public void run() {
            for (int i = 1; i <= 3; i++) {
                try {
                    Random random = new Random();
                    int sleepTime = random.nextInt(1000);
                    Thread.sleep(sleepTime);
                    System.out.println(Thread.currentThread().getName()+"跨越了第"+i+"个跨栏，花费时间(毫秒)："+sleepTime);
                    this.barrier.await();
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }
}
