package com.asiainfo.mynetty.eventloop;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.Executor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.asiainfo.mynetty.handler.Context;
import com.asiainfo.mynetty.pipeline.ChannelPipeline;

/**
 * 
 * @Description: worker实现类
 * 
 * @author       zq
 * @date         2017年10月5日  上午10:39:24
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class WorkerEvenloop extends AbstractEventLoop implements Worker {

	final Logger logger = LoggerFactory.getLogger(getClass());
	
    public WorkerEvenloop(Executor executor, String threadName, EventLoopGroup group) {
        super(executor, threadName, group);
    }

    @Override
    protected void process(Selector selector) throws Exception {
    	
    	logger.info("worker process selector!");
        Set<SelectionKey> selectedKeys = selector.selectedKeys();
        if (selectedKeys.isEmpty()) {
            return;
        }
        
        Iterator<SelectionKey> it = this.selector.selectedKeys().iterator();
        while (it.hasNext()) {
            SelectionKey key = it.next();
            // 移除，防止重复处理
            it.remove();

            logger.debug("worker process read event!");
            // 得到事件发生的Socket通道
            SocketChannel channel = (SocketChannel) key.channel();
            
            //pipeline对象
            final ChannelPipeline pipeline = (ChannelPipeline) key.attachment();
            
            // 读取数据
            int read = 0;
            int count = 0;
            boolean disconnect = true;
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            try {
            	while ((read = channel.read(buffer)) > 0) {
            		count += read;
            		//写入输入缓存中
                    buffer.flip();
                    while (buffer.hasRemaining()) {
                    	pipeline.getBuffer().write(buffer.get());
                    }
                    buffer.clear();
            	}
            	disconnect = false;
            } catch (IOException ex) {
            	//ignore
            }
            
            //判断是否连接已断开
            if (count == 0 || disconnect) {
            	logger.warn("channel is close, cancel key!");
                key.cancel();
                logger.debug("fire channel inactive!");
                try {
                	//channel inactive
					pipeline.fireChannelInactive();
				} catch (Exception e) {
					// ignore
				}
                continue;
            }
            
            logger.debug("worker fire channel read!");
            //执行输入handler
            try {
				pipeline.fireChannelRead(pipeline.getBuffer().toByteArray());
			} catch (Exception e) {
				logger.error("error on fireChannelRead!", e);
				pipeline.fireExceptionCaught(e);
			}
        }
    }

    @Override
    protected int select(Selector selector) throws IOException {
    	logger.debug("worker select()!");
        return selector.select(1000);
    }


	/* 
	 * @Description: TODO
	 * @param channel
	 * @see com.asiainfo.mynetty.selector.Worker#registerChannel(java.nio.channels.SocketChannel)
	 */
	@Override
	public void registerChannel(final SocketChannel channel) {

		logger.info("register Channel task!");
		final Selector selector = this.selector;
        registerTask(new Runnable() {
            @Override
            public void run() {
                try {
                	ChannelPipeline pipeline = buildChannelPipeline(channel);
                    //将客户端注册到selector中
                    channel.register(selector, SelectionKey.OP_READ, pipeline);
                    //channel active
                    pipeline.fireChannelActive();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
	}
	
	/*
	 * 初始化pipelline和hanlder
	 */
	protected ChannelPipeline buildChannelPipeline(final SocketChannel channel) throws Exception {
		
		logger.info("build channel pipeline & init handler!");
		ChannelPipeline pipeline = new ChannelPipeline(channel);
		Context.getContext().getInitializer().initChannel(pipeline);
		return pipeline;
	}
}
