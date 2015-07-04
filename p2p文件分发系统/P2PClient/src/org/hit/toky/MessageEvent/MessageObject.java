package org.hit.toky.MessageEvent;

/**
 * @author 	 tokysky (HIT-CS-ICES) 
 * @time	  于2015年6月25日下午12:13:25
 *
 * @description 自定义消息对象，包含事件源和消息
 **/

public class MessageObject {

	private String type;
	private Object message;
	public MessageObject(String type,Object message){
		this.type = type;
		this.message = message;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Object getMessage() {
		return message;
	}
	public void setMessage(Object message) {
		this.message = message;
	}
}
