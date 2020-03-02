package com.sam.cn.myproject;

import com.sam.cn.myproject.common.Constant;
import com.sam.cn.myproject.entity.PendingDoc;
import com.sam.cn.myproject.service.ProduceDocService;
import com.sam.cn.myproject.tools.CreatePendingDoc;
import com.sam.cn.myproject.tools.SQL_Question_Bank;

import java.util.List;
import java.util.concurrent.*;

/**
 * rpc服务端，使用生产者消费者模式以及生产者消费者级联模式
 * @author sam.liang
 */
public class RpcModelWeb {

    //负责生成文档线程池
    private static ExecutorService makeDocService = Executors.newFixedThreadPool(Constant.CPU_COUNT * 2);
    //负责上传文档
    private static ExecutorService uploadDocService = Executors.newFixedThreadPool(Constant.CPU_COUNT * 2);

    private static CompletionService<String> docCs = new ExecutorCompletionService<>(makeDocService);
    private static CompletionService<String> uploadCs = new ExecutorCompletionService<>(uploadDocService);

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        //初始化题库
        System.out.println("题库初始化-------------------");
        SQL_Question_Bank.initBank();
        System.out.println("题库初始化结束-------------------");
        long start = System.currentTimeMillis();
        //创建文档（文档与题目建立关联关系）
        List<PendingDoc> pendingList = CreatePendingDoc.makePendingDoc(20);
        //处理文档，生成本地文档（性能优化：题目缓存、更新缓存、异步处理文档，）
        /*
            将docCs.take()作为uploadCs的任务这里就实现了docCs生产者消费者模式级联uploadCs生产者消费者模式
         */
        long startMakeDocTime = System.currentTimeMillis();
        for (PendingDoc doc : pendingList) {
            docCs.submit(new MakeDocTask(doc));
        }
        System.out.println("文档处理完成总时间"+(System.currentTimeMillis() - startMakeDocTime));
        long startUploadTime = System.currentTimeMillis();
        //本地文档上传远程服务器
        for (PendingDoc doc:pendingList) {
            Future<String> take = docCs.take();
            uploadCs.submit(new UploadDocTask(take.get()));
        }
        System.out.println("文档上传耗时："+(System.currentTimeMillis()-startUploadTime));
        //这里是等待所有文档上传完毕（如果不着急下载可以把这部分等待时间去掉）
        for (PendingDoc doc:pendingList) {
            uploadCs.take().get();
        }
        System.out.println("总耗时："+(System.currentTimeMillis()-start));

    }

    /**
     * 本地文档上传远程服务器
     */
    private static class UploadDocTask implements Callable<String> {
        private String localPath;

        public UploadDocTask(String localPath) {
            this.localPath = localPath;
        }

        @Override
        public String call() throws Exception {
            String remoteUrl = ProduceDocService.uploadDoc(localPath);
            return remoteUrl;
        }
    }
    /**
     * 本地离线文档生成处理
     */
    private static class MakeDocTask implements Callable<String> {
        private PendingDoc doc;

        public MakeDocTask(PendingDoc doc) {
            this.doc = doc;
        }

        @Override
        public String call() throws Exception {
            String localPath = ProduceDocService.makeDocSync(doc);
            return localPath;
        }
    }
}
