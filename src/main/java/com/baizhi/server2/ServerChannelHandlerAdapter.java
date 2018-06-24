package com.baizhi.server2;

import com.baizhi.bean.End;
import com.baizhi.local.MethodInvokeMeta;
import com.baizhi.local.Result;
import com.baizhi.service.DemoService;
import com.baizhi.service.DemoServiceImpl;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

public class ServerChannelHandlerAdapter extends ChannelHandlerAdapter {
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("error：" + cause.getMessage());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Result result = new Result();
        ChannelFuture channelFuture = null;
        System.out.println("Server2 deal with..");

        MethodInvokeMeta request = (MethodInvokeMeta) msg;
        Class<?> targetInterfaces = request.getTargetInterfaces();
        String methodName = request.getMethod();
        Class<?>[] parameterTypes = request.getParameterTypes();
        Object[] args = request.getArgs();

        DemoService demoService = new DemoServiceImpl();
        try {
            Object invoke = targetInterfaces.getMethod(methodName, parameterTypes).invoke(demoService, args);
            result.setReturnValue(invoke);
        } catch (Exception e) {
            e.printStackTrace();
            result.setException(new RuntimeException(e.getMessage()));
        }

        System.out.printf("[%-14s]%s\n", "implementation", " => " + demoService.getClass());
        System.out.printf("[%-14s]%s\n", "method", " => " + request.getMethod());
        System.out.printf("[%-14s]%s", "parameterType", " => ");
        for (int i = 0; i < request.getParameterTypes().length; i++) {
            if (i == request.getParameterTypes().length - 1) {
                System.out.println(request.getParameterTypes()[i]);
            } else {
                System.out.print(request.getParameterTypes()[i] + ",");
            }
        }
        System.out.printf("[%-14s]%s", "args", " => ");
        for (int i = 0; i < request.getArgs().length; i++) {
            if (i == request.getArgs().length - 1) {
                System.out.println(request.getArgs()[i]);
            } else {
                System.out.print(request.getArgs()[i] + ",");
            }
        }

        System.out.println("Server provide result : " + result);
        channelFuture = ctx.writeAndFlush(result);

        channelFuture.addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
        channelFuture.addListener(ChannelFutureListener.CLOSE_ON_FAILURE);

        //关闭通道（由服务器进行关闭）
        channelFuture.addListener(ChannelFutureListener.CLOSE);
    }
}

