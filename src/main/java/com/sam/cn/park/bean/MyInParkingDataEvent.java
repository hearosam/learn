package com.sam.cn.park.bean;

import lombok.Data;

/**
 * 汽车信息
 * @author sam.liang
 */
@Data
public class MyInParkingDataEvent {
    /**
     * 车牌号
     */
    private String carLicense;
}
