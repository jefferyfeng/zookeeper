package com.baizhi.client;

import com.baizhi.bean.HostAndPort;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

public class NettyClient {
    //代理服务器
    private static final HostAndPort hostAndPort = new HostAndPort("localhost",9999);

    public static void main(String[] args) throws Exception{
        //1.创建服务启动引导
        Bootstrap bootstrap = new Bootstrap();
        //2.配置线程池组 parent child
        EventLoopGroup parent = new NioEventLoopGroup();
        //3.配置线程池组
        bootstrap.group(parent);

        //4.设置服务器实现
        bootstrap.channel(NioSocketChannel.class);
        //5.初始化通信管道
        bootstrap.handler(new ClientChannelInitializer());

        System.out.println("Client request..");
        //6.连接端口启动服务

        ChannelFuture channelFuture = bootstrap.connect(hostAndPort.getHost(),hostAndPort.getPort()).sync();
        //7.关闭SocketChannel
        channelFuture.channel().closeFuture().sync();

        //8.关闭线程资源
        parent.shutdownGracefully();
    }
}
