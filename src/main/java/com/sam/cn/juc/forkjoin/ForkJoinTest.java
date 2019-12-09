package com.sam.cn.juc.forkjoin;

import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;

/**
 * fork/join 演示
 * @author sam.liang
 */
public class ForkJoinTest {

    /**
     * 产生整型数组工具类
     */
    public static class ArrayUtil{
        private static int MAX_LENGTH = 400000;

        public ArrayUtil() {}

        public ArrayUtil(int maxLength) {
            MAX_LENGTH = maxLength;
        }

        public static int[] getMyArray() {
            int[] myArray = new int[MAX_LENGTH];
            Random random = new Random();
            for(int i=0;i<MAX_LENGTH;i++) {
                myArray[i] =  (random.nextInt(1000)+1);
//                myArray[i] =  i;
            }
            return myArray;
        }
    }
    /**
     * fork join 任务类
     */
    public static class MyTask extends RecursiveTask<Integer> {
        private int[] array;
        private int start;
        private int end;
        //每个任务处理工作量大小
        private static final  int TASK_SIZE = 100;

        public MyTask(int[] array,int start,int end) {
            this.array = array;
            this.start = start;
            this.end = end;
        }
        @Override
        protected Integer compute() {
            if(end - start <= TASK_SIZE) {
                int sum = 0;
                for(int i = start;i<end;i++) {
                    sum+=array[i];
                }
                return sum;
            }else{
                //当前任务量大小还没有打到阈值，继续切割任务
                int mid = (start+end)/2;
                MyTask leftJoin = new MyTask(array, start, mid);
                MyTask rightJoin = new MyTask(array, mid, end);
                //并行执行两个小任务
                leftJoin.fork();
                rightJoin.fork();
                //返回两个小任务结果
                return leftJoin.join() + rightJoin.join();
            }
        }
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        int[] myArray = ArrayUtil.getMyArray();
        //fork join 计算演示
        long start1 = System.currentTimeMillis();
        MyTask task = new MyTask(myArray,0,myArray.length);
        ForkJoinPool pool = new ForkJoinPool();
        ForkJoinTask<Integer> submit = pool.submit(task);
        long end1 = System.currentTimeMillis();
        System.out.println("forkjoin计算结果"+submit.get()+";耗时："+(end1-start1));
        //演示结束
        //遍历累计演示
        int sum = 0;
        long start2 = System.currentTimeMillis();
        for(int i = 0;i<myArray.length;i++) {
            sum+=myArray[i];
        }
        long end2 = System.currentTimeMillis();
        System.out.println("单线程循环遍历累计结果"+sum+";耗时："+(end2-start2));
    }
}
