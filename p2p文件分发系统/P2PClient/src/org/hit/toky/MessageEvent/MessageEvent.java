package org.hit.toky.MessageEvent;

import java.util.EventObject;

/**
 * @author 	 tokysky (HIT-CS-ICES) 
 * @time	  于2015年6月25日上午10:55:21
 *
 * @description 自定义消息事件MessageEvent
 **/

public class MessageEvent extends EventObject{

	
	private static final long serialVersionUID = 3620733954486212540L;
	private Object message;
	private Object source;
	public MessageEvent(Object source,Object message) {
		super(source);
		this.source = source;
		this.message = message;
	}
	
	public Object getMessage() {
		return message;
	}
	
	public void setMessage(Object message) {
		this.message = message;
	}

	public Object getSource() {
		return source;
	}

	public void setSource(Object source) {
		this.source = source;
	}
}
