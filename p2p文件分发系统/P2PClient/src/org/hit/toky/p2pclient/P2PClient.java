package org.hit.toky.p2pclient;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import org.hit.toky.MessageEvent.MessageEvent;
import org.hit.toky.MessageEvent.MessageListener;
import org.hit.toky.MessageEvent.MessageObject;
import org.hit.toky.p2pclient.Util;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author tokysky (HIT-CS-ICES)
 * @time 于2015年4月28日上午10:35:38
 *
 * @description P2P客户端主程序
 **/

public class P2PClient {
	final String UTF8 = "UTF-8";
	//服务器信息
	final int P2PServerUdpPort = 12300;
	final int P2PServerTcpPort = 12301;
	final String P2PServerHost = "192.168.1.91";
	/////用户自己的基本信息
	private int DefalutUdpPort = 12400;
	private int DefalutTcpPort = 12500;
	private int SelfUserId;
	private int SelfSex;
	private int SelfStatus;
	private String SelfUserName;
	private String SelfDescript;
	private Timer regTimer;//注册定时器
	private Timer logTimer;//登陆定时器
	// UDP发送套接字
	DatagramSocket udpSocket;
	InetAddress P2PServerAddr;
	// 自己的IP地址
	InetAddress localHostAddr;
	// UDP服务器
	UDPServer udpServer;
	Thread udpServerThread;
	LoginUI loginUI;
	RegistUI registUI;
	MainUI mainUI;
	List<User> friendList;
	SearchUI searchUI;
	boolean searchFlag = false;
	P2PFileUI fileUI;
	List<SeedObject> fileObjects;//种子列表
	List<ResObject> resObjects;//下载资源列表
	List<DloadObject> dloadObjects;//正在下载列表
	boolean seedFlag = false;
	Random random;
	Queue<String> requestQueue;// 将TCP请求放入请求队列中，依次向服务器请求
	Thread clientThread;
	TCPClientTask clientTask;
	// 客户端的TCP服务器
	ServerSocket tcpSocket;
	Thread tcpServerThread;
	TCPServer tcpServer;

	// 正在下载资源数
	private Timer dTimer;
	private int curDloadSize = 0;
	private BlockingQueue<Runnable> dloadQueue;// 队列
	private ThreadPoolExecutor dloadPool;// 线程池

