package com.asiainfo.mynetty.test;

import com.asiainfo.mynetty.handler.ChannelHandlerContext;
import com.asiainfo.mynetty.handler.ChannelInboundHandlerAdapter;

/**   
 * @Description: TODO
 * 
 * @author chenzq  
 * @date 2019年3月18日 下午4:22:09
 * @version V1.0
 * @Copyright: Copyright(c) 2019 jaesonchen.com Inc. All rights reserved. 
 */
public class SecondServerInboundHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("annother channelActive!");
        ctx.fireChannelActive();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("annother channelInactive!");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("annother channelRead!");
        ctx.fireChannelRead(msg);
    }
}
