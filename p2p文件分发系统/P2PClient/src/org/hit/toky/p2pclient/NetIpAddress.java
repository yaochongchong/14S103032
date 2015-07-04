package org.hit.toky.p2pclient;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.*;

/**
 * @author 	 tokysky (HIT-CS-ICES) 
 * @time	  于2015年4月26日下午4:05:56
 *
 * @description 
 **/

public class NetIpAddress {
	
	/**
	 * 
	 * @param 
	 * @return String
	 * @return 获取本地ipv4地址
	 */
	public static String getLocalIpv4Address() {
		try {
			Process pro = Runtime.getRuntime().exec("cmd /c ipconfig");
			InputStreamReader isr = new InputStreamReader(pro.getInputStream(),
					"gbk");
			BufferedReader br = new BufferedReader(isr);
			String str;
			String ipv4 = null;
			String gateway;
			while ((str = br.readLine()) != null) {
				if (str.startsWith("   IPv4")) {
					ipv4 = str.substring(2 + str.indexOf(": "));
					continue;
				}
				if (str.startsWith("   默认网关")) {
					gateway = str.substring(str.indexOf(": ") + 2);
					if (!gateway.isEmpty()) {
						return ipv4;
					}
					continue;
				}
			}
			br.close();
			isr.close();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
		return null;
	}
	
	/**
	 * 返回本地主机名称
	 * @return
	 * @throws UnknownHostException
	 */
	public static String getLocalHostName() throws UnknownHostException {
		return InetAddress.getLocalHost().getHostName();
	}
	
	
	/**
	 * 返回本地一个可用的ipv6地址
	 * @return
	 * @throws UnknownHostException
	 */
	public static String getLocalIpv6Address() throws UnknownHostException {
		InetAddress []inetAddress;
		inetAddress = InetAddress.getAllByName(getLocalHostName());
		for(int i=0;i<inetAddress.length;++i){
			if(inetAddress[i] instanceof Inet6Address ){
				return inetAddress[i].getHostAddress();
			}
		}
		return null;
	}
	
	public static void main(String[] args) {
		try {
			System.out.println(NetIpAddress.getLocalIpv4Address());
			System.out.println(NetIpAddress.getLocalIpv6Address());
		} catch (UnknownHostException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
	}
}
