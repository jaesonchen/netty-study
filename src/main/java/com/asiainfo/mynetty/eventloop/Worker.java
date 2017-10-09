package com.asiainfo.mynetty.eventloop;

import java.nio.channels.SocketChannel;

/**
 * 
 * @Description: worker接口
 * 
 * @author       zq
 * @date         2017年10月5日  上午10:10:03
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public interface Worker {
	
	/**
	 * 加入一个新的客户端会话
	 * @param channel
	 */
	public void registerChannel(SocketChannel channel);
}
