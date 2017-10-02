package com.asiainfo.netty.rpc.test;

import java.io.Serializable;

/**
 * @Description: TODO
 * 
 * @author       zq
 * @date         2017年9月7日  下午5:30:47
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class HelloBean implements Serializable {

	/** serialVersionUID */
	private static final long serialVersionUID = 1L;
	
	private String name;
	private int age;
	
	public HelloBean(String name, int age) {
		this.name = name;
		this.age = age;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	@Override
	public String toString() {
		return "HelloBean [name=" + name + ", age=" + age + "]";
	}
}
