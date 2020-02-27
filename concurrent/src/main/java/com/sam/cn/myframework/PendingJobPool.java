package com.sam.cn.myframework;

import com.sam.cn.myframework.context.JobContext;
import com.sam.cn.myframework.endity.ResultInfo;
import com.sam.cn.myframework.endity.ResultType;
import com.sam.cn.myframework.process.ITaskProcesser;

import java.util.List;
import java.util.concurrent.*;

/**
 * 框架核心类延迟工作池
 * @author sam.liang
 */
public class PendingJobPool {
    //cpu 密集型 ? IO 密集型
    private static final int THREAD_NUMBERS = Runtime.getRuntime().availableProcessors();
    //任务队列
    private ArrayBlockingQueue<Runnable> taskQueue = new ArrayBlockingQueue<>(5000);
    //缓存工作上下文的容器
    private ConcurrentHashMap<String, JobContext> jobContextMap = new ConcurrentHashMap<>();
    //线程池
    private Executor executor = new ThreadPoolExecutor(THREAD_NUMBERS,THREAD_NUMBERS,60,TimeUnit.NANOSECONDS,taskQueue);
    //单例模式构造实例
    private static class PendingJobPoolHolder{
        public static PendingJobPool instance = new PendingJobPool();
    }
    public static PendingJobPool getInstance() {
        return PendingJobPoolHolder.instance;
    }
    //对工作中的任务进行包装，提交给线程池使用，并处理任务的结果，写入缓存以供查询
    private static class PendingTask<R,T> implements Runnable{//R返回结果类型，T业务数据
        //作业上下文环境
        private JobContext<R> jobContext;
        //业务数据
        private T data;

        public PendingTask(JobContext<R> jobContext, T data) {
            this.data = data;
            this.jobContext = jobContext;
        }

        @Override
        public void run() {
            ResultInfo<R> resultInfo = null;
            try{
                ITaskProcesser<R, T> processer = (ITaskProcesser<R, T>) jobContext.getProcesser();
                //调用业务人员具体实现的任务处理器实现类
                resultInfo = (ResultInfo<R>) processer.taskExecute(data);
                //检查操作，任务处理器实现类处理完具体业务后必须返回系统返回类型
                if (resultInfo == null) {
                    resultInfo = new ResultInfo<R>(ResultType.EXCEPTION,"result is null");
                }else if (resultInfo.getResultType() == null) {//对处理结果进行友好提示处理
                    if(resultInfo.getReason() == null) {
                        resultInfo = new ResultInfo<R>(ResultType.EXCEPTION,"reason is null");
                    }else {
                        resultInfo = new ResultInfo<R>(ResultType.EXCEPTION,"result is not null ,but reason is " + resultInfo.getReason());
                    }
                }
            }catch (Exception e) {
                resultInfo = new ResultInfo<R>(ResultType.EXCEPTION,"exception  " + e.getMessage());
            }finally {
                //任务处理完提交
                jobContext.addTaskResult(resultInfo);
            }
        }
    }

    //注册作业类型
    //R返回结果类型，T业务数据
    public <R,T> void registerJob(String jobName,int taskNumbers,ITaskProcesser<R,T> processer,long expireTime) {
        JobContext jobContext = new JobContext(jobName,taskNumbers,processer,expireTime);
        //把作业注册到工作池缓存中
        if (jobContextMap.putIfAbsent(jobName,jobContext)!=null){
            throw new RuntimeException(jobName+ "<------当前作业已存在！");
        }
    }
    //pendTask
    //提交任务
    //R返回结果类型，T业务数据
    public <T,R> R submitTask(String jobName, T data) {
        return null;
    }

    //R返回结果类型，T业务数据
    private <R> JobContext<R> getJob(String jobName) {
        JobContext<R> jobContext = jobContextMap.get(jobName);
        if (jobContext == null) {
            throw new RuntimeException(jobName + "<-----非法作业！！！");
        }
        return jobContext;
    }

    public ConcurrentHashMap<String, JobContext> getJobContextMap() {
        return jobContextMap;
    }
    //查询任务详情，处理任务结果列表
    public<R> List<ResultInfo<R>> getTaskDetail(String jobName) {
        JobContext<Object> myJobContext = getJob(jobName);
        return myJobContext.getTaskDetail();
    }

    //查询任务进度
}
