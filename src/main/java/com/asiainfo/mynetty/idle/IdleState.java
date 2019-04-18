package com.asiainfo.mynetty.idle;

/**   
 * @Description: TODO
 * 
 * @author chenzq  
 * @date 2019年3月18日 下午5:03:07
 * @version V1.0
 * @Copyright: Copyright(c) 2019 jaesonchen.com Inc. All rights reserved. 
 */
public enum IdleState {
    
    /**
     * No data was received for a while.
     */
    READER_IDLE,
    /**
     * No data was sent for a while.
     */
    WRITER_IDLE,
    /**
     * No data was either received or sent for a while.
     */
    ALL_IDLE
}
