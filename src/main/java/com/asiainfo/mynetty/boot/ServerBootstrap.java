package com.asiainfo.mynetty.boot;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketOption;
import java.nio.channels.ServerSocketChannel;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Callable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.asiainfo.mynetty.eventloop.Boss;
import com.asiainfo.mynetty.eventloop.EventLoopGroup;
import com.asiainfo.mynetty.future.ChannelFuture;
import com.asiainfo.mynetty.future.DefaultChannelFuture;
import com.asiainfo.mynetty.handler.HandlerInitializerContext;
import com.asiainfo.mynetty.pipeline.ChannelInitializer;
import com.asiainfo.mynetty.pipeline.ChannelPipeline;
import com.asiainfo.mynetty.pipeline.ExecutorTools;

/**
 * 
 * @Description: 服务器启动类
 * 
 * @author       zq
 * @date         2017年10月5日  上午10:11:28
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class ServerBootstrap {

	final Logger logger = LoggerFactory.getLogger(getClass());
	
	private static Set<SocketOption<?>> options = new HashSet<>(8);
	static {
	    //options.add(ChannelOption.SO_BACKLOG);
	    //options.add(ChannelOption.SO_TIMEOUT);
	    options.add(ChannelOption.SO_KEEPALIVE);
	    options.add(ChannelOption.SO_REUSEADDR);
	    options.add(ChannelOption.TCP_NODELAY);
	}
	
    private EventLoopGroup group;
    private ServerSocketChannel ssChannel;
    public ServerBootstrap(EventLoopGroup group) throws IOException {
        this.group = group;
        // 打开一个ServerSocket通道
        this.ssChannel = ServerSocketChannel.open();
    }

    /**
     * @Description: 绑定端口
     * @author chenzq
     * @date 2019年5月9日 下午9:31:32
     * @param localAddress
     * @return
     * @throws IOException
     */
    public ChannelFuture bind(final InetSocketAddress localAddress) throws IOException {
    	
    	logger.info("bind to {}", localAddress);
    	// 设置通道为非阻塞
    	ssChannel.configureBlocking(false);
    	// 异步绑定端口并注册accept事件
        return executeServerChannel(ssChannel, localAddress);
    }
    
    /**
     * @Description: 设置socket属性
     * @author chenzq
     * @date 2019年5月9日 下午8:45:25
     * @param option
     * @param value
     * @return
     * @throws IOException 
     */
    public <T> ServerBootstrap option(SocketOption<T> option, T value) throws IOException {
        
        logger.info("set option: {}:{}", option.name(), value);
        if (!options.contains(option)) {
            throw new IllegalArgumentException("option not supported!");
        }
        this.ssChannel.setOption(option, value);
        return this;
    }
    
    /**
     * @Description: 注册channel handler
     * @author chenzq
     * @date 2019年5月9日 下午9:31:12
     * @param initializer
     * @return
     */
    public ServerBootstrap handler(ChannelInitializer initializer) {
    	
    	logger.info("set ChannelInitializer!");
    	HandlerInitializerContext.getContext().setInitializer(this.group, initializer);
    	return this;
    }
    
    // 异步绑定端口并注册accept事件
    private ChannelFuture executeServerChannel(final ServerSocketChannel ssChannel, final InetSocketAddress localAddress) {
    	
    	final DefaultChannelFuture future = new DefaultChannelFuture(new ChannelPipeline(ssChannel));
    	Callable<Void> callable = new Callable<Void>() {
			@Override
			public Void call() throws Exception {
		    	// 将该通道对应的ServerSocket绑定到port端口
		    	ssChannel.socket().bind(localAddress);
				//获取一个boss线程
		    	Boss nextBoss = ServerBootstrap.this.group.nextBoss();
		    	//向boss注册一个ServerSocket通道
		    	nextBoss.registerAcceptChannel(ssChannel, future);
		    	//等待执行完成
		    	// registerAcceptChannel在wakeup selector时，会立即执行select() block的线程，导致后续的notifier先于wait
		    	synchronized(future.getLock()) {
		    	    if (!future.isFinish()) {
		    	        future.await();
		    	    }
		    	}
		    	future.done();
				return null;
			}
    	};
    	return future.setFuture(ExecutorTools.getInstance().getExecutor().submit(callable));
    }
}
