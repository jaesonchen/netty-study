package com.asiainfo.mynetty.pipeline;

import java.io.ByteArrayOutputStream;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SocketChannel;
import java.util.concurrent.Callable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.asiainfo.mynetty.eventloop.EventLoopGroup;
import com.asiainfo.mynetty.future.ChannelFuture;
import com.asiainfo.mynetty.future.DefaultChannelFuture;
import com.asiainfo.mynetty.handler.ChannelHandler;
import com.asiainfo.mynetty.handler.ChannelHandlerContext;
import com.asiainfo.mynetty.handler.ChannelInboundHandlerAdapter;
import com.asiainfo.mynetty.handler.ChannelOutboundHandlerAdapter;
import com.asiainfo.mynetty.handler.HandlerInitializerContext;

/**
 * @Description: Pipeline代表一个socketChannel在整个生命周期内的所有活动，并注册Handler处理器
 * 
 * @author       zq
 * @date         2017年10月3日  上午11:54:42
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class ChannelPipeline {

	static final Logger logger = LoggerFactory.getLogger(ChannelPipeline.class);
	
	final ChannelHandlerContext head;
	final ChannelHandlerContext tail;
	final SelectableChannel channel;
	/**
	 * 用于缓存读到的字节
	 */
	final ByteArrayOutputStream buffer = new ByteArrayOutputStream();
	final Object closeLock = new Object();
	final ChannelFuture closeFuture = new DefaultChannelFuture() {
			@Override
			public void sync() throws Exception {
				logger.debug("DefaultChannelFuture$1.sync()!");
				synchronized(ChannelPipeline.this.closeLock) {
				    ChannelPipeline.this.closeLock.wait();
				}
			}
		}.setPipeline(this).setFuture(null);
	
	public ChannelPipeline(SelectableChannel channel) {
		
		this.channel = channel;
		this.head = new ChannelHandlerContext(this, new ChannelInboundHandlerAdapter());
		this.tail = new ChannelHandlerContext(this, new ChannelOutboundHandlerAdapter());
		this.head.setNext(this.tail);
		this.tail.setPrev(this.head);
	}

	/**
	 * @Description: 为当前的channel创建一个pipeline，并初始化Handler 处理器链
	 * @author chenzq
	 * @date 2019年3月16日 下午7:16:17
	 * @param channel
	 * @return
	 * @throws Exception
	 */
    public static ChannelPipeline buildChannelPipeline(final EventLoopGroup group, final SelectableChannel channel) throws Exception {
        
        logger.info("build channel pipeline & init handler!");
        ChannelPipeline pipeline = new ChannelPipeline(channel);
        HandlerInitializerContext.getContext().getInitializer(group).initChannel(pipeline);
        return pipeline;
    }
    
	public ChannelHandlerContext head() {
		return this.head;
	}
	public ChannelHandlerContext tail() {
		return this.tail;
	}
	public SelectableChannel channel() {
		return this.channel;
	}
	public SocketChannel socketChannel() {
		
		assert channel instanceof SocketChannel;
		return (SocketChannel) this.channel;
	}
	public ByteArrayOutputStream buffer() {
		return this.buffer;
	}

	/**
	 * @Description: 往Handler链最后添加Handler
	 * @author chenzq
	 * @date 2019年3月18日 上午9:14:57
	 * @param handler
	 * @return
	 */
	public ChannelPipeline addHandler(ChannelHandler handler) {
		
		logger.info("ChannelPipeline addHandler:{}", handler.getClass().getSimpleName());
		ChannelHandlerContext context = new ChannelHandlerContext(this, handler);
		this.tail.prev().setNext(context);
		context.setPrev(this.tail.prev());
		context.setNext(this.tail);
		this.tail.setPrev(context);
		return this;
	}
	
	/**
	 * @Description: 触发pipeline的active 执行链，在注册OP_READ事件后调用
	 * @author chenzq
	 * @date 2019年3月16日 下午7:08:34
	 * @throws Exception
	 */
	public void fireChannelActive() throws Exception {
		
		logger.debug("ChannelPipeline fireChannelActive!");
		this.head().fireChannelActive();
	}
	
	/**
	 * @Description: 触发pipeline的Inactive 执行链，在处理selectKey时发现channel关闭时调用
	 * @author chenzq
	 * @date 2019年3月16日 下午7:09:59
	 * @throws Exception
	 */
	public void fireChannelInactive() throws Exception {
		
		logger.debug("ChannelPipeline fireChannelInactive!");
		this.head.fireChannelInactive();
	}
	
	/**
	 * @Description: TODO
	 * @author chenzq
	 * @date 2019年3月16日 下午7:26:54
	 * @param event
	 * @throws Exception
	 */
	public void fireUserEventTriggered(Object event) throws Exception {
		
		logger.debug("ChannelPipeline fireUserEventTriggered!");
		this.head.fireUserEventTriggered(event);
	}
	
	/**
	 * @Description: 触发pipeline的ChannelRead 执行链，在处理selectKey时，当读取到channel中的所有可读取数据时调用
	 * @author chenzq
	 * @date 2019年3月16日 下午7:12:59
	 * @param msg
	 * @throws Exception
	 */
	public void fireChannelRead(Object msg) throws Exception {
		
		logger.debug("ChannelPipeline fireChannelRead!");
	    this.head.fireChannelRead(msg);
	}
	
	/**
	 * @Description: 触发pipeline的ExceptionCaught 执行链，在处理selectKey时，捕获由ChannelRead Handler抛出的异常
	 * @author chenzq
	 * @date 2019年3月16日 下午7:14:23
	 * @param cause
	 * @throws Exception
	 */
	public void fireExceptionCaught(Throwable cause) throws Exception {
		
		logger.debug("ChannelPipeline fireExceptionCaught!");
		this.head.fireExceptionCaught(cause);
	}
    
	/**
	 * @Description: 关闭pipeline代理的socketChannel，并唤醒等待在closeLock锁上的线程
	 * @author chenzq
	 * @date 2019年3月16日 下午7:21:35
	 * @throws Exception
	 */
	public void close() throws Exception {
		
		logger.debug("pipeline.close()!");
		try {
			this.channel.close();
		} catch (Exception ex) {
			logger.error("error on close socketChannel!", ex);
		} finally {
			synchronized(this.closeLock) {
				logger.debug("closeLock.notify()!");
				this.closeLock.notify();
			}
		}
	}
	
	/**
	 * @Description: 返回代表当前pipeline关闭事件的ChannelFuture，可以使客户端等待在sync上
	 * @author chenzq
	 * @date 2019年3月16日 下午7:23:45
	 * @return
	 */
	public ChannelFuture closeFuture() {
		return closeFuture;
	}
	   
    /**
     * @Description: 写消息并flush，调用tail开始的outbound handler
     * @author chenzq
     * @date 2019年3月16日 下午7:21:23
     * @param msg
     * @return
     * @throws Exception
     */
    public ChannelFuture writeAndFlush(Object msg) throws Exception {
        
        logger.debug("ChannelPipeline writeAndFlush!");
        final DefaultChannelFuture future = new DefaultChannelFuture(this);
        Callable<Void> callable = new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                try {
                    tail().writeAndFlush(msg);
                    return null;
                } finally {
                    future.done();
                }
            }
        };
        return future.setFuture(ExecutorTools.getInstance().getExecutor().submit(callable));
    }
    
    /**
     * @Description: 写消息，调用tail开始的outbound handler
     * @author chenzq
     * @date 2019年3月18日 上午9:53:07
     * @param msg
     * @throws Exception
     */
	public ChannelFuture write(Object msg) throws Exception {
		
		logger.debug("ChannelPipeline write!");
		final DefaultChannelFuture future = new DefaultChannelFuture();
        Callable<Void> callable = new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                try {
                    tail().write(msg);
                    return null;
                } finally {
                    future.done();
                }
            }
        };
        return future.setPipeline(this).setFuture(ExecutorTools.getInstance().getExecutor().submit(callable));
	}
	
    /**
     * @Description: flush写缓存，调用tail开始的outbound handler
     * @author chenzq
     * @date 2019年3月18日 上午9:53:07
     * @param msg
     * @throws Exception
     */
	public ChannelFuture flush() throws Exception {
		
		logger.debug("ChannelPipeline flush!");
        final DefaultChannelFuture future = new DefaultChannelFuture();
        Callable<Void> callable = new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                try {
                    tail().flush();
                    //模拟时，没有真正flush，导致太快执行完成（后面的listener还没加入）
                    Thread.sleep(100);
                    return null;
                } finally {
                    future.done();
                }
            }
        };
        return future.setPipeline(this).setFuture(ExecutorTools.getInstance().getExecutor().submit(callable));
	}
}