	public P2PClient() {
		random = new Random();
		// 获得一个可用的UDP端口
		while (true) {
			try {
				udpSocket = new DatagramSocket(DefalutUdpPort);
				break;
			} catch (Exception e) {
				// TODO: handle exception
				++DefalutUdpPort;
			}
		}

		while (true) {
			try {
				tcpSocket = new ServerSocket(DefalutTcpPort);
				break;
			} catch (Exception e) {
				// TODO: handle exception
				++DefalutTcpPort;
			}
		}

		try {
			P2PServerAddr = InetAddress.getByName(P2PServerHost);
			localHostAddr = InetAddress.getByName(NetIpAddress
					.getLocalIpv4Address());
		} catch (UnknownHostException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		udpServer = new UDPServer();
		udpServerThread = new Thread(udpServer);
		udpServerThread.start();
		loginUI = new LoginUI();
		loginUI.frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		loginUI.registLabel.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO 自动生成的方法存根
			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO 自动生成的方法存根
			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO 自动生成的方法存根
				loginUI.registLabel.setForeground(Color.white);
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO 自动生成的方法存根
				loginUI.registLabel.setForeground(Color.red);
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO 自动生成的方法存根
				loginUI.hide();
				registUI = new RegistUI();
				registUI.frame.addWindowListener(new WindowAdapter() {
					@Override
					public void windowClosing(WindowEvent e) {
						registUI.dispose();
						loginUI.display();
					}
				});
				registUI.regButton
						.addActionListener(new RegistButtonActionListner());
				registUI.display();
			}
		});
		loginUI.loginButton.addActionListener(new LoginButtonActionListner());
		loginUI.display();
	}

	public void sendtoPacket(String message, InetAddress addr, int port) {
		byte[] buffer = null;
		try {
			buffer = message.getBytes(UTF8);
			DatagramPacket sendtoPacket = new DatagramPacket(buffer,
					buffer.length, addr, port);
			udpSocket.send(sendtoPacket);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	private void recvMessageHandler(String recvMessage, InetAddress addr,
			int port) {
		// TODO 自动生成的方法存根
		char type = recvMessage.charAt(0);
		String message = recvMessage.substring(1);
		switch (type) {
		case P2PMessageType.MSG_LOGIN_T:
			loginRetHandler(message);
			break;
		case P2PMessageType.MSG_REGIST_T:
			registerRetHandler(message);
			break;
		case P2PMessageType.MSG_FINDFD_T:
			searchUserRetHandler(message);
			break;
		case P2PMessageType.MSG_ADDFD_T:
			addFriendRetHandler(message);
			break;
		case P2PMessageType.MSG_FDSTT_T:
			updateFriendStatus(message);
			break;
		case P2PMessageType.MSG_ADDRES_T:// 做种（服务器回复）
			addSeedRetHandler(message);
			break;
		case P2PMessageType.MSG_FINDRES_T:// 查找资源（服务器回复）
			break;
		case P2PMessageType.MSG_DLRES_T:// 请求下载资源（客户端向客户端）
			break;
		case P2PMessageType.MSG_SENDRES_T:// 主动发送数据（客户端向客户端）
			break;
		default:
			break;
		}
		message = null;
	}

	private void loginRetHandler(String message) {

		try {
			logTimer.cancel();
		} catch (Exception e) {

		}

		try {
			JSONObject json = new JSONObject(message);
			int ret = (Integer) json.get("ret");
			switch (ret) {
			case 1:
				friendList = new ArrayList<User>();
				requestQueue = new LinkedList<String>();
				clientTask = new TCPClientTask();
				clientThread = new Thread(clientTask);
				clientThread.start();
				loginUI.dispose();
				SelfSex = (Integer) json.get("sex");
				SelfUserName = (String) json.getString("name");
				JSONArray array = (JSONArray) json.getJSONArray("f");
				for (int i = 0; i < array.length(); ++i) {
					json = array.getJSONObject(i);
					User user = new User(json.getInt("id"));
					user.setuName(json.getString("name"));
					user.setuSex(json.getInt("sex"));
					user.setuStatus(json.getInt("status"));
					user.setuTcpPort(json.getInt("tcp"));
					user.setuUdpPort(json.getInt("udp"));
					user.setuAddress(json.getString("ip"));
					user.setIndex(random.nextInt(52) + 1);
					friendList.add(user);
				}
				array = null;
				json = null;
				fileObjects = new ArrayList<SeedObject>();
				dloadObjects = new ArrayList<DloadObject>();
				resObjects = new ArrayList<ResObject>();
				dloadQueue = new ArrayBlockingQueue<Runnable>(20);
				dloadPool = new ThreadPoolExecutor(3, 5, 10 * 60,
						TimeUnit.SECONDS, dloadQueue);
				tcpServer = new TCPServer();
				tcpServerThread = new Thread(tcpServer);
				tcpServerThread.start();
				mainUI = new MainUI(SelfUserName);
				mainUI.searchLabel
						.addMouseListener(new SearchExtMouseListener());
				mainUI.fileLabel.addMouseListener(new SearchExtMouseListener());
				updateFriendList();
				break;
			case 0:
				loginUI.reLogin();
				JOptionPane.showMessageDialog(null, "密码不正确，请确认后重试！", "错误",
						JOptionPane.OK_OPTION, null);
				break;
			case -1:
				loginUI.reLogin();
				JOptionPane.showMessageDialog(null, "账号不存在，请确认后重试！", "错误",
						JOptionPane.OK_OPTION, null);
				break;
			default:
				break;
			}
		} catch (JSONException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
	}

	private void registerRetHandler(String message) {
		try {
			regTimer.cancel();
		} catch (Exception e) {
			// TODO: handle exception
		}
		if (message.isEmpty()) {
			JOptionPane.showMessageDialog(null, "注册失败，稍后请重试！", "错误",
					JOptionPane.OK_OPTION, null);
			registUI.overRegist();
			return;
		}
		try {
			JSONObject json = new JSONObject(message);
			int ret = (Integer) json.get("ret");
			if (ret == -1) {
				registUI.overRegist();
				return;
			}
			int sex = (Integer) json.get("sex");
			String IconPath = null;
			if (sex == 0) {
				IconPath = "image\\Icon\\1.jpg";
			} else {
				IconPath = "image\\Icon\\0.jpg";
			}

			JOptionPane.showMessageDialog(null, "注册成功！您的P2P账号为" + ret
					+ "\r\n请妥善保管您的账号和密码", "注册成功", JOptionPane.OK_OPTION,
					new ImageIcon(IconPath));
			registUI.dispose();
			loginUI.display(ret);
		} catch (JSONException e) {
			// TODO: handle exception
		}
	}

	private void searchUserRetHandler(String message) {
		try {
			JSONObject json = new JSONObject(message);
			int ret = json.getInt("ret");
			if (ret == 0) {
				return;
			}
			JSONArray array = json.getJSONArray("lists");
			List<ListObject> list = new ArrayList<ListObject>();
			for (int i = 0; i < array.length(); i++) {
				json = array.getJSONObject(i);
				ListObject object = new ListObject(json.getInt("id"),
						json.getString("name"), json.getInt("status"),
						json.getInt("sex"), 0);
				list.add(object);
			}
			if (searchUI != null) {
				searchUI.updateTable(list);
			}
		} catch (JSONException e) {
			// TODO: handle exception
		}
	}

	private void addFriendRetHandler(String message) {
		String fName = null;
		try {
			JSONObject json = new JSONObject(message);
			User user = new User(json.getInt("id"));
			fName = json.getString("name");
			user.setuName(fName);
			user.setuSex(json.getInt("sex"));
			user.setuStatus(json.getInt("status"));
			user.setuTcpPort(json.getInt("tcp"));
			user.setuUdpPort(json.getInt("udp"));
			user.setuAddress(json.getString("ip"));
			user.setIndex(random.nextInt(52) + 1);
			friendList.add(user);
			updateFriendList();
		} catch (JSONException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
			return;
		}
		JOptionPane.showMessageDialog(null, "添加好友[" + fName + "]成功，你现在可以发起通话！",
				"添加好友", JOptionPane.OK_OPTION);
	}

	/**
	 * 
	 * @description
	 * @param message
	 *
	 */
	private void updateFriendStatus(String message) {

	}

	private void addSeedRetHandler(String message) {

	}

	private void updateFriendList() {
		List<ListObject> listObjects = new ArrayList<ListObject>();
		for (int i = 0; i < friendList.size(); i++) {
			ListObject object = new ListObject(friendList.get(i).getuId(),
					friendList.get(i).getuName(), friendList.get(i)
							.getuStatus(), friendList.get(i).getuSex(),
					friendList.get(i).getIndex());
			listObjects.add(object);
		}
		mainUI.UpdateFriendList(listObjects);
	}

	private void addClientTask(String task) {
		synchronized (requestQueue) {
			requestQueue.add(task);
		}
	}

	/**
	 * 
	 * @author tokysky (HIT-CS-ICES)
	 * @time 于2015年6月26日下午2:48:48
	 *
	 * @description UDP服务器
	 *
	 */
	class UDPServer implements Runnable {
		// 接收套接字
		DatagramPacket recvPacket;
		InetAddress recvAddr;
		int recvPort;
		byte[] recvBuffer = new byte[1472];

		public UDPServer() {
		}

		@Override
		public void run() {
			// TODO 自动生成的方法存根
			while (true) {
				recvPacket = new DatagramPacket(recvBuffer, recvBuffer.length);
				try {
					udpSocket.receive(recvPacket);
					recvAddr = recvPacket.getAddress();
					recvPort = recvPacket.getPort();
					byte[] messageBuffer = new byte[recvPacket.getLength()];
					System.arraycopy(recvBuffer, 0, messageBuffer, 0,
							messageBuffer.length);
					String recvMessage = new String(messageBuffer, UTF8);
					recvMessageHandler(recvMessage, recvAddr, recvPort);
					messageBuffer = null;
					recvMessage = null;
					Thread.sleep(200);
				} catch (IOException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				} catch (InterruptedException e) { 
					// TODO: handle exception
				}
			}
		}
	}

	/**
	 * 
	 * @author tokysky (HIT-CS-ICES)
	 * @time 于2015年6月26日下午2:50:32
	 *
	 * @description 文件传输服务器套接字子线程
	 *
	 */
	class TCPServer implements Runnable {

		private BlockingQueue<Runnable> queue;// 队列
		private ThreadPoolExecutor thPool;// 线程池

		public TCPServer() {
			queue = new ArrayBlockingQueue<Runnable>(20);
			thPool = new ThreadPoolExecutor(5, 10, 60 * 10, TimeUnit.SECONDS,queue);
		}

		@Override
		public void run() {
			// TODO 自动生成的方法存根
			while (true) {
				try {
					Socket client = tcpSocket.accept();
					thPool.execute(new TCPFileServer(client));
				} catch (IOException e) {
					// TODO 自动生成的 catch 块
				}
				try {
					Thread.sleep(200);
				} catch (Exception e) {
				}
			}
		}
	}

	/**
	 * 
	 * @author tokysky (HIT-CS-ICES)
	 * @time 于2015年6月26日下午2:50:41
	 *
	 * @description 文件传输服务器子线程
	 *
	 */
	class TCPFileServer implements Runnable {

		private Socket clientSocket;
		private DataInputStream inputStream;
		private DataOutputStream outputStream;
		private int stage;
		private boolean runFlag;
		private String fp;
		byte[] buffer;
		File file;
		DataInputStream fileIn;

		public TCPFileServer(Socket socket) {
			this.clientSocket = socket;
			stage = 0;
			runFlag = true;
		}

		@Override
		public void run() {
			// TODO 自动生成的方法存根
			try {
				clientSocket.setSoTimeout(30 * 1000);// 30秒超时时间
				inputStream = new DataInputStream(clientSocket.getInputStream());
				outputStream = new DataOutputStream(clientSocket.getOutputStream());
				String message;
				int readLength;
				while (runFlag) {
					switch (stage) {
					case 0:
						message = Util.recv(inputStream);
						if (message == null) {
							break;
						}
						JSONObject json = new JSONObject(message);
						String hash = json.getString("hash");
						json = new JSONObject();
						if ((fp = getSeedAbsPath(hash)) != null) {
							json.put("ret", 200);
							Util.send(outputStream, json.toString());
							stage = 1;
						} else {
							json.put("ret", 400);
							Util.send(outputStream, json.toString());
							json = null;
							buffer = null;
							hash = null;
							runFlag = false;
						}
						break;
					case 1:
						message = Util.recv(inputStream);
						if (message == null) {
							break;
						}
						json = new JSONObject(message);
						int status = json.getInt("status");
						if (status == 200) {
							file = new File(fp);
							fileIn = new DataInputStream(new FileInputStream(file));
							buffer = new byte[1024 * 200];
							stage = 2;
						}
						break;
					case 2:// 正式传输文件
						readLength = fileIn.read(buffer, 0, buffer.length);
						if (readLength > 0) {
							outputStream.write(buffer, 0, readLength);
						} else {
							fileIn.close();
							buffer = null;
							runFlag = false;
						}
						break;
					default:
						break;
					}
					try {
						Thread.sleep(random.nextInt(4) * 10);
					} catch (Exception e) {
					}
				}

			} catch (IOException | JSONException e) {
			}
			try {
				inputStream.close();
				outputStream.close();
				clientSocket.close();
			} catch (IOException e) {
			}
		}

		public String getSeedAbsPath(String hash) {
			synchronized (fileObjects) {
				for (int i = 0; i < fileObjects.size(); ++i) {
					if (fileObjects.get(i).getFileHash().equals(hash)) {
						return fileObjects.get(i).getFilePath();
					}
				}
			}
			return null;
		}

	}

	/**
	 * 
	 * @author 	 tokysky (HIT-CS-ICES) 
	 * @time	  于2015年6月30日下午9:51:26
	 *
	 * @description 文件传输客户端子线程
	 *
	 */
	class TCPFileClient implements Runnable {

		private ResObject object;
		private Socket clientSocket;
		private DataInputStream inputStream;
		private DataOutputStream outputStream;
		private int stage;
		private boolean runFlag;
		private File file;
		private DataOutputStream fileOut;
		private byte[] buffer;
		private long totalRecvLength;

		public TCPFileClient(ResObject o) {
			// TODO 自动生成的构造函数存根
			this.object = o;
			stage = 0;
			totalRecvLength = 0;
			runFlag = true;
			synchronized ((Integer) curDloadSize) {
				if (curDloadSize == 0) {
					dTimer = new Timer(true);
					dTimer.schedule(new TimerTask() {
						@Override
						public void run() {
							// TODO 自动生成的方法存根
							fileUI.updateDload(dloadObjects);
							synchronized (dloadObjects) {
								for(int i=0;i< dloadObjects.size();++i){
									dloadObjects.get(i).setstSpeed(dloadObjects.get(i).getspSpeed());
								}
							}
							synchronized ((Integer) curDloadSize) {
								if (curDloadSize == 0) {
									dTimer.cancel();
								}
							}
						}
					}, 500, 500);
				}
				++curDloadSize;
			}
		}

		@Override
		public void run() {
			// TODO 自动生成的方法存根
			JSONObject json;
			String message;
			try {

				clientSocket = new Socket(object.getAddr(), object.getTcp());
				clientSocket.setSoTimeout(30 * 1000);
				inputStream = new DataInputStream(clientSocket.getInputStream());
				outputStream = new DataOutputStream(clientSocket.getOutputStream());

				while (runFlag) {

					switch (stage) {
					case 0:
						json = new JSONObject();
						json.put("hash", object.getHash());
						Util.send(outputStream, json.toString());
						message = Util.recv(inputStream);
						json = new JSONObject(message);
						int status = json.getInt("ret");
						if (status == 200) {
							json = new JSONObject();
							json.put("status", 200);
							Util.send(outputStream, json.toString());
							String dir = (new File(object.getPath())).getParent();
							file = File.createTempFile(object.getPath(), ".tmp",new File(dir));
							fileOut = new DataOutputStream(new FileOutputStream(file));
							buffer = new byte[1024 * 200];
							stage = 1;
							fileUI.updataAllDload(dloadObjects);//可以正常下载则刷新
						} else {
							//否则，移除当前下载资源
							synchronized (dloadObjects) {
								dloadObjects.remove(object.getSeedId());
							}
							JOptionPane.showMessageDialog(null, "文件种子不存在或已过期，请刷新种子后重新下载！", "提示", JOptionPane.OK_OPTION); 
							runFlag = false;
							try {
								inputStream.close();
								outputStream.close();
								clientSocket.close();
								synchronized ((Integer) curDloadSize) {
									--curDloadSize;
								}
							} catch (IOException e) {
								// TODO 自动生成的 catch 块
								e.printStackTrace();
							}
							return;
						}
						break;
					case 1:// 等待接收
						int recvLength = inputStream.read(buffer, 0,buffer.length);
						if (recvLength <= 0) {
							break;
						}
						fileOut.write(buffer, 0, recvLength);
						totalRecvLength += recvLength;
						int percent = (int) (100 * totalRecvLength / object.getSize());
						synchronized (dloadObjects) {
							dloadObjects.get(object.getSeedId()).setdPercent(percent);
							dloadObjects.get(object.getSeedId()).setspSpeed(totalRecvLength);
							if(percent == 100){
								dloadObjects.get(object.getSeedId()).setstSpeed(totalRecvLength);
							}
						}
						if (percent == 100) {
							// 接收完成
							fileOut.close();
							file.renameTo(new File(object.getPath()));
							buffer = null;
							json = null;
							runFlag = false;
						}
						break;
					}
					try {
						Thread.sleep(random.nextInt(4) * 10);
					} catch (InterruptedException e) {
						// TODO 自动生成的 catch 块
						e.printStackTrace();
					}
				}

			} catch (UnknownHostException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			} catch (IOException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
			try {
				inputStream.close();
				outputStream.close();
				clientSocket.close();
				synchronized ((Integer) curDloadSize) {
					--curDloadSize;
				}
			} catch (IOException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
		}

		public ResObject getObject() {
			return object;
		}

		public void setObject(ResObject object) {
			this.object = object;
		}
	}

	/**
	 * 
	 * @author tokysky (HIT-CS-ICES)
	 * @time 于2015年6月26日下午2:44:59
	 *
	 * @description 客户端与服务器交互子线程,客户端仅有一个
	 *
	 */
	class TCPClientTask implements Runnable {

		private String request;
		private Socket clientSocket;
		private boolean isTask;
		private BufferedInputStream inputStream;
		private BufferedOutputStream outputStream;

		public TCPClientTask() {
			isTask = false;
		}

		@Override
		public void run() {
			// TODO 自动生成的方法存根
			while (true) {
				synchronized (requestQueue) {
					if (requestQueue.size() > 0) {
						request = requestQueue.poll();
						isTask = true;
					}
				}
				if (isTask) {
					try {
						clientSocket = new Socket(P2PServerHost,
								P2PServerTcpPort);
						inputStream = new BufferedInputStream(
								clientSocket.getInputStream());
						outputStream = new BufferedOutputStream(
								clientSocket.getOutputStream());
						sendToServer(outputStream, request);
						String message = recvFromServer(inputStream);
						tcpRetMessageHandler(message);
						inputStream.close();
						outputStream.close();
						clientSocket.close();
					} catch (UnknownHostException e) {
						// TODO 自动生成的 catch 块
					} catch (IOException e) {
						// TODO 自动生成的 catch 块
					}
					isTask = false;
				}
				try {
					Thread.sleep(10 + random.nextInt(4) * 10);
				} catch (Exception e) {
				}
			}
		}

		/**
		 * 
		 * @description
		 * @param message
		 * @return String
		 *
		 */
		private void tcpRetMessageHandler(String recvMes) {
			char type = recvMes.charAt(0);
			String message = recvMes.substring(1);
			switch (type) {
			case P2PMessageType.MSG_FINDRES_T:
				findResRetHandler(message);
				break;
			}
		}

		/**
		 * 
		 * @description
		 * @param message
		 *
		 */
		private void findResRetHandler(String message) {
			synchronized (resObjects) {
				resObjects.clear();
			}
			JSONObject json;
			try {
				json = new JSONObject(message);
				int ret = json.getInt("ret");
				if (ret <= 0) {
					fileUI.updateResObject(resObjects);
					return;
				}
				JSONArray array = json.getJSONArray("sd");
				for (int i = 0; i < array.length(); ++i) {
					json = array.getJSONObject(i);
					// 屏蔽自己提供的种子
					// if(json.getString("ad").equals(NetIpAddress.getLocalIpv4Address())){
					// continue;
					// }
					ResObject object = new ResObject(json.getInt("sd"),
							json.getString("nm"), json.getLong("sz"),
							json.getString("tg"), json.getString("hh"),
							json.getInt("pr"));
					object.setOfferInfo(json.getInt("od"),
							json.getString("ad"), json.getInt("ud"),
							json.getInt("tc"));
					synchronized (resObjects) {
						resObjects.add(object);
					}
				}
				array = null;
				json = null;
			} catch (JSONException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
			fileUI.updateResObject(resObjects);
		}

		/**
		 * 
		 * @description 从服务器接收数据
		 * @param in
		 * @return String
		 * @throws IOException
		 *
		 */
		private String recvFromServer(BufferedInputStream in)
				throws IOException {
			ByteArrayOutputStream arrayBuffer;
			arrayBuffer = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int recvLen;
			while ((recvLen = in.read(buffer, 0, buffer.length)) != -1) {
				arrayBuffer.write(buffer, 0, recvLen);
			}
			return arrayBuffer.toString();
		}

		/**
		 * 
		 * @description 将数据发送给服务器
		 * @param out
		 * @param message
		 * @throws IOException
		 *             void
		 *
		 */
		private void sendToServer(BufferedOutputStream out, String message)
				throws IOException {
			StringBuilder sb = new StringBuilder();
			sb.append(new String(Util.IntToByte(request.length())));
			sb.append(request);
			out.write(sb.toString().getBytes(), 0,
					sb.toString().getBytes().length);
			out.flush();
		}
	}

	// ************************
	// 以下类为所有UI界面事件监听类
	/**
	 * 
	 * @author tokysky (HIT-CS-ICES)
	 * @time 于2015年4月29日下午6:55:14
	 *
	 * @description 登陆按钮监听事件
	 *
	 */
	class LoginButtonActionListner implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO 自动生成的方法存根
			String id = loginUI.getId();
			String pwd = loginUI.getPwd();
			if (id.isEmpty() || pwd.isEmpty()) {
				JOptionPane.showMessageDialog(null, "账号或密码不能为空，请确认后重试！", "错误",
						JOptionPane.OK_OPTION, null);
				return;
			}
			SelfUserId = Integer.valueOf(id);
			JSONObject json = new JSONObject();
			try {
				json.put("id", id);
				json.put("pwd", MD5.MD5(pwd));
				json.put("tcp", DefalutTcpPort);
			} catch (Exception e2) {
				// TODO: handle exception
			}
			StringBuilder strBuilder = new StringBuilder();
			strBuilder.append(P2PMessageType.MSG_LOGIN_T);
			strBuilder.append(json.toString());
			sendtoPacket(strBuilder.toString(), P2PServerAddr, P2PServerUdpPort);
			strBuilder = null;
			json = null;
			id = null;
			pwd = null;
			loginUI.waitlogin();
			logTimer = new Timer(true);
			logTimer.schedule(new TimerTask() {

				@Override
				public void run() {
					// TODO 自动生成的方法存根
					if (loginUI == null) {
						return;
					}
					loginUI.reLogin();
					JOptionPane.showMessageDialog(null, "未能成功登录，请检查您的网络后重试",
							"错误", JOptionPane.WARNING_MESSAGE, null);
				}
			}, 5 * 1000);
		}
	}

	class RegistButtonActionListner implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			// TODO 自动生成的方法存根
			if (!registUI.verifySCode()) {
				JOptionPane.showMessageDialog(null, "验证码错误！", "错误",
						JOptionPane.OK_OPTION);
				return;
			}
			String text = registUI.getRegistJsonObject();
			StringBuilder strBuilder = new StringBuilder();
			strBuilder.append(P2PMessageType.MSG_REGIST_T);
			strBuilder.append(text);
			registUI.processRegist();
			sendtoPacket(strBuilder.toString(), P2PServerAddr, P2PServerUdpPort);
			text = null;
			strBuilder = null;
			regTimer = new Timer(true);
			regTimer.schedule(new TimerTask() {

				@Override
				public void run() {
					// TODO 自动生成的方法存根
					registUI.overRegist();
					JOptionPane.showMessageDialog(null, "与服务器连接失败，请检查您的网络后重试",
							"错误", JOptionPane.WARNING_MESSAGE, null);
				}
			}, 5 * 1000);
		}

	}

	class SearchExtActListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO 自动生成的方法存根
			String name = ((JButton) e.getSource()).getName();
			switch (name) {
			case "search_s":
				String key = searchUI.getSearchKey();
				if (key.isEmpty()) {
					JOptionPane.showMessageDialog(null, "请输入查找用户的ID", "提示",
							JOptionPane.OK_OPTION, null);
					return;
				}
				JSONObject json = new JSONObject();
				try {
					json.put("id", key);
				} catch (Exception e2) {
					// TODO: handle exception
				}
				StringBuilder strBuilder = new StringBuilder();
				strBuilder.append(P2PMessageType.MSG_FINDFD_T);
				strBuilder.append(json.toString());
				sendtoPacket(strBuilder.toString(), P2PServerAddr,
						P2PServerUdpPort);
				break;
			case "search_a":
				int id = searchUI.getSelectID();
				if (id == SelfUserId) {
					JOptionPane.showMessageDialog(null, "提示：不能添加自己为好友！", "提示",
							JOptionPane.OK_OPTION);
					return;
				}
				for (int i = 0; i < friendList.size(); ++i) {
					if (id == friendList.get(i).getuId()) {
						JOptionPane.showMessageDialog(null, "提示：对方已经是您的好友！",
								"提示", JOptionPane.OK_OPTION);
						return;
					}
				}
				String confirm = JOptionPane.showInputDialog(null, "请输入验证信息",
						"提示", JOptionPane.OK_CANCEL_OPTION);
				if (confirm == null) {
					return;
				}
				// System.out.println();
				json = new JSONObject();
				try {
					json.put("uid", SelfUserId);
					json.put("fid", id);
					json.put("confm", confirm);
				} catch (Exception e2) {
					// TODO: handle exception
				}
				strBuilder = new StringBuilder();
				strBuilder.append(P2PMessageType.MSG_ADDFD_T);
				strBuilder.append(json.toString());
				sendtoPacket(strBuilder.toString(), P2PServerAddr,
						P2PServerUdpPort);
				break;
			default:
				break;
			}
			name = null;
		}

	}

	class SearchExtMouseListener implements MouseListener {

		public void mouseClicked(MouseEvent e) {
			// TODO 自动生成的方法存根
			String name = ((JLabel) e.getSource()).getName();
			switch (name) {
			case "main_s":
				if (!searchFlag) {
					searchUI = new SearchUI();
					searchUI.frame.addWindowListener(new WindowAdapter() {
						@Override
						public void windowClosing(WindowEvent e) {

							searchUI.frame.setVisible(false);
							searchFlag = false;
						}
					});
					searchUI.searchButton
							.addActionListener(new SearchExtActListener());
					searchUI.addButton
							.addActionListener(new SearchExtActListener());
					searchFlag = true;
				} else {
					searchUI.frame.setVisible(true);
					searchUI.frame.setExtendedState(JFrame.NORMAL);
				}
				break;
			case "main_f":
				if (seedFlag) {
					fileUI.frame.setVisible(true);
					fileUI.frame.setExtendedState(JFrame.NORMAL);
					// fileUI.updateFileObject(fileObjects);
				} else {
					fileUI = new P2PFileUI();
					fileUI.frame.addWindowListener(new WindowAdapter() {
						@Override
						public void windowClosing(WindowEvent e) {
							fileUI.frame.setVisible(false);
							if (fileUI.FileSeedWindowDisplay()) {
								fileUI.FileSeedUIWindowClose();
							}
						}
					});
					fileUI.registerButtonAcListner(new P2PFileExtActListener());
					fileUI.registerMesEventListener(new MessageListener() {

						@Override
						public void actionPerformed(MessageEvent m) {
							// TODO 自动生成的方法存根
							MessageObject object = (MessageObject) m
									.getMessage();
							switch (object.getType()) {
							case "dload":
								dLoadFile((Integer) object.getMessage());
								break;
							}
						}
					});
					seedFlag = true;
				}
				break;
			default:
				break;
			}
			name = null;
		}

		private boolean seedIsDload(int seedId) {
			for (int i = 0; i < dloadObjects.size(); ++i) {
				if (dloadObjects.get(i).getResObject().getSeedId() == seedId) {
					return true;
				}
			}
			return false;
		}

		private void dLoadFile(int resIndex) {

			if (dloadQueue.size() == 20) {
				JOptionPane.showMessageDialog(null, "当前排列队列已满，请稍后重试", "提示",
						JOptionPane.YES_OPTION);
				return;
			}

			if (seedIsDload(resObjects.get(resIndex).getSeedId())) {
				int ret = JOptionPane.showConfirmDialog(null,
						"该下载任务已在下载列表中，是够重 新下载？", "提示",
						JOptionPane.YES_NO_OPTION);
				if (ret == JOptionPane.NO_OPTION) {
					return;
				}
			}
			JFileChooser fc = new JFileChooser(".");
			while (true) {
				fc.setSelectedFile(new File(resObjects.get(resIndex).getName()));
				fc.showSaveDialog(null);
				String selectPath = fc.getSelectedFile().getAbsolutePath();
				File fp = new File(selectPath);
				boolean bk = false;
				int ret;
				if (fp.exists()) {
					ret = JOptionPane.showConfirmDialog(null,
							"文件" + fp.getName() + "已经存在，是否覆盖？", "提示",
							JOptionPane.YES_NO_CANCEL_OPTION);
					switch (ret) {
					case JOptionPane.YES_OPTION:
						bk = true;
						break;
					case JOptionPane.NO_OPTION:
						break;
					case JOptionPane.CANCEL_OPTION:
						return;
					default:
						break;
					}
				} else {
					bk = true;
				}
				if (bk) {
					break;
				}
			}
			// 添加下载任务
			ResObject object = resObjects.get(resIndex);
			synchronized ((Integer) curDloadSize) {
				object.setSeedId(curDloadSize);
			}
			object.setTag(null);
			object.setName(fc.getSelectedFile().getName());//dir
			object.setPath(fc.getSelectedFile().getAbsolutePath());//文件路径
			DloadObject dload = new DloadObject(object);
			synchronized (dloadObjects) {
				dloadObjects.add(dload);
			}
			dloadPool.execute(new TCPFileClient(object));
		}

		/**
		 * 
		 * @author tokysky (HIT-CS-ICES)
		 * @time 于2015年6月24日上午10:53:54
		 *
		 * @description 文件种子监听事件
		 *
		 */
		class P2PFileExtActListener implements ActionListener {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO 自动生成的方法存根
				String name = ((JButton) e.getSource()).getName();

				switch (name) {
				case "s_add":
					break;
				case "s_del":
					break;
				case "s_edit":
					break;
				case "s_search":
					String tag = fileUI.getSearchTag();
					searchRequest(fileUI.getSearchTag());
					break;
				case "f_sub":// 提交种子信息
					SeedObject object = fileUI.getFileObject();
					if (seedExist(object)) {
						JOptionPane.showMessageDialog(null,
								"文件" + object.getFileName() + "已经是种子，不能重复做种！",
								"错误", JOptionPane.OK_OPTION);
						return;
					}
					fileObjects.add(object);
					fileUI.FileSeedUIWindowClose();
					fileUI.updateFileObject(fileObjects);
					JSONObject json = new JSONObject();
					try {
						json.put("id", SelfUserId);
						json.put("name", object.getFileName());
						json.put("size", object.getFileLength());
						json.put("hash", object.getFileHash());
						json.put("price", object.getFilePrice());
						json.put(
								"tag",
								object.getFileLabel1() + " "
										+ object.getFileLabel2());
					} catch (Exception e2) {
						// TODO: handle exception
					}
					StringBuilder strBuilder = new StringBuilder();
					strBuilder.append(P2PMessageType.MSG_ADDRES_T);
					strBuilder.append(json.toString());
					sendtoPacket(strBuilder.toString(), P2PServerAddr,
							P2PServerUdpPort);
					break;
				default:
					break;
				}
			}

			private void searchRequest(String tag) {
				JSONObject json = new JSONObject();
				try {
					json.put("tag", tag);
				} catch (Exception e) {
					// TODO: handle exception
				}
				String request = P2PMessageType.MSG_FINDRES_T + json.toString();
				addClientTask(request);
			}

			public boolean seedExist(SeedObject o) {

				for (int i = 0; i < fileObjects.size(); ++i) {
					if (fileObjects.get(i).getFileHash()
							.equals(o.getFileHash())) {
						return true;
					}
				}
				return false;
			}
		}

		public void mouseEntered(MouseEvent e) {
			// TODO 自动生成的方法存根
			((JLabel) e.getSource()).setForeground(new Color(0, 0, 0));
		}

		public void mouseExited(MouseEvent e) {
			// TODO 自动生成的方法存根
			((JLabel) e.getSource()).setForeground(new Color(100, 100, 100));
		}

		public void mousePressed(MouseEvent e) {
			// TODO 自动生成的方法存根

		}

		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO 自动生成的方法存根
		}

	}

	public static void main(String[] args) {
		P2PClient p2pClient = new P2PClient();
	}
}
