package com.asiainfo.netty.heartbeat;

import java.util.concurrent.TimeUnit;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;

/**
 * @Description: 心跳服务端handler
 * 
 * @author       zq
 * @date         2017年10月2日  下午1:17:41
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class IdleStateHandlerInitializer extends ChannelInitializer<Channel> {

	/* 
	 * @Description: TODO
	 * @param ch
	 * @throws Exception
	 * @see io.netty.channel.ChannelInitializer#initChannel(io.netty.channel.Channel)
	 */
	@Override
	protected void initChannel(Channel ch) throws Exception {

		ChannelPipeline pipeline = ch.pipeline();
		pipeline.addLast(new IdleStateHandler(0, 0, 60, TimeUnit.SECONDS));
		pipeline.addLast(new HeartbeatHandler());
	}
	
	public static final class HeartbeatHandler extends ChannelInboundHandlerAdapter {
		
		@Override
		public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {

			if (evt instanceof IdleStateEvent) {
				ctx.channel().close();
			} else {
				super.userEventTriggered(ctx, evt);
			}
		}
	}
}
