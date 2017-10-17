package com.asiainfo.netty.rpc.client;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.UUID;

import com.asiainfo.netty.rpc.model.RpcException;
import com.asiainfo.netty.rpc.model.RpcRequest;
import com.asiainfo.netty.rpc.model.RpcResponse;
import com.asiainfo.netty.rpc.registry.ServiceDiscovery;

/**
 * 
 * RPC 代理（用于创建 RPC 服务代理）
 * 
 * @author       zq
 * @date         2017年10月17日  下午1:04:39
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class RpcProxy {

    private ServiceDiscovery serviceDiscovery;
    
    public RpcProxy(ServiceDiscovery serviceDiscovery) {
        this.serviceDiscovery = serviceDiscovery;
    }

    /**
     * 创建代理
     *
     * @param interfaceClass
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T> T create(Class<T> interfaceClass) {
    	
        return (T) Proxy.newProxyInstance(interfaceClass.getClassLoader(),
                new Class<?>[]{interfaceClass}, new InvocationHandler() {
                	@Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                    	
                        //封装请求信息
                        RpcRequest request = packRequestInfo(method, args);
                        //查找服务
                        String[] array = getServerAddress();
                        String host = array[0];
                        int port = Integer.parseInt(array[1]);
                        //创建Netty客户端，连接服务端
                        RpcClient client = new RpcClient(host, port);
                        //发送请求
                        RpcResponse response = client.send(request);
                        //返回信息， 并判断是否异常
                        if (response.isError()) {
                            throw response.getError();
                        }
                        return response.getResult();
                    }
                });
    }

    private RpcRequest packRequestInfo(Method method, Object[] args) {
    	
        //创建RpcRequest，封装被代理类的属性
        RpcRequest request = new RpcRequest();
        request.setRequestId(UUID.randomUUID().toString());
        //通过反射，获取调用信息
        request.setClazzName(method.getDeclaringClass().getName());
        request.setMethodName(method.getName());
        request.setParameterTypes(method.getParameterTypes());
        request.setParameters(args);
        return request;
    }

    private String[] getServerAddress() {

        if (serviceDiscovery == null) {
            throw new RpcException("serviceDiscovery doesn't init");
        }
        //随机获取服务的地址
        return serviceDiscovery.discover().split(":");
    }
}
