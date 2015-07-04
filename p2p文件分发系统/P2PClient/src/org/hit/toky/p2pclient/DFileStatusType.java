package org.hit.toky.p2pclient;

/**
 * @author 	 tokysky (HIT-CS-ICES) 
 * @time	  于2015年6月29日下午9:52:12
 *
 * @description 文件下载的状态
 **/

public class DFileStatusType {

	public static final int DFILE_WAIT = 0;		//等待
	public static final int DFILE_ERROR	= 1;	//出错
	public static final int DFILE_PAUSE = 2;	//暂停
	public static final int DFILE_LOADING =3;	//下载中
	
	private static final String[] DFile ={
		"等待",
		"出错",
		"暂停",
		"下载中"
	};
	
	public static String getDFileStatus(int df){
		if(df < DFILE_WAIT || df > DFILE_LOADING){
			return "";
		}
		return DFile[df];
	}
}
