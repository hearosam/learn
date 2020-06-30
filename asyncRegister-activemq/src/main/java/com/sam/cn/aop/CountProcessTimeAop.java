package com.sam.cn.aop;

import com.sam.cn.entity.UserInfo;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * 统计相关方法处理时间
 * @author sam.liang
 */
@Aspect
@Component
public class CountProcessTimeAop {

    private UserInfo userInfo;

    public CountProcessTimeAop() {
        System.out.println("AOP 开启");
    }

    @Pointcut("execution(* com.sam.cn.service.impl.*.register(..))")
    public void around(){}

    @Around("around()&&args(userInfo)")
    public Object countTime(ProceedingJoinPoint point, UserInfo userInfo){
//        this.userInfo = userInfo;
        long start = System.currentTimeMillis();
        Object result = null;
        try {
            result = point.proceed(new Object[]{userInfo});
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        System.out.println("耗时---》"+(System.currentTimeMillis() - start) + "ms");
        return result;
    }
}
