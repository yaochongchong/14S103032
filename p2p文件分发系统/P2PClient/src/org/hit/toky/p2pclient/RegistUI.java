package org.hit.toky.p2pclient;

import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Pattern;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.json.JSONException;
import org.json.JSONObject;
import org.hit.toky.p2pclient.MD5;

/**
 * @author 	 tokysky (HIT-CS-ICES) 
 * @time	  于2015年5月24日下午8:04:06
 *
 * @description 注册的界面
 **/

public class RegistUI extends WindowAdapter{

	JFrame frame;
	Container cp;
	JPanel jPanel; 
	ImageIcon background;
	
	JLabel messageLabel;
	
	JLabel nameLabel;
	JTextField nameField;
	JLabel nameErrorLabel;
	
	JLabel pwdLabel;
	JPasswordField pwdField;
	JLabel pwdErrorLabel;
	
	JLabel rePwdLabel;
	JPasswordField rePwdField;
	JLabel rePwdErrorLabel;
	
	JLabel sexLabel;
	ButtonGroup sexGroup;
	JRadioButton secJRadio;
	JRadioButton manJRadio;
	JRadioButton womJRadio;
	
	JLabel scodeLabel;
	JTextField scodeField;
	JLabel scodePicLabel;
	JLabel scodeNextLabel;
	private int scodeIndex;
	private int sexFlag = -1;
	
	private final String[] verCodeArray = {"1354", "2233", "6889", "5566", 
			"1314", "1990", "9527", "2013", "6886", "6789"};
	private boolean [] regInforArray = {false,false,false};
	JButton regButton;
	
