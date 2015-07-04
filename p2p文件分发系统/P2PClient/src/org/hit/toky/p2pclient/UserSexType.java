package org.hit.toky.p2pclient;

/**
 * @author 	 tokysky (HIT-CS-ICES) 
 * @time	  于2015年6月14日下午10:18:15
 *
 * @description 性别对象
 **/

public class UserSexType {
	public static final int SEX_SECRECT		= -1;//性别保密
	public static final int SEX_MAN		 	= 0;//女
	public static final int SEX_WOMAN		= 1;//男
	public static final String [] SEX_ARRAY_STRINGS ={
		"保密",
		"女",
		"男"
	};
	public static String getSex(int sex){
		if(sex < SEX_SECRECT || sex > SEX_WOMAN){
			return "";
		}
		return SEX_ARRAY_STRINGS[sex + 1];
	}
}
