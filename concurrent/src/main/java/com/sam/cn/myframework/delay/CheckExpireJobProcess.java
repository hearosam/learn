package com.sam.cn.myframework.delay;

import com.sam.cn.myframework.PendingJobPool;

import java.util.concurrent.DelayQueue;

/**
 * 检查过期作业线程，这里设置为守护线程
 * @author sam.liang
 */
public class CheckExpireJobProcess {

    private static DelayQueue<JobItem> jobDelayQueue;

    //过期缓存队列
    public CheckExpireJobProcess(DelayQueue<JobItem> jobDelayQueue) {
        this.jobDelayQueue = jobDelayQueue;
    }

    /**
     * 处理队列中过期的任务
     */
    private static class FetchJob implements Runnable{

        @Override
        public void run() {
            while (true) {
                try {
                    JobItem jobItem = jobDelayQueue.take();
                    String jobName = jobItem.getJobName();
                    PendingJobPool.getInstance().getJobContextMap().remove(jobName);
                    System.out.println(jobName+" is out of date, remove of cache");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 将已经完成的作业放入过期检查缓存中
     * @param jobName
     * @param expireTime
     */
    public static void putJob(String jobName,long expireTime) {
        JobItem item = new JobItem(jobName,expireTime);
        jobDelayQueue.offer(item);
        System.out.println("job["+jobName+"],已经放入过期检查缓存，过期时长："+expireTime);
    }

    static {
        Thread thread = new Thread(new FetchJob());
        thread.setDaemon(true);
        thread.start();
        System.out.println("开启任务过期检查守护线程.............");
    }
}
