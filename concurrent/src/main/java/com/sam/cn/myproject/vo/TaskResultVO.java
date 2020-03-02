package com.sam.cn.myproject.vo;

import com.sam.cn.myproject.entity.QuestionInCache;

import java.util.concurrent.Future;

/**
 * 生成题目时候返回的结果定义
 * @author sam.liang
 */
public class TaskResultVO {
    private String questionDetail;
    private Future<QuestionInCache> questionFuture;

    public TaskResultVO(String questionDetail) {
        this.questionDetail = questionDetail;
    }

    public TaskResultVO(Future<QuestionInCache> questionFuture) {
        this.questionFuture = questionFuture;
    }

    public String getQuestionDetail() {
        return questionDetail;
    }

    public void setQuestionDetail(String questionDetail) {
        this.questionDetail = questionDetail;
    }

    public Future<QuestionInCache> getQuestionFuture() {
        return questionFuture;
    }

    public void setQuestionFuture(Future<QuestionInCache> questionFuture) {
        this.questionFuture = questionFuture;
    }
}
