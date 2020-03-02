package com.sam.cn.myproject.entity;

/**
 * 缓存中的题目实体对象
 * @author sam.liang
 */
public class QuestionInCache {
    /**
     * id
     */
    private int id;
    /**
     * 题目内容
     */
    private String detail;
    /**
     * 题目内容摘要
     */
    private String sha;

    public QuestionInCache(int id, String detail, String sha) {
        this.id = id;
        this.detail = detail;
        this.sha = sha;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getSha() {
        return sha;
    }

    public void setSha(String sha) {
        this.sha = sha;
    }
}
