package com.sam.cn.myframework.test;

import com.sam.cn.myframework.endity.ResultInfo;
import com.sam.cn.myframework.endity.ResultType;
import com.sam.cn.myframework.process.ITaskProcesser;
import java.util.Random;

/**
 * 任务处理器实现类
 * @author sam.liang
 */
public class MyTask implements ITaskProcesser<Integer,Integer> {

    @Override
    public ResultInfo<Integer> taskExecute(Integer data) {
        Random random = new Random();
        int randomValue = random.nextInt(500);
        ResultInfo<Integer> resultInfo = null;

        try {
            Thread.sleep(randomValue);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (randomValue<300) {
            data = data.intValue() + randomValue;
            resultInfo = new ResultInfo<>(ResultType.SUCCESS,data);
        }else if(randomValue < 400 ) {
            resultInfo = new ResultInfo<>(ResultType.FAIL,"random value > 300",data);
        }else{
            try {
                throw new RuntimeException("异常发生了");
            }catch (Exception e) {
                resultInfo = new ResultInfo<>(ResultType.EXCEPTION,e.getMessage(),data);
            }
        }
        return resultInfo;
    }
}
