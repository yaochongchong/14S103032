package org.hit.toky.p2pserver;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 	 tokysky (HIT-CS-ICES) 
 * @time	  于2015年4月27日下午4:23:29
 *
 * @description 
 **/

public class MysqlDB {
	
	private final String dbDriver = "com.mysql.jdbc.Driver";
	private String dbUrl;
	private String username;
	private String password;
	private String schemaName;
	MysqlPool mysqlPool;
	
	public MysqlDB(String localIpAddr,String username,String password,String schema){
		this.username = username;
		this.password = password;
		this.schemaName = schema;
		dbUrl = String.format("jdbc:mysql://%s:3306/%s", localIpAddr,schema);
		mysqlPool = new MysqlPool(dbDriver, dbUrl, username,password);
		try{
			mysqlPool.createPool();
		}catch(Exception e){
			System.out.println("创建数据库连接池失败");
		}
	}
	
	public synchronized ResultSet Query(String sql) throws SQLException{
		Connection conn;
		Statement state;
		ResultSet set;
		try {
			conn = mysqlPool.getConnection();
			state = conn.createStatement();
			set = state.executeQuery(sql);
			mysqlPool.returnConnection(conn);
		} catch (SQLException e) {
			// TODO: handle exception
			throw e;
		}
		return set;
	}

	public synchronized boolean Execute(String sql) throws SQLException{
		Connection conn;
		Statement state;
		boolean ret;
		try {
			conn = mysqlPool.getConnection();
			state = conn.createStatement();
			ret = state.executeUpdate(sql) == 1 ? true : false;
			mysqlPool.returnConnection(conn);
		} catch (SQLException e) {
			// TODO: handle exception
			throw e;
		}
		return ret;
	}
	public List<User> InitUserList(){
		List<User> userList = new ArrayList<User>();
		String sql = "SELECT `userid`,`nickname`,`usersex` FROM p2pserver.user;";
		try {
			ResultSet set = Query(sql);
			while(set.next()){
				int id = set.getInt("userid");
				String name = set.getString("nickname");
				int usersex = set.getInt("usersex");
				userList.add(new User(id,name, usersex, "0.0.0.0", 0, 0));
			}
		} catch (SQLException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		return userList;
	}
	
	public List<Friendship> InitFriendships(){
		List<Friendship> friendships = new ArrayList<Friendship>();
		String sql = "SELECT `uid`,`fid` FROM p2pserver.friendship;";
		try {
			ResultSet set = Query(sql);
			while(set.next()){
				int uid = set.getInt("uid");
				int fid = set.getInt("fid");
				friendships.add(new Friendship(uid, fid));
			}
		} catch (SQLException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		return friendships;
	}
	
	//
	public boolean AddFriendShip(Friendship fship){
		String sql = String.format(
				"INSERT INTO `p2pserver`.`friendship` (`uid`, `fid`) VALUES ('%1$d', '%2$d');",
				fship.uid,
				fship.fid);
		try {
			return Execute(sql);
		} catch (Exception e) {
			// TODO: handle exception
			return false;
		}
	}
	
	public boolean Register(User user,String pwd){
		String sql = String.format(
				"INSERT INTO `p2pserver`.`user` (`userid`,`nickname`, `pwd`,`usersex`)"
				+ " VALUES ('%1$d', '%2$s', '%3$s','%4$s');",
				user.getUserId(),
				user.getNickName(),
				pwd,
				user.getUserSex());
		boolean ret;
		try {
			ret = Execute(sql);
		} catch (Exception e) {
			// TODO: handle exception
			ret = false;
		} 
		return ret;
	}
	
	public int Login(String id,String pwd){
		String sql = String.format("SELECT `pwd` FROM `p2pserver`.`user` WHERE `userid` = '%1$s'", id);
		try {
			ResultSet set = Query(sql);
			if(set.next()){
				String user_pwd = set.getString("pwd");
				if(user_pwd.equals(pwd)){
					return LoginRetValue.LOGIN_SUCCEED;
				}
				return LoginRetValue.LOGIN_PWD_ERROR;
			}
			return LoginRetValue.LOGIN_ID_NOT_EST;
		} catch (SQLException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
			return -2;
		}
	}
	
	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public String getSchemaName() {
		return schemaName;
	}

}
