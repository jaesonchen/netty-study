package com.asiainfo.netty.rpc.serializer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import com.caucho.hessian.io.Hessian2Input;
import com.caucho.hessian.io.Hessian2Output;

/**
 * @Description: TODO
 * 
 * @author       zq
 * @date         2017年9月3日  上午9:56:34
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class HessianSerializer implements Serializer<Object> {

	/* 
	 * @Description: TODO
	 * @param t
	 * @return
	 * @throws SerializationException
	 * @see com.asiainfo.serializer.Serializer#serialize(java.lang.Object)
	 */
	@Override
	public byte[] serialize(Object t) throws SerializationException {
		
		if (t == null) {
			return new byte[0];
		}
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            Hessian2Output hos = new Hessian2Output(baos);
            hos.writeObject(t);
            hos.flush();
            return baos.toByteArray();
        } catch (IOException ex) {
            throw new SerializationException("Hessian serialize error " + ex.getMessage());
        } finally {
        	try {
        		baos.close();
        	} catch (IOException ex) {}
        }
	}

	/* 
	 * @Description: TODO
	 * @param bytes
	 * @return
	 * @throws SerializationException
	 * @see com.asiainfo.serializer.Serializer#deserialize(byte[])
	 */
	@Override
	public Object deserialize(byte[] bytes) throws SerializationException {
		
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        try {
            Hessian2Input his = new Hessian2Input(bais);
            return his.readObject();
        } catch (IOException e) {
            throw new SerializationException("Hessian deSerialize error " + e.getMessage());
        } finally {
            try {
            	bais.close();
            } catch (IOException e) {}
        }
	}
}
