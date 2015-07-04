package org.hit.toky.p2pclient;

/**
 * @author 	 tokysky (HIT-CS-ICES) 
 * @time	  于2015年6月12日上午11:23:48
 *
 * @description 好友列表对象
 **/

public class ListObject {
	int id;
	String nickname;
	int status;
	int index;
	int sex;
	String descript;
	public ListObject(int id,String nickname,int status,int sex,int index){
		this.id = id;
		this.nickname = nickname;
		this.status = status;
		this.index = index;
		this.sex = sex;
		this.descript = null;
	}
	public void setDescript(String des){
		this.descript = des;
	}
}
