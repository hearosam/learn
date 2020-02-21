package com.sam.cn.fork_join;

import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;

/**
 * fork/join数组求和
 * @author sam.liang
 */
public class ForkJoinSumArray {
    //统计数组的长度
    private static final int ARRAYLISTSIZE = 100000000;
    //每个task的工作量
    private static final int TASKSIZE = ARRAYLISTSIZE/150;

    private static class SumTask extends RecursiveTask<Long>{
        //开始下标
        private int startIndex;
        //结束下标
        private int endIndex;
        //当前task需要计算的工作量
        private int src[];

        public SumTask(int startIndex, int endIndex, int[] src) {
            this.startIndex = startIndex;
            this.endIndex = endIndex;
            this.src = src;
        }

        @Override
        protected Long compute() {
            if (endIndex - startIndex <= TASKSIZE) {
                long sum = 0;
                for (int i = this.startIndex;i<this.endIndex;i++) {
                    sum+=this.src[i];
                }
                return sum;
            }else {
                //二分法
                int midIndex = (startIndex + endIndex)/2;
                //任务划分
                SumTask leftJoin = new SumTask(this.startIndex,midIndex,this.src);
                SumTask rightJoin = new SumTask((midIndex+1),this.endIndex,this.src);
                //执行划分后的任务，invokeAll底层会调用--->task.compute()方法
                invokeAll(leftJoin,rightJoin);
                return leftJoin.join()+rightJoin.join();
            }
        }
    }
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        //生成数组
        int[] srcArray = new int[ARRAYLISTSIZE];
        Random random = new Random();
        for (int i = 0; i < ARRAYLISTSIZE; i++) {
            srcArray[i] = random.nextInt(ARRAYLISTSIZE);
        }
        long start = System.currentTimeMillis();

        ForkJoinPool pool = new ForkJoinPool();
        SumTask sumTask = new SumTask(0,srcArray.length-1,srcArray);
        /**
         * ForkJoinTask有三种提交任务的方式
         * 1.invoke(task)  //同步提交，提交任务的时候调用了task.join()方法，join方法会造成主线程挂起，直到返回计算结果
         * 2.submit(task) //异步提交，提交任务的时候没有调用task.join()方法，有返回结果，可以通过ForkJoinTask.get()方法获取最终计算结果，get()方法会阻塞
         * 3.execute(task) //异步提交，没有返回结果
         */
        Long invoke = pool.invoke(sumTask);
//        ForkJoinTask<Long> submit = pool.submit(sumTask);
//        long result = submit.get();
//        long result = 0;
//        for (int i = 0; i < srcArray.length; i++) {
//            result+=srcArray[i];
//        }
//        long result = submit.get();
        System.out.println("计算结果："+invoke+"--->耗时:"+(System.currentTimeMillis()-start));
    }
}
