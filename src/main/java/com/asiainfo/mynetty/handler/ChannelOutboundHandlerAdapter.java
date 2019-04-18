package com.asiainfo.mynetty.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Description: OutboundHandler Adapter，缺省实现，客户端编写Handler时可继承该类，只重写需要的方法。
 *               ChannelOutboundHandlerAdapter默认作为channelpipeline的handler队列最后一个处理器。
 * 
 * @author       zq
 * @date         2017年10月3日  上午11:04:35
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class ChannelOutboundHandlerAdapter implements ChannelOutboundHandler {

	final Logger logger = LoggerFactory.getLogger(getClass());
	
	/* 
	 * @Description: TODO
	 * @param ctx
	 * @param msg
	 * @throws Exception
	 * @see com.asiainfo.mynetty.handler.ChannelOutboundHandler#write(com.asiainfo.mynetty.handler.ChannelHandlerContext, java.lang.Object)
	 */
	@Override
	public void write(ChannelHandlerContext ctx, Object msg) throws Exception {
		logger.debug("ChannelOutboundHandlerAdapter write!");
		ctx.write(msg);
	}

	/* 
	 * @Description: TODO
	 * @param ctx
	 * @throws Exception
	 * @see com.asiainfo.mynetty.handler.ChannelOutboundHandler#flush(com.asiainfo.mynetty.handler.ChannelHandlerContext)
	 */
	@Override
	public void flush(ChannelHandlerContext ctx) throws Exception {
		logger.debug("ChannelOutboundHandlerAdapter flush!");
		ctx.flush();
	}
}
