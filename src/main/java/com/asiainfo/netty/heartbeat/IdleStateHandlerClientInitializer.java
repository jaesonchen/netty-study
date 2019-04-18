package com.asiainfo.netty.heartbeat;

import java.util.concurrent.TimeUnit;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.CharsetUtil;

/**
 * @Description: 心跳客户端handler
 * 
 * @author       zq
 * @date         2017年10月11日  下午4:52:01
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class IdleStateHandlerClientInitializer extends ChannelInitializer<Channel> {

	/* 
	 * @Description: TODO
	 * @param ch
	 * @throws Exception
	 * @see io.netty.channel.ChannelInitializer#initChannel(io.netty.channel.Channel)
	 */
	@Override
	protected void initChannel(Channel ch) throws Exception {
		
		ChannelPipeline pipeline = ch.pipeline();
		pipeline.addLast(new IdleStateHandler(0, 10, 0, TimeUnit.SECONDS));
		pipeline.addLast(new HeartBeatClientHandler());
	}

	public static final class HeartBeatClientHandler extends ChannelInboundHandlerAdapter {

		private static final ByteBuf HEARTBEAT_SEQUENCE = Unpooled.unreleasableBuffer(
				Unpooled.copiedBuffer("HEARTBEAT", CharsetUtil.UTF_8));
		
		@Override
		public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
			
			if (evt instanceof IdleStateEvent) {
	            IdleStateEvent event = (IdleStateEvent) evt;
	            if (event.state() == IdleState.WRITER_IDLE) {
	                ctx.channel().writeAndFlush(HEARTBEAT_SEQUENCE.duplicate());
	            }
	        } else {
				super.userEventTriggered(ctx, evt);
			}
		}
	}
}
