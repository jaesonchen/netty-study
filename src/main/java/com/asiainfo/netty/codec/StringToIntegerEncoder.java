package com.asiainfo.netty.codec;

import java.util.List;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

/**
 * @Description: TODO
 * 
 * @author       zq
 * @date         2017年10月2日  上午11:38:40
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class StringToIntegerEncoder extends MessageToMessageEncoder<String> {

	/* 
	 * @Description: TODO
	 * @param ctx
	 * @param msg
	 * @param out
	 * @throws Exception
	 * @see io.netty.handler.codec.MessageToMessageEncoder#encode(io.netty.channel.ChannelHandlerContext, java.lang.Object, java.util.List)
	 */
	@Override
	protected void encode(ChannelHandlerContext ctx, String msg, List<Object> out) throws Exception {
		out.add(Integer.valueOf(msg));
	}
}
