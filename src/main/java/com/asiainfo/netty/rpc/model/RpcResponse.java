package com.asiainfo.netty.rpc.model;

/**
 * 封装 RPC 响应
 * 封装相应object
 */
public class RpcResponse implements java.io.Serializable {

    /** serialVersionUID */
	private static final long serialVersionUID = 1L;
	
	private String requestId;
    private Throwable error;
    private Object result;

    public boolean isError() {
        return error != null;
    }
    public String getRequestId() {
        return requestId;
    }
    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }
    public Throwable getError() {
        return error;
    }
    public void setError(Throwable error) {
        this.error = error;
    }
    public Object getResult() {
        return result;
    }
    public void setResult(Object result) {
        this.result = result;
    }
	@Override
	public String toString() {
		return "RpcResponse [requestId=" + requestId + ", error=" + error + ", result=" + result + "]";
	}
}
