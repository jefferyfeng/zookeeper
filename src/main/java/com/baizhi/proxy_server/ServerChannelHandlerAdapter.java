package com.baizhi.proxy_server;

import com.baizhi.bean.End;
import com.baizhi.bean.MethodInvokeMeta;
import com.baizhi.bean.Result;
import com.baizhi.local.*;
import com.baizhi.service.DemoService;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

public class ServerChannelHandlerAdapter extends ChannelHandlerAdapter {
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("error："+cause.getMessage());
    }


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Result result = new Result();
        ChannelFuture channelFuture = null;
        if(msg instanceof End){
            channelFuture = ctx.writeAndFlush(msg);
            //关闭通道
            channelFuture.addListener(ChannelFutureListener.CLOSE);
        }else {
            System.out.println("Proxy server analysis request..");
            //客户端请求
            MethodInvokeMeta request = (MethodInvokeMeta) msg;

            Class<?> targetInterfaces = request.getTargetInterfaces();
            String methodName = request.getMethod();
            Class<?>[] parameterTypes = request.getParameterTypes();
            Object[] args = request.getArgs();

            try {
                DemoService proxy = new RPCProxy<DemoService>().createProxy(targetInterfaces);

                Object invoke = targetInterfaces.getMethod(methodName, parameterTypes).invoke(proxy, args);
                result.setReturnValue(invoke);

            } catch (Exception e) {
                e.printStackTrace();
                result.setException(new RuntimeException(e.getMessage()));
            }

            System.out.println("Proxy Server receive : " + result);

            channelFuture = ctx.writeAndFlush(result);

            channelFuture.addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
            channelFuture.addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
        }
    }
}
