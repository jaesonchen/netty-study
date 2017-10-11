package com.asiainfo.mynetty.future;

import java.nio.channels.SelectableChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.asiainfo.mynetty.pipeline.ChannelPipeline;

/**
 * @Description: TODO
 * 
 * @author       zq
 * @date         2017年10月7日  下午9:59:28
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class DefaultChannelFuture implements ChannelFuture {
	
	final Logger logger = LoggerFactory.getLogger(getClass());

	protected ChannelPipeline pipeline;
	protected Future<Void> future;
	protected List<FutureListener> listeners = new ArrayList<>();
	protected final Object obj = new Object();
	
	public DefaultChannelFuture setPipeline(ChannelPipeline pipeline) {
		this.pipeline = pipeline;
		return this;
	}
	public DefaultChannelFuture setFuture(Future<Void> future) {
		this.future = future;
		return this;
	}

	/* 
	 * @Description: TODO
	 * @return
	 * @see com.asiainfo.mynetty.future.ChannelFuture#channel()
	 */
	@Override
	public SelectableChannel channel() {
		return this.pipeline.channel();
	}
	
	/* 
	 * @Description: TODO
	 * @return
	 * @see com.asiainfo.mynetty.future.ChannelFuture#pipeline()
	 */
	@Override
	public ChannelPipeline pipeline() {
		return this.pipeline;
	}
	
	/* 
	 * @Description: TODO
	 * @param listener
	 * @return
	 * @see com.asiainfo.mynetty.future.ChannelFuture#addListener(com.asiainfo.mynetty.future.FutureListener)
	 */
	@Override
	public ChannelFuture addListener(FutureListener listener) {
		
		logger.debug("addListener()...");
		this.listeners.add(listener);
		return this;
	}

	/* 
	 * @Description: TODO
	 * @return
	 * @throws Exception
	 * @see com.asiainfo.mynetty.future.ChannelFuture#sync()
	 */
	@Override
	public void sync() throws Exception {
		
		logger.debug("sync()...");
		future.get();
	}

	/* 
	 * @Description: TODO
	 * @throws Exception
	 * @see com.asiainfo.mynetty.future.ChannelFuture#await()
	 */
	@Override
	public void await() throws Exception {
		
		logger.debug("await()...");
		synchronized(this.obj) {
			this.obj.wait();
		}
	}

	/* 
	 * @Description: TODO
	 * @see com.asiainfo.mynetty.future.ChannelFuture#notifier()
	 */
	@Override
	public void notifier() {
		
		logger.debug("notifier()...");
		synchronized(this.obj) {
			this.obj.notify();
		}
		this.done();
	}

	/* 
	 * @Description: TODO
	 * @see com.asiainfo.mynetty.future.ChannelFuture#done()
	 */
	@Override
	public void done() {
		
		logger.debug("done()...");
		try {
			for (FutureListener listener : listeners) {
				listener.operationComplete(this);
			}
		} catch (Exception ex) {
			//ignore
		}
	}
	
	/* 
	 * @Description: TODO
	 * @return
	 * @see com.asiainfo.mynetty.future.ChannelFuture#closeFuture()
	 */
	@Override
	public ChannelFuture closeFuture() {
		return this.pipeline.closeFuture();
	}
}
