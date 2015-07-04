package org.hit.toky.p2pclient;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

import org.apache.commons.codec.digest.DigestUtils;
import org.hit.toky.MessageEvent.MessageListener;
import org.hit.toky.MessageEvent.MessageSource;

/**
 * @author 	 tokysky (HIT-CS-ICES) 
 * @time	  于2015年6月22日下午2:59:31
 *
 * @description 添加文件种子界面
 **/

public class P2PFileSeedUI {
	
	JFrame frame;
	Container cp;
	JPanel jPanel;
	JLabel nameLabel;
	JLabel showNameLabel;
	JLabel sizeLabel;
	JLabel showSizeLabel;
	JLabel md5Label;
	JLabel showMd5Label;
	JLabel tag1Label;
	JTextField tag1Field;
	JLabel tag2Label;
	JTextField tag2Field;
	JLabel priceLabel;
	JTextField priceField;
	JLabel pricePicture;
	CButton loadCButton;
	CButton submitCButton;
	CButton cancalCButton;
	
	
	volatile SeedObject fileObject;
	Timer timer;
	private int counter;
	private int width = 400;
	private int height = 500;
	public P2PFileSeedUI(){
		
		
		frame = new JFrame();
		cp = frame.getContentPane();
		frame.setSize(width + 5,height + 28);
		ImageIcon background = new ImageIcon("image\\FileSeedUI.jpg");
        JLabel jl = new JLabel(background);
        jl.setBounds(0, 0, width, height);
        frame.getLayeredPane().add(jl, new Integer(Integer.MIN_VALUE));
        JPanel imagePanel = (JPanel) frame.getContentPane();
        imagePanel.setOpaque(false);
		jPanel = new JPanel();
		jPanel.setLayout(null);
		jPanel.setSize(width, height);
		jPanel.setOpaque(false);
		jPanel.setLocation(0, 0);
		cp.add(jPanel);
		nameLabel = new JLabel("文件名：");
		Util.setLabelProperty(jPanel,nameLabel,new Dimension(80, 30),
				new Point(10,90),new Font("楷体", Font.BOLD, 16),Color.BLUE,JLabel.CENTER);
		showNameLabel = new JLabel("");
		Util.setLabelProperty(jPanel,showNameLabel,new Dimension(250, 30),
				new Point(90,90),new Font("楷体", Font.BOLD, 16),Color.BLUE,JLabel.LEFT);
		sizeLabel =  new JLabel("大小：");
		Util.setLabelProperty(jPanel,sizeLabel,new Dimension(80, 30),
				new Point(10,130),new Font("楷体", Font.BOLD, 16),Color.BLUE,JLabel.CENTER);
		showSizeLabel = new JLabel("");
		Util.setLabelProperty(jPanel,showSizeLabel,new Dimension(250, 30),
				new Point(90,130),new Font("楷体", Font.BOLD, 16),Color.BLUE,JLabel.LEFT);
		md5Label = new JLabel("hash值：");
		Util.setLabelProperty(jPanel,md5Label,new Dimension(80, 30),
				new Point(10,170),new Font("楷体", Font.BOLD, 16),Color.BLUE,JLabel.CENTER);
		showMd5Label = new JLabel("");
		Util.setLabelProperty(jPanel,showMd5Label,new Dimension(300, 30),
				new Point(90,170),new Font("楷体", Font.BOLD, 16),Color.BLUE,JLabel.LEFT);
		tag1Label = new JLabel("标签1：");
		tag1Label.setToolTipText("文件标签 用于文件搜索的关键字 如\"社会计算\"（不超过15个字）");
		Util.setLabelProperty(jPanel,tag1Label,new Dimension(80, 30),
				new Point(10,210),new Font("楷体", Font.BOLD, 16),Color.BLUE,JLabel.CENTER);
		tag1Field = new JTextField();
		tag1Field.setFont(new Font("楷体", Font.BOLD, 16));
		tag1Field.setSize(250, 30);
		tag1Field.setLocation(90, 210);
		tag1Field.setDocument(new JTextFieldLimit(15));
		tag1Field.setEnabled(false);
		jPanel.add(tag1Field);
		tag2Label = new JLabel("标签2：");
		tag2Label.setToolTipText("文件标签 用于文件搜索的关键字 如\"社会计算\"（不超过15个字）");
		Util.setLabelProperty(jPanel,tag2Label,new Dimension(80, 30),
				new Point(10,250),new Font("楷体", Font.BOLD, 16),Color.BLUE,JLabel.CENTER);
		tag2Field = new JTextField(10);
		tag2Field.setFont(new Font("楷体", Font.BOLD, 16));
		tag2Field.setSize(250, 30);
		tag2Field.setLocation(90, 250);
		tag2Field.setDocument(new JTextFieldLimit(15));
		tag2Field.setEnabled(false);
		jPanel.add(tag2Field);
		priceLabel = new JLabel("价格：");
		priceLabel.setToolTipText("用户下载所需的铜币数（不得超过1000）");
		Util.setLabelProperty(jPanel,priceLabel,new Dimension(80, 30),
				new Point(10,290),new Font("楷体", Font.BOLD, 16),Color.BLUE,JLabel.CENTER);
		priceField = new JTextField();
		priceField.setFont(new Font("楷体", Font.BOLD, 16));
		priceField.setSize(150, 30);
		priceField.setLocation(90, 290);
		priceField.setDocument(new JTextFieldRegular("[0-9]*",1000));
		priceField.setEnabled(false);
		priceField.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO 自动生成的方法存根
				//System.out.println(priceField.getText());
				//按下Enter的事件
			}
		});
		jPanel.add(priceField);
		pricePicture = new JLabel(new ImageIcon("image\\wealth\\w_copper.png"));
		Util.setLabelProperty(jPanel,pricePicture,new Dimension(30, 30),
				new Point(250,290),new Font("楷体", Font.BOLD, 16),Color.BLUE,JLabel.CENTER);
		pricePicture.setToolTipText("铜币");
		loadCButton = new CButton();
		loadCButton.setSize(100, 30);
		loadCButton.setText("载入");
		loadCButton.setLocation(20, 350);
		loadCButton.setCButtonFont(new Font("楷体",Font.BOLD,15));
		loadCButton.setCButtonForeColor(Color.BLUE, Color.black);
		loadCButton.setName("f_load");
		loadCButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO 自动生成的方法存根
				JFileChooser fc = new JFileChooser(".");
				fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
				int ret = fc.showOpenDialog(null);
				if(ret == JFileChooser.APPROVE_OPTION){
					try {
						fileObject = new SeedObject(fc.getSelectedFile().getAbsolutePath());
						updateFileObject(fileObject);
						loadCButton.setEnabled(false);
						loadCButton.setToolTipText("正在计算文件hash值，不能再次载入");
						submitCButton.setEnabled(false);
						submitCButton.setToolTipText("正在计算文件hash，不能确定");
						Thread th = new Thread(new ThreadCalMd5Digist(fileObject.getFilePath()));
						th.start();
						counter = 0;
						timer = new Timer();
						timer.schedule(new TimerTask() {
							
							@Override
							public void run() {
								// TODO 自动生成的方法存根
								++counter;
								String mes = String.format("正在计算...（%d秒）", counter);
								showMd5Label.setText(mes);
							}
						}, 1000, 1000);
					} catch (Exception e2) {
						// TODO: handle exception
					}
				}
			}
		});
		jPanel.add(loadCButton);
		
		submitCButton = new CButton();
		submitCButton.setSize(100, 30);
		submitCButton.setText("确定");
		submitCButton.setLocation(130, 350);
		submitCButton.setCButtonFont(new Font("楷体",Font.BOLD,15));
		submitCButton.setCButtonForeColor(Color.BLUE, Color.black);
		submitCButton.setName("f_sub");
		submitCButton.setEnabled(false);
		jPanel.add(submitCButton);
		
		cancalCButton = new CButton();
		cancalCButton.setSize(100, 30);
		cancalCButton.setText("取消");
		cancalCButton.setLocation(240, 350);
		cancalCButton.setCButtonFont(new Font("楷体",Font.BOLD,15));
		cancalCButton.setCButtonForeColor(Color.BLUE, Color.black);
		cancalCButton.setName("f_can");
		jPanel.add(cancalCButton);
		frame.setTitle("添加种子");
		frame.setVisible(true);
		frame.setResizable(false);
		frame.setLocationRelativeTo(frame.getParent());
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
	}
	
	/**
	 * 
	 * @description 获取标签1、2和价格的数值
	 * @return FileObject
	 *
	 */
	public SeedObject getFileObject(){
		fileObject.setFileLabel1(tag1Field.getText());
		fileObject.setFileLabel2(tag2Field.getText());
		fileObject.setFilePrice(Integer.valueOf(priceField.getText()));
		//System.out.println(fileObject.getFileLabel1()+fileObject.getFileLabel2());
		return fileObject;
	}
	
	public void updateFileObject(SeedObject o){
		if(o == null){
			return;
		}
		showNameLabel.setText(o.getFileName());
		showSizeLabel.setText(Util.getFileLength(o.getFileLength()));
		showMd5Label.setText(o.getFileHash());
		priceField.setText(""+o.getFilePrice());
	}
	
	class ThreadCalMd5Digist implements Runnable{

		private String path;
		private String md5Digist;
		public ThreadCalMd5Digist(String path){
			this.path = path;
		}
		
		@Override
		public void run() {
			// TODO 自动生成的方法存根
			try {
				md5Digist = DigestUtils.md5Hex(new FileInputStream(path));
				fileObject.setFileHash(md5Digist);
				path = null;
				md5Digist = null;
				updateFileObject(fileObject);
				loadCButton.setEnabled(true);
				loadCButton.setToolTipText("载入本地文件");
				submitCButton.setEnabled(true);
				submitCButton.setToolTipText("确认提交种子信息");
				timer.cancel();
				submitCButton.setEnabled(true);
				tag1Field.setEnabled(true);
				tag2Field.setEnabled(true);
				priceField.setEnabled(true);
				tag1Field.grabFocus();
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	/**
	 * 
	 * @description 注册提交按钮监听器
	 * @param l 
	 *
	 */
	public void registerSubmitActListener(ActionListener l){
		if(l == null){
			return;
		}
		submitCButton.addActionListener(l);
	}
	
	/**
	 * 
	 * @description 注册取消按钮监听器
	 * @param l 
	 *
	 */
	public void registerCancelActListener(ActionListener l){
		if(l == null){
			return;
		}
		cancalCButton.addActionListener(l);
	}
	
	class JTextFieldLimit extends PlainDocument {
		private static final long serialVersionUID = 1323878669481639367L;
		private int limit;  //限制的长度  
	  
	    public JTextFieldLimit(int limit) {  
	        super(); //调用父类构造  
	        this.limit = limit;  
	    }  
	    public void insertString(int offset, String str, AttributeSet attr) throws BadLocationException {  
	        if(str == null) return;  
	        if((getLength() + str.length()) <= limit) {  
	            super.insertString(offset, str, attr);//调用父类方法  
	        }  
	    }  
	}

	/**
	 * 
	 * @author 	 tokysky (HIT-CS-ICES) 
	 * @time	  于2015年6月24日下午12:31:13
	 *
	 * @description 对JTextField输入进行过滤
	 *
	 */
	class JTextFieldRegular extends PlainDocument {
		
		private static final long serialVersionUID = -9016452566230851240L;
		private String regular;
		private int limit;

		public JTextFieldRegular(String s, int limit) {
			super(); // 调用父类构造
			this.regular = s;
			this.limit = limit;
		}

		public void insertString(int offset, String str, AttributeSet attr)
				throws BadLocationException {
			// if(str == null) return;
			if (Util.RegMatcher(regular, getText(0, getLength()) + str)) {
				if (Integer.valueOf(getText(0, getLength()) + str) > limit) {
					super.remove(0, getLength());
					super.insertString(0, "" + limit, attr);// 调用父类方法
				} else {
					super.insertString(offset, str, attr);// 调用父类方法
				}
			}
		}
	}
}
