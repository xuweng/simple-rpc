package com.rpc.study.client;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.Socket;

public class CallProxyHandler implements InvocationHandler {

    private String ip;
    private int port;

    public CallProxyHandler(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    /**
     * 获取代理对象
     *
     * @param clazz
     * @param <T>
     * @return
     */
    @SuppressWarnings("all")
    public <T> T getService(Class<T> clazz) {
        return (T) Proxy.newProxyInstance(CallProxyHandler.class.getClassLoader(),
                new Class<?>[]{clazz}, this);
    }

    /**
     * 将需要调用服务的方法名，参数类型，参数按照一定格式封装发送至服务端
     * 读取服务端返回的结果
     *
     * @param proxy
     * @param method
     * @param args
     * @return
     * @throws Throwable
     */
    @SuppressWarnings("all")
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        try (
                Socket socket = new Socket(ip, port);
                ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream input = new ObjectInputStream(socket.getInputStream())
        ) {
            output.writeObject(proxy.getClass().getInterfaces()[0]);
            output.writeUTF(method.getName());
//            output.writeObject(method.getParameterTypes());
            output.writeObject(args);
            output.flush();
            Object result = input.readObject();
            if (result instanceof Throwable) {
                throw (Throwable) result;
            }
            return result;
        }
    }
}

