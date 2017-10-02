package com.asiainfo.netty.rpc.test;

import java.io.IOException;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @Description: TODO
 * 
 * @author       zq
 * @date         2017年9月7日  下午10:12:03
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class ServerTest {

	/** 
	 * @Description: TODO
	 * 
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {

		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[] {"classpath:application-core-configure.xml"});
		System.in.read();
		context.close();
	}
}
