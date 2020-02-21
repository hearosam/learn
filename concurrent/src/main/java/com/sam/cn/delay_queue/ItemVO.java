package com.sam.cn.delay_queue;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * 订单VO
 * @author sam.liang
 */
public class ItemVO<T> implements Delayed {

    //到期时间(毫秒)
    private long activeTime;
    //负载数据
    private T data;

    public ItemVO(long activeTime, T data) {
        //将一个long类型的数字以毫秒为单位转纳秒
        this.activeTime = TimeUnit.NANOSECONDS.convert(activeTime,TimeUnit.MILLISECONDS)+System.nanoTime();
        this.data = data;
    }

    /**
     * 获取ItemVO延时剩余时间
     * @param unit
     * @return
     */
    @Override
    public long getDelay(TimeUnit unit) {
        return unit.convert(this.activeTime-System.nanoTime(),TimeUnit.NANOSECONDS);
    }

    /**
     * 按照剩余时间顺序排序（剩余时间越少越排到前面）
     * 0表示相等
     * 1表示大于
     * -1表示小于
     * @param o
     * @return
     */
    @Override
    public int compareTo(Delayed o) {
        //计算差值
        long difference = getDelay(TimeUnit.NANOSECONDS) - o.getDelay(TimeUnit.NANOSECONDS);
        return difference == 0 ? 0 : (difference>0 ? 1:-1);
    }

    public long getActiveTime() {
        return activeTime;
    }

    public T getData() {
        return data;
    }
}
