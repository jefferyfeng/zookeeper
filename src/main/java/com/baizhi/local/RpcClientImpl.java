package com.baizhi.local;

import com.baizhi.serialize.CustomMessageToMessageDecoder;
import com.baizhi.serialize.CustomMessageToMessageEncoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;

public class RpcClientImpl implements RpcClient {

    @Override
    public Result call(final MethodInvokeMeta mim, HostAndPort hap) {
        final Result result = new Result();
        //1.创建服务启动引导
        Bootstrap bootstrap = new Bootstrap();
        //2.配置线程池组 parent child
        EventLoopGroup parent = new NioEventLoopGroup();
        //3.配置线程池组
        bootstrap.group(parent);

        //4.设置服务器实现
        bootstrap.channel(NioSocketChannel.class);
        //5.初始化通信管道
        bootstrap.handler(new ChannelInitializer<SocketChannel> (){
            @Override
            protected void initChannel(SocketChannel socketChannel) throws Exception {
                ChannelPipeline pipeline = socketChannel.pipeline();
                pipeline.addLast(new LengthFieldPrepender(2));
                pipeline.addLast(new CustomMessageToMessageEncoder());
                pipeline.addLast(new LengthFieldBasedFrameDecoder(65535,0,2,0,2));
                pipeline.addLast(new CustomMessageToMessageDecoder());
                //最终执行者
                pipeline.addLast(new ChannelHandlerAdapter(){
                    @Override
                    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
                        System.out.println("error："+cause.getMessage());
                    }

                    @Override
                    public void channelActive(ChannelHandlerContext ctx) throws Exception {
                        ChannelFuture channelFuture = ctx.writeAndFlush(mim);
                        //捕获异常
                        channelFuture.addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
                        //异常关闭连接
                        channelFuture.addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
                    }

                    @Override
                    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                        Result r = (Result) msg;
                        result.setReturnValue(r.getReturnValue());
                        result.setException(r.getException());
                        //ctx.writeAndFlush(msg);
                    }
                });
            }
        });

        System.out.println("Transfer deal with..");

        try {
            //6.连接端口启动服务
            ChannelFuture channelFuture = bootstrap.connect(hap.getHost(),hap.getPort()).sync();
            //7.关闭SocketChannel
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //8.关闭线程资源
        parent.shutdownGracefully();
        System.out.println("Transfer result: "+result);
        return result;
    }
}
