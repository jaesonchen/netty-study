package com.asiainfo.netty.http;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;

/**
 * @Description: TODO
 * 
 * @author       zq
 * @date         2017年9月10日  下午1:02:31
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class HttpServer {

	Logger logger = LoggerFactory.getLogger(getClass());
	
	//@Value("${server.port:8080}")
    private int port;
    
    public HttpServer(int port) {
    	this.port = port;
    }
    
	/** 
	 * @Description: TODO
	 * 
	 * @param args
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws InterruptedException {
		new HttpServer(8080).start();
	}
	
	@PostConstruct
    public void start() throws InterruptedException {
		
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		try {
	        ServerBootstrap b = new ServerBootstrap();
	        b.group(bossGroup, workerGroup)
	                .channel(NioServerSocketChannel.class)
	                .childHandler(new ChannelInitializer<SocketChannel>() {
	                    @Override
	                    protected void initChannel(SocketChannel ch) throws Exception {
	                        ch.pipeline().addLast(new HttpResponseEncoder());
	                        ch.pipeline().addLast(new HttpRequestDecoder());
	                        ch.pipeline().addLast(new HttpServerHandler());
	                    }
	                })
	                .option(ChannelOption.SO_BACKLOG, 128)
	                .childOption(ChannelOption.SO_KEEPALIVE, true);
	
	        ChannelFuture f = b.bind(port).sync();
	        logger.info("monitor server start, listen:{}", port);
	        f.channel().closeFuture().sync();
		} finally {
			bossGroup.shutdownGracefully();
		}
    }
}
