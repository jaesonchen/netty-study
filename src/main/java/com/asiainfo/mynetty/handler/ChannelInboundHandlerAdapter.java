package com.asiainfo.mynetty.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Description: InboundHandler Adapter，缺省实现，客户端编写Handler时可继承该类，只重写需要的方法。
 *               ChannelInboundHandlerAdapter默认作为channelpipeline的handler队列第一个处理器。
 * 
 * @author       zq
 * @date         2017年10月3日  上午11:03:52
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class ChannelInboundHandlerAdapter implements ChannelInboundHandler {

	final Logger logger = LoggerFactory.getLogger(getClass());
	
	/* 
	 * @Description: TODO
	 * @param ctx
	 * @param cause
	 * @throws Exception
	 * @see com.asiainfo.mynetty.handler.ChannelHandler#exceptionCaught(com.asiainfo.mynetty.handler.ChannelHandlerContext, java.lang.Throwable)
	 */
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		logger.debug("ChannelInboundHandlerAdapter exceptionCaught!");
		ctx.fireExceptionCaught(cause);
	}

	/* 
	 * @Description: TODO
	 * @param ctx
	 * @throws Exception
	 * @see com.asiainfo.mynetty.handler.ChannelInboundHandler#channelActive(com.asiainfo.mynetty.handler.ChannelHandlerContext)
	 */
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		logger.debug("ChannelInboundHandlerAdapter channelActive!");
		ctx.fireChannelActive();
	}

	/* 
	 * @Description: TODO
	 * @param ctx
	 * @throws Exception
	 * @see com.asiainfo.mynetty.handler.ChannelInboundHandler#channelInactive(com.asiainfo.mynetty.handler.ChannelHandlerContext)
	 */
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		logger.debug("ChannelInboundHandlerAdapter channelInactive!");
		ctx.fireChannelInactive();
	}

	/* 
	 * @Description: TODO
	 * @param ctx
	 * @param msg
	 * @throws Exception
	 * @see com.asiainfo.mynetty.handler.ChannelInboundHandler#channelRead(com.asiainfo.mynetty.handler.ChannelHandlerContext, java.lang.Object)
	 */
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		logger.debug("ChannelInboundHandlerAdapter channelRead!");
		ctx.fireChannelRead(msg);
	}

	/* 
	 * @Description: TODO
	 * @param ctx
	 * @param evt
	 * @throws Exception
	 * @see com.asiainfo.mynetty.handler.ChannelInboundHandler#userEventTriggered(com.asiainfo.mynetty.handler.ChannelHandlerContext, java.lang.Object)
	 */
	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object event) throws Exception {
		logger.debug("ChannelInboundHandlerAdapter userEventTriggered!");
		ctx.fireUserEventTriggered(event);
	}
}
