package com.rpc.study.server;

import com.rpc.study.service.UserServiceImpl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Server {
    private static List<Object> serviceList = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        int port = 8081;
        serviceList.add(new UserServiceImpl());

        PublishUtil.publish(port, serviceList);
    }
}
