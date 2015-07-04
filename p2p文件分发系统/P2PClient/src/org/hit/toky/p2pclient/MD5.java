package org.hit.toky.p2pclient;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author tokysky (HIT-CS-ICES)
 * @time 于2015年4月27日下午8:05:52
 *
 * @description MD5加密密码
 **/

public class MD5 {
	
	public static String MD5(String src) {
		String des;
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(src.getBytes());
			byte[] bt = md.digest();
			int i;
			StringBuffer buffer = new StringBuffer("");
			for (int offset = 0; offset < bt.length; offset++) {
				i = bt[offset];
				if (i < 0) {
					i += 256;
				}
				if (i < 16) {
					buffer.append("0");
				}
				buffer.append(Integer.toHexString(i));
			}
			des = buffer.toString();
		} catch (NoSuchAlgorithmException e) {
			return null;
		}
		return des;
	}

	public static String MD5_16(String src) {
		return MD5(src).substring(8, 24);
	}
	public static void main(String[] args) {
		System.out.println(MD5("123456"));
		System.out.println("e10adc3949ba59abbe56e057f20f883e");
	}
}
