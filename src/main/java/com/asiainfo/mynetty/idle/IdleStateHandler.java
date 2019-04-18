package com.asiainfo.mynetty.idle;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.asiainfo.mynetty.handler.ChannelHandlerContext;
import com.asiainfo.mynetty.handler.ChannelInboundHandlerAdapter;
import com.asiainfo.mynetty.pipeline.ExecutorTools;

/**   
 * @Description: idle触发器
 * 
 * @author chenzq  
 * @date 2019年3月18日 下午5:05:20
 * @version V1.0
 * @Copyright: Copyright(c) 2019 jaesonchen.com Inc. All rights reserved. 
 */
public class IdleStateHandler extends ChannelInboundHandlerAdapter {

    final Logger logger = LoggerFactory.getLogger(getClass());
    
    private long idle = 5000L;
    
    public IdleStateHandler() {}
    public IdleStateHandler(long idle, TimeUnit unit) {
        this.idle = unit.toMillis(idle);
    }
    
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        
        logger.info("ReaderIdleTimeoutTask enable");
        //启动定时器扫描读时间
        ExecutorTools.getInstance().getSchedule().schedule(new ReaderIdleTimeoutTask(ctx), this.idle, TimeUnit.MILLISECONDS);
        super.channelActive(ctx);
    }
    
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //记录读时间
        ctx.setLastRead(System.currentTimeMillis());
        super.channelRead(ctx, msg);
    }

    // 定时读idle检测任务
    private final class ReaderIdleTimeoutTask implements Runnable {

        private ChannelHandlerContext ctx;
        public ReaderIdleTimeoutTask(ChannelHandlerContext ctx) {
            this.ctx = ctx;
        }
        
        @Override
        public void run() {
            // channel断开时不继续调度
            if (ctx.pipeline().socketChannel() == null || !ctx.pipeline().socketChannel().isOpen()) {
                return;
            }
            logger.info("ReaderIdleTimeoutTask run");
            long delay = System.currentTimeMillis() - ctx.getLastRead();
            if (delay >= IdleStateHandler.this.idle) {
                try {
                    ctx.fireUserEventTriggered(new IdleStateEvent(IdleState.READER_IDLE));
                } catch (Exception e) {
                    logger.error("error on fireUserEventTriggered!", e);
                }
            }
            //启动定时器扫描读时间
            ExecutorTools.getInstance().getSchedule().schedule(new ReaderIdleTimeoutTask(ctx), IdleStateHandler.this.idle, TimeUnit.MILLISECONDS);
        }
    }
}
