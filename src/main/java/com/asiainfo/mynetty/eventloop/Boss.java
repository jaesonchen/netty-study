package com.asiainfo.mynetty.eventloop;

import java.nio.channels.ServerSocketChannel;

import com.asiainfo.mynetty.future.ChannelFuture;

/**
 * 
 * @Description: boss接口
 * 
 * @author       zq
 * @date         2017年10月3日  下午6:01:44
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public interface Boss {
	
	/**
	 * 加入一个新的ServerSocket
	 * @param serverChannel
	 */
	public void registerAcceptChannel(ServerSocketChannel ssChannel, ChannelFuture future) throws Exception ;
}
