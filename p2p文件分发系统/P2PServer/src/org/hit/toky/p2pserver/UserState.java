package org.hit.toky.p2pserver;

/**
 * @author 	 tokysky (HIT-CS-ICES) 
 * @time	  于2015年4月27日下午2:59:43
 *
 * @description 
 **/

public class UserState {
	
	public static final int USER_OFFLINE	= 0x0000;//离线
	public static final int USER_STEAL		= 0x0001;//隐身
	public static final int USER_ONLINE 	= 0x0002;//在线
	public static final int USER_BUSY		= 0x0003;//忙碌
	public static final int USER_AWAY		= 0x0004;//离开
	
	private static final String[] UserStatusArray = {
		" [离线请留言]",
		" [离线请留言]",
		" [在线]",
		" [忙碌]",
		" [离开]"
	};
	private static final String[] UserSearchStatusArray = {
		"离线",
		"离线",
		"在线",
		"忙碌",
		"离开"
	};
	public static String UserStatus(int userStatus){
		if(userStatus <USER_OFFLINE || userStatus > USER_AWAY){
			return "未知状态";
		}
		return UserStatusArray[userStatus];
	}
	public static String UserSearchStatus(int userStatus){
		if(userStatus <USER_OFFLINE || userStatus > USER_AWAY){
			return "";
		}
		return UserSearchStatusArray[userStatus];
	}
}
