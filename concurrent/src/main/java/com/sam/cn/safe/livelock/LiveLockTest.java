package com.sam.cn.safe.livelock;

/**
 * 活锁演示示例
 * 妻子和丈夫两人公用晚餐，但是同一时刻只能一个人使用唯一一个勺子用餐
 * 但是妻子和丈夫两人相互谦让，都想让对方先吃，导致勺子传递来传递去，双方都不能用餐
 * @author sam.liang
 */
public class LiveLockTest {

    public static void main(String[] args) throws InterruptedException {
        //创建wife跟husband两个对象，同时两个对象都饿了
        Diner wife = new Diner("wife",true);
        Diner husband = new Diner("husband",true);
        //将勺子的所有权让给妻子
        Spoon spoon = new Spoon(wife);

        Thread w = new Thread() {
            @Override
            public void run() {
                wife.eatWith(husband, spoon);
            }
        };
        w.start();
        Thread h = new Thread() {
            @Override
            public void run() {
                husband.eatWith(wife, spoon);
            }
        };
        h.start();

        Thread.sleep(1000);
        h.interrupt();
        w.interrupt();

        //join()方法阻塞调用此方法的线程(calling thread)，直到线程t完成，此线程再继续；通常用于在main()主线程内，等待其它线程完成再结束main()主线程。
        h.join();
        w.join();

    }
}

/**
 * 勺子类
 */
class Spoon {
    //当前勺子的拥有者
    private Diner owner;

    public Diner getOwner() {
        return owner;
    }

    public void setOwner(Diner owner) {
        this.owner = owner;
    }

    public Spoon(Diner owner) {
        this.owner = owner;
    }

    //正在用餐
    public void use() {
        System.out.println(this.owner.getName()+"use this spoon and finish eat!");
    }
}

/**
 * 用餐者实体类
 */
class Diner{
    public Diner(String name, boolean isHungry) {
        this.name = name;
        this.isHungry = isHungry;
    }

    //用餐者名称
    private String name;
    //是否饿了
    private boolean isHungry;

    //和配偶吃饭
    public void eatWith(Diner spouse,Spoon shareSpoon) {
        //
        try {
            synchronized (shareSpoon) {
                while (isHungry) {
                    while (!shareSpoon.getOwner().getName().equals(name)) {
                        shareSpoon.wait();
                    }
                    //spouse此时是饿了，把勺子分给他，并通知他可以用餐
                    if (spouse.isHungry) {
                        System.out.println("i am " + name + " my "+spouse.getName()+" is hungry , I should give it to him(her).");
                        shareSpoon.setOwner(spouse);
                        shareSpoon.notifyAll();
                    }else {
                        //用餐
                        shareSpoon.use();
                        shareSpoon.setOwner(spouse);
                        isHungry = false;
                    }
                    Thread.sleep(500);
                }
            }
        }catch (Exception e) {
            System.out.println(name +" is interrupted");
        }
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isHungry() {
        return isHungry;
    }

    public void setHungry(boolean hungry) {
        isHungry = hungry;
    }
}
