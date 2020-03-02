package com.sam.cn.myproject.entity;

import java.util.List;

/**
 * 延时处理文档类
 * @author sam.liang
 */
public class PendingDoc {
    private String name;
    /**
     * 文档列表
     */
    private List<Integer> questionInDBList;

    public PendingDoc(String name, List<Integer> questionInDBList) {
        this.name = name;
        this.questionInDBList = questionInDBList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Integer> getQuestionInDBList() {
        return questionInDBList;
    }

    public void setQuestionInDBList(List<Integer> questionInDBList) {
        this.questionInDBList = questionInDBList;
    }
}
