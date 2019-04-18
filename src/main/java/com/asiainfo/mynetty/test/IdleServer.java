package com.asiainfo.mynetty.test;

import java.io.IOException;
import java.net.InetSocketAddress;

import com.asiainfo.mynetty.boot.ServerBootstrap;
import com.asiainfo.mynetty.eventloop.EventLoopGroup;
import com.asiainfo.mynetty.future.ChannelFuture;
import com.asiainfo.mynetty.future.ChannelFutureListener;
import com.asiainfo.mynetty.pipeline.ChannelInitializer;
import com.asiainfo.mynetty.pipeline.ChannelPipeline;
import com.asiainfo.util.ThreadPoolUtils;

/**   
 * @Description: TODO
 * 
 * @author chenzq  
 * @date 2019年3月31日 上午11:02:49
 * @version V1.0
 * @Copyright: Copyright(c) 2019 jaesonchen.com Inc. All rights reserved. 
 */
public class IdleServer {
    
    public static void main(String[] args) throws Exception {
       
        EventLoopGroup group = new EventLoopGroup(
                ThreadPoolUtils.getInstance().cachedThreadPool(), 
                ThreadPoolUtils.getInstance().cachedThreadPool(), 
                1, 2);
        ServerBootstrap b = new ServerBootstrap(group);
        b.handler(new ChannelInitializer() {
            @Override
            public void initChannel(ChannelPipeline ch) throws Exception {
                ch.addHandler(new IdleServerHandler());
            }});
        ChannelFuture f = b.bind(new InetSocketAddress(8080));
        f.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws IOException {
                System.out.println("server bind address finish!");
            }
        });
        System.out.println("my netty server started .");
        f.closeFuture().sync();
        System.exit(1);
    }
}
