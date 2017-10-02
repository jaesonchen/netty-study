package com.asiainfo.netty.rpc.codec;

import java.io.Serializable;

import com.asiainfo.netty.rpc.serializer.HessianSerializer;
import com.asiainfo.netty.rpc.serializer.Serializer;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @Description: TODO
 * 
 * @author       zq
 * @date         2017年9月10日  下午3:44:29
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class CodecEncoder extends MessageToByteEncoder<Serializable> {

	Serializer<Object> serializer = new HessianSerializer();
	
	/* 
	 * @Description: TODO
	 * @param ctx
	 * @param msg
	 * @param out
	 * @throws Exception
	 * @see io.netty.handler.codec.MessageToByteEncoder#encode(io.netty.channel.ChannelHandlerContext, java.lang.Object, io.netty.buffer.ByteBuf)
	 */
	@Override
	protected void encode(ChannelHandlerContext ctx, Serializable msg, ByteBuf out) throws Exception {

		// 将对象转换为byte, 可以使用任意序列化的工具
		byte[] body = serializer.serialize(msg);
		// 读取消息的长度
        int dataLength = body.length;
        // 先将消息长度写入，也就是消息头
        out.writeInt(dataLength);
        // 再写入具体的消息
        out.writeBytes(body);
	}
}
