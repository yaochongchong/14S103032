package org.hit.toky.p2pclient;

/**
 * @author 	 tokysky (HIT-CS-ICES) 
 * @time	  于2015年6月24日下午12:39:08
 *
 * @description 下载的文件信息
 **/

public class DloadObject {

	private int dPercent;
	private long stSpeed;//
	private long spSpeed;//字节数之差计算下载速度
	private int status;
	private ResObject resObject;

	public DloadObject(ResObject res){
		this.resObject = res;
		dPercent = 0;
		stSpeed = 0L;
		spSpeed = 0L;
		status = DFileStatusType.DFILE_WAIT;
	}

	public int getdPercent() {
		return dPercent;
	}

	public void setdPercent(int dPercent) {
		this.dPercent = dPercent;
	}

	/**
	 * 
	 * @description 获得当前下载速度（统计频率为0.5秒）
	 * @return long
	 *
	 */
	public long getSpeed() {
		return (spSpeed - stSpeed) * 2;
	}

	public void setstSpeed(long speed) {
		stSpeed = speed;
	}

	public long getspSpeed(){
		return spSpeed;
	}
	
	public void setspSpeed(long speed){
		spSpeed = speed;
	}
	
	public ResObject getResObject() {
		return resObject;
	}

	public void setResObject(ResObject resObject) {
		this.resObject = resObject;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

}
