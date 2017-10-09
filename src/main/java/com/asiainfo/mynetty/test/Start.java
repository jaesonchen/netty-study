package com.asiainfo.mynetty.test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import com.asiainfo.mynetty.eventloop.EventLoopGroup;
import com.asiainfo.mynetty.eventloop.ServerBootstrap;
import com.asiainfo.mynetty.future.ChannelFuture;
import com.asiainfo.mynetty.future.FutureListener;
import com.asiainfo.mynetty.pipeline.ChannelInitializer;
import com.asiainfo.mynetty.pipeline.ChannelPipeline;

/**
 * @Description: TODO
 * 
 * @author       zq
 * @date         2017年10月6日  下午4:06:48
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class Start {

	/** 
	 * @Description: TODO
	 * 
	 * @param args
	 * @throws IOException 
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws Exception {

		EventLoopGroup group = new EventLoopGroup(
				Executors.newCachedThreadPool(), 
				Executors.newCachedThreadPool(), 
				1, 2);
		ServerBootstrap b = new ServerBootstrap(group);
		b.handler(new ChannelInitializer() {
			@Override
			public void initChannel(ChannelPipeline ch) throws Exception {
				ch.addHandler(new InboundHandler());
			}});
		ChannelFuture f = b.bind(new InetSocketAddress(8080));
		f.addListener(new FutureListener() {
			@Override
			public void operationComplete(ChannelFuture future) throws Exception {
				System.out.println("operationComplete execute!");
			}
		});
		System.out.println("my netty server started .");
		f.closeFuture().sync();
	}
}