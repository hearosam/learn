package com.sam.netty.im.protocal.packet;

import com.sam.netty.im.protocal.command.Command;
import com.sam.netty.im.protocal.command.Packet;
import lombok.Data;

/**
 * 消息数据包
 * @author sam.liang
 */
@Data
public class MessageRequestPacket extends Packet {

    /**
     * 负载数据（消息内容）
     */
    private String message;
    /**
     * 消息发送目标用户
     */
    private String toUserId;

    @Override
    public byte getCommand() {
        return Command.MESSAGE_REQUEST;
    }
}
