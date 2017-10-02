package com.asiainfo.netty.rpc.serializer;

/**
 * @Description: TODO
 * 
 * @author       zq
 * @date         2017年8月21日  下午4:53:41
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class SerializationException extends RuntimeException {

	/** serialVersionUID */
	private static final long serialVersionUID = 1L;

	public SerializationException(String msg, Throwable cause) {
		super(msg, cause);
	}
	
	public SerializationException(String msg) {
		super(msg);
	}
}
