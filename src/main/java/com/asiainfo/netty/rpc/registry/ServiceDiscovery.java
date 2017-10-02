package com.asiainfo.netty.rpc.registry;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 本类用于client发现server节点的变化 ，实现负载均衡
 *
 */
public class ServiceDiscovery {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	private CountDownLatch latch = new CountDownLatch(1);

	private volatile List<String> dataList = new ArrayList<String>();

	private String registryAddress;

	private static String defaultRegistryAddress;
	
	private AtomicLong index = new AtomicLong(0);

	static 	{
		try {
			defaultRegistryAddress = InetAddress.getLocalHost().getHostAddress() + ":" + 2181;
		} catch (UnknownHostException e) {}
	}

	public ServiceDiscovery() {
		this(defaultRegistryAddress);
	}

	/**
	 * zk链接
	 * 
	 * @param registryAddress
	 */
	public ServiceDiscovery(String registryAddress) {
		
		this.registryAddress = registryAddress;
		ZooKeeper zk = connectServer();
		if (zk != null) {
			watchNode(zk);
		}
	}

	/**
	 * 发现新节点
	 * 
	 * @return
	 */
	public String discover() {
		
		String data = null;
		int size = dataList.size();
		// 存在新节点，使用即可
		if (size > 0) {
			if (size == 1) {
				data = dataList.get(0);
				logger.debug("using only data: {}", data);
			} else {
				data = dataList.get((int) (index.incrementAndGet() % dataList.size()));
				logger.debug("using roundrobin data: {}", data);
			}
		}
		return data;
	}

	/**
	 * 链接
	 * 
	 * @return
	 */
	private ZooKeeper connectServer() {
		
		ZooKeeper zk = null;
		try {
			zk = new ZooKeeper(registryAddress, Constant.ZK_SESSION_TIMEOUT,
					new Watcher() {
						public void process(WatchedEvent event) {
							if (event.getState() == Event.KeeperState.SyncConnected) {
								latch.countDown();
							}
						}
					});
			latch.await();
		} catch (Exception ex) {
			logger.error("error on connect ZooKeeper!", ex);
		}
		return zk;
	}

	/**
	 * 监听
	 * 
	 * @param zk
	 */
	private void watchNode(final ZooKeeper zk) {
		
		try {
			// 获取所有子节点
			List<String> nodeList = zk.getChildren(Constant.ZK_REGISTRY_PATH,
					new Watcher() {
						public void process(WatchedEvent event) {
							// 节点改变
							if (event.getType() == Event.EventType.NodeChildrenChanged) {
								watchNode(zk);
							}
						}
					});
			List<String> dataList = new ArrayList<String>();
			// 循环子节点
			for (String node : nodeList) {
				// 获取节点中的服务器地址
				byte[] bytes = zk.getData(Constant.ZK_REGISTRY_PATH + "/" + node, false, null);
				// 存储到list中
				dataList.add(new String(bytes));
			}
			logger.debug("node data: {}", dataList);
			// 将节点信息记录在成员变量
			this.dataList = dataList;
		} catch (Exception ex) {
			logger.error("error on watch node!", ex);
		}
	}
}