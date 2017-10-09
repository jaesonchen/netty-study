package com.asiainfo.mynetty.handler;

/**
 * @Description: TODO
 * 
 * @author       zq
 * @date         2017年10月3日  上午10:46:42
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public interface ChannelOutboundHandler extends ChannelHandler {

	void write(ChannelHandlerContext ctx, Object msg) throws Exception;
	
	void flush(ChannelHandlerContext ctx) throws Exception;
}
