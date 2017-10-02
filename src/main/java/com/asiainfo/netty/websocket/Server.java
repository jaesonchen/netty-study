package com.asiainfo.netty.websocket;

import java.net.InetSocketAddress;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.ImmediateEventExecutor;

/**
 * @Description: TODO
 * 
 * @author       zq
 * @date         2017年10月2日  下午2:15:18
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class Server {

	final ChannelGroup group = new DefaultChannelGroup(ImmediateEventExecutor.INSTANCE);
	final EventLoopGroup workerGroup = new NioEventLoopGroup();
	Channel channel;

	public ChannelFuture start(InetSocketAddress address) {
		
		ServerBootstrap b = new ServerBootstrap();
		b.group(workerGroup)
			.channel(NioServerSocketChannel.class)
			.childHandler(createInitializer(group));
		ChannelFuture f = b.bind(address).syncUninterruptibly();
		channel = f.channel();
		return f;
	}

	public void destroy() {

		if (channel != null) {
			channel.close();
		}
		group.close();
		workerGroup.shutdownGracefully();
	}

	protected ChannelInitializer<Channel> createInitializer(ChannelGroup group) {
		return new ServerInitializer(group);
	}

	public static void main(String[] args) {
		
		final Server server = new Server();
		ChannelFuture f = server.start(new InetSocketAddress(2048));
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				server.destroy();
			}
		});
		f.channel().closeFuture().syncUninterruptibly();
	}
}
