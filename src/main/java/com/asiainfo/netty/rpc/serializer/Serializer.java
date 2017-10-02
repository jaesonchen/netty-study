package com.asiainfo.netty.rpc.serializer;

/**
 * @Description: TODO
 * 
 * @author       zq
 * @date         2017年8月21日  下午4:52:50
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public interface Serializer<T> {
	
	byte[] serialize(T t) throws SerializationException;
	
	T deserialize(byte[] bytes) throws SerializationException;
}
