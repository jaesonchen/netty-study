package com.asiainfo.mynetty.test;

import java.io.IOException;

import com.asiainfo.mynetty.boot.Bootstrap;
import com.asiainfo.mynetty.boot.ChannelOption;
import com.asiainfo.mynetty.eventloop.EventLoopGroup;
import com.asiainfo.mynetty.future.ChannelFuture;
import com.asiainfo.mynetty.future.ChannelFutureListener;
import com.asiainfo.mynetty.pipeline.ChannelInitializer;
import com.asiainfo.mynetty.pipeline.ChannelPipeline;
import com.asiainfo.util.ThreadPoolUtils;

/**
 * @Description: TODO
 * 
 * @author       zq
 * @date         2017年10月10日  下午10:29:39
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class Client {

	public static void main(String[] args) throws Exception {

		EventLoopGroup group = new EventLoopGroup(ThreadPoolUtils.getInstance().cachedThreadPool(), 1);
		Bootstrap b = new Bootstrap(group);
		b.handler(new ChannelInitializer() {
			@Override
			public void initChannel(ChannelPipeline ch) throws Exception {
				ch.addHandler(new ClientInboundHandler());
			}})
        .option(ChannelOption.SO_KEEPALIVE, true);
		ChannelFuture f = b.connect("localhost", 8080);
		f.addListener(new ChannelFutureListener() {
			@Override
			public void operationComplete(ChannelFuture future) throws IOException {
				System.out.println("client connect finish!");
			}
		});
		System.out.println("my netty client started!");

		f.closeFuture().sync();
		System.out.println("my netty client stopped!");
		System.exit(1);
	}
}
