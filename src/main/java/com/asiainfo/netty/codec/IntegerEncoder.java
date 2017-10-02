package com.asiainfo.netty.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @Description: TODO
 * 
 * @author       zq
 * @date         2017年10月2日  上午11:36:44
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class IntegerEncoder extends MessageToByteEncoder<Integer> {

	/* 
	 * @Description: TODO
	 * @param ctx
	 * @param msg
	 * @param out
	 * @throws Exception
	 * @see io.netty.handler.codec.MessageToByteEncoder#encode(io.netty.channel.ChannelHandlerContext, java.lang.Object, io.netty.buffer.ByteBuf)
	 */
	@Override
	protected void encode(ChannelHandlerContext ctx, Integer msg, ByteBuf out) throws Exception {
		out.writeInt(msg);
	}
}
