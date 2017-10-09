package com.asiainfo.mynetty.eventloop;

import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @Description: selector线程管理者
 * 
 * @author       zq
 * @date         2017年10月5日  上午10:32:50
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class EventLoopGroup {

	final Logger logger = LoggerFactory.getLogger(getClass());
	
	/**
	 * boss线程数组
	 */
	private final AtomicInteger bossIndex = new AtomicInteger();
	private Boss[] bosses;

	/**
	 * worker线程数组
	 */
	private final AtomicInteger workerIndex = new AtomicInteger();
	private Worker[] workers;

	
	public EventLoopGroup(Executor boss, Executor worker, int bossNum, int workNum) {
		initBoss(boss, bossNum);
		initWorker(worker, workNum);
	}

	/**
	 * 初始化boss线程
	 * @param boss
	 * @param count
	 */
	private void initBoss(Executor executor, final int count) {
		
		logger.info("init boss EventLoopGroup, count={}", count);
		this.bosses = new BossEvenloop[count];
		for (int i = 0; i < bosses.length; ) {
			bosses[i] = new BossEvenloop(executor, "boss " + (++i), this);
		}
	}

	/**
	 * 初始化worker线程
	 * @param worker
	 * @param count
	 */
	private void initWorker(Executor executor, int count) {
		
		logger.info("init worker EventLoopGroup, count={}", count);
		this.workers = new WorkerEvenloop[count];
		for (int i = 0; i < workers.length; ) {
			workers[i] = new WorkerEvenloop(executor, "worker " + (++i), this);
		}
	}

	/**
	 * 获取一个worker
	 * @return
	 */
	public Worker nextWorker() {
		 return workers[Math.abs(workerIndex.getAndIncrement() % workers.length)];
	}

	/**
	 * 获取一个boss
	 * @return
	 */
	public Boss nextBoss() {
		 return bosses[Math.abs(bossIndex.getAndIncrement() % bosses.length)];
	}
}
