package com.sam.cn.myframework.process;

import com.sam.cn.myframework.endity.ResultInfo;

/**
 * 任务处理器接口
 * @author sam.liang
 */
public interface ITaskProcesser<R,T> {
    ResultInfo<R> taskExecute(T data);
}
