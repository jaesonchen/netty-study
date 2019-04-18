package com.asiainfo.mynetty.future;

/**
 * @Description: ChannelPipeline事件监听器接口
 * 
 * @author       zq
 * @date         2017年10月3日  上午11:36:12
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public interface ChannelFutureListener {
	
	/**
	 * 缺省的channel关闭事件监听
	 */
	ChannelFutureListener CLOSE = new ChannelFutureListener() {
        @Override
        public void operationComplete(ChannelFuture future) throws Exception {
        	System.out.println("FutureListener.CLOSE.operationComplete()!");
            future.pipeline().close();
        }
    };
    
    /**
     * @Description: 事件处理方法
     * @author chenzq
     * @date 2019年3月16日 下午7:34:16
     * @param future
     * @throws Exception
     */
    void operationComplete(ChannelFuture future) throws Exception;
}
