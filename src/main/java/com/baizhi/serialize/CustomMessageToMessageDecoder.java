package com.baizhi.serialize;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import org.apache.commons.lang3.SerializationUtils;

import java.util.List;

/**
 * 解码 将byteBuf类型的对象解码为Object
 */
public class CustomMessageToMessageDecoder extends MessageToMessageDecoder<ByteBuf> {

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        System.out.println("decode");
        //创建bytes数组用来存储序列化后的对象的数据
        byte[] bytes = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(bytes);
        //解码回Object对象
        Object o = SerializationUtils.deserialize(bytes);

        //数据帧
        list.add(o);
    }
}
