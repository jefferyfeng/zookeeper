package com.baizhi.local;

import org.I0Itec.zkclient.ZkClient;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RPCProxy<T> implements InvocationHandler {
    private RpcClient rpcClient = null;
    private Class targetInterface;
    private static final ZkClient CLIENT = new ZkClient("192.168.199.128:2181");
    private List<HostAndPort> hostAndPorts;

    public T createProxy(Class targetInterface){
        this.targetInterface = targetInterface;
        return (T) Proxy.newProxyInstance(RPCProxy.class.getClassLoader(), new Class[]{targetInterface}, this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {


        MethodInvokeMeta mim=new MethodInvokeMeta();
        mim.setTargetInterfaces(targetInterface);
        mim.setArgs(args);
        mim.setMethod(method.getName());
        mim.setParameterTypes(method.getParameterTypes());

        List<String> children = CLIENT.getChildren("/test");
        hostAndPorts = new ArrayList<HostAndPort>();
        for (String child : children) {
            hostAndPorts.add((HostAndPort) CLIENT.readData("/test/"+child));
        }
        HostAndPort hostAndPort = hostAndPorts.get(new Random().nextInt(hostAndPorts.size()));

        rpcClient = new RpcClientImpl();

        Result result = rpcClient.call(mim, hostAndPort);

        System.out.println("Random server : ");
        System.out.printf("[%-13s]%s\n","host"," => "+hostAndPort.getHost());
        System.out.printf("[%-13s]%s\n","port"," => "+hostAndPort.getPort());

        //System.out.println("RPCProxy"+result);
        return result.getReturnValue();// 返回method方法执行后的返回值

    }
}
