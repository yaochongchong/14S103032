package org.hit.toky.p2pclient;

/**
 * @author 	 tokysky (HIT-CS-ICES) 
 * @time	  于2015年6月20日下午2:48:10
 *
 * @description 财富
 **/

public class Wealth {
	
	public final int Copper_M 	= 1;
	public final int Silver_M	= 100;
	public final int Gold_M		= 10000;
	public final int Diamond_M	= 1000000;
	private int w_copper;
	private int w_sliver;
	private int w_gold;
	private int w_diamond;
	
	public Wealth(int wealth){
		w_diamond = wealth / Diamond_M;
		wealth %= Diamond_M;
		w_gold = wealth / Gold_M;
		wealth %= Gold_M;
		w_sliver = wealth / Silver_M;
		w_copper = wealth % Silver_M;
	}
	public int getWealth(Wealth w){
		int wealth;
		wealth = w.w_copper;
		wealth += w.w_sliver * Silver_M;
		wealth += w.w_gold * Gold_M;
		wealth += w.w_diamond * Diamond_M;
		return wealth;
	}
	public int getW_copper() {
		return w_copper;
	}
	public int getW_sliver() {
		return w_sliver;
	}
	public int getW_gold() {
		return w_gold;
	}
	public int getW_diamond() {
		return w_diamond;
	}
	
	public static void main(String[] args) {
		Wealth wealth = new Wealth(87765);
		
		System.out.println(wealth.w_diamond + "钻石 "
				+ wealth.w_gold + "金币  "
				+ wealth.w_sliver + "银元  "
				+ wealth.w_copper + "铜钱"
				);
		System.out.println(wealth.getWealth(wealth));
	}
}
