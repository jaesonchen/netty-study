package com.asiainfo.mynetty.eventloop;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.util.concurrent.Callable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.asiainfo.mynetty.future.ChannelFuture;
import com.asiainfo.mynetty.future.DefaultChannelFuture;
import com.asiainfo.mynetty.handler.Context;
import com.asiainfo.mynetty.pipeline.ChannelInitializer;
import com.asiainfo.mynetty.pipeline.ExecutorTools;

/**
 * 
 * @Description: 启动类
 * 
 * @author       zq
 * @date         2017年10月5日  上午10:11:28
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class ServerBootstrap {

	final Logger logger = LoggerFactory.getLogger(getClass());
	
    private EventLoopGroup group;

    public ServerBootstrap(EventLoopGroup group) {
        this.group = group;
    }

    /**
     * 绑定端口
     *
     * @param localAddress
     * @throws IOException 
     */
    public ChannelFuture bind(final InetSocketAddress localAddress) throws IOException {
    	
    	logger.info("bind to {}", localAddress);
    	// 获得一个ServerSocket通道
    	ServerSocketChannel ssChannel = ServerSocketChannel.open();
    	// 设置通道为非阻塞
    	ssChannel.configureBlocking(false);
    	// 将该通道对应的ServerSocket绑定到port端口
    	ssChannel.socket().bind(localAddress);
    	
        return executeServerChannel(ssChannel);
    }
    
    /**
     * 注册channel初始化
     * 
     * @param initializer
     * @return
     */
    public ServerBootstrap handler(ChannelInitializer initializer) {
    	
    	logger.info("register channel handler initializer!");
    	Context.getContext().setInitializer(initializer);
    	return this;
    }
    
    protected ChannelFuture executeServerChannel(final ServerSocketChannel ssChannel) {
    	
    	final DefaultChannelFuture future = new DefaultChannelFuture();
    	Callable<Void> callable = new Callable<Void>() {
			@Override
			public Void call() throws Exception {
				//获取一个boss线程
		    	Boss nextBoss = ServerBootstrap.this.group.nextBoss();
		    	//向boss注册一个ServerSocket通道
		    	nextBoss.registerAcceptChannel(ssChannel, future);
		    	//等待执行完成
		    	future.await();
				return null;
			}
    	};
    	
    	future.setChannel(ssChannel).setFuture(ExecutorTools.getInstance().getExecutor().submit(callable));
    	return future;
    }
}
