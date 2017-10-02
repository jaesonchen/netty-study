package com.asiainfo.netty.rpc.codec;

import java.util.List;

import com.asiainfo.netty.rpc.serializer.HessianSerializer;
import com.asiainfo.netty.rpc.serializer.Serializer;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

/**
 * @Description: TODO
 * 
 * @author       zq
 * @date         2017年9月10日  下午3:40:14
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class CodecDecoder extends ByteToMessageDecoder {

	Serializer<Object> serializer = new HessianSerializer();
	
	/* 
	 * @Description: TODO
	 * @param ctx
	 * @param in
	 * @param out
	 * @throws Exception
	 * @see io.netty.handler.codec.ByteToMessageDecoder#decode(io.netty.channel.ChannelHandlerContext, io.netty.buffer.ByteBuf, java.util.List)
	 */
	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {

		// 如果读取的字节数小于消息头, 直接返回
        if (in.readableBytes() < 4) {
            return;
        }

        // 标记一下当前的readIndex的位置
        in.markReaderIndex();
        // 读取传送过来的消息的长度。ByteBuf 的readInt()方法会让readIndex增加4
        int dataLength = in.readInt();
        // 读到的消息体长度如果小于传送过来的消息长度，则resetReaderIndex.
        // 这个配合markReaderIndex使用的。把readIndex重置到mark的地方
        if (in.readableBytes() < dataLength) {
            in.resetReaderIndex();
            return;
        }

        byte[] body = new byte[dataLength];
        // 读取消息体
        in.readBytes(body);
        Object obj = serializer.deserialize(body);
        out.add(obj);
	}
}
