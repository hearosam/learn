package com.sam.cn.myframework.context;

import com.sam.cn.myframework.delay.CheckExpireJobProcess;
import com.sam.cn.myframework.endity.ResultInfo;
import com.sam.cn.myframework.endity.ResultType;
import com.sam.cn.myframework.process.ITaskProcesser;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 作业上下文环境，维护着当前这类作业的详细信息，如任务个数，任务处理情况等
 * @author sam.liang
 */
public class JobContext<R> {
    //作业名称
    private String jobName;
    //任务总个数
    private int taskNumbers;
    //结果队列,拿结果从头开始拿，放结果从后面开始放
    private LinkedBlockingDeque<ResultInfo<R>> resultQueue;
    //任务处理成功个数
    private AtomicInteger successNumbers;
    //已经任务处理个数
    private AtomicInteger processNumbers;
    //任务处理器
    private ITaskProcesser<?,?> processer;
    //作业过期时间
    private long expireTime;

    public JobContext(String jobName,int taskNumbers,ITaskProcesser<?,?>  processer,long expireTime) {
        this.jobName = jobName;
        successNumbers = new AtomicInteger(0);
        processNumbers = new AtomicInteger(0);
        this.taskNumbers = taskNumbers;
        this.expireTime = expireTime;
        this.processer = processer;
        resultQueue = new LinkedBlockingDeque<ResultInfo<R>>(taskNumbers);
    }

    public ITaskProcesser<?, ?> getProcesser() {
        return processer;
    }

    /**
     * 放入任务结果到结果队列中，这里不需要很高的实时性，保证最终一致性即可，所以这里不用加锁
     * @param resultInfo
     */
    public void addTaskResult(ResultInfo<R> resultInfo) {
        //设置成功处理任务数
        if (ResultType.SUCCESS.equals(resultInfo.getResultType())) {
            successNumbers.incrementAndGet();
        }
        resultQueue.addLast(resultInfo);
        //设置已经处理任务个数
        int processNums = processNumbers.incrementAndGet();
        //当前作业任务处理完之后，将作业名称放入延时队列，到期后自动清除框架缓存的这个作业数据
        if (processNums == resultQueue.size()) {
            CheckExpireJobProcess.putJob(this.jobName,this.expireTime);
        }
    }

    /**
     * 获取任务详情
     * @param <R>
     * @return
     */
    public <R> List<ResultInfo<R>> getTaskDetail() {
        List<ResultInfo<R>> detailList = new ArrayList<>();
        ResultInfo<R> resultInfo;

        while((resultInfo= (ResultInfo<R>) resultQueue.pollFirst())!=null) {
            detailList.add(resultInfo);
        }
        return detailList;
    }

    public String getTotalProcess() {
        return "Success["+successNumbers.get()+"]/Current["
                +processNumbers.get()+"] Total["+taskNumbers+"]";
    }
}
