package com.sam.nio.demo;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Set;

/**
 * nio演示示例
 * @author sam.liang
 */
public class NioDemo {

    public static void main(String[] args) throws IOException {
        Selector serverSelector = Selector.open();
        Selector clientSelector = Selector.open();

        //IO编程服务端启动
        new Thread(()->{
            try {
                ServerSocketChannel listenerChannel = ServerSocketChannel.open();
                listenerChannel.socket().bind(new InetSocketAddress(8000));
                //设置成异步的(不阻塞)
                listenerChannel.configureBlocking(false);
                //将serverSocketchannel注册到selector上，然后设置selector要扫描的事件
                listenerChannel.register(serverSelector, SelectionKey.OP_ACCEPT);

                while (true) {
                    //监测是否有新的链接，这里的1表示阻塞时间是1ms
                    if(serverSelector.select(1) > 0) {
                        Set<SelectionKey> set = serverSelector.selectedKeys();
                        Iterator<SelectionKey> it = set.iterator();
                        while (it.hasNext()) {
                            SelectionKey key = it.next();
                            if (key.isAcceptable()) {
                                try {
                                    //每新来一个链接，不需要创建一个线程，而是直接注册到clientSelector上
                                    SocketChannel clientChannel = ((ServerSocketChannel)key.channel()).accept();
                                    clientChannel.configureBlocking(false);
                                    clientChannel.register(clientSelector,SelectionKey.OP_READ);
                                }finally {
                                    it.remove();
                                }
                            }
                         }
                    }
                }
            }catch (IOException e) {
            }
        }).start();

        //IO编程客户端启动
        new Thread(()->{
            try {
                while (true) {
                    //批量轮询是否有那些链接有数据可读，这里的1指的是select这个方法阻塞时间为1ms
                    if(clientSelector.select(1) > 0) {
                        Set<SelectionKey> set = clientSelector.selectedKeys();
                        Iterator<SelectionKey> it = set.iterator();
                        while (it.hasNext()) {
                            SelectionKey key = it.next();
                            if(key.isReadable()) {
                                try {
                                    SocketChannel clientChannel = (SocketChannel) key.channel();
                                    ByteBuffer buffer = ByteBuffer.allocate(1024);
                                    //面向buffer
                                    clientChannel.read(buffer);
                                    buffer.flip();
                                    System.out.println(Charset.defaultCharset().newDecoder().decode(buffer).toString());
                                }finally {
                                    it.remove();
                                    key.interestOps(SelectionKey.OP_READ);
                                }
                            }
                        }
                    }
                }
            }catch (IOException e) {

            }
        }).start();
    }
}
