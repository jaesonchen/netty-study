package com.asiainfo.netty.http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.QueryStringDecoder;

/**
 * @Description: TODO
 * 
 * @author       zq
 * @date         2017年9月10日  下午12:51:39
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class HttpServerHandler extends ChannelInboundHandlerAdapter {

	Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
    	
        if (msg instanceof HttpRequest) {
            HttpRequest request = (HttpRequest) msg;

            String url = request.uri();
            logger.info("request url:{}", url);

            if ("/favicon.ico".equals(url)) {
                return;
            }

            // 获取请求参数
            QueryStringDecoder queryStringDecoder = new QueryStringDecoder(url);
            String name = queryStringDecoder.parameters().get("name").get(0);

            // 响应JSON
            String responseJson = "{\"name\":\"" + name + "\"}";
            byte[] responseBytes = responseJson.getBytes("UTF-8");
            int contentLength = responseBytes.length;

            // 构造FullHttpResponse对象，FullHttpResponse包含message body
            FullHttpResponse response = new DefaultFullHttpResponse(
            		HttpVersion.HTTP_1_1, HttpResponseStatus.OK, Unpooled.wrappedBuffer(responseBytes));
            response.headers().set("Content-Type", "application/json;charset=UTF-8");
            response.headers().set("Content-Length", Integer.toString(contentLength));

            ctx.writeAndFlush(response);
        }
    }
}
