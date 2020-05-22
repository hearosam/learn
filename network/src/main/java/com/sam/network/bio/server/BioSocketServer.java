package com.sam.network.bio.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * bio socket server
 * @author sam.liang
 */
public class BioSocketServer {

    private static ExecutorService executorService = Executors.newFixedThreadPool(5);
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket();
        System.out.println("启动服务器，监听8090端口");
        serverSocket.bind(new InetSocketAddress(8090));
        while (true) {
            //阻塞方法
            Socket socket = serverSocket.accept();
            System.out.println("接收到客户端连接");
            //线程池
            executorService.execute(new ServerHandler(socket));
        }
    }
}

/**
 * 服务端消息处理器
 * @author sam.liang
 */
class ServerHandler implements Runnable {

    private Socket socket;

    public ServerHandler (Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try(BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            String receiveMassage = null;
            PrintWriter pw = new PrintWriter(socket.getOutputStream());
            while ((receiveMassage = br.readLine())!=null) {
                System.out.println("接收到客户端消息："+receiveMassage);
                pw.println("server response :"+ receiveMassage);
                pw.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            clear();
        }
    }
    public void clear() {
        if (socket!=null) {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}