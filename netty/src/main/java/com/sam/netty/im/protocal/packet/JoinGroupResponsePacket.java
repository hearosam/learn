package com.sam.netty.im.protocal.packet;

import com.sam.netty.im.protocal.command.Command;
import com.sam.netty.im.protocal.command.Packet;
import lombok.Data;

/**
 * 加入群聊响应数据包
 * @author sam.liang
 */
@Data
public class JoinGroupResponsePacket extends Packet {
    private String groupId;
    private String userId;
    private String reason;
    private boolean success;

    @Override
    public byte getCommand() {
        return Command.JOIN_GROUP_RESPONSE;
    }
}
