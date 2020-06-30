package com.sam.delayorderforjava;

/**
 * 使用jdk提供的delayQueue实现延时订单的功能
 * 优点：不用集成其他第三方组件
 * 缺点：
 *  1.机器重启后保存在queue中的订单会丢失(解决：可以在应用重启的时候检索数据库，然后将未处理订单初始化到内存中)
 */