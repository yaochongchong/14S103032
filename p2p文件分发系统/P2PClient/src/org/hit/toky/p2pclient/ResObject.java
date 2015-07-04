package org.hit.toky.p2pclient;

import java.io.File;

/**
 * @author 	 tokysky (HIT-CS-ICES) 
 * @time	  于2015年6月24日下午8:30:46
 *
 * @description 资源对象
 **/

public class ResObject {
	private int seedId;
	private String name;
	private long size;
	private String tag;
	private String hash;
	private int price;
	private String path;
	//提供者信息
	private int offerId;
	private String addr;
	private int udp;
	private int tcp;
	
	public ResObject(int seedId,String name,long size,String tag,String hash,int price){
		this.seedId = seedId;
		this.name = name;
		this.size = size;
		this.tag = tag;
		this.hash = hash;
		this.price = price;
	}
	
	public void setOfferInfo(int offerId,String addr,int udp,int tcp){
		this.offerId = offerId;
		this.addr = addr;
		this.udp = udp;
		this.tcp = tcp;
	}
	
	public int getSeedId() {
		return seedId;
	}
	public void setSeedId(int seedId) {
		this.seedId = seedId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public long getSize() {
		return size;
	}
	public void setSize(long size) {
		this.size = size;
	}
	public String getTag() {
		return tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
	}
	public String getHash() {
		return hash;
	}
	public void setHash(String hash) {
		this.hash = hash;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	public int getOfferId() {
		return offerId;
	}
	public void setOfferId(int offerId) {
		this.offerId = offerId;
	}
	public String getAddr() {
		return addr;
	}
	public void setAddr(String addr) {
		this.addr = addr;
	}
	public int getUdp() {
		return udp;
	}
	public void setUdp(int udp) {
		this.udp = udp;
	}
	public int getTcp() {
		return tcp;
	}
	public void setTcp(int tcp) {
		this.tcp = tcp;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
}
