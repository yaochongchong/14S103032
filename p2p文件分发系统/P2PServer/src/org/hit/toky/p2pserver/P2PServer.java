package org.hit.toky.p2pserver;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.swing.ImageIcon;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.mysql.jdbc.Buffer;

/**
 * @author tokysky (HIT-CS-ICES)
 * @time 于2015年4月27日下午2:39:47
 *
 * @description
 **/

public class P2PServer {

	final String UTF8 = "UTF-8";
	//数据库信息
	final String localHostAddr = "127.0.0.1";
	final String dbName = "p2pserver";
	final String userName = "toky";
	final String password = "123456";
	final int ThreadSize = 10;
	private final int serverUdpPort = 12300;// Udp服务器端口号
	private final int serverTcpPort = 12301;// Tcp服务器端口
	
	private int registerCounter;
	private int onlineCounter = 0;
	private int resourseCounter = 0;
	private int seedIdMarker = 0;
	
	MysqlDB mysqlDB;
	Thread udpServerThread[];
	UDPServer udpServer[];
	//DatagramSocket recvSocket;// 接收套接字
	//DatagramSocket sendtoSocket;//发送套接字
	DatagramSocket udpSocket;
	ServerUI serverUI;
	List<User> userList;
	List<Friendship> friendships;
	List<SeedObject> seedList;
	
	//tcp套接字
	ServerSocket tcpSocket;
	Thread tcpThread;
	TCPServer tcpServer;

	
	public P2PServer() {
		try {
			udpSocket = new DatagramSocket(serverUdpPort);
			tcpSocket = new ServerSocket(serverTcpPort);
			mysqlDB = new MysqlDB(localHostAddr, userName, password, dbName);
		} catch (SocketException e) {
			System.exit(0);
		} catch (IOException e) {
			System.exit(0);
		}
		userList = mysqlDB.InitUserList();
		registerCounter = userList.size();
		friendships = mysqlDB.InitFriendships();
		//System.out.println(friendships.size());
		seedList = new ArrayList<SeedObject>();
		serverUI = new ServerUI();
		serverUI.updateRegistUserNum(registerCounter);
		//创建Udp服务器
		udpServer = new UDPServer[ThreadSize];
		udpServerThread = new Thread[ThreadSize];
		for (int i = 0; i < ThreadSize; ++i) {
			udpServer[i] = new UDPServer();
			udpServerThread[i] = new Thread(udpServer[i]);
			udpServerThread[i].start();
		}
		//创建Tcp服务器
		tcpServer = new TCPServer();
		tcpThread = new Thread(tcpServer);
		tcpThread.start();
	}

	/**
	 * 
	 * @author 	 tokysky (HIT-CS-ICES) 
	 * @time	  于2015年6月25日下午3:28:01
	 *
	 * @description 用于多线程的UDP服务器
	 *
	 */
	class UDPServer implements Runnable {
		DatagramPacket recvPacket;// 接收包
		InetAddress address;// 客户端ip地址
		int udpPort;// 客户端端口
		byte[] buffer;

		// byte[]
		public UDPServer() {
			buffer = new byte[1472];
		}

