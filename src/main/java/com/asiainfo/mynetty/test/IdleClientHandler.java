package com.asiainfo.mynetty.test;

import java.io.IOException;

import com.asiainfo.mynetty.future.ChannelFuture;
import com.asiainfo.mynetty.future.ChannelFutureListener;
import com.asiainfo.mynetty.handler.ChannelHandlerContext;
import com.asiainfo.mynetty.handler.ChannelInboundHandlerAdapter;
import com.asiainfo.mynetty.idle.IdleState;
import com.asiainfo.mynetty.idle.IdleStateEvent;
import com.asiainfo.mynetty.pipeline.ReleaseUtil;

/**   
 * @Description: idle事件处理，用于保持长连接
 * 
 * @author chenzq  
 * @date 2019年3月18日 下午5:46:40
 * @version V1.0
 * @Copyright: Copyright(c) 2019 jaesonchen.com Inc. All rights reserved. 
 */
public class IdleClientHandler extends ChannelInboundHandlerAdapter {
    
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        try {
            byte[] bt = (byte[]) msg;
            switch (bt[0]) {
            case IdleMessageType.PING:
                sendPongMsg(ctx);
                break;
            case IdleMessageType.PONG:
                System.out.println("get pong from server!");
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
        System.out.println("get ping from server!");
        ctx.pipeline().writeAndFlush(IdleMessageType.wrap(IdleMessageType.PONG));
    }
    
    private void handleData(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("get message from server: " + new String(IdleMessageType.unwrapMessage((byte[]) msg)));
    }
    
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object event) throws Exception {
        
        if (event instanceof IdleStateEvent) {
            IdleStateEvent ent = (IdleStateEvent) event;
            if (ent.state() == IdleState.READER_IDLE) {
                ctx.pipeline().writeAndFlush(IdleMessageType.wrap(IdleMessageType.PING))
                    .addListener(new ChannelFutureListener() {
                        @Override
                        public void operationComplete(ChannelFuture future) throws IOException {
                            System.out.println("idle send finish!");
                        }
                    });
            }
        } else {
            super.userEventTriggered(ctx, event);
        }
    }
}
