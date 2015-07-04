package org.hit.toky.p2pclient;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 	 tokysky (HIT-CS-ICES) 
 * @time	  于2015年6月11日下午8:48:18
 *
 * @description 用户对象
 **/

public class User {

	private int uId;
	private String uName;
	private String uAddress;
	private int uUdpPort;
	private int uTcpPort;
	private int uStatus;
	private int uSex;
	private int index;
	List<ChatMessage> uChatList;
	int uReadMCounter;
	
	public User(){
		uChatList = new ArrayList<ChatMessage>();
		uReadMCounter = 0;
	}
	public User(int uId){
		this.uId = uId;
		uChatList = new ArrayList<ChatMessage>();
		uReadMCounter = 0;
	}
	
	public int getuId() {
		return uId;
	}
	public void setuId(int uId) {
		this.uId = uId;
	}
	public String getuName() {
		return uName;
	}
	public void setuName(String uName) {
		this.uName = uName;
	}
	public String getuAddress() {
		return uAddress;
	}
	public void setuAddress(String uAddress) {
		this.uAddress = uAddress;
	}
	public int getuUdpPort() {
		return uUdpPort;
	}
	public void setuUdpPort(int uUdpPort) {
		this.uUdpPort = uUdpPort;
	}
	public int getuTcpPort() {
		return uTcpPort;
	}
	public void setuTcpPort(int uTcpPort) {
		this.uTcpPort = uTcpPort;
	}
	public int getuStatus() {
		return uStatus;
	}
	public void setuStatus(int uStatus) {
		this.uStatus = uStatus;
	}
	public int getuSex() {
		return uSex;
	}
	public void setuSex(int uSex) {
		this.uSex = uSex;
	}
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
}
