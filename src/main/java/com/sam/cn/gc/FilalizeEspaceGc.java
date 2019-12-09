package com.sam.cn.gc;

/**
 * 此代碼演示2點
 * 1.對象可以在gc前自我拯救
 * 2.这种救赎的机会只有一次，因为任何一个对象的finalize()方法只会被系统自动调用一次
 * @author sam.liang
 */
public class FilalizeEspaceGc {

    private static FilalizeEspaceGc SAVE_HOOK = null;

    public void isAlive(){
        System.out.println("yes i am alive :)");
    }

    /**
     * 系统在用可达性算法分析对象是否存活的时候会将不可达的对象放置到一个叫F-Queue的队列中
     * 稍后系统会自动启动一个级别比较低的Finalizer线程去依次执行在F-Queue里的所有对象的finalize()方法
     * 任何一个对象的finalize()方法只能被系统自动执行依次
     * @throws Throwable
     */
    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        System.out.println("finalize is excute ");
        SAVE_HOOK = this;
    }

    public static void main(String[] args) throws InterruptedException {
        SAVE_HOOK = new FilalizeEspaceGc();
        //置空，断开引用
        SAVE_HOOK = null;
        //调用System.gc()方法，提示jvm可以进行gc操作
        System.gc();
        Thread.sleep(500);
        if (SAVE_HOOK != null ) {
            SAVE_HOOK.isAlive();
        }else {
            System.out.println("no i am dead :(");
        }

        //置空，断开引用
        SAVE_HOOK = null;
        //调用System.gc()方法，提示jvm可以进行gc操作
        System.gc();
        Thread.sleep(500);
        if (SAVE_HOOK != null ) {
            SAVE_HOOK.isAlive();
        }else {
            System.out.println("no i am dead :(");
        }
    }
}
