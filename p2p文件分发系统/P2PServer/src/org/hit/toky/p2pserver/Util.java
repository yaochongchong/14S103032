package org.hit.toky.p2pserver;

/**
 * @author 	 tokysky (HIT-CS-ICES) 
 * @time	  于2015年6月25日下午8:31:23
 *
 * @description 
 **/

public class Util {
	
	public static byte[] IntToByte(int i){
		byte[] b = new byte[4];
		b[0] = (byte) (i& 0xff);// 最低位   
		b[1] = (byte) ((i >> 8) & 0xff);// 次低位   
		b[2] = (byte) ((i >> 16) & 0xff);// 次高位   
		b[3] = (byte) (i >>> 24);// 最高位,无符号右移。
		return b;
	}

	public static int ByteToInt(byte []b){
		return b[0] | (b[1] << 8) | (b[2] << 16) | (b[3] << 24);
	}
}
