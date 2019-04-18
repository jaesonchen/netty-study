package com.asiainfo.netty.reconnect;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * 
 * @Description: 断线重连
 * 
 * @author       zq
 * @date         2017年10月17日  上午11:40:15
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class ReconnectClient {

    final Logger logger = LoggerFactory.getLogger(getClass());

    private Bootstrap bootstrap;
    private EventLoopGroup workerGroup;

    final String host = "127.0.0.1";
    final int port = 5000;

    private void init() throws Exception {
    	
        workerGroup = new NioEventLoopGroup();
        bootstrap = new Bootstrap();
        bootstrap.group(workerGroup);
        bootstrap.channel(NioSocketChannel.class);
        bootstrap.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                ChannelPipeline pipeline = ch.pipeline();
                pipeline.addFirst(new ChannelInboundHandlerAdapter() {
                    @Override
                    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
                        super.channelInactive(ctx);
                        logger.info("client inactive reconnect");
                        ctx.channel().eventLoop().schedule(() -> connect(), 10, TimeUnit.SECONDS);
                    }
                });
            }
        });

        connect();
    }

    private void connect() {
    	
        ChannelFuture future = bootstrap.connect(host, port);
        future.addListener(new ChannelFutureListener() {
        	@Override
            public void operationComplete(ChannelFuture f) throws Exception {
                if (f.isSuccess()) {
                    logger.info("client connected success");
                } else {
                    logger.info("client connected failed");
                    f.channel().eventLoop().schedule(() -> connect(), 10, TimeUnit.SECONDS);
                }
            }
        });
    }

    public static void main(String[] args) throws Exception {
        new ReconnectClient().init();
    }
}