	Random random;
	private Timer timer;
	private int counter;
	public RegistUI() {
		// TODO 自动生成的构造函数存根
		
		random = new Random();
		frame = new JFrame();
		cp = frame.getContentPane();
		cp.setLayout(null);
		jPanel = new JPanel();
		String Path = "image\\reg.jpg";
		frame.setSize(500, 400);
		background = new ImageIcon(Path);
		JLabel jl = new JLabel(background);
		jl.setBounds(0, 0, 500, 400);
		frame.getLayeredPane().add(jl, new Integer(Integer.MIN_VALUE));
		JPanel imagePanel = (JPanel) frame.getContentPane();
		imagePanel.setOpaque(false);
		jPanel.setOpaque(false);
		jPanel.setLayout(null);
		jPanel.setSize(500, 400);
		jPanel.setLocation(0, 0);
		messageLabel = new JLabel();
		messageLabel.setSize(200, 30);
		messageLabel.setLocation(120, 0);
		messageLabel.setFont(new Font("宋体", Font.BOLD, 20));
		messageLabel.setForeground(Color.gray);
		messageLabel.setHorizontalAlignment(JLabel.LEFT);
		//messageLabel.setText("注册中，请稍等.");
		jPanel.add(messageLabel);
		
		nameLabel = new JLabel();
		nameLabel.setSize(100, 30);
		nameLabel.setLocation(20, 50);
		nameLabel.setFont(new Font("宋体", Font.BOLD, 20));
		nameLabel.setForeground(Color.RED);
		nameLabel.setHorizontalAlignment(JLabel.CENTER);
		nameLabel.setText("昵称:");
		jPanel.add(nameLabel);
		nameField = new JTextField();
		nameField.setSize(200, 30);
		nameField.setLocation(120, 50);
		nameField.setFont(new Font("宋体", Font.BOLD, 17));
		nameField.getDocument().addDocumentListener(new DocumentListener() {
			
			@Override
			public void removeUpdate(DocumentEvent e) {
				// TODO 自动生成的方法存根
				String name = nameField.getText();
				if(name.isEmpty()){
					setErrorMessage(nameErrorLabel,"昵称不能为空",Color.RED);
					regInforArray[0] = false;
					return;
				}
				if(name.length() <= 30){
					setErrorMessage(nameErrorLabel,"昵称符合要求",Color.GREEN);
					regInforArray[0] = true;
				}else{
					setErrorMessage(nameErrorLabel,"昵称不能长于30",Color.RED);
					regInforArray[0] = false;
				}
			}
			
			@Override
			public void insertUpdate(DocumentEvent e) {
				// TODO 自动生成的方法存根
				String name = nameField.getText();
				if(name.length() <= 30){
					setErrorMessage(nameErrorLabel,"昵称符合要求",Color.GREEN);
					regInforArray[0] = true;
				}else{
					setErrorMessage(nameErrorLabel,"昵称不能长于30",Color.RED);
					regInforArray[0] = false;
				}
			}
			
			@Override
			public void changedUpdate(DocumentEvent e) {
				// TODO 自动生成的方法存根
				
			}
		});
		jPanel.add(nameField);
		nameErrorLabel = new JLabel();
		nameErrorLabel.setSize(180, 30);
		nameErrorLabel.setLocation(330, 50);
		nameErrorLabel.setFont(new Font("宋体", Font.BOLD, 17));
		nameErrorLabel.setForeground(Color.RED);
		nameErrorLabel.setHorizontalAlignment(JLabel.LEFT);
		nameErrorLabel.setText("请输入昵称");
		jPanel.add(nameErrorLabel);
		
		pwdLabel = new JLabel();
		pwdLabel.setSize(100, 30);
		pwdLabel.setLocation(20, 100);
		pwdLabel.setFont(new Font("宋体", Font.BOLD, 20));
		pwdLabel.setForeground(Color.RED);
		pwdLabel.setHorizontalAlignment(JLabel.CENTER);
		pwdLabel.setText("密码:");
		jPanel.add(pwdLabel);
		pwdField = new JPasswordField();
		pwdField.setEchoChar('*');
		pwdField.setSize(200, 30);
		pwdField.setLocation(120, 100);
		pwdField.setFont(new Font("宋体", Font.BOLD, 17));
		pwdField.getDocument().addDocumentListener(new DocumentListener() {
			
			@Override
			public void removeUpdate(DocumentEvent e) {
				// TODO 自动生成的方法存根
				String pwd = new String(pwdField.getPassword());
				if(pwd.length() < 8){
					setErrorMessage(pwdErrorLabel, "密码长度不得少于8", Color.red);
					regInforArray[1] = false;
				}else{
					Pattern pattern = Pattern.compile("[a-zA-Z0-9]+");
					if(pattern.matcher(pwd).matches()){
						setErrorMessage(pwdErrorLabel, "密码符合要求", Color.green);
						regInforArray[1] = true;
					}else{
						setErrorMessage(pwdErrorLabel, "密码含非法字符", Color.red);
						regInforArray[1] = false;
					}
					pattern = null;
					pwd = null;
				}
			}
			
			@Override
			public void insertUpdate(DocumentEvent e) {
				// TODO 自动生成的方法存根
				String pwd = new String(pwdField.getPassword());
				if(pwd.length() < 8){
					setErrorMessage(pwdErrorLabel, "密码长度不得少于8", Color.red);
					regInforArray[1] = false;
				}else{
					Pattern pattern = Pattern.compile("[a-zA-Z0-9]+");
					if(pattern.matcher(pwd).matches()){
						setErrorMessage(pwdErrorLabel, "密码符合要求", Color.green);
						regInforArray[1] = true;
					}else{
						setErrorMessage(pwdErrorLabel, "密码含非法字符", Color.red);
						regInforArray[1] = false;
					}
					pattern = null;
					pwd = null;
				}
			}
			
			@Override
			public void changedUpdate(DocumentEvent e) {
				// TODO 自动生成的方法存根
				
			}
		});
		
		jPanel.add(pwdField);
		pwdErrorLabel = new JLabel();
		pwdErrorLabel.setSize(180, 30);
		pwdErrorLabel.setLocation(330, 100);
		pwdErrorLabel.setFont(new Font("宋体", Font.BOLD, 17));
		pwdErrorLabel.setForeground(Color.RED);
		pwdErrorLabel.setHorizontalAlignment(JLabel.LEFT);
		pwdErrorLabel.setText("请输入密码");
		pwdErrorLabel.setToolTipText("由8位及以上大小写字母和数字组成");
		jPanel.add(pwdErrorLabel);
		
		rePwdLabel = new JLabel();
		rePwdLabel.setSize(100, 30);
		rePwdLabel.setLocation(20, 150);
		rePwdLabel.setFont(new Font("宋体", Font.BOLD, 20));
		rePwdLabel.setForeground(Color.RED);
		rePwdLabel.setHorizontalAlignment(JLabel.CENTER);
		rePwdLabel.setText("确认密码:");
		jPanel.add(rePwdLabel);
		rePwdField = new JPasswordField();
		rePwdField.setSize(200, 30);
		rePwdField.setLocation(120, 150);
		rePwdField.setFont(new Font("宋体", Font.BOLD, 17));
		rePwdField.setEchoChar('*');
		rePwdField.getDocument().addDocumentListener(new DocumentListener() {

			@Override
			public void removeUpdate(DocumentEvent e) {
				// TODO 自动生成的方法存根
				String pwd = new String(pwdField.getPassword());
				String rePwd = new String(rePwdField.getPassword());
				if(pwd.equals(rePwd)){
					setErrorMessage(rePwdErrorLabel, "两次输入相同", Color.green);
					regInforArray[2] = true;
				}else{
					setErrorMessage(rePwdErrorLabel, "两次输入不相同", Color.red);
					regInforArray[2] = false;
				}
				pwd = null;
				rePwd = null;
			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				// TODO 自动生成的方法存根
				String pwd = new String(pwdField.getPassword());
				String rePwd = new String(rePwdField.getPassword());
				if(pwd.equals(rePwd)){
					setErrorMessage(rePwdErrorLabel, "两次输入相同", Color.green);
					regInforArray[2] = true;
				}else{
					setErrorMessage(rePwdErrorLabel, "两次输入不相同", Color.red);
					regInforArray[2] = false;
				}
				pwd = null;
				rePwd = null;
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				// TODO 自动生成的方法存根
				
			}
		});

		jPanel.add(rePwdField);
		rePwdErrorLabel = new JLabel();
		rePwdErrorLabel.setSize(180, 30);
		rePwdErrorLabel.setLocation(330, 150);
		rePwdErrorLabel.setFont(new Font("宋体", Font.BOLD, 17));
		rePwdErrorLabel.setForeground(Color.RED);
		rePwdErrorLabel.setHorizontalAlignment(JLabel.LEFT);
		rePwdErrorLabel.setText("请再次输入密码");
		jPanel.add(rePwdErrorLabel);
		
		sexLabel = new JLabel();
		sexLabel.setSize(100, 30);
		sexLabel.setLocation(20, 200);
		sexLabel.setFont(new Font("宋体", Font.BOLD, 20));
		sexLabel.setForeground(Color.RED);
		sexLabel.setHorizontalAlignment(JLabel.CENTER);
		sexLabel.setText("性别:");
		jPanel.add(sexLabel);
		
		secJRadio = new JRadioButton("保密",true);
		secJRadio.setSize(80, 30);
		secJRadio.setLocation(120, 200);
		secJRadio.setOpaque(false);
		secJRadio.setFont(new Font("宋体", Font.BOLD, 17));
		secJRadio.setForeground(Color.BLUE);
		secJRadio.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO 自动生成的方法存根
				sexFlag = -1;
			}
		});
		jPanel.add(secJRadio);
		
		manJRadio = new JRadioButton("男",false);
		manJRadio.setSize(50, 30);
		manJRadio.setLocation(200, 200);
		manJRadio.setOpaque(false);
		manJRadio.setFont(new Font("宋体", Font.BOLD, 17));
		manJRadio.setForeground(Color.BLUE);
		manJRadio.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO 自动生成的方法存根
				sexFlag = 1;
			}
		});
		jPanel.add(manJRadio);
		
		womJRadio = new JRadioButton("女",false);
		womJRadio.setSize(50, 30);
		womJRadio.setLocation(250, 200);
		womJRadio.setOpaque(false);
		womJRadio.setFont(new Font("宋体", Font.BOLD, 17));
		womJRadio.setForeground(Color.BLUE);
		womJRadio.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO 自动生成的方法存根
				sexFlag = 0;
			}
		});
		jPanel.add(womJRadio);
		
		sexGroup = new ButtonGroup();
		sexGroup.add(secJRadio);
		sexGroup.add(manJRadio);
		sexGroup.add(womJRadio);
		
		scodeLabel = new JLabel();
		scodeLabel.setSize(100, 30);
		scodeLabel.setLocation(20, 250);
		scodeLabel.setFont(new Font("宋体", Font.BOLD, 20));
		scodeLabel.setForeground(Color.RED);
		scodeLabel.setHorizontalAlignment(JLabel.CENTER);
		scodeLabel.setText("验证码:");
		jPanel.add(scodeLabel);
		scodeField = new JTextField();
		scodeField.setSize(95, 30);
		scodeField.setLocation(120, 250);
		scodeField.setFont(new Font("宋体", Font.BOLD, 17));
		jPanel.add(scodeField);
		scodePicLabel = new JLabel();
		scodePicLabel.setSize(100, 30);
		scodePicLabel.setLocation(220, 250);
		jPanel.add(scodePicLabel);
		scodeIndex = nextPicture();
		scodeNextLabel = new JLabel();     
		scodeNextLabel.setSize(80, 30);
		scodeNextLabel.setLocation(330, 250);
		scodeNextLabel.setFont(new Font("宋体", Font.BOLD, 17));
		scodeNextLabel.setForeground(Color.white);
		scodeNextLabel.setHorizontalAlignment(JLabel.LEFT);
		scodeNextLabel.setText("换一张");
		jPanel.add(scodeNextLabel);
		scodeNextLabel.addMouseListener(new MouseListener() {
			
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
				scodeNextLabel.setForeground(Color.WHITE);
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO 自动生成的方法存根
				scodeNextLabel.setForeground(Color.RED);
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO 自动生成的方法存根
				scodeIndex = nextPicture();
			}
		});
		regButton = new JButton();
		regButton.setSize(150, 50);
		regButton.setLocation(150, 300);
		regButton.setIcon(new ImageIcon("image\\regBut.jpg"));
		jPanel.add(regButton);
		
		cp.add(jPanel);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.setResizable(false);
		frame.setTitle("注册");
	}
	
	private void setErrorMessage(JLabel label,String text,Color c){
		if(label == null){
			return;
		}
		label.setForeground(c);
		label.setText(text);
	}
	
	private int nextPicture() {
		int index = random.nextInt(9);
		String path = "image\\verCode\\" + index + ".jpg";
		scodePicLabel.setIcon(new ImageIcon(path));
		return index;
	}
	
	public boolean verifySCode() {
		String scode = scodeField.getText();
		if(scode.equals(verCodeArray[scodeIndex])){
			return true;
		}
		return false;
	}
	
	public String getRegistJsonObject(){
		for(int i=0;i< regInforArray.length;++i){
			if(!regInforArray[i]){
				JOptionPane.showMessageDialog(null, "信息填写有误，请修改后重试！","错误",JOptionPane.OK_OPTION);
				return null;
			}
		}
		JSONObject json = new JSONObject();
		try {
			json.put("name", nameField.getText());
			json.put("pwd", MD5.MD5(new String(pwdField.getPassword())));
			json.put("sex", sexFlag);
			return json.toString();
		} catch (JSONException e) {
			// TODO 自动生成的 catch 块
			return null;
		}
	}
	
	public void processRegist(){
		counter = 0;
		timer = new Timer(true);
		timer.schedule(new TimerTask() {
			
			@Override
			public void run() {
				// TODO 自动生成的方法存根
				String text = null;
				switch(counter){
					case 0:
						text = "注册中，请稍等.";
						break;
					case 1:
						text = "注册中，请稍等..";
						break;
					case 2:
						text = "注册中，请稍等...";
						break;
				}
				messageLabel.setText(text);
				++counter;
				counter %= 3;
				text = null;
			}
		},0, 500);
		nameField.setEnabled(false);
		pwdField.setEnabled(false);
		rePwdField.setEnabled(false);
		regButton.setEnabled(false);
	}
	public void overRegist(){

		timer.cancel();
		messageLabel.setText(null);
		nameField.setEnabled(true);
		pwdField.setEnabled(true);
		pwdField.setText(null);
		rePwdField.setEnabled(true);
		rePwdField.setText(null);
		setErrorMessage(pwdErrorLabel, "请输入密码", Color.red);
		setErrorMessage(rePwdErrorLabel, "请再次输入密码", Color.red);
		regButton.setEnabled(true);
		scodeIndex = nextPicture();
	}
	
	public void display() {
		frame.setVisible(true);
		frame.setLocationRelativeTo(null);
	}
	public void dispose(){
		frame.dispose();
		frame = null;
		cp = null;
		jPanel = null;
		background = null;
	}
}
