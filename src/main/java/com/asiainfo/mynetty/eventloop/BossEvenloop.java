package com.asiainfo.mynetty.eventloop;

import java.io.IOException;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.Executor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.asiainfo.mynetty.future.ChannelFuture;

/**
 * 
 * @Description: boss实现类
 * 
 * @author       zq
 * @date         2017年10月5日  上午11:05:19
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class BossEvenloop extends AbstractEventLoop implements Boss {

	final Logger logger = LoggerFactory.getLogger(getClass());
	
	public BossEvenloop(Executor executor, String threadName, EventLoopGroup group) {
		super(executor, threadName, group);
	}

	@Override
	protected void process(Selector selector) throws Exception {
		
		logger.info("boss process selector!");
		Set<SelectionKey> selectedKeys = selector.selectedKeys();
        if (selectedKeys.isEmpty()) {
            return;
        }
        
        Iterator<SelectionKey> it = this.selector.selectedKeys().iterator();
        while (it.hasNext()) {
        	SelectionKey key = it.next();
            // 移除，防止重复处理
            it.remove();
            
            ServerSocketChannel ssChannel = (ServerSocketChannel) key.channel();
            logger.debug("boss accept new connection!");
    		// 新客户端
    		SocketChannel channel = ssChannel.accept();
    		// 设置为非阻塞
    		channel.configureBlocking(false);
    		// 获取一个worker
    		Worker nextworker = this.getEventLoopGroup().nextWorker();
    		// 注册新客户端接入任务
    		nextworker.registerChannel(channel, SelectionKey.OP_READ, null);
        }
	}
	
	@Override
	protected int select(Selector selector) throws IOException {
		logger.debug("boss select()!");
		return selector.select();
	}

	/* 
	 * @Description: TODO
	 * @param serverChannel
	 * @param future
	 * @see com.asiainfo.mynetty.eventloop.Boss#registerAcceptChannel(java.nio.channels.ServerSocketChannel, com.asiainfo.mynetty.future.ChannelFuture)
	 */
	@Override
	public void registerAcceptChannel(ServerSocketChannel ssChannel, ChannelFuture future) {

		logger.info("register Accept Channel task!");
		final Selector selector = this.selector;
		registerTask(new Runnable() {
			@Override
			public void run() {
				try {
					//注册serverChannel到selector
					ssChannel.register(selector, SelectionKey.OP_ACCEPT);
				} catch (ClosedChannelException e) {
					e.printStackTrace();
				} finally {
					if (null != future) {
						future.notifier();
					}
				}
			}
		});
	}
}
