package org.hit.toky.MessageEvent;

import java.util.EventListener;

/**
 * @author 	 tokysky (HIT-CS-ICES) 
 * @time	  于2015年6月25日上午11:01:08
 *
 * @description MessageEvent消息处理函数接口
 **/

public interface MessageListener extends EventListener{
	public void actionPerformed(MessageEvent m);
}
