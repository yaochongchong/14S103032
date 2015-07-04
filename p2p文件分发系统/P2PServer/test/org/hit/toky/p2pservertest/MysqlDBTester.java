package org.hit.toky.p2pservertest;

import org.hit.toky.p2pserver.MysqlDB;
import org.hit.toky.p2pserver.NetIpAddress;
import org.hit.toky.p2pserver.User;

/**
 * @author 	 tokysky (HIT-CS-ICES) 
 * @time	  于2015年4月27日下午7:47:17
 *
 * @description 
 **/

public class MysqlDBTester {
	static String localHostAddr;
	static String dbName = "p2pserver";
	static String userName = "toky";
	static String password = "123456";
	
	public static void main(String[] args) {
		try {
			localHostAddr = NetIpAddress.getLocalIpv4Address();
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("获取本地IP失败");
			System.exit(-1);
		}
		MysqlDB mysqlDB = new MysqlDB(localHostAddr, userName, password, dbName);
		boolean b;
		int ret = mysqlDB.Login("11", "12345");
		System.out.println(ret);
	}
}
