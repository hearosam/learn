package com.sam.cn.myproject.service;

import com.sam.cn.myproject.entity.QuestionInDB;
import com.sam.cn.myproject.tools.SQL_Question_Bank;

/**
 * 调用对单个题目进行处理服务类
 */
public class SingleQstService {

    /**
     * 模拟：业务方法到数据库查询题目详情数据，并且调用业务方法处理题目中可能存在的图片下载等方法
     * @param questionId 题目id
     * @return 处理好的题目文本内容
     */
    public static String makeQuestion(Integer questionId) {
        QuestionInDB questionInDB = SQL_Question_Bank.getQuestionInDB(questionId);
        if(questionInDB != null) {
            return BaseQuestionProcessor.makeQuestion(questionInDB);
        }else {
            return "";
        }
    }
}
