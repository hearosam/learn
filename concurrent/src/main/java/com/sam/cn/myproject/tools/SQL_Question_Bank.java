package com.sam.cn.myproject.tools;

import com.sam.cn.myproject.common.Constant;
import com.sam.cn.myproject.entity.QuestionInDB;

import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 *题目初始化工具类
 * @author sam.liang
 */
public class SQL_Question_Bank {
    //数据库中的题库，key就是题目id，value就是题目详情
    private static ConcurrentHashMap<Integer, QuestionInDB> questionBankMap = new ConcurrentHashMap<>();
    //定时任务池，负责定时更新题库数据
    private static ScheduledExecutorService updateQuestionBank = new ScheduledThreadPoolExecutor(1);

    /**
     * 初始化题库
     */
    public static void initBank() {
        for (int i = 0; i < Constant.SIZE_OF_QUESTION_BANK; i++) {
            String questionContent = makeQuestionDetail(Constant.QUESTION_LENGTH);
            //生成文章摘要
            String sha = EncryptUtils.EncryptByMD5(questionContent);
            QuestionInDB questionInDB = new QuestionInDB(i,questionContent,sha);
            questionBankMap.put(i,questionInDB);
        }
    }

    /**
     * 获取题目详情
     * @param index
     * @return
     */
    public static QuestionInDB getQuestionInDB(int index) {
        return questionBankMap.get(index);
    }

    /**
     * 获取题目摘要方法
     * @param index
     * @return
     */
    public static String getQuestionSha(int index) {
        QuestionInDB questionInDB = questionBankMap.get(index);
        return questionInDB.getSha();
    }


    /**
     * 生成题目内容，
     * @param questionLength 题目长度
     * @return 题目内容
     */
    private static String makeQuestionDetail(int questionLength) {
        String base = "abcdefghijklmnopqrstuvwxyz";
        StringBuffer sb = new StringBuffer();
        Random random = new Random();
        for (int i = 0; i < questionLength; i++) {
            sb.append(base.charAt(random.nextInt(base.length())));
        }
        return sb.toString();
    }
}
