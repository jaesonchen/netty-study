package com.asiainfo.mynetty.future;

import java.nio.channels.SelectableChannel;

import com.asiainfo.mynetty.pipeline.ChannelPipeline;

/**
 * @Description: Future接口
 * 
 * @author       zq
 * @date         2017年10月3日  上午11:34:59
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public interface ChannelFuture {

	SelectableChannel channel();
	
	ChannelPipeline pipeline();
	
	ChannelFuture closeFuture();
	
	Object getLock();
	
	boolean isFinish();
	
	void setFinish(boolean finish);
	
	/**
	 * @Description: 增加监听器，在done()后执行
	 * @author chenzq
	 * @date 2019年3月18日 上午9:34:58
	 * @param listener
	 * @return
	 */
	ChannelFuture addListener(ChannelFutureListener listener);
	
	/**
	 * @Description: sync等待在持有的Future.get()上
	 * @author chenzq
	 * @date 2019年3月18日 上午9:35:28
	 * @throws Exception
	 */
	void sync() throws Exception;
	
	/**
	 * @Description: Future执行完成时调用，用于触发listener
	 * @author chenzq
	 * @date 2019年3月18日 上午9:36:57
	 */
    void done();
    
    /**
     * @Description: 同步等待
     * @author chenzq
     * @date 2019年3月18日 上午10:08:12
     * @throws Exception
     */
	void await() throws Exception;
	
	/**
	 * @Description: 同步唤醒
	 * @author chenzq
	 * @date 2019年3月18日 上午10:08:30
	 */
	void notifier();
}
