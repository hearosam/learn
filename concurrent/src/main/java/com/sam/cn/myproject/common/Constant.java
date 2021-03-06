package com.sam.cn.myproject.common;

/**
 * 系统公用常量
 * @author sam.liang
 */
public class Constant {
    /**
     * 缓存中对象数量：超过这个数量会使用LRU算法从缓存中清除过期对象
     */
    public static final int CACHE_SIZE = 200;
    /**
     * 每篇文档80道题目
     */
    public static final int DOC_QUESTION_SIZE = 80;
    /**
     * 每道题目800个字节
     */
    public static final int QUESTION_LENGTH = 800;
    /**
     * 题库大小
     */
    public static final int SIZE_OF_QUESTION_BANK = 2000;
    /**
     * 本机cpu核心数
     */
    public static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
}
