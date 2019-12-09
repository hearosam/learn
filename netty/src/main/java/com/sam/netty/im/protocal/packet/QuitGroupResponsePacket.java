package com.sam.netty.im.protocal.packet;

import com.sam.netty.im.protocal.command.Command;
import com.sam.netty.im.protocal.command.Packet;
import lombok.Data;

/**
 * 退出群聊响应数据包
 * @author sam.liang
 */
@Data
public class QuitGroupResponsePacket extends Packet {
    private String groupId;
    private String userId;
    private boolean success;
    private String reason;

    @Override
    public byte getCommand() {
        return Command.QUIT_GROUP_RESPONSE;
    }
}
