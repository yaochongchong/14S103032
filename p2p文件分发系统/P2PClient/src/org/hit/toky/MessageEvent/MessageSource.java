package org.hit.toky.MessageEvent;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

/**
 * @author 	 tokysky (HIT-CS-ICES) 
 * @time	  于2015年6月25日上午11:09:22
 *
 * @description MessageEvent事件的事件源
 **/

public class MessageSource {
	Collection<MessageListener> listeners;
	/**
	 * 构造函数，初始化监听器对象容器
	 */
	public MessageSource(){
		listeners = new HashSet<MessageListener>();
	}
	/**
	 * 
	 * @description 添加监听器
	 * @param ml 
	 *
	 */
	public void addMessageListener(MessageListener ml){
		listeners.add(ml);
	}
	
	/**
	 * 
	 * @description 移除监听器
	 * @param ml 
	 *
	 */
	public void removeMessageListener(MessageListener ml){
		listeners.remove(ml);
	}
	
	/**
	 * 
	 * @description 移除全部监听器
	 *
	 */
	public void removeAll(){
		listeners.removeAll(listeners);
	}
	
	/**
	 * 
	 * @description 将事件e激活所有的监听器
	 * @param e 
	 *
	 */
	public void notify(MessageEvent e){
		Iterator<MessageListener> it = listeners.iterator();
		while(it.hasNext()){
			MessageListener ml = (MessageListener) it.next();
			ml.actionPerformed(e);
		}
	}
}
