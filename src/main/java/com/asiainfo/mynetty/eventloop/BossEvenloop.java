package com.asiainfo.mynetty.eventloop;

import java.io.IOException;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.concurrent.Executor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.asiainfo.mynetty.future.ChannelFuture;

/**
 * 
 * @Description: boss线程实现类，每个boss线程监听一个端口
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
        Iterator<SelectionKey> it = getSelector().selectedKeys().iterator();
        while (it.hasNext()) {
        	SelectionKey key = it.next();
            // 移除，防止重复处理
            it.remove();
            
            logger.info("boss process event!");
            if (key.isAcceptable()) {
                ServerSocketChannel ssChannel = (ServerSocketChannel) key.channel();
                logger.debug("boss accept new connection!");
        		// 新客户端
        		SocketChannel channel = ssChannel.accept();
        		// 设置为非阻塞
        		channel.configureBlocking(false);
        		// 获取一个worker
        		Worker nextworker = getEventLoopGroup().nextWorker();
        		// 注册新客户端接入任务
        		nextworker.registerChannel(channel, SelectionKey.OP_READ, null);
            } else {
                logger.warn("upsupported event, key.interestOps={}", key.interestOps());
            }
        }
	}
	
	@Override
	protected int select(Selector selector) throws IOException {
		logger.debug("boss select()!");
		return selector.select();
	}

	@Override
	public void registerAcceptChannel(ServerSocketChannel ssChannel, ChannelFuture future) throws Exception {

		logger.info("register Accept Channel task!");
		registerTask(new Runnable() {
			@Override
			public void run() {
				try {
					//注册serverChannel到selector
					ssChannel.register(getSelector(), SelectionKey.OP_ACCEPT);
				} catch (ClosedChannelException e) {
					logger.error("error on register ServerSocketChannel!", e);
				} finally {
				    if (future != null) {
    				    synchronized(future.getLock()) {
    				        future.setFinish(true);
    				        future.notifier();
    				    }
				    }
				}
			}
		});
	}
}
