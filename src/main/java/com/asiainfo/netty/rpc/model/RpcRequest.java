package com.asiainfo.netty.rpc.model;

import java.util.Arrays;

/**
 * 封装 RPC 请求
 * 封装发送的object的反射属性
 */
public class RpcRequest implements java.io.Serializable {

    /** serialVersionUID */
	private static final long serialVersionUID = 1L;
	
	private String requestId;
    private String clazzName;
    private String methodName;
    private Class<?>[] parameterTypes;
    private Object[] parameters;

    public String getRequestId() {
        return requestId;
    }
    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }
    public String getClazzName() {
		return clazzName;
	}
	public void setClazzName(String clazzName) {
		this.clazzName = clazzName;
	}
	public String getMethodName() {
        return methodName;
    }
    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }
    public Class<?>[] getParameterTypes() {
        return parameterTypes;
    }
    public void setParameterTypes(Class<?>[] parameterTypes) {
        this.parameterTypes = parameterTypes;
    }
    public Object[] getParameters() {
        return parameters;
    }
    public void setParameters(Object[] parameters) {
        this.parameters = parameters;
    }
	@Override
	public String toString() {
		return "RpcRequest [requestId=" + requestId + ", clazzName=" + clazzName + ", methodName=" + methodName
				+ ", parameterTypes=" + Arrays.toString(parameterTypes) + ", parameters=" + Arrays.toString(parameters)
				+ "]";
	}
}
