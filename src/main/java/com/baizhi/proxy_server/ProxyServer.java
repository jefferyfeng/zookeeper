package com.baizhi.proxy_server;

import com.baizhi.local.HostAndPort;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class ProxyServer {
    //代理服务器
    private static final HostAndPort hostAndPort = new HostAndPort("localhost",9999);

    public static void main(String[] args) throws Exception{
        //1.创建服务启动引导
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        //2.配置线程池组 parent child
        EventLoopGroup parent = new NioEventLoopGroup();
        EventLoopGroup child = new NioEventLoopGroup();
        //3.配置线程池组
        serverBootstrap.group(parent,child);

        //4.设置服务器实现
        serverBootstrap.channel(NioServerSocketChannel.class);
        //5.初始化通信管道
        serverBootstrap.childHandler(new ServerChannelInitializer());

        System.out.println("Proxy Server wait..");
        //6.绑定端口启动服务

        ChannelFuture channelFuture = serverBootstrap.bind(hostAndPort.getHost(),hostAndPort.getPort()).sync();
        //7.关闭SocketChannel
        channelFuture.channel().closeFuture().sync();

        //8.关闭线程资源
        parent.shutdownGracefully();
        child.shutdownGracefully();
    }
}
