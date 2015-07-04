package org.hit.toky.p2pserver;

/**
 * @author 	 tokysky (HIT-CS-ICES) 
 * @time	  于2015年6月24日下午3:50:36
 *
 * @description 
 **/

public class SeedObject {

	private int seedId;
	private int price;
	private String name;
	private String hash;
	private String tag;
	private long size;
	
	private int offerId;
	//private String addr;
	//private int tcp;
	//private int udp;
	
	public SeedObject(int seedId,int offerId,int price, String name,String hash,String tag,long size){
		this.seedId = seedId;
		this.price  = price;
		this.name = name;
		this.hash = hash;
		this.tag = tag;
		this.size = size;
		this.offerId = offerId;
	}
	
	public int getSeedId() {
		return seedId;
	}
	
	public void setSeedId(int seedId) {
		this.seedId = seedId;
	}

	public int getOfferId() {
		return offerId;
	}
	public void setOfferId(int offerId) {
		this.offerId = offerId;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getHash() {
		return hash;
	}
	public void setHash(String hash) {
		this.hash = hash;
	}
	public String getTag() {
		return tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
	}
	public long getSize() {
		return size;
	}
	public void setSize(long size) {
		this.size = size;
	}
}
