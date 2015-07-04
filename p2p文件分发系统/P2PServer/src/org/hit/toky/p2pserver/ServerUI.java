package org.hit.toky.p2pserver;

import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

/**
 * @author tokysky (HIT-CS-ICES)
 * @time 于2015年4月27日下午8:44:36
 *
 * @description
 **/

public class ServerUI {
	
	JFrame frame = new JFrame();
	Container cp = frame.getContentPane();
	JPanel jPanel = new JPanel();
	JScrollPane userJSP;
	JTable userTab;
	DefaultTableModel defaultTableModel;
	Vector<String> tableColumnNames;
	JScrollPane messageJSP;
	JTextArea messageTextArea;
	JPanel descripPanel;
	JLabel registerLabel;
	JLabel registerNumLabel;
	JLabel onlineLabel;
	JLabel onlineNumLabel;
	JLabel resourseLabel;
	JLabel resourseNumLabel;
	private final int WIDTH = 1000;
	private final int HEIGHT = 800;
	private int totalMesLength = 0;
	final Color bgColor = new Color(193, 210, 240);
	public ServerUI() {
		jPanel.setLayout(null);
		tableColumnNames = new Vector<String>();
		tableColumnNames.add("id");
		tableColumnNames.add("昵称");
		tableColumnNames.add("性别");
		tableColumnNames.add("状态");
		tableColumnNames.add("Ip地址");
		tableColumnNames.add("聊天端口");
		tableColumnNames.add("传输端口");
		defaultTableModel = new DefaultTableModel(null, tableColumnNames);
		userTab = new JTable(defaultTableModel);
		userTab.getTableHeader().setResizingAllowed(false);
		userTab.getTableHeader().setReorderingAllowed(false);
		userTab.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		DefaultTableCellRenderer render = new DefaultTableCellRenderer();
		render.setHorizontalAlignment(JLabel.CENTER);
		userTab.getColumn("id").setPreferredWidth(80);
		userTab.getColumn("id").setCellRenderer(render);
		userTab.getColumn("昵称").setPreferredWidth(190);
		userTab.getColumn("昵称").setCellRenderer(render);
		userTab.getColumn("性别").setPreferredWidth(80);
		userTab.getColumn("性别").setCellRenderer(render);
		userTab.getColumn("状态").setPreferredWidth(80);
		userTab.getColumn("状态").setCellRenderer(render);
		userTab.getColumn("Ip地址").setPreferredWidth(190);
		userTab.getColumn("Ip地址").setCellRenderer(render);
		userTab.getColumn("聊天端口").setPreferredWidth(80);
		userTab.getColumn("聊天端口").setCellRenderer(render);
		userTab.getColumn("传输端口").setPreferredWidth(80);
		userTab.getColumn("传输端口").setCellRenderer(render);
		userJSP = new JScrollPane(userTab);
		userJSP.setSize(800,400);
		userJSP.setLocation(5, 5);
		jPanel.add(userJSP);
		messageTextArea = new JTextArea();
		messageTextArea.setLineWrap(true);
		messageTextArea.setEditable(false);
		messageJSP = new JScrollPane(messageTextArea);
		messageJSP.setSize(800, 360);
		messageJSP.setLocation(5, 410);
		jPanel.add(messageJSP);
		descripPanel = new JPanel();
		
		registerLabel = new JLabel("注册总人数：");
		registerLabel.setSize(100, 20);
		registerLabel.setLocation(0, 0);
		registerLabel.setHorizontalAlignment(JLabel.LEFT);
		registerLabel.setFont(new Font("楷体", Font.BOLD, 15));
		registerLabel.setForeground(Color.BLUE);
		descripPanel.add(registerLabel);
		registerNumLabel = new JLabel();
		registerNumLabel.setSize(80, 20);
		registerNumLabel.setLocation(100, 0);
		registerNumLabel.setHorizontalAlignment(JLabel.LEFT);
		registerNumLabel.setFont(new Font("楷体", Font.BOLD, 15));
		registerNumLabel.setForeground(Color.BLUE);
		descripPanel.add(registerNumLabel);
		
		onlineLabel = new JLabel("在线总人数：");
		onlineLabel.setSize(100, 20);
		onlineLabel.setLocation(0, 20);
		onlineLabel.setHorizontalAlignment(JLabel.LEFT);
		onlineLabel.setFont(new Font("楷体", Font.BOLD, 15));
		onlineLabel.setForeground(Color.BLUE);
		descripPanel.add(onlineLabel);
		onlineNumLabel = new JLabel("0");
		onlineNumLabel.setSize(80, 20);
		onlineNumLabel.setLocation(100, 20);
		onlineNumLabel.setHorizontalAlignment(JLabel.LEFT);
		onlineNumLabel.setFont(new Font("楷体", Font.BOLD, 15));
		onlineNumLabel.setForeground(Color.BLUE);
		descripPanel.add(onlineNumLabel);
		
		resourseLabel = new JLabel("资源总数：");
		resourseLabel.setSize(100, 20);
		resourseLabel.setLocation(0, 40);
		resourseLabel.setHorizontalAlignment(JLabel.LEFT);
		resourseLabel.setFont(new Font("楷体", Font.BOLD, 15));
		resourseLabel.setForeground(Color.BLUE);
		descripPanel.add(resourseLabel);
		resourseNumLabel = new JLabel("0");
		resourseNumLabel.setSize(80, 20);
		resourseNumLabel.setLocation(100, 40);
		resourseNumLabel.setHorizontalAlignment(JLabel.LEFT);
		resourseNumLabel.setFont(new Font("楷体", Font.BOLD, 15));
		resourseNumLabel.setForeground(Color.BLUE);
		descripPanel.add(resourseNumLabel);
		
		descripPanel.setLayout(null);
		descripPanel.setSize(180, 300);
		descripPanel.setLocation(810, 5);
		descripPanel.setOpaque(false);
		jPanel.add(descripPanel);
		cp.setLayout(null);
		jPanel.setSize(WIDTH - 5, HEIGHT - 10);
		jPanel.setLocation(0, 0);
		jPanel.setBackground(bgColor);
		cp.add(jPanel);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				// ExitSystem();
				System.exit(0);
			}
		});
		frame.setSize(WIDTH, HEIGHT);
		frame.setResizable(false);
		frame.setTitle("P2P服务端");
		frame.setVisible(true);
		frame.setLocationRelativeTo(null);
	}

	public void updateRegistUserNum(int count) {
		registerNumLabel.setText("" + count);
	}

	public void updateOnlineUserNum(int count) {
		onlineNumLabel.setText("" + count);
	}
	public synchronized void appendMessage(String message){
		messageTextArea.append("1>" + message + "\r\n");
		totalMesLength += message.length() + 4;
		if(totalMesLength > 1 * 1024){
			StringBuilder sBuilder = new StringBuilder(messageTextArea.getText());
			int index = sBuilder.indexOf("1>", 512);
			totalMesLength = index;
			messageTextArea.setText(sBuilder.substring(index));
			sBuilder = null;
		}
		messageTextArea.setCaretPosition(messageTextArea.getText().length());
		
	} 
	
	public static void main(String[] args) {
		ServerUI serverUI = new ServerUI();
	}
}
