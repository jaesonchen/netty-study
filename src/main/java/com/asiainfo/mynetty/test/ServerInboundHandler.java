package com.asiainfo.mynetty.test;

import java.io.IOException;

import com.asiainfo.mynetty.future.ChannelFuture;
import com.asiainfo.mynetty.future.ChannelFutureListener;
import com.asiainfo.mynetty.handler.ChannelHandlerContext;
import com.asiainfo.mynetty.handler.ChannelInboundHandlerAdapter;
import com.asiainfo.mynetty.pipeline.ReleaseUtil;

/**
 * @Description: TODO
 * 
 * @author       zq
 * @date         2017年10月6日  下午4:26:42
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class ServerInboundHandler extends ChannelInboundHandlerAdapter {
	
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		
		System.out.println("channelActive!");
		ctx.fireChannelActive();
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		
		System.out.println("channelInactive!");
		ctx.fireChannelInactive();
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		
	    try {
    		byte[] bt = (byte[]) msg;
    		System.out.println(new String(bt));
    		if (!"bye".equals(new String(bt))) {
    			ctx.pipeline().writeAndFlush(("hello " + new String(bt)).getBytes()).addListener(new ChannelFutureListener() {
    				@Override
    				public void operationComplete(ChannelFuture future) throws IOException {
    					System.out.println("Server futureListener after writeAndFlush!");
    				}
    			});
    		}
	    } finally {
            ReleaseUtil.release(ctx);
        }
	}
}
