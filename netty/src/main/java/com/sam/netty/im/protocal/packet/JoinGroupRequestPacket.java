package com.sam.netty.im.protocal.packet;

import com.sam.netty.im.protocal.command.Command;
import com.sam.netty.im.protocal.command.Packet;
import lombok.Data;

/**
 * 加入群聊请求数据包
 * @author sam.liang
 */
@Data
public class JoinGroupRequestPacket extends Packet {
    private String groupId;

    @Override
    public byte getCommand() {
        return Command.JOIN_GROUP_REQUEST;
    }
}
