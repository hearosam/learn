package com.sam.cn.daemon_thread;

/**
 * 守护线程&守护线程中的finally语句
 */
public class DaemonThread {

    private static class UseDaemonThread extends Thread {
        @Override
        public void run() {
            try {
                while (!isInterrupted()){
                    System.out.println("当前线程名称："+Thread.currentThread().getName() +"-------isInterrupted:"+isInterrupted());
                }
                System.out.println("当前线程名称："+Thread.currentThread().getName() +"-------isInterrupted:"+isInterrupted());
            }finally {
                System.out.println("当前线程名称："+Thread.currentThread().getName()+"----> finallys");
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        UseDaemonThread daemonThread = new UseDaemonThread();
        daemonThread.setDaemon(true);
        daemonThread.start();
        /**
         * 如果不调用interrupt()---> while一直循环--->主线程退出--->守护线程也会退出---->不会执行finally语句(守护线程会与主线程同生共死)
         */
//        daemonThread.interrupt();
        Thread.sleep(5);
    }
}
