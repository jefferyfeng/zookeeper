package com.baizhi.serialize;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import org.apache.commons.lang3.SerializationUtils;

import java.io.Serializable;
import java.util.List;

public class CustomMessageToMessageEncoder extends MessageToMessageEncoder<Object> {
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Object o, List<Object> list) throws Exception {
        System.out.println("encode..\n");
        //获取buf对象
        ByteBuf buf = channelHandlerContext.alloc().buffer();
        //将对象序列后的数据写入buf中
        buf.writeBytes(SerializationUtils.serialize((Serializable) o));
        //存入list中，list是数据帧
        list.add(buf);
    }
}
