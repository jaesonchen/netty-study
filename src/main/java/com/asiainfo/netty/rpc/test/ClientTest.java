package com.asiainfo.netty.rpc.test;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.asiainfo.netty.rpc.client.RpcProxy;

/**
 * @Description: TODO
 * 
 * @author       zq
 * @date         2017年9月7日  下午9:50:23
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class ClientTest {

	/** 
	 * @Description: TODO
	 * 
	 * @param args
	 */
	public static void main(String[] args) {

		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[] {"classpath:application-client-configure.xml"});
		RpcProxy proxy = context.getBean(RpcProxy.class);
		IHelloService service = proxy.create(IHelloService.class);
		System.out.println(service.getHello("chenzq"));
		context.close();
	}
}
