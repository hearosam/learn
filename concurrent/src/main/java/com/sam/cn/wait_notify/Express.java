package com.sam.cn.wait_notify;

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

    public Express(int km, String site) {
        this.km = km;
        this.site = site;
    }

    //变化公里数，然后通知wait状态并处理公里数的线程进行业务处理
    public synchronized void changeKM() {
        this.km=101;
        notifyAll();
    }

    //变化里程数，然后通知wait状态并处理里程数的线程进行业务处理
    public synchronized void changeSite(){
        this.site = "beijing";
        notify();
    }

    public synchronized void waitKM() {
        while (this.km <= 100) {
            try {
                wait();
                System.out.println("check km thread ["+Thread.currentThread().getName()+"] is be notified");
            }catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
            System.out.println("the km is "+this.km + ",i will change in db");
        }
    }

    public synchronized void waitSite() {
        while (CITY_SITE.equals(this.site)) {
            try {
                wait();
                System.out.println("check site thread["+Thread.currentThread().getName()+"] is be notified");
            }catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
            System.out.println("the site is "+this.site+",i will change in db");
        }
    }
}
