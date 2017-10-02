package com.asiainfo.netty.websocket;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;

/**
 * @Description: TODO
 * 
 * @author       zq
 * @date         2017年10月2日  下午1:53:21
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class TextWebSocketFrameHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

	final ChannelGroup group;
	public TextWebSocketFrameHandler(ChannelGroup group) {
		this.group = group;
	}
	
	/* 
	 * @Description: TODO
	 * @param ctx
	 * @param msg
	 * @throws Exception
	 * @see io.netty.channel.SimpleChannelInboundHandler#channelRead0(io.netty.channel.ChannelHandlerContext, java.lang.Object)
	 */
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {

		//将接收的消息通过ChannelGroup转发到所以已连接的客户端
		group.writeAndFlush(msg.retain());
	}
	
	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {

		//如果WebSocket握手完成
		if (evt instanceof WebSocketServerProtocolHandler.HandshakeComplete) {

			//删除ChannelPipeline中的HttpRequestHandler
			ctx.pipeline().remove(HttpRequestHandler.class);
			//写一个消息到ChannelGroup
			group.writeAndFlush(new TextWebSocketFrame("Client " + ctx.channel() + " joined"));
			//将Channel添加到ChannelGroup
			group.add(ctx.channel());
		} else {
			super.userEventTriggered(ctx, evt);
		}
	}
}
