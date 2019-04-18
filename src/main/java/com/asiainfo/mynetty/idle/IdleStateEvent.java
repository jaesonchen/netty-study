package com.asiainfo.mynetty.idle;

/**   
 * @Description: TODO
 * 
 * @author chenzq  
 * @date 2019年3月18日 下午5:02:04
 * @version V1.0
 * @Copyright: Copyright(c) 2019 jaesonchen.com Inc. All rights reserved. 
 */
public class IdleStateEvent {

    private final IdleState state;
    
    public IdleStateEvent(IdleState state) {
        this.state = state;
    }
    
    /**
     * Returns the idle state.
     */
    public IdleState state() {
        return state;
    }
}
