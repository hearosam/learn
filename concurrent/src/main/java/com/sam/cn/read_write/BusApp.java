package com.sam.cn.read_write;

import java.util.Random;

/**
 * 演示类
 * @author sam.liang
 */
public class BusApp {

    private static class GetGoodsInfoThread implements Runnable{

        private GoodsService service;

        public GetGoodsInfoThread(GoodsService service) {
            this.service = service;
        }

        @Override
        public void run() {
            long start = System.currentTimeMillis();
            this.service.getGoodsInfo();
            System.out.println(Thread.currentThread().getName()+"查询商品信息，耗时"+(System.currentTimeMillis() - start));
        }
    }

    private static class ComputeGoodsAmountThread implements Runnable{

        private GoodsService service;
        private int goodsNum;

        public ComputeGoodsAmountThread(GoodsService service,int goodsNum) {
            this.service = service;
            this.goodsNum = goodsNum;
        }

        @Override
        public void run() {
            long start = System.currentTimeMillis();
            this.service.computeAmount(this.goodsNum);
            System.out.println(Thread.currentThread().getName()+"修改了商品数量，耗时"+(System.currentTimeMillis() - start));
        }
    }

    private static class StartGetThread implements Runnable {

        private GoodsService service;

        public StartGetThread(GoodsService service) {
            this.service = service;
        }

        @Override
        public void run() {
            for (int i = 0; i < 100; i++) {
                new Thread(new GetGoodsInfoThread(service),"客户"+i).start();
            }
        }
    }
    private static class StartComputeThread implements Runnable {

        private GoodsService service;
        Random random = new Random();

        public StartComputeThread(GoodsService service) {
            this.service = service;
        }

        @Override
        public void run() {
            for (int i = 0; i < 50; i++) {
                new Thread(new ComputeGoodsAmountThread(service,(random.nextInt(10)+1)),"客户"+i).start();
            }
        }
    }

    public static void main(String[] args) {

        GoodsInfo goodsInfo = new GoodsInfo("秒杀商品",11.3,100);
        GoodsService service = new GoodsServiceRwLockImpl(goodsInfo);
        new Thread(new StartComputeThread(service)).start();
        new Thread(new StartGetThread(service)).start();


    }
}
