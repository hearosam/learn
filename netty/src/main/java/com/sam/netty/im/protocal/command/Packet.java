package com.sam.netty.im.protocal.command;

import lombok.Data;

/**
 * 数据包基类
 * @author sam.liang
 */
@Data
public abstract class Packet {
    /**
     * 协议版本
     */
    private byte version = 1;

    /**
     * 获取指令
     * @return
     */
    public abstract byte getCommand();
}
