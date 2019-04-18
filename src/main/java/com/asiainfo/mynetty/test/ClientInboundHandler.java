package com.asiainfo.mynetty.test;

import com.asiainfo.mynetty.future.ChannelFutureListener;
import com.asiainfo.mynetty.handler.ChannelHandlerContext;
import com.asiainfo.mynetty.handler.ChannelInboundHandlerAdapter;
import com.asiainfo.mynetty.pipeline.ReleaseUtil;

/**
 * @Description: TODO
 * 
 * @author       zq
 * @date         2017年10月10日  下午10:32:14
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class ClientInboundHandler extends ChannelInboundHandlerAdapter {

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		
		System.out.println("client channelActive!");
		ctx.pipeline().writeAndFlush(new String("chenzq").getBytes("utf-8"));
		ctx.fireChannelActive();
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		
		System.out.println("client channelInactive!");
		ctx.fireChannelInactive();
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		try {
    		byte[] bt = (byte[]) msg;
    		System.out.println(new String(bt));
    		ctx.pipeline().writeAndFlush("bye".getBytes()).addListener(ChannelFutureListener.CLOSE);
		} finally {
		    ReleaseUtil.release(ctx);
		}
	}
}
