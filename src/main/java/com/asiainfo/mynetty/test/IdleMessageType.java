package com.asiainfo.mynetty.test;

/**   
 * @Description: TODO
 * 
 * @author chenzq  
 * @date 2019年3月31日 上午11:09:50
 * @version V1.0
 * @Copyright: Copyright(c) 2019 jaesonchen.com Inc. All rights reserved. 
 */
public class IdleMessageType {

    public static final byte PING = 1;
    public static final byte PONG = 2;
    public static final byte MESSAGE = 3;
    
    public static byte[] wrap(byte bt) {
        return new byte[] {bt};
    }
    
    public static byte[] wrapMessage(byte[] bt) {
        byte[] result = new byte[bt.length + 1];
        result[0] = IdleMessageType.MESSAGE;
        System.arraycopy(bt, 0, result, 1, bt.length);
        return result;
    }
    
    public static byte[] unwrapMessage(byte[] bt) {
        byte[] result = new byte[bt.length - 1];
        System.arraycopy(bt, 1, result, 0, result.length);
        return result;
    }
}
