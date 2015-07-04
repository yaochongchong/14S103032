package org.hit.toky.p2pclient;

import java.awt.Color;
import java.awt.Container;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

/**
 * @author 	 tokysky (HIT-CS-ICES) 
 * @time	  于2015年6月12日下午6:06:23
 *
 * @description 查询界面
 **/

public class SearchUI {
	JFrame frame;
	Container cp;
	JPanel jPanel;
	JLabel searchLabel;
	JTextField searchField;
	JButton searchButton;
	JPanel searchPanel;
	JScrollPane tableJSP;
	JTable table;
	DefaultTableModel tableModel;
	Vector<String> tableColumns;
	JButton addButton;
	int selectIndex;
	public SearchUI(){
		frame = new JFrame();
		cp = frame.getContentPane();
		frame.setSize(450,350);
		JLabel jl = new JLabel(new ImageIcon("image\\search.jpg"));//
		jl.setBounds(0, 0, 450, 350);
        frame.getLayeredPane().add(jl, new Integer(Integer.MIN_VALUE));
        JPanel imagePanel = (JPanel) frame.getContentPane();
        imagePanel.setOpaque(false);
        jPanel = new JPanel();
        jPanel.setSize(450, 350);
        jPanel.setOpaque(false);
        jPanel.setLocation(0, 0);
        jPanel.setLayout(null);
        cp.add(jPanel);
        searchLabel = new JLabel("请输入要查找的用户ID：");
        searchLabel.setSize(200, 20);
        searchLabel.setLocation(20, 20);
        jPanel.add(searchLabel);
        searchField = new JTextField();
        searchField.setSize(300, 25);
        searchField.setLocation(20, 40);
        jPanel.add(searchField);
        searchButton = new JButton(new ImageIcon("image\\searchLabel.jpg"));
        searchButton.setSize(71, 23);
        searchButton.setLocation(340, 41);
        searchButton.setName("search_s");
        jPanel.add(searchButton);
        searchPanel = new JPanel();
        searchPanel.setSize(450,220);
        searchPanel.setLocation(0, 80);
        searchPanel.setLayout(null);
        searchPanel.setOpaque(false);
        jPanel.add(searchPanel);
        tableColumns = new Vector<String>();
        tableColumns.add("ID");
        tableColumns.add("昵称");
        tableColumns.add("性别");
        tableColumns.add("状态");
        tableColumns.add("签名");
        tableModel = new DefaultTableModel(null,tableColumns);
        tableModel.addRow(tableColumns);
        table = new JTable(tableModel){
			private static final long serialVersionUID = 4361546504880720762L;
			@Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return false;
            }
        };
        table.getTableHeader().setReorderingAllowed(false);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        //table;
        table.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        DefaultTableCellRenderer render = new DefaultTableCellRenderer();
        render.setHorizontalAlignment(JLabel.CENTER);
        table.getColumn("ID").setPreferredWidth(60);
        table.getColumn("ID").setCellRenderer(render);
        table.getColumn("昵称").setPreferredWidth(100);
        table.getColumn("昵称").setCellRenderer(render);
        table.getColumn("性别").setPreferredWidth(60);
        table.getColumn("性别").setCellRenderer(render);
        table.getColumn("状态").setPreferredWidth(60);
        table.getColumn("状态").setCellRenderer(render);
        table.getColumn("签名").setPreferredWidth(117);
        table.getColumn("签名").setCellRenderer(render);
        table.addMouseListener(new MouseAdapter() {
        	public void mouseReleased(MouseEvent e){
        		int i = table.getSelectedRow();
        		if(i >= 0){
        			selectIndex = i;
        			EnableAddButton();
        		}
        	}
		});
        tableJSP = new JScrollPane(table);
        tableJSP.setSize(400, 180);
        tableJSP.setLocation(20, 0);
        searchPanel.add(tableJSP);
        addButton = new JButton(new ImageIcon("image\\addFriend.jpg"));
        addButton.setSize(71, 23);
        addButton.setLocation(340, 190);
        addButton.setEnabled(false);
        addButton.setName("search_a");
        searchPanel.add(addButton);
        searchPanel.setVisible(false);
        frame.setResizable(false);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.setLocationRelativeTo(null);
	}
	public void updateTable(List<ListObject> list){
		tableModel.setRowCount(0);
		if(list == null){
			DisEnableAddButton();
			return;
		}
		if (list.isEmpty()) {
			DisEnableAddButton();
			return;
		}
		selectIndex = 0;
		searchPanel.setVisible(true);
		Vector<String> row;
		for(int i=0;i<list.size();++i){
			row = new Vector<String>();
			row.add(""+list.get(i).id);
			row.add(list.get(i).nickname);
			row.add(UserSexType.getSex(list.get(i).sex));
			row.add(UserStatusType.UserSearchStatus(list.get(i).status));
			row.add(list.get(i).descript);
			tableModel.addRow(row);
		}
		table.invalidate();
	}
	
	public String getSelect(){
		return (String)tableModel.getValueAt(selectIndex, 0);
	}
	public int getSelectID(){
		if(selectIndex == -1){
			return -1;
		}
		return Integer.valueOf((String)tableModel.getValueAt(selectIndex, 0));
	}
	public String getSearchKey(){
		return searchField.getText();
	}
	
	public void EnableAddButton() {
		addButton.setEnabled(true);
	}
	
	public void DisEnableAddButton() {
		addButton.setEnabled(false);
	}
	
	/*
	public static void main(String[] args) {
		List<ListObject> list = new ArrayList<ListObject>();
		for(int i = 0;i< 5;++i){
			list.add(new ListObject(i, "小二"+i, i % 2,i, 0));
		}
		SearchUI searchUI = new SearchUI();
		searchUI.updateTable(list);
	}
	*/
}
