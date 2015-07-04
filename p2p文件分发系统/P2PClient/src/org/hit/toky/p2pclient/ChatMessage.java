package org.hit.toky.p2pclient;

/**
 * @author 	 tokysky (HIT-CS-ICES) 
 * @time	  于2015年6月11日下午9:01:49
 *
 * @description 聊天消息类
 **/

public class ChatMessage {
	String message;
	String time;
	public ChatMessage(String message,String time){
		this.message = message;
		this.time = time;
	}
}
