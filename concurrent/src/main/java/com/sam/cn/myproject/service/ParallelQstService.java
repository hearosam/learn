package com.sam.cn.myproject.service;

import com.googlecode.concurrentlinkedhashmap.ConcurrentLinkedHashMap;
import com.googlecode.concurrentlinkedhashmap.Weighers;
import com.sam.cn.myproject.common.Constant;
import com.sam.cn.myproject.entity.QuestionInCache;
import com.sam.cn.myproject.entity.QuestionInDB;
import com.sam.cn.myproject.tools.SQL_Question_Bank;
import com.sam.cn.myproject.vo.TaskResultVO;

import java.util.concurrent.*;

/**
 * 并发处理题目服务
 * @author sam.liang
 */
public class ParallelQstService {

    //已经处理题目的缓存 (使用google开源的ConcurrentHashMap),会自动清理最少使用的缓存数据
    //这里容量可以根据测试然后得到一个合理值
    private static ConcurrentLinkedHashMap<Integer, QuestionInCache> questionCache = new ConcurrentLinkedHashMap
            .Builder<Integer,QuestionInCache>()
            .maximumWeightedCapacity(200) //容量
            .weigher(Weighers.singleton()) //单例
            .build();
    //正在处理题目的缓存  正在处理的题目是不能自动清理的
    private static ConcurrentHashMap<Integer, Future<QuestionInCache>> processingQuestionCache = new ConcurrentHashMap<>();
    private static ExecutorService makeQuestionService = Executors.newFixedThreadPool(Constant.CPU_COUNT * 2);

    public static TaskResultVO makeQuestion(Integer questionId) {
        //先从缓存中取数据
        QuestionInCache questionInCache = questionCache.get(questionId);
        if (questionInCache == null) {
            System.out.println("....题目【"+questionId+"】在缓存中不存在，准备启动任务");
            return new TaskResultVO(getQstFuture(questionId));
        }else {
            //从数据库中获取摘要方法
            String questionSha = SQL_Question_Bank.getQuestionSha(questionId);
            if(questionInCache.getSha().equals(questionSha)) {
                System.out.println("题目【"+questionId+"】在缓存中发现。。。。。。");
                return new TaskResultVO(questionInCache.getDetail());
            }else {
                System.out.println("题目【"+questionId+"】在缓存中已经存在，但题目内容发生更改，更新缓存。。。。");
                return new TaskResultVO(getQstFuture(questionId));
            }
        }
    }

    /**
     * 生成计算任务
     * @param questionId
     * @return
     */
    private static Future<QuestionInCache> getQstFuture(Integer questionId) {
        //从正在处理的题目缓存中获取
        Future<QuestionInCache> questionInCacheFuture = processingQuestionCache.get(questionId);
        try{
            if (questionInCacheFuture == null) {
                //构造计算任务
                QuestionInDB questionInDB = SQL_Question_Bank.getQuestionInDB(questionId);
                QuestionTask task = new QuestionTask(questionInDB,questionId);
                FutureTask<QuestionInCache> ft = new FutureTask<>(task);
                questionInCacheFuture = processingQuestionCache.putIfAbsent(questionId,ft);
                if(questionInCacheFuture == null) {
                    //现在缓存map中占位
                    questionInCacheFuture = ft;
                    makeQuestionService.execute(ft);
                    System.out.println("成功启动了题目【"+questionId+"】的计算任务，请等待。。。。。");
                }else {
                    System.out.println("<<<<<<<<<<<有其他线程刚刚启动了题目【"+questionId+"】的计算任务，本人无无需启动>>>>>>>>>");
                }
            }else {
                System.out.println("题目【"+questionId+"】已经存在计算任务，无需重新生成计算任务");
            }
        }catch (Exception e) {
            processingQuestionCache.remove(questionId);
            System.out.println(e.getMessage());
            throw e;
        }
        return questionInCacheFuture;
    }

    /**
     * 解析题目任务类
     */
    private static class QuestionTask implements Callable<QuestionInCache>{
        private QuestionInDB questionInDB;
        private Integer questionId;

        public QuestionTask(QuestionInDB questionInDB, Integer questionId) {
            this.questionInDB = questionInDB;
            this.questionId = questionId;
        }

        @Override
        public QuestionInCache call() throws Exception {
            try {
                //解析题目内容(比如：下载图片)
                String questionContent = BaseQuestionProcessor.makeQuestion(questionInDB);
                QuestionInCache questionInCache = new QuestionInCache(questionId, questionContent, questionInDB.getSha());
                //将数据放入缓存
                questionCache.put(questionId,questionInCache);
                return questionInCache;
            } finally {
                //不管生成题目的任务正常与否，这个任务都要从正在处理题目的缓存中删除
                processingQuestionCache.remove(questionId);
            }
        }
    }
}
