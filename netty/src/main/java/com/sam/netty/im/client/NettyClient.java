package com.sam.netty.im.client;

import com.sam.netty.im.client.handler.*;
import com.sam.netty.im.commom.Spliter;
import com.sam.netty.im.server.handler.IMIdleStateHandler;
import com.sam.netty.im.server.handler.PacketCodecHandler;
import com.sam.netty.im.util.LoginUtil;
import com.sam.netty.im.util.console.ConsoleCommandManager;
import com.sam.netty.im.util.console.LoginConsoleCommand;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.Date;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

/**
 * netty IM 客户端 启动类
 * @author sam.liang
 */
public class NettyClient {
    private final String HOST;
    private final int PORT;
    private final int MAX_RETRY = 5;

    public NettyClient(String host, int port) {
        this.HOST = host;
        this.PORT = port;
    }

    public static void main(String[] args) {
        //启动客户端
        new NettyClient("localhost",8080).start();
    }
    /**
     * 客户端启动方法
     */
    public void start() {
        NioEventLoopGroup workgroup = new NioEventLoopGroup();
        Bootstrap b = new Bootstrap();

        b.group(workgroup)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel sc) throws Exception {
                        //连接空闲检测handler
                        sc.pipeline().addLast(new IMIdleStateHandler());
                        //拆包器
                        sc.pipeline().addLast(new Spliter());
                        //数据包编解码器
                        sc.pipeline().addLast(PacketCodecHandler.INSTANCE);
                        //心跳数据包逻辑处理器一定要放在解码器之后，不然服务端就无法解码心跳数据包，就会造成心跳数据包丢失
                        sc.pipeline().addLast(new HeartBeatTimeHandler());

                        /// sc.pipeline().addLast(new PacketDecoder());
                        sc.pipeline().addLast(new LoginResponseHandler());
                        sc.pipeline().addLast(new CreateGroupResponseHandler());
                        sc.pipeline().addLast(new JoinGroupResponseHandler());
                        sc.pipeline().addLast(new QuitGroupResponseHandler());
                        sc.pipeline().addLast(new ListGroupUserResponseHandler());
                        sc.pipeline().addLast(new GroupMessageResponseHandler());
                        sc.pipeline().addLast(new MessageResponseHandler());
                        /// sc.pipeline().addLast(new PacketEncoder());
                    }
                });
        //开始连接
        connect(b,this.HOST,this.PORT,MAX_RETRY);
    }

    /**
     * 连接服务端
     * @param b bootstrap
     * @param host 服务端主机
     * @param port 服务端端口
     * @param retry 重试次数
     */
    private void connect(Bootstrap b,String host,int port,int retry) {
        b.connect(host, port).addListener(future -> {
            if (future.isSuccess()) {
                System.out.println("建立连接成功！");
                //与服务端连接成功之后启动控制台线程
                Channel channel = ((ChannelFuture) future).channel();
                startConsoleThread(channel);
            } else if (retry == 0) {
                System.out.println("重试次数已经用完了，放弃连接！");
            } else {
                //第几次重连
                int order = (MAX_RETRY - retry) + 1;
                //本次重连间隔
                int delay = 1 << order;
                System.out.println(new Date() + "： 连接失败： 第" + order + "次重连。。。");
                b.config().group().schedule(() -> connect(b, host, port, retry - 1), delay, TimeUnit.SECONDS);
            }
        });
    }

    /**
     * netty 客户端 控制台线程
     * @param channel
     */
    private void startConsoleThread(Channel channel) {
        new Thread(()->{
            Scanner scanner = new Scanner(System.in);
            LoginConsoleCommand loginConsoleCommand = new LoginConsoleCommand();
            ConsoleCommandManager commandManager = new ConsoleCommandManager();

            while (!Thread.interrupted()) {
                if(!LoginUtil.hasLogin(channel)) {
                   loginConsoleCommand.exec(scanner,channel);
                }else{
                    commandManager.exec(scanner,channel);
                }
            }
        }).start();
    }
}
