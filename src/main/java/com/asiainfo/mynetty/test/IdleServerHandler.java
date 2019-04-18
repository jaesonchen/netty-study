package com.asiainfo.mynetty.test;

import com.asiainfo.mynetty.handler.ChannelHandlerContext;
import com.asiainfo.mynetty.handler.ChannelInboundHandlerAdapter;
import com.asiainfo.mynetty.pipeline.ReleaseUtil;

/**   
 * @Description: TODO
 * 
 * @author chenzq  
 * @date 2019年3月31日 上午11:06:52
 * @version V1.0
 * @Copyright: Copyright(c) 2019 jaesonchen.com Inc. All rights reserved. 
 */
public class IdleServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        try {
            byte[] bt = (byte[]) msg;
            switch (bt[0]) {
            case IdleMessageType.PING:
                sendPongMsg(ctx);
                break;
            case IdleMessageType.PONG:
                System.out.println("get pong from client!");
                break;
            case IdleMessageType.MESSAGE:
                handleData(ctx, msg);
                break;
            default:
                break;
            }
        } finally {
            ReleaseUtil.release(ctx);
        }
    }
    
    private void sendPongMsg(ChannelHandlerContext ctx) throws Exception {
        System.out.println("get ping from client!");
        ctx.pipeline().writeAndFlush(IdleMessageType.wrap(IdleMessageType.PONG));
    }
    
    private void handleData(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("get message from client: " + new String(IdleMessageType.unwrapMessage((byte[]) msg)));
        ctx.pipeline().writeAndFlush(IdleMessageType.wrapMessage("welcome to server!".getBytes()));
    }
}
