package org.hit.toky.p2pclient;

import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.event.WindowAdapter;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

/**
 * @author 	 tokysky (HIT-CS-ICES) 
 * @time	  于2015年4月29日下午4:40:13
 *
 * @description 登陆界面
 **/

public class LoginUI extends WindowAdapter {
	JFrame frame;
	Container cp;
	JPanel jPanel;
	JLabel idLabel;
	private JTextField idField;
	JLabel pwdLabel;
	private JPasswordField pwdField;
	JButton loginButton;
	JLabel registLabel;
	ImageIcon background;
	public LoginUI(){
		frame = new JFrame();
        cp = frame.getContentPane();
        cp.setLayout(null);
        jPanel = new JPanel();
        String Path = "image\\login.jpg";
        frame.setSize(380, 300);
        background = new ImageIcon(Path);
        JLabel jl = new JLabel(background);
        jl.setBounds(0, 0, 380, 300);
        frame.getLayeredPane().add(jl, new Integer(Integer.MIN_VALUE));
        JPanel imagePanel = (JPanel) frame.getContentPane();
        imagePanel.setOpaque(false);
        jPanel.setLayout(null);
        idLabel = new JLabel();
        idLabel.setSize(100, 30);
        idLabel.setLocation(20, 100);
        idLabel.setFont(new Font("宋体", Font.BOLD, 20));
        idLabel.setForeground(Color.BLACK);
        idLabel.setHorizontalAlignment(JLabel.CENTER);
        idLabel.setText("账号 :");
        jPanel.add(idLabel);
        idField = new JTextField();
        idField.setSize(150, 30);
        idField.setLocation(120, 100);
        idField.setFont(new Font("宋体", Font.BOLD, 20));
        idField.setForeground(Color.BLACK);
        jPanel.add(idField);
        pwdLabel = new JLabel();
        pwdLabel.setSize(100, 30);
        pwdLabel.setLocation(20, 150);
        pwdLabel.setFont(new Font("宋体", Font.BOLD, 20));
        pwdLabel.setForeground(Color.black);
        pwdLabel.setHorizontalAlignment(JLabel.CENTER);
        pwdLabel.setText("密码 :");
        jPanel.add(pwdLabel);
        pwdField = new JPasswordField();
        pwdField.setSize(150, 30);
        pwdField.setLocation(120, 150);
        pwdField.setFont(new Font("宋体", Font.BOLD, 20));
        pwdField.setForeground(Color.black);
        pwdField.setEchoChar('*');
        jPanel.add(pwdField);
        registLabel = new JLabel();
        registLabel.setSize(80, 25);
        registLabel.setLocation(280, 105);
        registLabel.setFont(new Font("宋体", Font.BOLD, 15));
        registLabel.setHorizontalAlignment(JLabel.CENTER);
        registLabel.setForeground(Color.WHITE);
        registLabel.setText("注册账号");
        registLabel.setToolTipText("注册新的P2P账号");
        jPanel.add(registLabel);
        loginButton = new JButton();
        loginButton.setLocation(115, 210);
        loginButton.setSize(150, 30);
        loginButton.setBorderPainted(true);
        loginButton.setIcon(new ImageIcon("image\\login_button.jpg"));
        jPanel.add(loginButton);
        jPanel.setSize(380, 300);
        jPanel.setLocation(0, 0);
        jPanel.setOpaque(false);
        cp.add(jPanel);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.setResizable(false);
		frame.setTitle("登陆");
	}
	
	public String getId() {
		return idField.getText();
	}

	
	public void setId(String id) {
		this.idField.setText(id);
	}

	public String getPwd() {
		return new String(pwdField.getPassword());
	}


	public void display() {
		frame.setVisible(true);
		frame.setLocationRelativeTo(null);
	}
	
	public void display(int id) {
		idField.setText(String.valueOf(id));
		frame.setVisible(true);
		frame.setLocationRelativeTo(null);
		pwdField.grabFocus();
	}
	
	public void hide(){  
		frame.setVisible(false);
	}
	
	public void dispose(){
		frame.dispose();
		frame = null;
		cp = null;
		jPanel = null;
	}
	
	public void waitlogin(){
		jPanel.setVisible(false);
	}
	public void reLogin(){
		
		if(frame == null){
			return;
		}
		jPanel.setVisible(true);
	}
}
