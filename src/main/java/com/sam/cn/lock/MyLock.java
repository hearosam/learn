package com.sam.cn.lock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * 基於aqs自定义实现同步锁
 */
public class MyLock implements Lock {

    private static final Sync sync = new Sync();
    /**
     * Sync继承aqs，重写aqs中获取同步状态的几个方法（因为是排它锁，重写阻塞方法即可）
     * 同步状态 state
     *  0：表示当前锁已经释放，其他线程可以竞争锁
     *  1：表示当前锁已经被其他线程独占，当前请求锁的线程只能进入同步队列进行等待
     */
    static class Sync extends AbstractQueuedSynchronizer{
        /**
         * 获取锁,获取到锁将状态从0修改为1，修改成功则将自身的线程设置为当前用有锁的线程
         * @param arg
         * @return
         */
        @Override
        protected boolean tryAcquire(int arg) {
            if(compareAndSetState(0,1)) {
                setExclusiveOwnerThread(Thread.currentThread());
                System.out.println(Thread.currentThread().getName()+"-------竞争锁成功");
                return true;
            }
            System.out.println(Thread.currentThread().getName()+"-------竞争锁失败");
            return false;
        }
        /**
         * 释放锁,将当前拥有锁的线程置位null
         * 释放锁的时候因为独占锁只有一个线程会获取到锁，所以释放锁时只有一个线程修改同步状态
         * 所以不需要用cas去修改同步状态
         * @param arg
         * @return
         */
        @Override
        protected boolean tryRelease(int arg) {
            if(getState()==0) {
                throw new UnsupportedOperationException();
            }
            System.out.println(Thread.currentThread().getName()+"-------释放锁");
            setState(0);
            setExclusiveOwnerThread(null);
            return true;
        }
        /**
         * 如果同步进行返回true
         * 否则返回false
         * 这里实现同步锁，所以就直接返回true了
         */
        @Override
        protected boolean isHeldExclusively() {
            return getState() == 1;
        }
        /**
         * 支持在当前这个同步锁上面设置条件
         * @return
         */
        protected Condition newCondition() {
            return new ConditionObject();
        }
    }

    @Override
    public void lock() {
        //加锁
        sync.acquire(1);
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {
        //加锁，可响应中断
        sync.acquireInterruptibly(1);
    }

    @Override
    public boolean tryLock() {
        //尝试加锁
        return sync.tryAcquire(1);
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        //加锁，可超时
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
