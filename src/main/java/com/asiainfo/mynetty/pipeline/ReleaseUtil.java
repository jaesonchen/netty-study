package com.asiainfo.mynetty.pipeline;

import java.io.ByteArrayOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.asiainfo.mynetty.handler.ChannelHandlerContext;

/**
 * @Description: TODO
 * 
 * @author       zq
 * @date         2017年10月6日  下午4:52:06
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class ReleaseUtil {

	static final Logger logger = LoggerFactory.getLogger(ReleaseUtil.class);
	
	public static void release(ChannelHandlerContext ctx) {
		logger.debug("release message!");
		ctx.pipeline().buffer().reset();
	}
	
	public static void release(ChannelHandlerContext ctx, int length) {
		
		logger.debug("release message, length={}!", length);
		ByteArrayOutputStream buff = ctx.pipeline().buffer();
		byte[] bt = buff.toByteArray();
		buff.reset();
		if (bt.length == length) {
			return;
		}
		buff.write(bt, length, bt.length - length);
	}
}
