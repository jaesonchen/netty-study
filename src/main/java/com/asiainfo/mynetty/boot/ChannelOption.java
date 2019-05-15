package com.asiainfo.mynetty.boot;

import java.net.SocketOption;
import java.net.StandardSocketOptions;

/**   
 * @Description: TODO
 * 
 * @author chenzq  
 * @date 2019年5月9日 下午8:26:24
 * @version V1.0
 * @Copyright: Copyright(c) 2019 jaesonchen.com Inc. All rights reserved. 
 */
public class ChannelOption {

    //public static final SocketOption<Integer> SO_BACKLOG = 
    //public static final SocketOption<Integer> SO_TIMEOUT = 
    public static final SocketOption<Boolean> SO_REUSEADDR = StandardSocketOptions.SO_REUSEADDR;
    public static final SocketOption<Boolean> SO_KEEPALIVE = StandardSocketOptions.SO_KEEPALIVE;
    public static final SocketOption<Boolean> TCP_NODELAY = StandardSocketOptions.TCP_NODELAY;
    public static final SocketOption<Integer> SO_LINGER = StandardSocketOptions.SO_LINGER;
}
