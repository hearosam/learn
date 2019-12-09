package com.sam.cn.mylearnproject.bean;

import lombok.Data;

/**
 * 第一步创建数据单元Event
 * event:从生产者到消费者所处理的数据的基本单元
 * @author sam.liang
 */
@Data
public class MyDataEvent {
    private Long value;
}
