package com.sam.network.netty.framework.vo;

import lombok.Data;

/**
 * 消息数据包
 * @author sam.liang
 */
@Data
public class MessagePacket {
    private PacketHeader packetHeader;
    private Object body;

    @Override
    public String toString() {
        return "MessagePacket{" +
                "packetHeader=" + packetHeader +
                ", body=" + body +
                '}';
    }
}
