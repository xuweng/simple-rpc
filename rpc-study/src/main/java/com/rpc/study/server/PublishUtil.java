package com.rpc.study.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class PublishUtil {
    //服务接口集合
    private static List<Object> serviceList;
    private static ThreadPoolExecutor executor = new ThreadPoolExecutor(5, 10, 10, TimeUnit.SECONDS,
            new LinkedBlockingQueue<Runnable>(10));

    public static void publish(int port, Object... services) throws IOException {
        System.out.println("server start-------------------------->");
        serviceList = Arrays.asList(services);
        ServerSocket server = new ServerSocket(port);
        Socket client;
        while (true) {
            //阻塞等待请求
            client = server.accept();
            //使用线程池处理请求
            executor.submit(new ServerHandler(client, serviceList));
        }

    }
}

