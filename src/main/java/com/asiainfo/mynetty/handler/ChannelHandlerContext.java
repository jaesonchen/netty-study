package com.asiainfo.mynetty.handler;

import java.nio.ByteBuffer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.asiainfo.mynetty.pipeline.ChannelPipeline;

/**
 * @Description: TODO
 * 
 * @author       zq
 * @date         2017年10月3日  上午10:39:09
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class ChannelHandlerContext {
	
	final Logger logger = LoggerFactory.getLogger(getClass());

	private ChannelPipeline pipeline;
	private ChannelHandler handler;
	private ChannelHandlerContext prev;
	private ChannelHandlerContext next;
	private boolean inbound;
	
	public ChannelHandlerContext() {}
	public ChannelHandlerContext(ChannelPipeline pipeline, ChannelHandler handler) {
		this.pipeline = pipeline;
		this.handler = handler;
		inbound = handler instanceof ChannelInboundHandler;
	}

	public void setPrev(ChannelHandlerContext prev) {
		this.prev = prev;
	}
	public void setNext(ChannelHandlerContext next) {
		this.next = next;
	}
	
	public ChannelHandlerContext prev() {
		return this.prev;
	}
	public ChannelHandlerContext next() {
		return this.next;
	}
	public ChannelPipeline pipeline() {
		return this.pipeline;
	}
	public ChannelHandler handler() {
		return this.handler;
	}
	public boolean inbound() {
		return this.inbound;
	}
	
	public void fireChannelActive() throws Exception {
		
		logger.debug("ChannelHandlerContext fireChannelActive!");
		if (this.next != null) {
			if (this.next.inbound()) {
				ChannelInboundHandler nextHandler = (ChannelInboundHandler) this.next.handler;
				nextHandler.channelActive(this.next);
			}
			else {
				this.next.fireChannelActive();
			}
		}
    }
	
	public void fireChannelInactive() throws Exception {
    	
		logger.debug("ChannelHandlerContext fireChannelInactive!");
		if (this.next != null) {
			if (this.next.inbound()) {
				ChannelInboundHandler nextHandler = (ChannelInboundHandler) this.next.handler;
				nextHandler.channelInactive(this.next);
			}
			else {
				this.next.fireChannelInactive();
			}
		}
    }
	
	public void fireUserEventTriggered(Object event) throws Exception {
    	
		logger.debug("ChannelHandlerContext fireUserEventTriggered!");
		if (this.next != null) {
			if (this.next.inbound()) {
				ChannelInboundHandler nextHandler = (ChannelInboundHandler) this.next.handler;
				nextHandler.userEventTriggered(this.next, event);
			}
			else {
				this.next.fireUserEventTriggered(event);
			}
		}
    }
	
	public void fireChannelRead(Object msg) throws Exception {
		
		logger.debug("ChannelHandlerContext fireChannelRead!");
		if (this.next != null) {
			if (this.next.inbound()) {
				ChannelInboundHandler nextHandler = (ChannelInboundHandler) this.next.handler;
				nextHandler.channelRead(this.next, msg);
			}
			else {
				this.next.fireChannelRead(msg);
			}
		}
    }
	
	public void fireExceptionCaught(Throwable cause) throws Exception {
    	
		logger.debug("ChannelHandlerContext fireExceptionCaught!");
		if (this.next != null) {
			this.next.handler.exceptionCaught(this.next, cause);
		}
    }
	
	public void write(Object msg) throws Exception {
		
		logger.debug("ChannelHandlerContext write!");
		if (this.next != null) {
			if (!this.next.inbound()) {
				ChannelOutboundHandler nextHandler = (ChannelOutboundHandler) this.next.handler;
				nextHandler.write(this.next, msg);
			}
			else {
				this.next.write(msg);
			}
		}
		else {
			this.pipeline.getChannel().write(ByteBuffer.wrap((byte[]) msg));
		}
	}
	
	public void flush() throws Exception {
		
		logger.debug("ChannelHandlerContext flush!");
		if (this.next != null) {
			if (!this.next.inbound()) {
				ChannelOutboundHandler nextHandler = (ChannelOutboundHandler) this.next.handler;
				nextHandler.flush(this.next);
			}
			else {
				this.next.flush();
			}
		}
		else {
			logger.debug("finally flush!");
		}
	}
}
