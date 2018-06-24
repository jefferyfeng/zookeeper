package com.baizhi.server1;

import com.baizhi.serialize.CustomMessageToMessageDecoder;
import com.baizhi.serialize.CustomMessageToMessageEncoder;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;

public class ServerChannelInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();
        pipeline.addLast(new LengthFieldPrepender(2));
        pipeline.addLast(new CustomMessageToMessageEncoder());
        pipeline.addLast(new LengthFieldBasedFrameDecoder(65535,0,2,0,2));
        pipeline.addLast(new CustomMessageToMessageDecoder());
        pipeline.addLast(new ServerChannelHandlerAdapter());
    }
}
