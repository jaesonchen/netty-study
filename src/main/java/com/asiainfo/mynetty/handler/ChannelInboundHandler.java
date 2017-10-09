package com.asiainfo.mynetty.handler;

/**
 * @Description: TODO
 * 
 * @author       zq
 * @date         2017年10月3日  上午10:43:10
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public interface ChannelInboundHandler extends ChannelHandler {

	void channelActive(ChannelHandlerContext ctx) throws Exception;
	
	void channelInactive(ChannelHandlerContext ctx) throws Exception;
	
	void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception;
	
	void userEventTriggered(ChannelHandlerContext ctx, Object event) throws Exception;
}
