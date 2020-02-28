package com.sam.cn.myframework.test;

import com.sam.cn.myframework.PendingJobPool;
import com.sam.cn.myframework.endity.ResultInfo;
import java.util.List;
import java.util.Random;

/**
 * 并行框架演示实例
 */
public class TestApp {

    private static String JOB_NAME = "sum";
    private static int TASK_NUMBERS = 100;
    private static long EXPIRE_TIME = 5000;

    /**
     * 查询任务进度线程
     */
    private static class QueryThread implements Runnable{

        private PendingJobPool instance;
        private String jobName;

        public QueryThread(PendingJobPool instance,String jobName) {
            this.instance = instance;
            this.jobName = jobName;
        }
        @Override
        public void run() {
            List<ResultInfo<Integer>> taskDetail = instance.getTaskDetail(jobName);
            for (ResultInfo<Integer> detail : taskDetail) {
                System.out.println("reason: " + detail.getReason() + " data: " + detail.getReturnData() + " result type: " + detail.getResultType());
             }

        }
    }

    public static void main(String[] args) throws InterruptedException {
        PendingJobPool instance = PendingJobPool.getInstance();
        instance.registerJob(JOB_NAME,TASK_NUMBERS,new MyTask(),EXPIRE_TIME * 5);
        Random random = new Random();
        for (int i = 0; i < TASK_NUMBERS; i++) {
            instance.submitTask(JOB_NAME,random.nextInt(500));
        }
        //表示10秒钟后查询一次任务处理进度
        Thread.sleep(1000*10);
        //启动查询进度线程
        new Thread(new QueryThread(instance,JOB_NAME)).start();

    }
}
