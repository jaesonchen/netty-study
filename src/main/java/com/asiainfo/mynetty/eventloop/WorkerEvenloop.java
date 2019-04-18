package com.asiainfo.mynetty.eventloop;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.concurrent.Executor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.asiainfo.mynetty.future.ChannelFuture;
import com.asiainfo.mynetty.future.DefaultChannelFuture;
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
        
        Iterator<SelectionKey> it = this.selector.selectedKeys().iterator();
        while (it.hasNext()) {
            SelectionKey key = it.next();
            // 移除，防止重复处理
            it.remove();

            logger.info("worker process event!");
            //pipeline对象
            final ChannelPipeline pipeline = (ChannelPipeline) key.attachment();
            // a connection was established with a remote server.
            if (key.isConnectable()) {
            	logger.info("worker process connect!");
            	SocketChannel channel = (SocketChannel) key.channel();
            	if(channel.isConnectionPending()) {
            		channel.finishConnect();
				}
            	channel.configureBlocking(false);
            	this.registerChannel(channel, SelectionKey.OP_READ, new DefaultChannelFuture(pipeline));
            // a channel is ready for reading
            } else if (key.isReadable()) {
            	logger.info("worker process read!");
	            // 读取数据
	            int read = 0;
	            int count = 0;
	            boolean disconnect = true;
	            ByteBuffer buffer = ByteBuffer.allocate(1024);
	            try {
	            	while ((read = pipeline.socketChannel().read(buffer)) > 0) {
	            		count += read;
	            		//写入输入缓存中
	                    buffer.flip();
	                    while (buffer.hasRemaining()) {
	                    	pipeline.buffer().write(buffer.get());
	                    }
	                    buffer.clear();
	            	}
	            	disconnect = false;
	            } catch (IOException ex) {
	            	//ignore
	            }
	            
	            //判断是否连接已断开
	            if (count == 0 || disconnect) {
	                try {
	                	logger.info("channel is close, cancel key!");
		                key.cancel();
	                	logger.debug("fire channel inactive!");
	                	//channel inactive
						pipeline.fireChannelInactive();
						//notify close future
						pipeline.close();
					} catch (Exception e) {
						// ignore
					}
	                continue;
	            }
	            
	            //执行输入handler
	            try {
	            	logger.info("worker fire channel read!");
					pipeline.fireChannelRead(pipeline.buffer().toByteArray());
				} catch (Exception e) {
					logger.error("error on fireChannelRead!", e);
					pipeline.fireExceptionCaught(e);
				}
            } else {
            	logger.warn("upsupported event, key.interestOps={}", key.interestOps());
            }
        }
    }

    @Override
    protected int select(Selector selector) throws IOException {
    	logger.debug("worker select()!");
        return selector.select(5000);
    }

	/* 
	 * @Description: TODO
	 * @param channel
	 * @param op
	 * @param future
	 * @see com.asiainfo.mynetty.eventloop.Worker#registerChannel(java.nio.channels.SocketChannel, int, com.asiainfo.mynetty.future.ChannelFuture)
	 */
	@Override
	public void registerChannel(final SocketChannel channel, final int op, final ChannelFuture future) throws Exception {

		logger.info("register Channel task!");
        registerTask(new Runnable() {
            @Override
            public void run() {
                try {
                	ChannelPipeline pipeline = (null == future || null == future.pipeline()) ? 
            				ChannelPipeline.buildChannelPipeline(WorkerEvenloop.super.group, channel) : future.pipeline();
                	//注册读事件
                	if (SelectionKey.OP_READ == op) {
                		//将客户端注册到selector中
                        channel.register(WorkerEvenloop.super.selector, op, pipeline);
                        //channel active
                        pipeline.fireChannelActive();
                	}
                	//注册客户端连接事件
                	else if (SelectionKey.OP_CONNECT == op) {
                		//将客户端注册到selector中
                        channel.register(WorkerEvenloop.super.selector, op, pipeline);
                	}
                } catch (Exception e) {
                    logger.error("error on register SocketChannel!", e);
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
