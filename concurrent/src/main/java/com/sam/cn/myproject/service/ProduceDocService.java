package com.sam.cn.myproject.service;

import com.sam.cn.myproject.entity.PendingDoc;
import com.sam.cn.myproject.entity.QuestionInCache;
import com.sam.cn.myproject.tools.SleepTool;
import com.sam.cn.myproject.vo.TaskResultVO;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutionException;

/**
 * 生成文档服务
 * @author sam.liang
 */
public class ProduceDocService {

    /**
     * 将待处理文档生成本地实际pdf文档
     * @param pendingDoc 待处理文档
     * @return 实际文档本地存储url
     */
    public static String makeDoc(PendingDoc pendingDoc) {
        System.out.println("开始处理文档。。。。。");
        StringBuffer sb = new StringBuffer();
        for (Integer questionId : pendingDoc.getQuestionInDBList()) {
            String questionContent = SingleQstService.makeQuestion(questionId);
            sb.append(questionContent);
        }
        return "complete_"+System.currentTimeMillis()+"_"+pendingDoc.getName()+".pdf";
    }

    /**
     * 模拟文档上传
     * @param localPath
     * @return
     */
    public static String uploadDoc(String localPath) {
        Random random = new Random();
        //模拟网络耗时
        SleepTool.buisness(random.nextInt(900+random.nextInt(100)));
        return "http://www.xxx.com/file/upload/"+localPath;
    }

    /**
     * 线程安全的将待处理文档生成本地离线文档
     * @param doc 待处理文档
     * @return 本地离线文档地址
     */
    public static String makeDocSync(PendingDoc doc) throws ExecutionException, InterruptedException {
        System.out.println("多线程模式；开始处理文档。。。。。");
        Map<Integer, TaskResultVO> qstResultMap = new HashMap<>();
        //循环处理文档中的每一个题目，准备进行并行化处理
        for (Integer questionId : doc.getQuestionInDBList()) {
            qstResultMap.put(questionId,ParallelQstService.makeQuestion(questionId));
        }
        StringBuffer sb = new StringBuffer();
        for (Integer questionId : doc.getQuestionInDBList()) {
            TaskResultVO taskResultVO = qstResultMap.get(questionId);
            sb.append(taskResultVO.getQuestionDetail()==null?taskResultVO.getQuestionFuture().get().getDetail():taskResultVO.getQuestionDetail());
            //调用第三方api生成本地pdf
        }
        return "complete_"+System.currentTimeMillis()+"_"+doc.getName()+".pdf";
    }
}
