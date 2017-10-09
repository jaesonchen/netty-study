package com.asiainfo.mynetty.handler;

/**
 * @Description: TODO
 * 
 * @author       zq
 * @date         2017年10月3日  上午10:41:10
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public interface ChannelHandler {

	void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception;
}
