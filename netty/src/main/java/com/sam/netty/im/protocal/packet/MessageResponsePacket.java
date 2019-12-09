package com.sam.netty.im.protocal.packet;

import com.sam.netty.im.protocal.command.Command;
import com.sam.netty.im.protocal.command.Packet;
import lombok.Data;

/**
 * 消息响应数据包
 * @author sam.liang
 */
@Data
public class MessageResponsePacket extends Packet {

    private String message;
    private String fromUserName;
    private String fromUserId;

    @Override
    public byte getCommand() {
        return Command.MESSAGE_RESPONSE;
    }
}
