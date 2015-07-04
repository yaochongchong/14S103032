package org.hit.toky.p2pserver;

/**
 * @author 	 tokysky (HIT-CS-ICES) 
 * @time	  于2015年4月27日下午2:43:14
 *
 * @description 
 **/

public class User {
	
	private int userId;		//用户id
	private String nickName;//用户昵称
	private int userSex;//用户性别
	private int userState; //用户状态
	private String userIpAddr;//用户当前ip地址
	private int udpPort;	//用户聊天的udp端口
	private int tcpPort;	//用户用于传送文件的端口
	public User(int id,String nickname,int usersex,String ipAddr,int udpPort,int tcpPort){
		userId = id;
		nickName = nickname;
		userSex = usersex;
		userState = UserState.USER_OFFLINE;
		userIpAddr = ipAddr;
		this.udpPort = udpPort;
		this.tcpPort = tcpPort;
	}
	
	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	
	public int getUserSex() {
		return userSex;
	}

	public void setUserSex(int userSex) {
		this.userSex = userSex;
	}

	public String getUserIpAddr() {
		return userIpAddr;
	}

	public void setUserIpAddr(String userIpAddr) {
		this.userIpAddr = userIpAddr;
	}

	public int getUdpPort() {
		return udpPort;
	}

	public void setUdpPort(int udpPort) {
		this.udpPort = udpPort;
	}

	public int getTcpPort() {
		return tcpPort;
	}

	public void setTcpPort(int tcpPort) {
		this.tcpPort = tcpPort;
	}

	public int getUserState() {
		return userState;
	}

	public void setUserState(int userState) {
		this.userState = userState;
	}
}
