package com.asiainfo.mynetty.test;

import java.io.IOException;

import com.asiainfo.mynetty.eventloop.Bootstrap;
import com.asiainfo.mynetty.eventloop.EventLoopGroup;
import com.asiainfo.mynetty.future.ChannelFuture;
import com.asiainfo.mynetty.future.FutureListener;
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

	/** 
	 * @Description: TODO
	 * 
	 * @param args
	 * @throws IOException 
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws Exception {

		EventLoopGroup group = new EventLoopGroup(ThreadPoolUtils.getInstance().cachedThreadPool(), 1);
		Bootstrap b = new Bootstrap(group);
		b.handler(new ChannelInitializer() {
			@Override
			public void initChannel(ChannelPipeline ch) throws Exception {
				ch.addHandler(new ClientInboundHandler());
			}});
		ChannelFuture f = b.connect("localhost", 8080);
		f.addListener(new FutureListener() {
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
