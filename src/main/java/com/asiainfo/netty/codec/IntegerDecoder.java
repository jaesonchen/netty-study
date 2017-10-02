package com.asiainfo.netty.codec;

import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

/**
 * @Description: TODO
 * 
 * @author       zq
 * @date         2017年10月2日  上午11:28:09
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class IntegerDecoder extends ByteToMessageDecoder {

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

		if (in.readableBytes() >= 4) {
			out.add(in.readInt());
		}
	}
}
