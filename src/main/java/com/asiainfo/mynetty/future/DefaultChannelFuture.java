package com.asiainfo.mynetty.future;

import java.nio.channels.Channel;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

/**
 * @Description: TODO
 * 
 * @author       zq
 * @date         2017年10月7日  下午9:59:28
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class DefaultChannelFuture implements ChannelFuture {

	private Channel channel;
	private Future<Void> future;
	private List<FutureListener> listeners = new ArrayList<>();
	final Object obj = new Object();
	
	public DefaultChannelFuture setChannel(Channel channel) {
		this.channel = channel;
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
	public Channel channel() {
		return this.channel;
	}

	/* 
	 * @Description: TODO
	 * @param listener
	 * @return
	 * @see com.asiainfo.mynetty.future.ChannelFuture#addListener(com.asiainfo.mynetty.future.FutureListener)
	 */
	@Override
	public ChannelFuture addListener(FutureListener listener) {
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
		future.get();
	}

	/* 
	 * @Description: TODO
	 * @throws Exception
	 * @see com.asiainfo.mynetty.future.ChannelFuture#await()
	 */
	@Override
	public void await() throws Exception {
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
		return new DefaultChannelFuture() {
			@Override
			public void sync() throws Exception {
				synchronized(this.obj) {
					this.obj.wait();
				}
			}
		}.setChannel(channel).setFuture(null);
	}
}
