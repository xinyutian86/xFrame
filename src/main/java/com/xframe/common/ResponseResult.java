package com.xframe.common;

/**
 * 响应数据的包装类，所有请求的结果都使用该类包装后转换成json返回
 * @author yu
 *
 */
public class ResponseResult {
	private String status;
	private String message;
	private Object data;
	
	public ResponseResult(){
		this.status = "0";
		this.message = "操作成功";
	}
	
	public ResponseResult(String status,String message,Object data) {
		this.status = status;
		this.message = message;
		this.data = data;
	}
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
	
}
