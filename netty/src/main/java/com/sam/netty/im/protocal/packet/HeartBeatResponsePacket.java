package com.sam.netty.im.protocal.packet;

import com.sam.netty.im.protocal.command.Command;
import com.sam.netty.im.protocal.command.Packet;

/**
 * 服务端心跳响应数据包
 * @author sam.liang
 */
public class HeartBeatResponsePacket extends Packet {

    @Override
    public byte getCommand() {
        return Command.HEARTBEAT_RESPONSE;
    }
}
