package org.hit.toky.p2pclient;

/**
 * @author 	 tokysky (HIT-CS-ICES) 
 * @time	  于2015年4月29日下午3:54:28
 *
 * @description 自定义消息类型
 **/

public class P2PMessageType {

	public static final char MSG_REGIST_T	= '0';//注册
	public static final char MSG_LOGIN_T	= '1';//登陆
	public static final char MSG_LOGOUT_T	= '2';//退出
	public static final char MSG_FINDFD_T	= '3';//查询好友
	public static final char MSG_ADDFD_T	= '4';//添加好友
	public static final char MSG_DELFD_T	= '5';//删除好友
	public static final char MSG_FDSTT_T	= '6';//好友状态改变
	public static final char MSG_FDCHAT_T	= '7';//聊天信息
	public static final char MSG_ADDRES_T	= '8';//向服务器添加资源
	public static final char MSG_FINDRES_T	= '9';//向服务器查找资源
	public static final char MSG_REQRES_T	= 'A';//请求资源数据（向服务器）
	public static final char MSG_DLRES_T	= 'B';//请求下载资源（向客户端）
	public static final char MSG_SENDRES_T	= 'C';//主动发送数据（客户端向客户端）
	public static final char MSG_NULL_T		= ' ';//空信息，未知或者数据已经损坏
}
