package com.sam.netty.im.protocal.packet;

import com.sam.netty.im.protocal.command.Command;
import com.sam.netty.im.protocal.command.Packet;
import lombok.Data;

/**
 * 退出群聊请求数据包
 * @author sam.liang
 */
@Data
public class QuitGroupRequestPacket extends Packet {
    private String groupId;

    @Override
    public byte getCommand() {
        return Command.QUIT_GROUP_REQUEST;
    }
}
