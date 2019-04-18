package com.asiainfo.mynetty.handler;

/**
 * @Description: Outbound 接口，定义channel output
 * 
 * @author       zq
 * @date         2017年10月3日  上午10:46:42
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public interface ChannelOutboundHandler extends ChannelHandler {

    /**
     * @Description: channel写
     * @author chenzq
     * @date 2019年3月16日 下午6:01:32
     * @param ctx
     * @param msg
     * @throws Exception
     */
	void write(ChannelHandlerContext ctx, Object msg) throws Exception;
	
	/**
	 * @Description: channel 写缓存刷新
	 * @author chenzq
	 * @date 2019年3月16日 下午6:02:38
	 * @param ctx
	 * @throws Exception
	 */
	void flush(ChannelHandlerContext ctx) throws Exception;
}
