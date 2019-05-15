package com.asiainfo.mynetty.handler;

import java.nio.ByteBuffer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.asiainfo.mynetty.pipeline.ChannelPipeline;

/**
 * @Description: ChannelHandler的代理，负责调用真正的Handler和传递channelpipeline的Handler调用链。
 * 
 * @author       zq
 * @date         2017年10月3日  上午10:39:09
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class ChannelHandlerContext {
	
	final Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * 所属的Pipeline
	 */
	private ChannelPipeline pipeline;
	/**
	 * 持有的Handler
	 */
	private ChannelHandler handler;
	/**
	 * 前一个Handler
	 */
	private ChannelHandlerContext prev;
	/**
	 * 后一个Handler
	 */
	private ChannelHandlerContext next;
	/**
	 * 是否inbound handler
	 */
	private boolean inbound;
	/**
	 * 是否outbound handler
	 */
	private boolean outbound;
	/**
	 * 上次读的时间
	 */
	private volatile long lastRead = System.currentTimeMillis();
	
	public ChannelHandlerContext() {}
	public ChannelHandlerContext(ChannelPipeline pipeline, ChannelHandler handler) {
		this.pipeline = pipeline;
		this.handler = handler;
		this.inbound = handler instanceof ChannelInboundHandler;
		this.outbound = handler instanceof ChannelOutboundHandler;
	}

	public ChannelHandlerContext setPrev(ChannelHandlerContext prev) {
		this.prev = prev;
		return this;
	}
	public ChannelHandlerContext setNext(ChannelHandlerContext next) {
		this.next = next;
		return this;
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
    public void setLastRead(long lastRead) {
        this.lastRead = lastRead;
    }
	public long getLastRead() {
        return lastRead;
    }
	
    /**
	 * @Description: 调用inbound Handler处理器，如果不是inbound类型，传递到下一个Handler
	 * @author chenzq
	 * @date 2019年3月16日 下午6:32:13
	 * @throws Exception
	 */
	public void fireChannelActive() throws Exception {
		
		logger.debug("ChannelHandlerContext fireChannelActive!");
		// next inbound handler
		ChannelHandlerContext ctx = findContextInbound();
        if (ctx != null) {
            ((ChannelInboundHandler) ctx.handler()).channelActive(ctx);
        }
    }
	
	/**
	 * @Description: 调用inbound Handler处理器，如果不是inbound类型，传递到下一个Handler
	 * @author chenzq
	 * @date 2019年3月16日 下午6:37:02
	 * @throws Exception
	 */
	public void fireChannelInactive() throws Exception {
    	
		logger.debug("ChannelHandlerContext fireChannelInactive!");
        // next inbound handler
        ChannelHandlerContext ctx = findContextInbound();
        if (ctx != null) {
            ((ChannelInboundHandler) ctx.handler()).channelInactive(ctx);
        }
    }
	
	/**
	 * @Description: 调用inbound Handler处理器，如果不是inbound类型，传递到下一个Handler
	 * @author chenzq
	 * @date 2019年3月16日 下午6:37:52
	 * @param event
	 * @throws Exception
	 */
	public void fireUserEventTriggered(Object event) throws Exception {
    	
		logger.debug("ChannelHandlerContext fireUserEventTriggered!");
        // next inbound handler
        ChannelHandlerContext ctx = findContextInbound();
        if (ctx != null) {
            ((ChannelInboundHandler) ctx.handler()).userEventTriggered(ctx, event);
        }
    }
	
	/**
	 * @Description: 调用inbound Handler处理器，如果不是inbound类型，传递到下一个Handler
	 * @author chenzq
	 * @date 2019年3月16日 下午6:38:35
	 * @param msg
	 * @throws Exception
	 */
	public void fireChannelRead(Object msg) throws Exception {
		
		logger.debug("ChannelHandlerContext fireChannelRead!");
        // next inbound handler
        ChannelHandlerContext ctx = findContextInbound();
        if (ctx != null) {
            ((ChannelInboundHandler) ctx.handler()).channelRead(ctx, msg);
        }
    }
	
	/**
	 * @Description: 调用exception Handler处理器
	 * @author chenzq
	 * @date 2019年3月16日 下午6:39:21
	 * @param cause
	 * @throws Exception
	 */
	public void fireExceptionCaught(Throwable cause) throws Exception {
    	
		logger.debug("ChannelHandlerContext fireExceptionCaught!");
        // next inbound handler
        ChannelHandlerContext ctx = findContextInbound();
        if (ctx != null) {
            ((ChannelInboundHandler) ctx.handler()).exceptionCaught(ctx, cause);
        }
    }
	
	/**
	 * @Description: 写消息并flush
	 * @author chenzq
	 * @date 2019年3月17日 上午12:54:02
	 * @param msg
	 * @return
	 * @throws Exception
	 */
	public void writeAndFlush(Object msg) throws Exception {
	    
	    logger.debug("ChannelHandlerContext writeAndFlush!");
	    write(msg);
	    flush();
	}
	
	/**
	 * @Description: 写消息
	 * @author chenzq
	 * @date 2019年3月16日 下午6:42:32
	 * @param msg
	 * @throws Exception
	 */
	public void write(Object msg) throws Exception {
		
		logger.debug("ChannelHandlerContext write!");
		// next outbound handler
        ChannelHandlerContext ctx = findContextOutbound();
        if (ctx != null) {
            ((ChannelHandlerContext) ctx.handler()).write(msg);
        } else {
            invokeWrite(msg);
        }
	}
	
	/**
	 * @Description: flush写缓存
	 * @author chenzq
	 * @date 2019年3月16日 下午6:42:40
	 * @throws Exception
	 */
	public void flush() throws Exception {
		
		logger.debug("ChannelHandlerContext flush!");
	    // next outbound handler
        ChannelHandlerContext ctx = findContextOutbound();
        if (ctx != null) {
            ((ChannelHandlerContext) ctx.handler()).flush();
        } else {
            invokeFlush();
        }
	}
	
	// 写入缓冲区
	private void invokeWrite(Object msg) throws Exception {
	    logger.debug("invoke write!");
	    pipeline().socketChannel().write(ByteBuffer.wrap((byte[]) msg));
	}
	
	// flush缓冲区
	private void invokeFlush() throws Exception {
	    logger.debug("invoke flush!");
	    pipeline().socketChannel().socket().getOutputStream().flush();
	}
	
	// 查找下一个inbound handler
    private ChannelHandlerContext findContextInbound() {
        
        ChannelHandlerContext ctx = this;
        do {
            ctx = ctx.next;
        } while (ctx != null && !ctx.inbound);
        return ctx;
    }
    
    // 查找下一个outbound handler
    private ChannelHandlerContext findContextOutbound() {
        
        ChannelHandlerContext ctx = this;
        do {
            ctx = ctx.prev;
        } while (ctx != null && !ctx.outbound);
        return ctx;
    }
}
