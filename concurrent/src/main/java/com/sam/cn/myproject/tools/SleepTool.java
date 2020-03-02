package com.sam.cn.myproject.tools;

/**
 * 线程休眠工具类,模拟业务耗时
 * @author sam.liang
 */
public class SleepTool {
    public static void buisness(int sleepTime) {
        try {
            Thread.sleep(sleepTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
