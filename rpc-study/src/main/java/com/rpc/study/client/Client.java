package com.rpc.study.client;

import com.rpc.study.api.UserService;

public class Client {
    public static void main(String[] args) {
        CallProxyHandler handler = new CallProxyHandler("127.0.0.1", 8081);
        UserService userService = handler.getService(UserService.class);

        String result = userService.getUser();
        System.out.println(result);
    }
}

