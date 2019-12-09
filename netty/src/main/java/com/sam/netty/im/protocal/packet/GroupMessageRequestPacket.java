package com.sam.netty.im.protocal.packet;

import com.sam.netty.im.protocal.command.Command;
import com.sam.netty.im.protocal.command.Packet;
import lombok.Data;

/**
 * 群组消息请求数据包
 * @author sam.liang
 */
@Data
public class GroupMessageRequestPacket extends Packet {

    private String groupId;
    private String message;

    @Override
    public byte getCommand() {
        return Command.GROUP_MESSAGE_REQUEST;
    }
}
