package com.asiainfo.netty.echo.server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;
import io.netty.util.ReferenceCountUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EchoServerHandler extends ChannelInboundHandlerAdapter {

	Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
    	
    	try {
	        logger.info("server: channelRead event");
	        ByteBuf in = (ByteBuf) msg;
	        logger.info("server: receive msg: {}", in.toString(CharsetUtil.UTF_8));
	        ctx.write(msg);
    	} finally {
    		ReferenceCountUtil.release(msg);
    	}
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
    	
        logger.info("server: channelReadComplete event");
        ctx.writeAndFlush(Unpooled.EMPTY_BUFFER) //flush掉所有写回的数据
        	.addListener(ChannelFutureListener.CLOSE); //当flush完成后关闭channel
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
    	
        logger.info("server: exceptionCaught event");
        cause.printStackTrace();
        ctx.close();
    }
}
