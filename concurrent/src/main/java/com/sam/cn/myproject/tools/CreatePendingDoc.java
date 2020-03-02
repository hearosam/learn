package com.sam.cn.myproject.tools;

import com.sam.cn.myproject.common.Constant;
import com.sam.cn.myproject.entity.PendingDoc;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 创建文档
 */
public class CreatePendingDoc {


    /**
     * 创建文档
     * @param docSize 文档数量
     * @return 文档列表
     */
    public static List<PendingDoc> makePendingDoc(int docSize) {
        List<PendingDoc> docList = new ArrayList<>();
        List<Integer> questionList = null;
        Random random = new Random();
        for (int i = 0; i < docSize; i++) {
            questionList = new ArrayList<>();
            for (int j = 0; j < Constant.DOC_QUESTION_SIZE; j++) {
                int questionId = getQuestionId(random, questionList);
                questionList.add(questionId);
            }
            docList.add(new PendingDoc(""+i,questionList));
        }
        return docList;
    }

    /**
     * 递归获取题目id（防止一篇文档存在重复id）
     * @return
     */
    private static int getQuestionId(Random random,List<Integer> questionList) {
        int questionId = random.nextInt(Constant.SIZE_OF_QUESTION_BANK);
        if (questionList.contains(questionId)) {
            return getQuestionId(random,questionList);
        }else{
            return questionId;
        }
    }
}
