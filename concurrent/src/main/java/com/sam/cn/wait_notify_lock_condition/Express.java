package com.sam.cn.wait_notify_lock_condition;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 快递实体类
 * @author sam.liang
 */
public class Express {
    public static final String CITY_SITE = "shanghai";
    //快递运输里程数
    private int km;
    //快递运输到达地点
    private String site;
    private Lock kmLock = new ReentrantLock();
    private Lock siteLock = new ReentrantLock();

    Condition kmCondition = kmLock.newCondition();
    Condition siteCondition = siteLock.newCondition();

    public Express(int km, String site) {
        this.km = km;
        this.site = site;
    }

    //变化公里数，然后通知wait状态并处理公里数的线程进行业务处理
    public void changeKM() {
        kmLock.lock();
        try {
            this.km=101;
//        notifyAll();
            kmCondition.signal();
        }finally {
            kmLock.unlock();
        }

    }

    //变化里程数，然后通知wait状态并处理里程数的线程进行业务处理
    public void changeSite(){
        siteLock.lock();
        try {
            this.site = "beijing";
//        notify();
            siteCondition.signal();
        }finally {
            siteLock.unlock();
        }
    }

    public void waitKM() {
        kmLock.lock();
        try {
            while (this.km <= 100) {
                try {
//                wait();
                    kmCondition.await();
                    System.out.println("check km thread ["+Thread.currentThread().getName()+"] is be notified");
                }catch (InterruptedException e) {
                    System.out.println(e.getMessage());
                }
                System.out.println("the km is "+this.km + ",i will change in db");
            }
        }finally {
            kmLock.unlock();
        }
    }

    public void waitSite() {
        siteLock.lock();
        try {
            while (CITY_SITE.equals(this.site)) {
                try {
//                wait();
                    siteCondition.await();
                    System.out.println("check site thread["+Thread.currentThread().getName()+"] is be notified");
                }catch (InterruptedException e) {
                    System.out.println(e.getMessage());
                }
                System.out.println("the site is "+this.site+",i will change in db");
            }
        }finally {
            siteLock.unlock();
        }
    }
}
