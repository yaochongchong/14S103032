package org.hit.toky.p2pclient;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;

import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * @author tokysky (HIT-CS-ICES)
 * @time 于2015年6月20日下午12:37:30
 *
 * @description 工具类
 **/

public class Util {

	public final static long KB = 1024; // 1024B
	public final static long MB = 1048576; // 1024K
	public final static long GB = 1073741824; // 1024M
	public final static long TB = 1099511627776L; // 1024G

	/**
	 * 
	 * @description 获取文件的大小
	 * @param path
	 *            文件路径
	 * @return long 返回文件的字节数
	 *
	 */
	public static long getFileLength(String path) {
		File fp = new File(path);
		if (fp.exists() && fp.isFile()) {
			return fp.length();
		}
		return -1;
	}

	/**
	 * 
	 * @description 获取指定字节数的字符串描述
	 * @param length
	 *            文件字节数
	 * @return String 字符串描述
	 *
	 */
	public static String getFileLength(long length) {

		if (length < KB) {
			return length + "B";
		} else if (length < MB) {
			float value = (float) (1.0 * length / KB);
			return String.format("%.2fKB", value);
		} else if (length < GB) {
			float value = (float) (1.0 * length / MB);
			return String.format("%.2fMB", value);
		}
		float value = (float) (1.0 * length / GB);
		return String.format("%.2fGB", value);
	}

	/**
	 * 
	 * @description 设置JLabel的属性
	 * @param parent
	 *            父控件
	 * @param comp
	 *            子控件
	 * @param d
	 *            大小
	 * @param p
	 *            位置
	 * @param font
	 *            字体
	 * @param c
	 *            颜色
	 *
	 */
	public static void setLabelProperty(JPanel parent, JLabel comp,
			Dimension d, Point p, Font font, Color c,int alignment) {
		if (comp == null || parent == null) {
			return;
		}
		comp.setSize(d);
		comp.setLocation(p);
		comp.setFont(font);
		comp.setForeground(c);
		comp.setHorizontalAlignment(alignment);
		parent.add(comp);
	}
	
	
	/**
	 * 
	 * @description
	 * @param reg
	 * 
	 * @param src
	 * @return boolean
	 *
	 */
	public static boolean RegMatcher(String reg,String src){
		return src.matches(reg);
	}
	
	/**
	 * 
	 * @description
	 * @param i
	 * @return byte[]
	 *
	 */
	public static byte[] IntToByte(int i){
		byte[] b = new byte[4];
		b[0] = (byte) (i& 0xff);// 最低位   
		b[1] = (byte) ((i >> 8) & 0xff);// 次低位   
		b[2] = (byte) ((i >> 16) & 0xff);// 次高位   
		b[3] = (byte) (i >>> 24);// 最高位,无符号右移。
		return b;
	}
	
	/**
	 * 
	 * @description
	 * @param b
	 * @return int
	 *
	 */
	public static int ByteToInt(byte []b){
		return b[0] | (b[1] << 8) | (b[2] << 16) | (b[3] << 24);
	}
	
	/**
	 * 
	 * @description
	 * @param in
	 * @return String
	 * @throws IOException 
	 *
	 */
	public static String recv(DataInputStream in) throws IOException{
		return recv(in,4 * 1024);
	}
	
	/**
	 * 
	 * @description
	 * @param in
	 * @param maxLength
	 * @return String
	 * @throws IOException 
	 *
	 */
	public static String recv(DataInputStream in,int maxLength) throws IOException{
		byte[] buffer = new byte[maxLength];
		int recvLength = in.read(buffer,0, buffer.length);
		if(recvLength <= 0){
			return null;
		}
		byte[] data = new byte[recvLength];
		System.arraycopy(buffer, 0, data, 0, recvLength);
		buffer = null;
		return new String(data);
 	}
	
	/**
	 * 
	 * @description
	 * @param out
	 * @param message
	 * @throws IOException 
	 *
	 */
	public static void send(DataOutputStream out,String message) throws IOException{
		out.write(message.getBytes(), 0, message.getBytes().length);
		out.flush();
	}
}
