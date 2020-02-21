package com.sam.cn.custom_lock;

/**
 * 测试自定义可重入锁
 */
public class TestCustomLockApp {

    public static void main(String[] args) {
        CustomLock lock = new CustomLock();
        for (int i = 0; i < 10; i++) {
            new Thread(new TestClass(lock),"线程"+i).start();
        }
    }

    private static class TestClass implements Runnable{
        private CustomLock lock;

        public TestClass(CustomLock lock) {
            this.lock = lock;
        }

        @Override
        public void run() {
            lock.lock();
            try {
                //一秒钟打印一次
                System.out.println(Thread.currentThread().getName()+"获取锁");
                Thread.sleep(1000);
            }catch (Exception e){
                System.out.println(e.getMessage());
            }finally {
                lock.unlock();
            }
        }
    }
}
