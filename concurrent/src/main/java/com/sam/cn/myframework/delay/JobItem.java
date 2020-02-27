package com.sam.cn.myframework.delay;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * 延迟作业实体类
 */
public class JobItem implements Delayed {
    //作业名称
    private String jobName;
    //过期时间
    private long expireTime;

    public JobItem(String jobName, long expireTime) {
        this.jobName = jobName;
        //毫秒转纳秒
        this.expireTime = TimeUnit.NANOSECONDS.convert(expireTime,TimeUnit.MILLISECONDS) + System.nanoTime();
    }

    /**
     * 获取JobItem延时剩余时间
     * @param unit
     * @return
     */
    @Override
    public long getDelay(TimeUnit unit) {
        //纳秒转毫秒
        return unit.convert((System.nanoTime() - expireTime),TimeUnit.NANOSECONDS);
    }

    /**
     * 按照剩余时间顺序排序（剩余时间越少越排在前面）
     * @param o
     * @return
     */
    @Override
    public int compareTo(Delayed o) {
        long over = this.getDelay(TimeUnit.MILLISECONDS) - o.getDelay(TimeUnit.MILLISECONDS);
        return over == 0 ? 0 : (over > 0 ? 1 : -1);
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public long getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(long expireTime) {
        this.expireTime = expireTime;
    }
}
