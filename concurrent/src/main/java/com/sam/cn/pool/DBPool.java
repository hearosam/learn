package com.sam.cn.pool;

import java.sql.Connection;
import java.util.LinkedList;

/**
 * wait/notify等待超时模式连接池实现
 * @author sam.liang
 */
public class DBPool {
    /*
    初始化连接池
    获取连接
    释放连接
     */
    //数据库连接池容器
    private static LinkedList<Connection> pool = new LinkedList<>();

    /**
     * 连接池初始化方法
     * @param initalSize 初始化连接数量
     */
    public DBPool(int initalSize) {
        if (initalSize>0) {
            for (int i = 0; i < initalSize; i++) {
                pool.addFirst(SqlConnectionImpl.fetchConnection());
            }
        }
    }
    /**
     * 在指定时间内获取数据库连接，如果超时了就直接返回null
     * @param remaining 超时时间(单位毫秒)
     * @return Connection
     */
    public Connection fetchConnection(int remaining) throws InterruptedException {
        synchronized (pool) {
            if(remaining<0) {
                while (pool.isEmpty()) {
                    pool.wait();
                }
                return pool.removeLast();
            }else {
                //系统当前时间
                long mills = System.currentTimeMillis();
                //超时时间
                long overTime = mills + remaining;
                while (pool.isEmpty() && overTime >0 ) {
                    pool.wait(remaining);
                    overTime = overTime - System.currentTimeMillis();
                }
                Connection result = null;
                if (!pool.isEmpty()) {
                    result = pool.removeLast();
                }
                return result;
            }
        }
    }

    /**
     * 释放连接
     * @param conn
     */
    public void releaseConn(Connection conn) {
        if (conn!=null) {
          synchronized (pool) {
              pool.addFirst(conn);
              pool.notifyAll();
          }
        }
    }
}
