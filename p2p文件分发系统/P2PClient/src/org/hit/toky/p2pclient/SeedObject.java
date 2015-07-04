package org.hit.toky.p2pclient;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * @author 	 tokysky (HIT-CS-ICES) 
 * @time	  于2015年6月20日下午3:12:27
 *
 * @description 种子数据信息
 **/

public class SeedObject {
	
	private int fileId;			//做种成功之后，服务器返回的种子编号
	private String fileName;	//文件名
	private String filePath;	//绝对路径
	private long fileLength;	//文件长度(B)
	private String fileHash;	//文件hash值
	private int filePrice;		//下载价钱
	private int fileConn;		//当前连接数
	private int fileContri;		//文件贡献值
	private String fileLabel1;	//文件标签1
	private String fileLabel2;	//文件标签2
	public SeedObject(String path) throws IOException{
		File fp = new File(path);
		if(!fp.exists()||!fp.isFile()){
			throw new IOException("文件不存在或者文件路径不是文件");
		}
		fileName = fp.getName();
		filePath = fp.getAbsolutePath();
		fileHash = "正在计算中";
		fileLength = fp.length();
		filePrice = 0;
		fileConn = 0;
		fileContri = 0;
		fileId = 0;
	}
	
	public String getFileName() {
		return fileName;
	}

	public String getFilePath() {
		return filePath;
	}
	
	public synchronized void fileDLoadOver(){
		fileContri += filePrice;
	}
	
	public int getFileContri(){
		return fileContri;
	}

	public long getFileLength() {
		return fileLength;
	}

	public String getFileHash() {
		return fileHash;
	}

	public void setFileHash(String fileHash) {
		this.fileHash = fileHash;
	}

	public void fileConnect(){
		++fileConn;
	}
	
	public void fileDisconnect(){
		--fileConn;
	}
	
	public int getFileConn(){
		return fileConn;
	}
	
	public int getFilePrice() {
		return filePrice;
	}

	public void setFilePrice(int filePrice) {
		this.filePrice = filePrice;
	}

	public String getFileLabel1() {
		return fileLabel1 == null ? "":fileLabel1;
	}

	public void setFileLabel1(String fileLabel1) {
		this.fileLabel1 = fileLabel1;
	}
	
	public String getFileLabel2() {
		return fileLabel2 == null ? "":fileLabel2;
	}
	
	public void setFileLabel2(String fileLabel2) {
		this.fileLabel2 = fileLabel2;
	}

	public int getFileId() {
		return fileId;
	}

	public void setFileId(int fileId) {
		this.fileId = fileId;
	}
}
