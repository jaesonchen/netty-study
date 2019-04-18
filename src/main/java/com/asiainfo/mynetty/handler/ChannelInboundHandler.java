package com.asiainfo.mynetty.handler;

/**
 * @Description: Inbound 接口，定义channel input
 * 
 * @author       zq
 * @date         2017年10月3日  上午10:43:10
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public interface ChannelInboundHandler extends ChannelHandler {

    /**
     * @Description: channel激活
     * @author chenzq
     * @date 2019年3月16日 下午6:12:46
     * @param ctx
     * @throws Exception
     */
	void channelActive(ChannelHandlerContext ctx) throws Exception;
	
	/**
	 * @Description: channel关闭
	 * @author chenzq
	 * @date 2019年3月16日 下午6:13:17
	 * @param ctx
	 * @throws Exception
	 */
	void channelInactive(ChannelHandlerContext ctx) throws Exception;
	
	/**
	 * @Description: channel OP_READ
	 * @author chenzq
	 * @date 2019年3月16日 下午6:13:30
	 * @param ctx
	 * @param msg
	 * @throws Exception
	 */
	void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception;
	
	/**
	 * @Description: 用户事件触发，通常用于心跳、idle
	 * @author chenzq
	 * @date 2019年3月16日 下午6:13:54
	 * @param ctx
	 * @param event
	 * @throws Exception
	 */
	void userEventTriggered(ChannelHandlerContext ctx, Object event) throws Exception;
	
    /**
     * @Description: 异常处理
     * @author chenzq
     * @date 2019年3月16日 下午6:20:45
     * @param ctx
     * @param cause
     * @throws Exception
     */
    void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception;
}
