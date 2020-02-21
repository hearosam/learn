package com.sam.cn.tools;

import java.util.concurrent.Semaphore;

/**
 * 演示Semaphore信号量基本使用
 * 控制同时访问某个特定资源的线程数量，用在流量控制
 * 适用场景：
 * 比如说，又到了十一假期，买票是重点，必须圈起来。在购票大厅里，有5个售票窗口，也就是说同一时刻可以服务5个人。要实现这种业务需求，用synchronized显然不合适
 * 下面代码实现的场景也比较符合
 * @author sam.liang
 */
public class SemaphoreTest {
    //运行代码课件useful和unless共享所有凭证（permits）
    //可用连接数
    private static Semaphore useful = new Semaphore(10);
    //已用连接数
    private static Semaphore unless = new Semaphore(0);

    public static void main(String[] args) {

        for (int i = 0; i < 10; i++) {
            new Thread(new GetConnection(),"请求连接线程"+i).start();
        }
        for (int i = 0; i < 10; i++) {
            new Thread(new releaseConnection(),"释放线程"+i).start();
        }
    }

    private static class GetConnection implements Runnable {
        @Override
        public void run() {
            try {
                System.out.println("获取连接前：可用连接数："+useful.availablePermits()+",已用连接数："+unless.availablePermits());
                useful.acquire();
                Thread.sleep(100);
                unless.release();
                System.out.println("获取连接后：可用连接数："+useful.availablePermits()+",已用连接数："+unless.availablePermits());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    private static class releaseConnection implements Runnable{

        @Override
        public void run() {
            //释放连接
            try {
                System.out.println("释放连接前：可用连接数："+useful.availablePermits()+",已用连接数："+unless.availablePermits());
                unless.acquire();
                Thread.sleep(100);
                useful.release();
                System.out.println("释放连接后：可用连接数："+useful.availablePermits()+",已用连接数："+unless.availablePermits());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
