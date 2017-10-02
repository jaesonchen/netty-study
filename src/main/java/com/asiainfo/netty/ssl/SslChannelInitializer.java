package com.asiainfo.netty.ssl;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.handler.ssl.SslHandler;

/**
 * @Description: TODO
 * 
 * @author       zq
 * @date         2017年10月2日  下午1:02:36
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class SslChannelInitializer extends ChannelInitializer<Channel> {

	private final SSLContext context;
	private final boolean client;
	private final boolean startTls;

	public SslChannelInitializer(SSLContext context, boolean client, boolean startTls) {
		this.context = context;
		this.client = client;
		this.startTls = startTls;
	}
	
	/* 
	 * @Description: TODO
	 * @param ch
	 * @throws Exception
	 * @see io.netty.channel.ChannelInitializer#initChannel(io.netty.channel.Channel)
	 */
	@Override
	protected void initChannel(Channel ch) throws Exception {

		SSLEngine engine = context.createSSLEngine();
		engine.setUseClientMode(client);
		//SslHandler必须要添加到ChannelPipeline的第一个位置
		ch.pipeline().addFirst("ssl", new SslHandler(engine, startTls));
	}
}
