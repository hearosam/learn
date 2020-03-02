package com.sam.cn.myproject.service;

import com.sam.cn.myproject.entity.QuestionInDB;
import com.sam.cn.myproject.tools.SleepTool;

import java.util.Random;

/**
 * 单个题目处理器
 * @author sam.liang
 */
public class BaseQuestionProcessor {

    /**
     * 对题目进行处理，如解析文本，下载图片等
     * @param questionInDB 题目详情
     * @return 题目处理后的文本数据
     */
    public static String makeQuestion(QuestionInDB questionInDB) {
        Random random = new Random();
        //模拟业务处理耗时
        SleepTool.buisness(450+random.nextInt(100));
        return "CompleteQuestion[id="+questionInDB.getId()+" content=:"+questionInDB.getDetail()+"]";
    }
}
