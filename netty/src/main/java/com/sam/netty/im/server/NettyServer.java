package com.sam.netty.im.server;

import com.sam.netty.im.commom.Spliter;
import com.sam.netty.im.protocal.coder.PacketDecoder;
import com.sam.netty.im.protocal.coder.PacketEncoder;
import com.sam.netty.im.server.handler.*;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.util.concurrent.TimeUnit;

/**
 * netty IM server 启动类
 * @author sam.liang
 */
public class NettyServer {
    private final int PORT;
    /**
     * 最大连接重试次数
     */
    private final int MAX_RETRY = 5;
    private final NioEventLoopGroup bossGroup = new NioEventLoopGroup(1);
    private final NioEventLoopGroup workGroup = new NioEventLoopGroup();

    public NettyServer(int port) {
        this.PORT = port;
    }

    /**
     * 服务端启动
     */
    public void start() {
        ServerBootstrap b = new ServerBootstrap();
        b.group(bossGroup,workGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel sc) throws Exception {
//                        //拆包器
//                        sc.pipeline().addLast(new Spliter());
//                        //解码器
//                        sc.pipeline().addLast(PacketCodecHandler.INSTANCE);
//                        /// sc.pipeline().addLast(new PacketDecoder());
//                        //登录请求数据包逻辑处理器
//                        sc.pipeline().addLast(LoginRequestHandler.INSTANCE);
//                        //处理当前通道是否认证的handler
//                        sc.pipeline().addLast(AuthHandler.INSTANCE);
//                        //创建群聊数据包逻辑处理器
//                        sc.pipeline().addLast(CreateGroupRequestHandler.INSTANCE);
//                        //加入群聊数据包逻辑处理器
//                        sc.pipeline().addLast(JoinGroupRequestHandler.INSTANCE);
//                        //退出群聊数据包逻辑处理器
//                        sc.pipeline().addLast(QuitGroupRequestHandler.INSTANCE);
//                        //列出群聊所有人数 数据包逻辑处理器
//                        sc.pipeline().addLast(ListGroupUserRequestHandler.INSTANCE);
//                        //处理群组消息 数据包逻辑处理器
//                        sc.pipeline().addLast(GroupMessageRequestHandler.INSTANCE);
//                        //消息请求数据包逻辑处理器
//                        sc.pipeline().addLast(MessageRequestHandler.INSTANCE);
//                        //数据包编码器
//                        ///sc.pipeline().addLast(new PacketEncoder());
                        //服务端空闲检查handler
                        sc.pipeline().addLast(new IMIdleStateHandler());
                        sc.pipeline().addLast(new Spliter());
                        //解码器
                        sc.pipeline().addLast(PacketCodecHandler.INSTANCE);
                        //客户端心跳数据包逻辑处理器
                        sc.pipeline().addLast(HeartBeatRequestHandler.INSTANCE);

                        sc.pipeline().addLast(LoginRequestHandler.INSTANCE);
                        sc.pipeline().addLast(AuthHandler.INSTANCE);
                        /// 压缩handler
                        sc.pipeline().addLast(IMHandler.INSTANCE);
                    }
                });
        connect(b,this.PORT,this.MAX_RETRY);
    }
    private void connect(ServerBootstrap b, int port, int retry) {
        b.bind(port).addListener(future -> {
            if (future.isSuccess()) {
                System.out.println("服务器启动成功！！！！");
            }else if(retry == 0) {
                System.out.println("重试次数用完了，放弃启动！");
            }else{
                int order = (MAX_RETRY - retry) + 1;
                int delay = 1 << order;
                b.config().group().schedule(()->connect(b,port,retry-1),delay, TimeUnit.SECONDS);
            }
        });
    }

    public static void main(String[] args) {
        new NettyServer(8080).start();
    }
}
