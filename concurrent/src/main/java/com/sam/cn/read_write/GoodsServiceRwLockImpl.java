package com.sam.cn.read_write;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 *
 * @author sam.liang
 */
public class GoodsServiceRwLockImpl implements GoodsService {

    private ReadWriteLock lock = new ReentrantReadWriteLock();
    //读锁
    private Lock getLock = lock.readLock();
    //写锁
    private Lock setLock = lock.writeLock();

    private GoodsInfo goodsInfo;

    public GoodsServiceRwLockImpl(GoodsInfo goodsInfo) {
        this.goodsInfo = goodsInfo;
    }

    @Override
    public GoodsInfo getGoodsInfo() {
        getLock.lock();
        try{
            Thread.sleep(1000);
            return this.goodsInfo;
        }catch (Exception e){
            System.out.println(e.getMessage());
        }finally {
            getLock.unlock();
        }
        return null;
    }

    @Override
    public void computeAmount(int goodsNum) {
        setLock.lock();
        try{
            Thread.sleep(1000);
            this.goodsInfo.computeAmount(goodsNum);
        }catch (Exception e) {
            System.out.println(e.getMessage());
        }finally {
            setLock.unlock();
        }

    }
}
