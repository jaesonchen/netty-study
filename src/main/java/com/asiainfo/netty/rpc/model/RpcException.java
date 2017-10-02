package com.asiainfo.netty.rpc.model;

/**
 * @Description: TODO
 * 
 * @author       zq
 * @date         2017年9月7日  下午2:59:43
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class RpcException extends RuntimeException {

	/** serialVersionUID */
	private static final long serialVersionUID = 1L;
	
    public RpcException(String message) {
        super(message);
    }

    public RpcException(String msg, Throwable cause) {
    	super(msg, cause);
    }
}
