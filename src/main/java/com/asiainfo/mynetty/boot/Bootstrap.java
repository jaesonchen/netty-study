package com.asiainfo.mynetty.boot;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketOption;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.HashSet;
import java.util.Set;
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
	
    private static Set<SocketOption<?>> options = new HashSet<>(8);
    static {
        //options.add(ChannelOption.SO_TIMEOUT);
        options.add(ChannelOption.SO_KEEPALIVE);
        options.add(ChannelOption.TCP_NODELAY);
    }
    
	private EventLoopGroup group;
	private SocketChannel channel;
    public Bootstrap(EventLoopGroup group) throws IOException {
        this.group = group;
        // 打开一个socket通道
        this.channel = SocketChannel.open();
    }

    /**
     * @Description: 连接服务器
     * @author chenzq
     * @date 2019年5月9日 下午9:30:28
     * @param ip
     * @param port
     * @return
     * @throws Exception
     */
    public ChannelFuture connect(String ip, int port) throws Exception {
    	
    	logger.info("connect to server: {}:{}", ip, port);
    	channel.configureBlocking(false);
    	// 异步连接服务器，并注册connect事件和初始化管道handler链
		return executeChannel(channel, new InetSocketAddress(ip, port));
    }
    
    /**
     * @Description: 设置socket属性
     * @author chenzq
     * @date 2019年5月9日 下午8:45:25
     * @param option
     * @param value
     * @return
     */
    public <T> Bootstrap option(SocketOption<T> option, T value) throws IOException {
        
        logger.info("set option: {}:{}", option.name(), value);
        if (!options.contains(option)) {
            throw new IllegalArgumentException("option not supported!");
        }
        this.channel.setOption(option, value);
        return this;
    }

    /**
     * @Description: 注册channel handler
     * @author chenzq
     * @date 2019年5月9日 下午9:30:40
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
