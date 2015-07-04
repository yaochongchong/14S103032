package org.hit.toky.p2pservertest;

import java.sql.Connection;
import java.sql.SQLException;

import org.hit.toky.p2pserver.MysqlPool;
import org.hit.toky.p2pserver.NetIpAddress;

/**
 * @author tokysky (HIT-CS-ICES)
 * @time 于2015年4月27日下午3:40:17
 *
 * @description
 **/

public class MysqlPoolTester {
	
	//private static final String userName = null;
	static String dbDriver = "com.mysql.jdbc.Driver";
	static String localHostAddr;
	static String dbUrl;
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
		dbUrl = String.format("jdbc:mysql://%s:3306/%s",localHostAddr,dbName);
		System.out.println(dbUrl);
		MysqlPool mysqlPool = new MysqlPool(dbDriver, dbUrl, userName, password);
		Connection conn;
		try{
			mysqlPool.createPool();
		}catch(Exception e){
			System.out.println("创建数据库连接池失败");
		}
		try {
			conn = mysqlPool.getConnection();
			if(conn != null){
				System.out.println("获得连接成功");
			}
			conn.close();
			mysqlPool.closeConnectionPool();
		} catch (SQLException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		
	}
}
