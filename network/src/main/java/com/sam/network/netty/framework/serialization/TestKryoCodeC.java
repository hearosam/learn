package com.sam.network.netty.framework.serialization;

import com.sam.network.netty.framework.vo.MessagePacket;
import com.sam.network.netty.framework.vo.PacketHeader;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.util.HashMap;
import java.util.Map;

/**
 * 类说明：序列化器的测试类
 */
public class TestKryoCodeC {

    public MessagePacket getMessage() {
		MessagePacket myMessage = new MessagePacket();
		PacketHeader myHeader = new PacketHeader();
		myHeader.setLength(123);
		myHeader.setSessionId(99999);
		myHeader.setType((byte) 1);
		myHeader.setPriority((byte) 7);
		Map<String, Object> attachment = new HashMap<String, Object>();
		for (int i = 0; i < 10; i++) {
			attachment.put("ciyt --> " + i, "lilinfeng " + i);
		}
		myHeader.setAttachment(attachment);
		myMessage.setPacketHeader(myHeader);
		myMessage.setBody("abcdefg-----------------------AAAAAA");
		return myMessage;
    }

    /**
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
		TestKryoCodeC testC = new TestKryoCodeC();

		for (int i = 0; i < 5; i++) {
			ByteBuf sendBuf = Unpooled.buffer();
			MessagePacket message = testC.getMessage();
            System.out.println("Encode:"+message + "[body ] " + message.getBody());
            KryoSerializer.serialize(message, sendBuf);
			MessagePacket decodeMsg = (MessagePacket)KryoSerializer.deserialize(sendBuf);
			System.out.println("Decode:"+decodeMsg + "<body > "
					+ decodeMsg.getBody());
			System.out
				.println("-------------------------------------------------");
		}

    }

}
