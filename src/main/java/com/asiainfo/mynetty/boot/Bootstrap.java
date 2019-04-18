package com.asiainfo.mynetty.boot;

import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.concurrent.Callable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.asiainfo.mynetty.eventloop.EventLoopGroup;
import com.asiainfo.mynetty.eventloop.Worker;
import com.asiainfo.mynetty.future.ChannelFuture;
import com.asiainfo.mynetty.future.DefaultChannelFuture;
import com.asiainfo.mynetty.handler.HandlerInitializerContext;
import com.asiainfo.mynetty.pipeline.ChannelInitializer;
import com.asiainfo.mynetty.pipeline.ChannelPipeline;
import com.asiainfo.mynetty.pipeline.ExecutorTools;

/**
 * @Description: 客户端启动程序
 * 
 * @author       zq
 * @date         2017年10月10日  下午5:34:39
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class Bootstrap {

	final Logger logger = LoggerFactory.getLogger(getClass());
	
	private EventLoopGroup group;
    public Bootstrap(EventLoopGroup group) {
        this.group = group;
    }

    /**
     * 连接服务器
     * 
     * @param ip
     * @param port
     * @return
     * @throws Exception
     */
    public ChannelFuture connect(String ip, int port) throws Exception {
    	
    	logger.info("connect to server: {}:{}", ip, port);
    	SocketChannel channel = SocketChannel.open();
    	channel.configureBlocking(false);
		return executeChannel(channel, new InetSocketAddress(ip, port));
    }
    
    /**
     * 注册channel初始化
     * 
     * @param initializer
     * @return
     */
    public Bootstrap handler(ChannelInitializer initializer) {
    	
    	logger.info("set ChannelInitializer!");
    	HandlerInitializerContext.getContext().setInitializer(this.group, initializer);
    	return this;
    }
    
    // 连接服务器，返回ChannelFuture
    private ChannelFuture executeChannel(final SocketChannel channel, final InetSocketAddress localAddress) throws Exception {
    	
    	final DefaultChannelFuture future = new DefaultChannelFuture(ChannelPipeline.buildChannelPipeline(this.group, channel));
    	Callable<Void> callable = new Callable<Void>() {
			@Override
			public Void call() throws Exception {
				// 连接server
				channel.connect(localAddress);
				//获取一个worker线程
		    	Worker nextworker = Bootstrap.this.group.nextWorker();
		    	// 注册新客户端接入任务
	    		nextworker.registerChannel(channel, SelectionKey.OP_CONNECT, future);
	    		// registerChannel在wakeup selector时，会立即执行select() block的线程，导致后续的notifier先于wait
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
