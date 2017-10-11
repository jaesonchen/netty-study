package com.asiainfo.mynetty.future;

/**
 * @Description: TODO
 * 
 * @author       zq
 * @date         2017年10月3日  上午11:36:12
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public interface FutureListener {
	
	void operationComplete(ChannelFuture future) throws Exception;
	
	FutureListener CLOSE = new FutureListener() {
        @Override
        public void operationComplete(ChannelFuture future) throws Exception {
        	System.out.println("FutureListener.CLOSE.operationComplete()!");
            future.pipeline().close();
        }
    };
}
