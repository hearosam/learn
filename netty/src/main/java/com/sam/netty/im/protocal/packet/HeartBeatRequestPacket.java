package com.sam.netty.im.protocal.packet;

import com.sam.netty.im.protocal.command.Command;
import com.sam.netty.im.protocal.command.Packet;

/**
 * 心跳数据包
 * @author sam.liang
 */
public class HeartBeatRequestPacket extends Packet {

    @Override
    public byte getCommand() {
        return Command.HEARTBEAT_REQUEST;
    }
}
