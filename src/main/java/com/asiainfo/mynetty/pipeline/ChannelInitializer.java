package com.asiainfo.mynetty.pipeline;

/**
 * @Description: TODO
 * 
 * @author       zq
 * @date         2017年10月6日  下午4:15:32
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public interface ChannelInitializer {

	void initChannel(ChannelPipeline ch) throws Exception;
}
