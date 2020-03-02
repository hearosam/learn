package com.sam.cn.myproject;

import com.sam.cn.myproject.entity.PendingDoc;
import com.sam.cn.myproject.service.ProduceDocService;
import com.sam.cn.myproject.tools.CreatePendingDoc;
import com.sam.cn.myproject.tools.SQL_Question_Bank;

import java.util.List;

/**
 * 传统的web程序处理实例（单线程、串行处理）
 */
public class SingleWeb {

    public static void main(String[] args) {
        //初始化题库
        System.out.println("题库初始化-------------------");
        SQL_Question_Bank.initBank();
        System.out.println("题库初始化结束-------------------");
        long start = System.currentTimeMillis();
        //创建文档（题目id跟文档建立关联关系）
        List<PendingDoc> pendingList = CreatePendingDoc.makePendingDoc(2);
        //获取题目详情，生成文档内容
        for (PendingDoc pendingDoc : pendingList) {
            long startMakeTime = System.currentTimeMillis();
            //生成本地文档
            String localPath = ProduceDocService.makeDoc(pendingDoc);
            System.out.println("文档生成耗时"+(System.currentTimeMillis()-startMakeTime));
            long startUploadTime = System.currentTimeMillis();
            //上传文档到远程地址供下载
            String remoteUrl = ProduceDocService.uploadDoc(localPath);
            System.out.println("文档上传耗时"+(System.currentTimeMillis()-startUploadTime));
        }
        System.out.println("总耗时："+(System.currentTimeMillis()-start));
    }
}
