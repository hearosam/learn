package com.sam.cn.custom_lock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * 自定义可重入锁
 * @author sam.liang
 */
public class CustomLock implements Lock {

    //state 表示同步状态 state=1 获取到了锁，state=0，表示这个锁当前没有线程拿到
    private static class Sync extends AbstractQueuedSynchronizer{
        /**
         * 请求锁
         * @param arg
         * @return
         */
        @Override
        protected boolean tryAcquire(int arg) {
            //设置同步状态
            if(compareAndSetState(0,1)) {
                //独占模式：设置当前线程为同步状态所有者
                setExclusiveOwnerThread(Thread.currentThread());
                return true;
            }
            return false;
        }

        @Override
        protected boolean tryRelease(int arg) {
            //获取当前同步状态，如果同步状态为初始状态就不存在释放操作，抛出异常
            if(getState() == 0) {
                throw  new UnsupportedOperationException();
            }
            //独占模式：设置当前同步状态拥有者为null
            setExclusiveOwnerThread(null);
            //设置同步状态为初始状态
            setState(0);
            return true;
        }

        /**
         * 是否在独占模式下被线程占用
         * @return
         */
        @Override
        protected boolean isHeldExclusively() {
            return getState() == 1;
        }

        Condition newCondition() {
            return newCondition();
        }
    }

    //单例
    private final Sync sync = new Sync();

    @Override
    public void lock() {
        sync.acquire(0);
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {
        sync.acquireInterruptibly(1);
    }

    @Override
    public boolean tryLock() {
        return sync.tryAcquire(1);
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return sync.tryAcquireNanos(1,unit.toNanos(time));
    }

    @Override
    public void unlock() {
        sync.release(0);
    }

    @Override
    public Condition newCondition() {
        return sync.newCondition();
    }
}
