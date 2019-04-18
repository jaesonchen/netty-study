package com.asiainfo.mynetty.test;

import java.util.concurrent.TimeUnit;

import com.asiainfo.mynetty.boot.Bootstrap;
import com.asiainfo.mynetty.eventloop.EventLoopGroup;
import com.asiainfo.mynetty.future.ChannelFuture;
import com.asiainfo.mynetty.future.ChannelFutureListener;
import com.asiainfo.mynetty.idle.IdleStateHandler;
import com.asiainfo.mynetty.pipeline.ChannelInitializer;
import com.asiainfo.mynetty.pipeline.ChannelPipeline;
import com.asiainfo.util.ThreadPoolUtils;

/**   
 * @Description: TODO
 * 
 * @author chenzq  
 * @date 2019年3月18日 下午5:45:37
 * @version V1.0
 * @Copyright: Copyright(c) 2019 jaesonchen.com Inc. All rights reserved. 
 */
public class IdleClient {

    public static void main(String[] args) throws Exception {

        EventLoopGroup group = new EventLoopGroup(ThreadPoolUtils.getInstance().cachedThreadPool(), 1);
        Bootstrap b = new Bootstrap(group);
        b.handler(new ChannelInitializer() {
            @Override
            public void initChannel(ChannelPipeline ch) throws Exception {
                ch.addHandler(new IdleStateHandler(5, TimeUnit.SECONDS));
                ch.addHandler(new IdleClientHandler());
            }});
        ChannelFuture f = b.connect("localhost", 8080);
        f.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                System.out.println("client connect finish!");
                future.pipeline().writeAndFlush(IdleMessageType.wrapMessage("hello world".getBytes()));
            }
        });
        System.out.println("my netty client started!");

        f.closeFuture().sync();
        System.out.println("my netty client stopped!");
        System.exit(1);
    }
}