		@Override
		public void run() {
			
			// TODO 自动生成的方法存根
			while (true) {
				try {
					recvPacket = new DatagramPacket(buffer, buffer.length);
					udpSocket.receive(recvPacket);
					address = recvPacket.getAddress();
					udpPort = recvPacket.getPort();
					byte[] messageBuffer = new byte[recvPacket.getLength()];
					System.arraycopy(buffer, 0, messageBuffer, 0, messageBuffer.length);
					String recvMes = new String(messageBuffer, UTF8);
					DatagramPacket sendtoPacket = recvMessageHandler(recvMes,
							address, udpPort);
					sendto(sendtoPacket);
					address = null;
					recvPacket = null;
					sendtoPacket = null;
					Thread.sleep(200);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		/**
		 * 收到来自客户端的信息处理函数，提取相应的类型，进行相关处理
		 * @param recvMes
		 * @param addr
		 * @param port
		 * @return
		 */
		private DatagramPacket recvMessageHandler(String recvMes,
				InetAddress addr, int port) {
			byte[] sbuffer = null;
			char type = recvMes.charAt(0);
			String message = recvMes.substring(1);
			StringBuilder strBuilder = new StringBuilder(); 
			switch (type) {
			case P2PMessageType.MSG_REGIST_T:
				message = registerHandler(message);
				strBuilder.append(P2PMessageType.MSG_REGIST_T);
				strBuilder.append(message);
				break;
			case P2PMessageType.MSG_LOGIN_T:
				message = loginHandler(message,addr.getHostAddress(),port);
				strBuilder.append(P2PMessageType.MSG_LOGIN_T);
				strBuilder.append(message);
				break;
			case P2PMessageType.MSG_FINDFD_T:
				message = searchUserHandler(message);
				strBuilder.append(P2PMessageType.MSG_FINDFD_T);
				strBuilder.append(message);
				break;
			case P2PMessageType.MSG_ADDFD_T:
				message = addFriendHandler(message);
				strBuilder.append(P2PMessageType.MSG_ADDFD_T);
				strBuilder.append(message);
				break;
			case P2PMessageType.MSG_ADDRES_T://做种
				message = addSeedHandler(message);
				strBuilder.append(P2PMessageType.MSG_ADDRES_T);
				strBuilder.append(message);
				break;
			case P2PMessageType.MSG_REQRES_T:////请求资源数据（向服务器）
				message = reqResHandler(message);
				strBuilder.append(P2PMessageType.MSG_ADDRES_T);
				strBuilder.append(message);
				break;
			default:
				break;
			}
			try {
				sbuffer = strBuilder.toString().getBytes(UTF8);
			} catch (UnsupportedEncodingException e) {
				// TODO: handle exception
			}
			DatagramPacket sendtoPacket = new DatagramPacket(sbuffer,
					sbuffer.length, addr, port);
			//System.out.println(addr +"/" + port);
			sbuffer = null;
			message = null;
			return sendtoPacket;
		}
		
		private synchronized List<ListObject> getSearchListObject(String id) {
			List<ListObject> list = new ArrayList<ListObject>();
			ListObject object;
			String uid;
			for(int i=0;i< userList.size();++i){
				uid = String.valueOf(userList.get(i).getUserId());
				if(uid.startsWith(id)){
					object = new ListObject(
							userList.get(i).getUserId(),
							userList.get(i).getNickName(),
							userList.get(i).getUserState(),
							userList.get(i).getUserSex(),
							0);
					list.add(object);
				}
			}
			return list;
		}
		
		/**
		 * 
		 * @description
		 * @param id
		 * @return List<User>
		 *
		 */
		private List<User> getFriendList(int id){
			List<User> list = new ArrayList<User>();
			int uid;
			int fid;
			int index;
			synchronized (friendships) {
				for(int i=0;i<friendships.size();++i){
					uid = friendships.get(i).uid;
					fid = friendships.get(i).fid;
					if(uid != id && fid != id){
						continue;
					}
					if(uid == id){
						index = fid;
					}else{
						index = uid;
					}
					list.add(userList.get(index-1));
				}
				return list;
			}
		}
		
		private String searchUserHandler(String message){
			JSONObject json;
			JSONObject finalJson;
			try {
				json  = new JSONObject(message);
				String id = (String)json.get("id");
				List<ListObject> list = getSearchListObject(id);
				if(list.isEmpty()){
					return "{\"ret\":0}";
				}
				JSONArray array = new JSONArray();
				for(int i=0;i< list.size();++i){
					json = new JSONObject();
					json.put("id", list.get(i).id);
					json.put("name", list.get(i).nickname);
					json.put("status", list.get(i).status);
					json.put("sex", list.get(i).sex);
					array.put(json);
				}
				finalJson = new JSONObject();
				finalJson.put("ret",list.size());
				finalJson.put("lists", array);
			} catch (JSONException e) {
				// TODO 自动生成的 catch 块
				return null;
			}
			return finalJson.toString();
		}
		
		private synchronized void addFriendExec(int uid,int fid){
			Friendship fship = new Friendship(uid, fid);
			friendships.add(fship);
			fship = null;
		}
		
		private String addFriendHandler(String message) {
			JSONObject json = null;
			try {
				json = new JSONObject(message);
				int uid = json.getInt("uid");
				int fid = json.getInt("fid");
				String confirm = json.getString("confm");
				if(mysqlDB.AddFriendShip(new Friendship(uid, fid))){
					addFriendExec(uid,fid);
					//如果被加的人在线，则给fid发送uid的信息
					if(userList.get(fid - 1).getUserState() >= UserState.USER_ONLINE){
						json = new JSONObject();
						json.put("id",uid);
						json.put("name", userList.get(uid - 1).getNickName());
						json.put("sex", userList.get(uid - 1).getUserSex());
						json.put("status", userList.get(uid - 1).getUserState());
						json.put("ip", userList.get(uid - 1).getUserIpAddr());
						json.put("tcp", userList.get(uid - 1).getTcpPort());
						json.put("udp", userList.get(uid - 1).getUdpPort());
						String reMessage = P2PMessageType.MSG_BYADDFD_T + json.toString();
						try{
							DatagramPacket packet = new DatagramPacket(reMessage.getBytes(),
									reMessage.getBytes().length,
									InetAddress.getByName(userList.get(uid - 1).getUserIpAddr()),
									userList.get(uid - 1).getUdpPort());
							sendto(packet);
						}catch(Exception e){
						}
					}
					json = new JSONObject();
					json.put("id",fid);
					json.put("name", userList.get(fid - 1).getNickName());
					json.put("sex", userList.get(fid - 1).getUserSex());
					json.put("status", userList.get(fid - 1).getUserState());
					json.put("ip", userList.get(fid - 1).getUserIpAddr());
					json.put("tcp", userList.get(fid - 1).getTcpPort());
					json.put("udp", userList.get(fid - 1).getUdpPort());
					//System.out.println(json.toString());
					return json.toString();
				}
			} catch (JSONException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
			return "{}";
		}
		
		private String loginHandler(String message,String addr,int udp) {
			JSONObject json;
			// TODO 自动生成的方法存根
			int ret;
			try {
				json  = new JSONObject(message);
				String id = (String)json.get("id");
				String pwd = (String) json.get("pwd");
				int tcp = (Integer)json.get("tcp");
				ret = mysqlDB.Login(id, pwd);
				json  = new JSONObject();
				json.put("ret", ret);
				//登陆成功
				if(ret == LoginRetValue.LOGIN_SUCCEED){
					userList.get(Integer.valueOf(id) -1).setTcpPort(tcp);
					userList.get(Integer.valueOf(id) -1).setUdpPort(udp);
					userList.get(Integer.valueOf(id) -1).setUserIpAddr(addr);
					json.put("name", userList.get(Integer.valueOf(id) - 1).getNickName());
					json.put("sex", userList.get(Integer.valueOf(id) - 1).getUserSex());
					List<User> list = getFriendList(Integer.valueOf(id));
					JSONArray array = new JSONArray();
					for(int i=0;i< list.size();++i){
						JSONObject object = new JSONObject();
						object.put("id",list.get(i).getUserId());
						object.put("name", list.get(i).getNickName());
						object.put("sex", list.get(i).getUserSex());
						object.put("status", list.get(i).getUserState());
						object.put("ip", list.get(i).getUserIpAddr());
						object.put("tcp", list.get(i).getTcpPort());
						object.put("udp", list.get(i).getUdpPort());
						array.put(object);
					}
					json.put("f", array);
				}
				id = null;
				pwd = null;
			} catch (JSONException e) {
				// TODO 自动生成的 catch 块
				return null;
			}
			return json.toString();
		}
		
		private String registerHandler(String message){
			JSONObject json;
			User user;
			try {
				json = new JSONObject(message);
				String name = (String)json.get("name");
				String pwd = (String)json.get("pwd");
				int sex = (Integer)json.get("sex");
				++registerCounter;
				serverUI.updateRegistUserNum(registerCounter);
				user = new User(registerCounter,name, sex, null, 0, 0);
				userList.add(user);
				json = new JSONObject();
				if(mysqlDB.Register(user, pwd)){
					json.put("ret", user.getUserId());
					json.put("sex", sex);
				}else{
					json.put("ret", -1);
				}
			} catch (Exception e) {
				// TODO: handle exception
				return null;
			}
			return json.toString();
		}
		
		private synchronized void addSeed(SeedObject o){
			seedList.add(o);
			++resourseCounter;
		}
		
		private String addSeedHandler(String message){
			
			JSONObject json;
			SeedObject sObject;
			int copy_seedIdMarker;
			try {
				json = new JSONObject(message);
				//对seedIdMarker加锁，保存本地副本，防止多线程造成seedIdMarker数据不一致
				synchronized ((Integer)seedIdMarker) {
					copy_seedIdMarker = seedIdMarker;
					++seedIdMarker;
				}
				sObject = new SeedObject(
						copy_seedIdMarker,
						json.getInt("id"),
						json.getInt("price"),
						json.getString("name"),
						json.getString("hash"),
						json.getString("tag"),
						json.getLong("size"));
				addSeed(sObject);
				json = new JSONObject();
				json.put("seedId", copy_seedIdMarker);
				return json.toString();
			} catch (Exception e) {
				// TODO: handle exception
			}
			return "{\"seedId\",-1}";
		}
		
		
		private String reqResHandler(String message) {
			return "{}";
		}
		
		private synchronized void sendto(DatagramPacket sendtoPacket) {
			try {
				udpSocket.send(sendtoPacket);
			} catch (IOException e) {
				// TODO: handle exception
			}
		}

	}

	/**
	 * 
	 * @author 	 tokysky (HIT-CS-ICES) 
	 * @time	  于2015年6月25日下午3:27:33
	 *
	 * @description 用于多线程的TCP服务器
	 *
	 */
	class TCPServer implements Runnable{

		private BlockingQueue<Runnable> queue;
		private ThreadPoolExecutor thPool;
		public TCPServer() {
			// TODO 自动生成的构造函数存根
			//创建大小为20的线程等待队列
			queue = new ArrayBlockingQueue<Runnable>(20);
			thPool = new ThreadPoolExecutor(5, 10, 60 *10, TimeUnit.SECONDS, queue);
		}
		@Override
		public void run() {
			// TODO 自动生成的方法存根
			while(true){
				try {
					Socket client = tcpSocket.accept();
					thPool.execute(new TCPTask(client));
				} catch (IOException e1) {
				}
				try {
					Thread.sleep(200);
				} catch (Exception e) {
				}
			}
		}
		
		class TCPTask implements Runnable{

			private Socket clientSocket;
			private DataInputStream inputStream;
			private DataOutputStream outputStream;
			public TCPTask(Socket clientSocket) {
				this.clientSocket = clientSocket;
			}
			@Override
			public void run() {
				try {
					inputStream = new DataInputStream(clientSocket.getInputStream());
					outputStream = new DataOutputStream(clientSocket.getOutputStream());	
					String message = recvFromClient(inputStream);
					message = tcpMessageHandler(message);
					sendToClient(outputStream,message);
					//关闭资源
					outputStream.close();
					inputStream.close();
					clientSocket.close();
					Thread.sleep(200);
				} catch (Exception e) {
					System.out.println(e.getMessage());
				}
			}
			
			private List<SeedObject> getSeedObjects(String tag){
				List<SeedObject> list = new ArrayList<SeedObject>();
				if(seedList.isEmpty()){
					return list;
				}
				if(tag.isEmpty()){
					synchronized (seedList) {
						if(seedList.size() <= 10){
							list.addAll(seedList);
						}else{
							Collection<Integer> c = new HashSet<Integer>();
							int index;
							Random random = new Random();
							while(c.size() < 10){
								index = random.nextInt(seedList.size());
								if(!c.contains(index)){
									c.add(index);
									list.add(seedList.get(index));
								}
							}
							
						}
						return list;
					}
					
				}else{
					String[] keys = tag.split(" ");
					synchronized (seedList) {
						for(int i=0;i<seedList.size();++i){
							for(int j = 0;j<keys.length;++j){
								if(seedList.get(i).getName().contains(keys[j])){
									list.add(seedList.get(i));
									if(list.size() == 10){
										return list;
									}
									break;
								}
								if(seedList.get(i).getTag().contains(keys[j])){
									list.add(seedList.get(i));
									if(list.size() == 10){
										return list;
									}
									break;
								}
							}
						}
					}
					return list;
				}
			}
			
			private String findSeedHandler(String message){
				JSONObject json;
				try {
					json = new JSONObject(message);
					String tag = json.getString("tag");
					List<SeedObject> objects = getSeedObjects(tag);
					json = new JSONObject();
					JSONArray array = new JSONArray();
					JSONObject o;
					synchronized (userList) {
						for(int i=0;i< objects.size();++i){
							o = new JSONObject();
							o.put("sd",objects.get(i).getSeedId());
							o.put("nm", objects.get(i).getName());
							o.put("sz", objects.get(i).getSize());
							o.put("pr", objects.get(i).getPrice());
							o.put("od", objects.get(i).getOfferId());
							o.put("tg", objects.get(i).getTag());
							o.put("hh", objects.get(i).getHash());
							o.put("ad", userList.get(objects.get(i).getOfferId() -1).getUserIpAddr());
							o.put("tc", userList.get(objects.get(i).getOfferId() -1).getTcpPort());
							o.put("ud", userList.get(objects.get(i).getOfferId() -1).getUdpPort());
							array.put(o);
						}
					}
					json.put("ret", array.length());
					json.put("sd", array);
					return json.toString();
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
				return "{\"ret\":0}";
			}
			
			private String tcpMessageHandler(String recvMes){
				char type = recvMes.charAt(0);
				String message = recvMes.substring(1);
				StringBuilder strBuilder = new StringBuilder();
				switch(type){
				case P2PMessageType.MSG_FINDRES_T://查找资源
					message = findSeedHandler(message);
					strBuilder.append(P2PMessageType.MSG_FINDRES_T);
					strBuilder.append(message);
					break;
					
				}
				return strBuilder.toString();
			}
			
			private String recvFromClient(DataInputStream in) throws IOException{
				if(in == null){
					return null;
				}
				byte[] lengthBuffer = new byte[4];
				byte[] buffer;
				in.read(lengthBuffer, 0, 4);
				int total = Util.ByteToInt(lengthBuffer);
				buffer = new byte[total];
				in.read(buffer, 0, buffer.length);
				return new String(buffer);
			}
			
			private void sendToClient(DataOutputStream out,String message) throws IOException{
				if(out == null){
					return;
				}
				//返回数据
				out.write(message.getBytes());
				out.flush();
			}
		}
		
	}
	
	public static void main(String[] args) {
		P2PServer p2pServer = new P2PServer();
	}
}
