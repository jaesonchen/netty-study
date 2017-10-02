package com.asiainfo.netty.codec;

import java.util.List;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

/**
 * @Description: TODO
 * 
 * @author       zq
 * @date         2017年10月2日  上午11:30:04
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class IntegerToStringDecoder extends MessageToMessageDecoder<Integer> {

	/* 
	 * @Description: TODO
	 * @param ctx
	 * @param msg
	 * @param out
	 * @throws Exception
	 * @see io.netty.handler.codec.MessageToMessageDecoder#decode(io.netty.channel.ChannelHandlerContext, java.lang.Object, java.util.List)
	 */
	@Override
	protected void decode(ChannelHandlerContext ctx, Integer msg, List<Object> out) throws Exception {
		out.add(String.valueOf(msg));
	}
}
