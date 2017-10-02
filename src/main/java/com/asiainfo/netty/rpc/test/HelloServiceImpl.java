package com.asiainfo.netty.rpc.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.asiainfo.netty.rpc.server.RpcService;

/**
 * @Description: TODO
 * 
 * @author       zq
 * @date         2017年9月7日  下午5:32:30
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
@RpcService(IHelloService.class)
public class HelloServiceImpl implements IHelloService {

	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	/* 
	 * @Description: TODO
	 * @param name
	 * @return
	 * @see com.asiainfo.rpc.test.IHelloService#getHello(java.lang.String)
	 */
	@Override
	public HelloBean getHello(String name) {
		
		logger.info("request param={}", name);
		return new HelloBean(name, 20);
	}
}
