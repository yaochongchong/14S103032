package org.hit.toky.p2pclient;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import org.hit.toky.MessageEvent.MessageEvent;
import org.hit.toky.MessageEvent.MessageListener;
import org.hit.toky.MessageEvent.MessageObject;
import org.hit.toky.MessageEvent.MessageSource;

/**
 * @author 	 tokysky (HIT-CS-ICES) 
 * @time	  于2015年6月20日上午10:38:11
 *
 * @description P2P文件分发界面
 **/

public class P2PFileUI {

	
	private final String[] heads = {"select","文件名","文件大小","标签","hash值","消耗财富","贡献值","连接数"};
	
	private final String[] resHeads = {"种子ID","文件名","文件大小","标签","hash值","消耗财富","下载"};
	private final String[] dloadHeads = {"种子ID","文件名","文件大小","消耗财富","下载进度","下载速度","操作"};
	JFrame frame;
	Container cp;
	JPanel jPanel;
	JTabbedPane tabPane;
	//种子面板
	JPanel seedPanel;
	JLabel seedLabel;
	JScrollPane seedJSP;
	JTable seedTable;
	DefaultTableModel seedModel;
	//下载管理
	JPanel loadPanel;
	JLabel tagLabel;
	JTextField tagField;
	CButton searchSeedButton;
	
	JLabel resLabel;
	JScrollPane resJSP;
	JTable	resTable;
	DefaultTableModel resModel;
	int resHoverRow = -1;
	
	JLabel dloadLabel;
	JScrollPane dloadJSP;
	JTable	dloadTable;
	DefaultTableModel dloadModel;
	
