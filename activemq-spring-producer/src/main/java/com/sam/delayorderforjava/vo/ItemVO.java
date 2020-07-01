package com.sam.delayorderforjava.vo;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

public class ItemVO<T> implements Delayed {
    //到期时间，传递进来的是过期时长，这里要将系统当前时间+时长=到期时间
    private long activeTime;
    private T data;//业务数据，泛型

    public ItemVO(long expireTime, T data) {
        this.activeTime = System.currentTimeMillis()+(expireTime * 1000);
        this.data = data;
    }

    public long getActiveTime() {
        return this.activeTime;
    }
    public T getData(){
        return this.data;
    }

    /**
     * 返回ItemVo剩余到期时长，时长大小由unit单位计算决定
     * @param unit 时间单位
     * @return 剩余时长
     */
    @Override
    public long getDelay(TimeUnit unit) {
        return unit.convert(this.activeTime-System.currentTimeMillis(),unit);
    }

    /**
     * 按照剩余时长排序，实际计算考虑精度为纳秒
     * @param o Delayed
     * @return 比较后的值
     */
    @Override
    public int compareTo(Delayed o) {
        long d = this.getDelay(TimeUnit.NANOSECONDS) - o.getDelay(TimeUnit.NANOSECONDS);
        return d==0 ? 0 : (d > 0 ? 1 : -1);
    }
}
