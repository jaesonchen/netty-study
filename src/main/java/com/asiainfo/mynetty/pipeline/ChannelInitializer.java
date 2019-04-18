package com.asiainfo.mynetty.pipeline;

/**
 * @Description: ChannelPipeline的Handler链初始化接口
 * 
 * @author       zq
 * @date         2017年10月6日  下午4:15:32
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public interface ChannelInitializer {

    /**
     * @Description: 初始化ChannelPipeline的Handler链
     * @author chenzq
     * @date 2019年3月18日 上午10:12:21
     * @param ch
     * @throws Exception
     */
	void initChannel(ChannelPipeline ch) throws Exception;
}
