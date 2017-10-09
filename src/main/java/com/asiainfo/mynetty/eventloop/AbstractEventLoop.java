package com.asiainfo.mynetty.eventloop;

import java.io.IOException;
import java.nio.channels.Selector;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 
 * @Description: 抽象selector线程类
 * 
 * @author       zq
 * @date         2017年10月5日  上午10:13:15
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public abstract class AbstractEventLoop implements Runnable {
	
	final Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * 线程池
	 */
	private final Executor executor;
	private String threadName;

	/**
	 * 选择器wakenUp状态标记
	 */
	private final AtomicBoolean wakenUp = new AtomicBoolean();

	/**
	 * 任务队列
	 */
	private final BlockingQueue<Runnable> taskQueue = new LinkedBlockingQueue<Runnable>();
	
	/**
	 * 选择器
	 */
	protected Selector selector;
	
	/**
	 * 线程管理对象
	 */
	protected EventLoopGroup group;

	public AbstractEventLoop(Executor executor, String threadName, EventLoopGroup group) {
		this.executor = executor;
		this.threadName = threadName;
		this.group = group;
		openSelector();
	}

	/**
	 * 获取selector并启动线程
	 */
	private void openSelector() {
		
		try {
			logger.debug("open selector!");
			this.selector = Selector.open();
		} catch (IOException e) {
			logger.error("error on open selector!", e);
			throw new RuntimeException("Failed to create a selector.");
		}
		executor.execute(this);
	}

	@Override
	public void run() {
		
		logger.info("EventLoop runing, threadName={}!", this.threadName);
		Thread.currentThread().setName(this.threadName);
		while (true) {
			try {
				logger.debug("set wakenUp=false!");
				//设置唤醒标志
				wakenUp.set(false);
				logger.debug("block on select()!");
				//select
				select(selector);
				logger.debug("selector wakeup!");
				//运行任务队列中新加入的任务
				processTaskQueue();
				logger.debug("process selector event!");
				//处理select事件
				process(selector);
			} catch (Exception e) {
				// ignore
			}
		}

	}

	/**
	 * 注册一个任务并激活selector
	 * 
	 * @param task
	 */
	protected final void registerTask(Runnable task) {
		
		logger.info("register task!");
		taskQueue.add(task);
		Selector selector = this.selector;
		if (selector != null) {
			if (wakenUp.compareAndSet(false, true)) {
				logger.info("wakeup selector!");
				selector.wakeup();
			}
		} else {
			logger.warn("selector is null, remove task!");
			taskQueue.remove(task);
		}
	}

	/**
	 * 执行队列里的任务
	 */
	private void processTaskQueue() {
		
		logger.info("process task queue!");
		for (;;) {
			final Runnable task = taskQueue.poll();
			if (task == null) {
				break;
			}
			task.run();
		}
	}
	
	/**
	 * 获取线程管理对象
	 * @return
	 */
	public EventLoopGroup getEventLoopGroup() {
		return this.group;
	}

	/**
	 * select抽象方法
	 * 
	 * @param selector
	 * @return
	 * @throws IOException
	 */
	protected abstract int select(Selector selector) throws IOException;

	/**
	 * selector的业务处理
	 * 
	 * @param selector
	 * @throws IOException
	 */
	protected abstract void process(Selector selector) throws Exception;
}