	//自定义消息源
	MessageSource mesSource;
	//
	CheckBoxRenderer BoxRenderer;
	CButton addSeedButton;
	CButton delSeedButton;
	CButton editSeedButton;
	P2PFileSeedUI fileSeedUI;
	boolean fileSeedUIFlag = false;
	ActionListener fileSeedAL;
	private int width = 1024;
	private int height = 768;
	public P2PFileUI() {
		
		mesSource = new MessageSource();
		// TODO 自动生成的构造函数存根
		frame = new JFrame("P2P文件客户端");
		cp = frame.getContentPane();
		frame.setSize(width, height);
		String Path = "image\\fileUI.jpg";
        ImageIcon background = new ImageIcon(Path);
        JLabel jl = new JLabel(background);
        jl.setBounds(0, 0, width, height);
        frame.getLayeredPane().add(jl, new Integer(Integer.MIN_VALUE));
        JPanel imagePanel = (JPanel) frame.getContentPane();
        imagePanel.setOpaque(false);
        cp.setLayout(null);
        jPanel = new JPanel();
        jPanel.setSize(width, height);
        jPanel.setLocation(0, 0);
        jPanel.setOpaque(false);
        jPanel.setLayout(null);
        cp.add(jPanel);
        
        tabPane = new JTabbedPane();
        tabPane.setSize(800, 600);
        tabPane.setLocation(10, 120);
        jPanel.add(tabPane);
        
        seedPanel = new JPanel();
        seedPanel.setSize(800, 470);
        seedPanel.setOpaque(false);
        seedPanel.setLayout(null);
        seedLabel = new JLabel("种子列表");
        Util.setLabelProperty(seedPanel,seedLabel,new Dimension(100, 30),
        		new Point(10,0),new Font("楷体", Font.BOLD, 15),Color.BLUE,JLabel.LEFT);
        
        seedModel = new DefaultTableModel(null,heads){
        	
			private static final long serialVersionUID = 7337231555548874794L;
			@Override
        	public Class getColumnClass(int c){
        		Object value = getValueAt(0, c);
        		if(value != null){
        			return value.getClass();
        		}else{
        			return super.getClass();
        		}
        	}
        };
        seedTable = new JTable(seedModel){
        	
			private static final long serialVersionUID = -5882894299766679748L;

			@Override
             public boolean isCellEditable(int rowIndex, int columnIndex) {
                 if (columnIndex == 0) {
                     return true;
                 }
                 return false;
             }
        };
        seedTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        seedTable.getTableHeader().setReorderingAllowed(false);
        seedTable.getTableHeader().addMouseListener(new MouseAdapter() {
        	
        	@Override
        	public void mouseClicked(MouseEvent e) {
        		
        		if(seedTable.getColumnModel().getColumnIndexAtX(e.getX()) == 0){
        			boolean b = !BoxRenderer.isSelected();
        			BoxRenderer.setSelected(b);
        			seedTable.getTableHeader().repaint();
        			for(int i = 0; i < seedTable.getRowCount(); i++){
        				seedTable.getModel().setValueAt(b, i, 0);
        			}
            		seedTable.repaint();
        		}
        		
        	}
		});
        seedTable.addMouseListener(new MouseAdapter() {
        	
        	@Override
        	public void mouseClicked(MouseEvent e) {
        		if(e.getClickCount() == 2){
        			int index = seedTable.getSelectedRow();
        			Boolean bool = (Boolean)seedModel.getValueAt(index, 0);
        			seedModel.setValueAt(!bool, index, 0);
        		}
        		seedTable.repaint();
        	}
		});
        seedTable.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        seedTable.setRowHeight(25);
        seedTable.setFont(new Font("黑体", Font.BOLD, 13));
        DefaultTableCellRenderer render = new DefaultTableCellRenderer();
        render.setHorizontalAlignment(JLabel.CENTER);
        
        BoxRenderer = new CheckBoxRenderer();
        seedTable.getColumn("select").setHeaderRenderer(BoxRenderer);
        seedTable.getColumn("select").setPreferredWidth(25);
        seedTable.getColumn("select").setMinWidth(25);
        seedTable.getColumn("select").setMaxWidth(25);
        seedTable.getColumn("文件名").setPreferredWidth(125);
        seedTable.getColumn("文件名").setCellRenderer(render);
        seedTable.getColumn("文件大小").setPreferredWidth(80);
        seedTable.getColumn("文件大小").setCellRenderer(render);
        seedTable.getColumn("hash值").setPreferredWidth(214);
        seedTable.getColumn("hash值").setCellRenderer(render);
        seedTable.getColumn("标签").setPreferredWidth(150);
        seedTable.getColumn("标签").setCellRenderer(render);
        seedTable.getColumn("消耗财富").setPreferredWidth(60);
        seedTable.getColumn("消耗财富").setCellRenderer(render);
        seedTable.getColumn("贡献值").setPreferredWidth(60);
        seedTable.getColumn("贡献值").setCellRenderer(render);
        seedTable.getColumn("连接数").setPreferredWidth(60);
        seedTable.getColumn("连接数").setCellRenderer(render);
        seedJSP = new JScrollPane(seedTable);
        seedJSP.setSize(777,400);
        seedJSP.setLocation(10, 30);
        seedPanel.add(seedJSP);
        
        addSeedButton = new CButton();
        addSeedButton.setSize(100, 30);
        addSeedButton.setText("添加种子");
        addSeedButton.setLocation(10, 450);
        addSeedButton.setCButtonFont(new Font("宋体",Font.BOLD,12));
        addSeedButton.setName("s_add");
        addSeedButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO 自动生成的方法存根
				if(!fileSeedUIFlag){
					fileSeedUI = new P2PFileSeedUI();
					fileSeedUI.frame.addWindowListener(new WindowAdapter() {
						@Override
						public void windowClosing(WindowEvent e) {
							int ret = JOptionPane.showConfirmDialog(null, "退出将丢失未保存的种子信息，是否继续？", "提示", JOptionPane.YES_NO_OPTION);
							if(ret == JOptionPane.YES_OPTION){
								FileSeedUIWindowClose();
							}
						}
					});
					fileSeedUI.registerSubmitActListener(fileSeedAL);
					fileSeedUI.registerCancelActListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							// TODO 自动生成的方法存根
							int ret = JOptionPane.showConfirmDialog(null, "取消将丢失未保存的种子信息，是否继续？", "提示", JOptionPane.YES_NO_OPTION);
							if(ret == JOptionPane.YES_OPTION){
								FileSeedUIWindowClose();
							}
						}
					});
					fileSeedUIFlag = true;
				}else{
					fileSeedUI.frame.setVisible(true);
					fileSeedUI.frame.setExtendedState(JFrame.NORMAL);
				}
			}
		});
        seedPanel.add(addSeedButton);
        
        delSeedButton = new CButton();
        delSeedButton.setSize(100, 30);
        delSeedButton.setText("删除种子");
        delSeedButton.setLocation(130, 450);
        delSeedButton.setCButtonFont(new Font("宋体",Font.BOLD,12));
        delSeedButton.setName("s_del");
        seedPanel.add(delSeedButton);
        
        editSeedButton = new CButton();
        editSeedButton.setSize(100, 30);
        editSeedButton.setText("编辑种子");
        editSeedButton.setLocation(250, 450);
        editSeedButton.setCButtonFont(new Font("宋体",Font.BOLD,12));
        editSeedButton.setName("s_edit");
        seedPanel.add(editSeedButton);
        
        loadPanel = new JPanel();
        loadPanel.setSize(600, 470);
        loadPanel.setLayout(null);
        loadPanel.setOpaque(false);
        
        tagLabel = new JLabel("搜索关键字(以空格隔开)");
        Util.setLabelProperty(loadPanel,tagLabel,new Dimension(200, 30),
        		new Point(10,0),new Font("楷体", Font.BOLD, 15),Color.BLUE,JLabel.LEFT);
        tagField = new JTextField();
        tagField.setSize(450, 30);
        tagField.setLocation(10, 30);
        tagField.setFont(new Font("宋体",Font.BOLD,14));
        loadPanel.add(tagField);
        
        searchSeedButton = new CButton();
        searchSeedButton.setSize(100, 30);
        searchSeedButton.setText("搜索");
        searchSeedButton.setLocation(480, 30);
        searchSeedButton.setName("s_search");
        searchSeedButton.setCButtonFont(new Font("楷体",Font.BOLD,15));
        loadPanel.add(searchSeedButton);
        
        resLabel = new JLabel("资源列表");
        Util.setLabelProperty(loadPanel,resLabel,new Dimension(100, 30),
        		new Point(10,60),new Font("楷体", Font.BOLD, 15),Color.BLUE,JLabel.LEFT);
		resModel = new DefaultTableModel(null, resHeads) {

			private static final long serialVersionUID = 2246755565698171584L;
			@Override
        	public Class getColumnClass(int c){
        		Object value = getValueAt(6, c);
        		if(value != null){
        			return value.getClass();
        		}else{
        			return super.getClass();
        		}
        	}
		};
        
        resTable = new JTable(resModel){
  
			private static final long serialVersionUID = 5632676208187281457L;

			@Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return false;
            }
        };
        resTable.addMouseListener(new MouseAdapter() {
        	@Override
        	public void mouseClicked(MouseEvent e) {
        		int row;
        		if(e.getClickCount() == 2){
        			row = resTable.rowAtPoint(e.getPoint());
        			int ret = JOptionPane.showConfirmDialog(null, "您将下载资源"+resModel.getValueAt(row, 1)+"，是否继续？", "提示", JOptionPane.YES_NO_OPTION);
        			if(ret == JOptionPane.YES_OPTION){
        				MessageObject object =  new MessageObject("dload", (Integer)row);
        				notifyMesEventListener(object);
        			}
        			return;
        		}
        		row = resTable.rowAtPoint(e.getPoint());
        		int col = resTable.columnAtPoint(e.getPoint());
        		if(col == 6){
        			int ret = JOptionPane.showConfirmDialog(null, "您将下载资源"+resModel.getValueAt(row, 1)+"，是否继续？", "提示", JOptionPane.YES_NO_OPTION);
        			if(ret == JOptionPane.YES_OPTION){
        				MessageObject object =  new MessageObject("dload", (Integer)row);
        				notifyMesEventListener(object);
        			}
        		}
        	}
		});
        
        resTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        resTable.getTableHeader().setReorderingAllowed(false);
        resTable.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        resTable.setFont(new Font("黑体", Font.BOLD, 13));
        resTable.setRowHeight(20);
        resTable.getColumn("种子ID").setPreferredWidth(60);
        resTable.getColumn("种子ID").setCellRenderer(render);
        resTable.getColumn("文件名").setPreferredWidth(150);
        resTable.getColumn("文件名").setCellRenderer(render);
        resTable.getColumn("文件大小").setPreferredWidth(80);
        resTable.getColumn("文件大小").setCellRenderer(render);
        resTable.getColumn("hash值").setPreferredWidth(228);
        resTable.getColumn("hash值").setCellRenderer(render);
        resTable.getColumn("标签").setPreferredWidth(150);
        resTable.getColumn("标签").setCellRenderer(render);
        resTable.getColumn("消耗财富").setPreferredWidth(60);
        resTable.getColumn("消耗财富").setCellRenderer(render);
        resTable.getColumn("下载").setPreferredWidth(30);
        resTable.getColumn("下载").setMinWidth(30);
        resTable.getColumn("下载").setMaxWidth(30);
        resTable.getColumn("下载").setCellRenderer(new DloadRender());
        resTable.addMouseMotionListener(new MouseMotionListener() {
			
			@Override
			public void mouseMoved(MouseEvent e) {
				// TODO 自动生成的方法存根
			}
			
			@Override
			public void mouseDragged(MouseEvent e) {
				// TODO 自动生成的方法存根
			}
		});
        resJSP = new JScrollPane(resTable);
        resJSP.setSize(777, 200);
        resJSP.setLocation(10, 90);
        loadPanel.add(resJSP);
        
        dloadLabel = new JLabel("下载列表");
        Util.setLabelProperty(loadPanel,dloadLabel,new Dimension(100, 30),
        		new Point(10,290),new Font("楷体", Font.BOLD, 15),Color.BLUE,JLabel.LEFT);
        
        dloadModel = new DefaultTableModel(null,dloadHeads){

			private static final long serialVersionUID = 630621864318123703L;
			@Override
        	public Class getColumnClass(int c){
        		Object value = getValueAt(6, c);
        		if(value != null){
        			return value.getClass();
        		}else{
        			return super.getClass();
        		}
        	}
			
        };
        
        dloadTable = new JTable(dloadModel){
			private static final long serialVersionUID = -4363062186431803703L;
			
			@Override
			public boolean isCellEditable(int row, int column) {
				// TODO 自动生成的方法存根
				return false;
			}
        };
        dloadTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        dloadTable.getTableHeader().setReorderingAllowed(false);
        dloadTable.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        dloadTable.setFont(new Font("黑体", Font.BOLD, 13));
        dloadTable.setRowHeight(20);
        dloadTable.getColumn("种子ID").setPreferredWidth(60);
        dloadTable.getColumn("种子ID").setCellRenderer(render);
        dloadTable.getColumn("文件名").setPreferredWidth(150);
        dloadTable.getColumn("文件名").setCellRenderer(render);
        dloadTable.getColumn("文件大小").setPreferredWidth(80);
        dloadTable.getColumn("文件大小").setCellRenderer(render);
        dloadTable.getColumn("消耗财富").setPreferredWidth(60);
        dloadTable.getColumn("消耗财富").setCellRenderer(render);
        dloadTable.getColumn("下载进度").setPreferredWidth(230);
        dloadTable.getColumn("下载进度").setCellRenderer(new TProgressBar());
        dloadTable.getColumn("下载速度").setPreferredWidth(80);
        dloadTable.getColumn("下载速度").setCellRenderer(render);
        dloadTable.getColumn("操作").setPreferredWidth(100);
        dloadTable.getColumn("操作").setCellRenderer(render);
        
        dloadJSP = new JScrollPane(dloadTable);
        dloadJSP.setSize(777, 240);
        dloadJSP.setLocation(10, 320);
        loadPanel.add(dloadJSP);
        
        tabPane.add("种子管理",seedPanel);
        tabPane.add("下载管理",loadPanel);
        
        frame.setVisible(true);
        frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
	}
	
	/**
	 * 
	 * @description 种子信息添加窗口关闭函数
	 *
	 */
	public void FileSeedUIWindowClose(){
		fileSeedUI.frame.dispose();
		fileSeedUIFlag = false;
	}
	
	/**
	 * 
	 * @description 文件种子窗体是否显示
	 * @return boolean
	 *
	 */
	public boolean FileSeedWindowDisplay(){
		return fileSeedUIFlag;
	}
	
	/**
	 * 
	 * @description 获取文件种子信息编辑页面的FileObject数据
	 * @return FileObject
	 *
	 */
	public SeedObject getFileObject(){
		return fileSeedUI.getFileObject();
	}
	
	/**
	 * 
	 * @description 更新当前资源列表
	 * @param seeds 
	 *
	 */
	public void updateResObject(List<ResObject> res){
		if(res == null){
			return;
		}
		resModel.setRowCount(0);
		for(int i=0;i<res.size();++i){
			Object [] datas = {
					res.get(i).getSeedId(),
					res.get(i).getName(),
					Util.getFileLength(res.get(i).getSize()),
					res.get(i).getTag(),
					res.get(i).getHash(),
					res.get(i).getPrice(),
					false
			};
			resModel.addRow(datas);
		}
		resTable.repaint();
	}
	
	/**
	 * 
	 * @description 仅刷新当前下载进度和速度
	 * @param dloads 
	 *
	 */
	public void updateDload(List<DloadObject> dloads){
		if(dloads == null){
			return;
		}
		for(int i= 0;i<dloads.size();++i){
			dloadModel.setValueAt(dloads.get(i).getdPercent(), i, 4);
			dloadModel.setValueAt(Util.getFileLength(dloads.get(i).getSpeed()) +"/s", i, 5);
		}
		dloadTable.repaint();
	}
	
	/**
	 * 
	 * @description 刷新当前表格全部数据
	 * @param dloads 
	 *
	 */
	public void updataAllDload(List<DloadObject> dloads){
		
		if(dloads == null){
			return;
		}
		dloadModel.setRowCount(0);
		for(int i=0;i<dloads.size();++i){
			Object [] datas = {
					dloads.get(i).getResObject().getSeedId(),
					dloads.get(i).getResObject().getName(),
					Util.getFileLength(dloads.get(i).getResObject().getSize()),
					dloads.get(i).getResObject().getPrice(),
					dloads.get(i).getdPercent(),
					Util.getFileLength(dloads.get(i).getSpeed()) +"/s",
					0
			};
			dloadModel.addRow(datas);
		}
		dloadTable.repaint();
	}
	
	/**
	 * 
	 * @description 更新当前表格中的FileObject数据
	 * @param files FileObject列表 
	 *
	 */
	public void updateFileObject(List<SeedObject> files){
		
		seedModel.setRowCount(0);
		BoxRenderer.setSelected(false);
		seedTable.getTableHeader().repaint();
		if(files == null){
			return;
		}
		for(int i=0;i< files.size();++i){
			Object [] data = {
					false,
					files.get(i).getFileName(),
					Util.getFileLength(files.get(i).getFileLength()),
					files.get(i).getFileLabel1() + " " + files.get(i).getFileLabel2(),
					files.get(i).getFileHash(),
					files.get(i).getFilePrice(),
					files.get(i).getFileContri(),
					files.get(i).getFileConn()
					};  
			seedModel.addRow(data);
		}
		seedTable.invalidate();
		seedTable.repaint();
	}
	
	/**
	 * 
	 * @return
	 */
	public String getSearchTag(){
		return tagField.getText();
	}
	
	/**
	 * 
	 * @description 激活监听器，传递消息
	 * @param o 消息
	 *
	 */
	public void notifyMesEventListener(MessageObject o){

		MessageEvent event = new MessageEvent(mesSource, o);
		mesSource.notify(event);
	}
	
	/**
	 * 
	 * @description 激活监听器，传递消息和真正的事件源
	 * @param notifySource 真正的事件源
	 * @param o 消息
	 *
	 */
	public void notifyMesEventListener(Object notifySource,MessageObject o){
		MessageEvent event = new MessageEvent(notifySource, o);
		mesSource.notify(event);
	}
	
	/**
	 * 
	 * @description 为自定义事件源注册监听器
	 * @param ml 
	 *
	 */
	public void registerMesEventListener(MessageListener ml){
		mesSource.addMessageListener(ml);
	}
	
	/**
	 * 
	 * @description		对删除、编辑种子按钮添加监听事件，同时作为种子添加页面确认的监听事件
	 * @param l			事件监听器
	 *
	 */
	public void registerButtonAcListner(ActionListener l){
		if(l == null){
			return;
		}
		delSeedButton.addActionListener(l);
		editSeedButton.addActionListener(l);
		searchSeedButton.addActionListener(l);
		fileSeedAL = l;
	}
	
	@SuppressWarnings("serial")
	class CheckBoxRenderer extends JCheckBox implements TableCellRenderer{

		public CheckBoxRenderer() {
			this.setBorderPainted(true);
	    }
		
		@Override
		public Component getTableCellRendererComponent(JTable arg0,
				Object arg1, boolean arg2, boolean arg3, int arg4, int arg5) {
			// TODO 自动生成的方法存根
			return this;
		}
	}
	
	
	class DloadRender extends JButton implements TableCellRenderer{

		private static final long serialVersionUID = -6465891579170385572L;
		private boolean hover;
		public DloadRender(){
			hover = false;
			setSize(20, 20);
			setIcon(new ImageIcon("image\\Icon\\dload.png"));
			setToolTipText("下载资源");
			addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO 自动生成的方法存根
					System.out.println("gh");
				}
			});
		}
		
		@Override
		public Component getTableCellRendererComponent(JTable arg0,
				Object arg1, boolean arg2, boolean arg3, int arg4, int arg5) {
			// TODO 自动生成的方法存根
			hover = (Boolean) arg1;
			if(hover){
				setIcon(new ImageIcon("image\\Icon\\dload_hover.png"));	
			}else{
				setIcon(new ImageIcon("image\\Icon\\dload.png"));
			}
			//repaint();
			return this;
		}
	}
	
	
	class TProgressBar extends JProgressBar implements TableCellRenderer{

		private static final long serialVersionUID = 6961017454851919368L;

		public TProgressBar(){
			super();
			setOpaque(true);
			setStringPainted(true);
			setMinimum(0);
			setMaximum(100);
			setBorderPainted(true);
			setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
		}
		@Override
		public Component getTableCellRendererComponent(JTable table,
				Object value, boolean isSelected, boolean hasFocus, int row,
				int column) {
			Integer i = (Integer) value;
			setValue(i);
			return this;
		}
	}

	class HandlerRender extends JPanel implements TableCellRenderer{

		private static final long serialVersionUID = 4591722336244853810L;

		@Override
		public Component getTableCellRendererComponent(JTable arg0,
				Object arg1, boolean arg2, boolean arg3, int arg4, int arg5) {
			// TODO 自动生成的方法存根
			return null;
		}
		
	}
	
	public static void main(String[] args) {
		P2PFileUI p2pFileUI = new P2PFileUI();
		List<SeedObject> objects = new ArrayList<SeedObject>();
		for(int i=1;i<= 5;i++){
			try {
				SeedObject o = new SeedObject("G:\\test\\f"+i+".txt");
				o.setFileHash("123123123aaaaaaaaaaaaaaaaaaaaaaa");
				o.setFileLabel1("科学计算 社会计算");
				objects.add(o);
			} catch (IOException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
		}
		p2pFileUI.updateFileObject(objects);
		List<ResObject> resObjects = new ArrayList<ResObject>();
		List<DloadObject> dloadObjects = new ArrayList<DloadObject>();
		for(int i=0;i< 5;i++){
			ResObject ro = new ResObject(i, "完全解读" +i, 1234, "", "hdiaueghieagfi", 12);
			resObjects.add(ro);
			DloadObject dlo = new DloadObject(ro);
			dlo.setdPercent(i * 10);
			dlo.setspSpeed(0);
			dlo.setstSpeed(10000);
			dloadObjects.add(dlo);
		}
		p2pFileUI.updateResObject(resObjects);
		p2pFileUI.updataAllDload(dloadObjects);
		p2pFileUI.registerMesEventListener(new MessageListener() {
			
			@Override
			public void actionPerformed(MessageEvent m) {
				// TODO 自动生成的方法存根
				//System.out.println(m.getMessage());
			}
		});
	}
}




