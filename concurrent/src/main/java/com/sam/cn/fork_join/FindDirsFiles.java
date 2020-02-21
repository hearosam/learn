package com.sam.cn.fork_join;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

/**
 * fork/join遍历目录（execute方式提交任务）
 * @author sam.liang
 */
public class FindDirsFiles  extends RecursiveAction {
    private File filePath;
    public FindDirsFiles(File filePath) {
        this.filePath = filePath;
    }

    public static void main(String[] args) {
        ForkJoinPool pool = new ForkJoinPool();
        FindDirsFiles fatherTask = new FindDirsFiles(new File("F:\\Bypass"));
        //异步调用
        pool.execute(fatherTask);
        int ortherWork = 0;
        for(int i = 0 ; i<1000;i++) {
            ortherWork+=i;
        }
        System.out.println("main thread ortherWork result"+ortherWork);
        //此处阻塞主线程，等待任务执行完毕
        fatherTask.join();
        System.out.println("task end");
    }
    @Override
    protected void compute() {
        File[] files = this.filePath.listFiles();
        List<FindDirsFiles> taskList = new ArrayList<>();
        for (File file : files) {
            if (file.isDirectory()) {
                taskList.add(new FindDirsFiles(file));
            }else {
                if (file.getAbsolutePath().endsWith("txt")) {
                    System.out.println("文件名称："+file.getName());
                }
            }
        }
        if (!taskList.isEmpty()) {
            for (FindDirsFiles sonTask : invokeAll(taskList)) {
                sonTask.join();//等待子任务完成
            }
        }
    }
}
