package com.asiainfo.mynetty.pipeline;

import java.io.ByteArrayOutputStream;
import java.nio.channels.SocketChannel;
import java.util.concurrent.Callable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.asiainfo.mynetty.future.ChannelFuture;
import com.asiainfo.mynetty.future.DefaultChannelFuture;
import com.asiainfo.mynetty.handler.ChannelHandler;
import com.asiainfo.mynetty.handler.ChannelHandlerContext;
import com.asiainfo.mynetty.handler.ChannelInboundHandler;
import com.asiainfo.mynetty.handler.ChannelInboundHandlerAdapter;
import com.asiainfo.mynetty.handler.ChannelOutboundHandler;
import com.asiainfo.mynetty.handler.ChannelOutboundHandlerAdapter;

/**
 * @Description: TODO
 * 
 * @author       zq
 * @date         2017年10月3日  上午11:54:42
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class ChannelPipeline {

	final Logger logger = LoggerFactory.getLogger(getClass());
	
	final ChannelHandlerContext head;
	final ChannelHandlerContext tail;
	final SocketChannel channel;
	final ByteArrayOutputStream buffer = new ByteArrayOutputStream();
	
	public ChannelPipeline(SocketChannel channel) {
		
		this.channel = channel;
		head = new ChannelHandlerContext(this, new ChannelInboundHandlerAdapter() {});
		tail = new ChannelHandlerContext(this, new ChannelOutboundHandlerAdapter() {});
		head.setNext(tail);
		tail.setPrev(head);
	}

	
	public ChannelHandlerContext getHead() {
		return head;
	}
	public ChannelHandlerContext getTail() {
		return tail;
	}
	public SocketChannel getChannel() {
		return channel;
	}
	public ByteArrayOutputStream getBuffer() {
		return buffer;
	}

	public ChannelPipeline addHandler(ChannelHandler handler) {
		
		logger.info("ChannelPipeline addHandler:{}", handler.getClass().getSimpleName());
		ChannelHandlerContext context = new ChannelHandlerContext(this, handler);
		tail.prev().setNext(context);
		context.setPrev(tail.prev());
		context.setNext(tail);
		tail.setPrev(context);
		return this;
	}
	
	public void fireChannelActive() throws Exception {
		
		logger.debug("ChannelPipeline fireChannelActive!");
		ChannelInboundHandler inboundHandler = (ChannelInboundHandler) this.head.handler();
		inboundHandler.channelActive(this.head);
	}
	
	public void fireChannelInactive() throws Exception {
		
		logger.debug("ChannelPipeline fireChannelInactive!");
		ChannelInboundHandler inboundHandler = (ChannelInboundHandler) this.head.handler();
		inboundHandler.channelInactive(this.head);
	}
	
	public void fireUserEventTriggered(Object event) throws Exception {
		
		logger.debug("ChannelPipeline fireUserEventTriggered!");
		ChannelInboundHandler inboundHandler = (ChannelInboundHandler) this.head.handler();
		inboundHandler.userEventTriggered(this.head, event);
	}
	
	public void fireChannelRead(Object msg) throws Exception {
		
		logger.debug("ChannelPipeline fireChannelRead!");
		ChannelInboundHandler inboundHandler = (ChannelInboundHandler) this.head.handler();
		inboundHandler.channelRead(this.head, msg);
	}
	
	public void fireExceptionCaught(Throwable cause) throws Exception {
		
		logger.debug("ChannelPipeline fireExceptionCaught!");
		this.head.handler().exceptionCaught(this.head, cause);
	}
	
	public ChannelFuture writeAndFlush(Object msg) throws Exception {
		
		logger.debug("ChannelPipeline writeAndFlush!");
		return executeChannel(channel, msg);
	}
	
	protected void write(Object msg) throws Exception {
		
		logger.debug("ChannelPipeline write!");
		ChannelOutboundHandler outboundHandler = (ChannelOutboundHandler) this.tail.handler();
		outboundHandler.write(this.tail, msg);
	}
	
	protected void flush() throws Exception {
		
		logger.debug("ChannelPipeline flush!");
		ChannelOutboundHandler outboundHandler = (ChannelOutboundHandler) this.tail.handler();
		outboundHandler.flush(this.tail);
		//模拟时，没有真正flush，导致太快执行完成，后面的listener还没加入
    	Thread.sleep(100);
	}
	
	protected ChannelFuture executeChannel(SocketChannel channel, Object msg) {
		
		final DefaultChannelFuture future = new DefaultChannelFuture();
		Callable<Void> callable = new Callable<Void>() {
			@Override
			public Void call() throws Exception {
				try {
					write(msg);
			    	flush();
					return null;
				} finally {
					future.done();
				}
			}
    	};
    	
    	return future.setChannel(channel).setFuture(ExecutorTools.getInstance().getExecutor().submit(callable));
	}
}
