package org.hit.toky.p2pclient;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.DefaultListModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;

import org.hit.toky.p2pclient.MainUI.ListItem;

/**
 * @author 	 tokysky (HIT-CS-ICES) 
 * @time	  于2015年6月11日下午8:45:15
 *
 * @description 主界面
 **/

public class MainUI{
	JFrame frame;
	Container cp;
	JPanel jPanel;
	JLabel faceLabel;
	JLabel sexLabel;
	JLabel nicknameLabel;
	JLabel friendLabel;
	JList<ListItem> friendList;
	JScrollPane friendJSP;
	ImageIcon background;
	Random random;
	JLabel searchLabel;
	JLabel fileLabel;
	public MainUI(String nickName){
		random = new Random();
		frame = new JFrame("P2P");
		cp = frame.getContentPane();
		frame.setSize(300, 600);
		background = new ImageIcon("image\\Main_JP.jpg");
		JLabel jl = new JLabel(background);
		jl.setBounds(0, 0, 300, 600);
		frame.getLayeredPane().add(jl, new Integer(Integer.MIN_VALUE));
		JPanel imagePanel = (JPanel) frame.getContentPane();
		imagePanel.setOpaque(false);
		jPanel = new JPanel();
		jPanel.setSize(300, 600);
		jPanel.setLocation(0, 0);
		jPanel.setLayout(null);
		jPanel.setOpaque(false);
	    cp.add(jPanel);
	    int index = random.nextInt(11) + 1;
	    faceLabel = new JLabel(new ImageIcon("image\\Face\\" + index + ".jpg"));
	    faceLabel.setSize(100, 100);
	    faceLabel.setLocation(0, 0);
	    sexLabel = new JLabel();
	    sexLabel.setSize(30, 30);
	    sexLabel.setLocation(100, 0);
	    sexLabel.setToolTipText("性别");
	     
	    nicknameLabel = new JLabel(nickName);
	    nicknameLabel.setSize(160, 30);
	    nicknameLabel.setLocation(100, 50);
	    nicknameLabel.setOpaque(false);
	    nicknameLabel.setFont(new Font("宋体", Font.BOLD, 16));
        nicknameLabel.setForeground(Color.black);
        nicknameLabel.setHorizontalAlignment(JLabel.CENTER);
        nicknameLabel.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
            }

            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            	nicknameLabel.setForeground(Color.WHITE);
            }

            @Override
            public void mouseExited(MouseEvent e) {
            	nicknameLabel.setForeground(Color.black);
            }
        });
        
        friendLabel = new JLabel();
        friendLabel.setSize(200, 30);
        friendLabel.setLocation(50, 130);
        friendLabel.setOpaque(false);
        friendLabel.setFont(new Font("宋体", Font.BOLD, 15));
        friendLabel.setForeground(Color.BLACK);
        friendLabel.setHorizontalAlignment(JLabel.CENTER);
        friendLabel.setText("我的好友 [0/0]");
        friendList = new JList<ListItem>();
        
        friendList.setCellRenderer(new ListView());
        friendJSP = new JScrollPane(friendList);
        friendJSP.setSize(280, 360);
        friendJSP.setLocation(10, 160);
        searchLabel = new JLabel(new ImageIcon("image\\Icon\\search.jpg"), JLabel.LEFT);
        searchLabel.setSize(60, 20);
        searchLabel.setText("查找");
        searchLabel.setToolTipText("查找添加好友");
        searchLabel.setHorizontalAlignment(JLabel.RIGHT);
        searchLabel.setLocation(70, 530);
        searchLabel.setName("main_s");
        searchLabel.setForeground(new Color(100,100,100));
        jPanel.add(searchLabel);
        fileLabel = new JLabel(new ImageIcon("image\\Icon\\file.jpg"), JLabel.LEFT);
        fileLabel.setSize(60, 20);
        fileLabel.setText("文件");
        fileLabel.setToolTipText("P2P文件共享");
        fileLabel.setHorizontalAlignment(JLabel.RIGHT);
        fileLabel.setLocation(140, 530);
        fileLabel.setForeground(new Color(100,100,100));
        fileLabel.setName("main_f");
        jPanel.add(fileLabel);
        jPanel.add(friendJSP);
        jPanel.add(friendLabel);
        jPanel.add(nicknameLabel);
	    jPanel.add(sexLabel);
	    jPanel.add(faceLabel);
	    frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				//退出
				System.exit(0);
			}
		});
	    frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
	
	public void UpdateFriendList(List<ListObject> listObjects){
		DefaultListModel<ListItem> model = new DefaultListModel<ListItem>();
		int counter = 0;
		for(int i=0;i<listObjects.size();++i){
			if(listObjects.get(i).status != UserStatusType.USER_OFFLINE){
				ListItem item = new ListItem(
						"image\\Icon\\FaceIcon\\" + listObjects.get(i).index+".bmp",
						listObjects.get(i).id,
						listObjects.get(i).status,
						listObjects.get(i).nickname,
						listObjects.get(i).nickname + UserStatusType.UserStatus(listObjects.get(i).status)
						);
				model.addElement(item);
				++counter;
			}
		}
		
		for(int i=0;i< listObjects.size();++i){
			if(listObjects.get(i).status == UserStatusType.USER_OFFLINE){
				ListItem item = new ListItem(
						"image\\Icon\\FaceIcon\\" + listObjects.get(i).index+".bmp",
						listObjects.get(i).id,
						listObjects.get(i).status,
						listObjects.get(i).nickname,
						listObjects.get(i).nickname + " [离线请留言]"
						);
				model.addElement(item);
			}
		}
		friendList.setModel(model);
		friendList.invalidate();
		String string = String.format("我的好友[%d/%d]", counter,listObjects.size());
		friendLabel.setText(string);
		string = null;
	}
	
	class ListView extends JLabel implements ListCellRenderer{
		private static final long serialVersionUID = 7647287622434112004L;
		private final Color HIGHLIGHT_COLOR = new Color(0, 0, 128);
		public ListView() {
			// TODO 自动生成的构造函数存根
			 setOpaque(true);
	         setIconTextGap(5);
		}
		
		@Override
		public Component getListCellRendererComponent(JList arg0, Object arg1,
				int arg2, boolean arg3, boolean arg4) {
			// TODO 自动生成的方法存根
			ListItem item = (ListItem)arg1;
			this.setIcon(item.getIcon());
			this.setText(item.getText());
			if(arg3){
				 setBackground(HIGHLIGHT_COLOR);
	             setForeground(Color.white);
			}else{
				 setBackground(Color.white);
	             setForeground(Color.black);
			}
			//focus
			return this;
		}
	}
	
	class ListItem{
		Icon icon;
		String text;
		String nickname;
		int id;
		int status;
		public ListItem(String iconPath,int id,int status,String nickname,String text){
			try {
				Image image = ImageIO.read(new File(iconPath));
				this.icon = new ImageIcon(image);
				this.id = id;
				this.status = status;
				this.nickname = nickname;
				this.text = text;
			} catch (IOException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
		}
		public Icon getIcon(){
			return this.icon;
		}
		public int getId(){
			return this.id;
		}
		public int getStatus(){
			return this.status;
		}
		public String getNickname(){
			return this.nickname;
		}
		public String getText(){
			return this.text;
		}
	}
	
	public static void main(String[] args) {
		MainUI mainUI = new MainUI("一二三四五");
		List<ListObject> listObjects = new ArrayList<ListObject>();
		for(int i=0;i< 10;i++){
			ListObject object = new ListObject(i, "小二" + i, i % 2,i % 4, i+1);
			listObjects.add(object);
		}
		mainUI.UpdateFriendList(listObjects);
	}
}
