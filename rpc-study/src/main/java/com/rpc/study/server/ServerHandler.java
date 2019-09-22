package com.rpc.study.server;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.net.Socket;
import java.util.List;

public class ServerHandler implements Runnable {
    private Socket client = null;

    private List<Object> serviceList = null;

    public ServerHandler(Socket client, List<Object> service) {
        this.client = client;
        this.serviceList = service;
    }

    @Override
    public void run() {
        try (
                ObjectInputStream input = new ObjectInputStream(client.getInputStream());
                ObjectOutputStream output = new ObjectOutputStream(client.getOutputStream())
        ) {
            // 读取客户端要访问那个service
            Class serviceClass = (Class) input.readObject();

            // 找到该服务类
            Object obj = findService(serviceClass);
            if (obj == null) {
                output.writeObject(serviceClass.getName() + "服务未发现");
            } else {
                //利用反射调用该方法，返回结果
                String methodName = input.readUTF(); //读取UTF编码的String字符串
                //读取参数类型
                Class<?>[] parameterTypes = (Class<?>[]) input.readObject();
                //读取参数
                Object[] arguments = (Object[]) input.readObject();
                Method method = obj.getClass().getMethod(methodName, parameterTypes);
                //反射执行方法
                Object result = method.invoke(obj, arguments);
                output.writeObject(result);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Object findService(Class serviceClass) {
        for (Object obj : serviceList) {
            boolean isFather = serviceClass.isAssignableFrom(obj.getClass());
            if (isFather) {
                return obj;
            }
        }
        return null;
    }
}
