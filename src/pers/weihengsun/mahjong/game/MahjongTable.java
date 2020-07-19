package pers.weihengsun.mahjong.game;

import java.io.*;
import java.util.*;

public class MahjongTable{
	// operation code
	private static final int MO = 10;
	private static final int FA = 11;
	private static final int MO_FROM_BOTTOM = 12;
	private static final int DA = 20;
	private static final int CHI = 30;
	private static final int PENG = 40;
	private static final int GANG = 50;
	private static final int HU = 0;
//	private static final int again = 60;
	// if hu or not
	private boolean hasWinner = false;
	
	/*
	牌用数字表示

	数字 {01 ~ 09} 表示  {1 ~ 9} 万

	数字 {11 ~ 19} 表示  {1 ~ 9} 条

	数字 {21 ~ 29} 表示  {1 ~ 9} 筒

	数字 {31 33 35 37 } 表示 { 东 南 西 北 }

	数字 {41 43 45} 表示 {中 發 白}
	*/
	public static String[] n2p = { // 编码表
		"无效","无效","无效","无效","无效","无效","无效","无效","无效","无效",
		"无效","一萬","二萬","三萬","四萬","五萬","六萬","七萬","八萬","九萬",
		"无效","一条","二条","三条","四条","五条","六条","七条","八条","九条",
		"无效","一筒","二筒","三筒","四筒","五筒","六筒","七筒","八筒","九筒",
		"无效","東风","无效","南风","无效","西风","无效","北风","无效","无效",
		"无效","红中","无效","發财","无效","白板"};

	int[] deck = null; //牌库：长度136(牌的总数，不含花牌)，内容是牌的数字
	int top; //牌库顶指针(角标)
	int bottom; //牌库底指针(角标)
	int zhuangNum; //庄家玩家编号
	// turn of player
	int turn;
	Hashtable<Integer, Integer> discardPile = null; //弃牌堆，key是牌的编号，value是已经出现的张数(包括打掉的，吃碰杠亮出来的)
	// writers
	PrintWriter[] writers;

	MahjongTable(PrintWriter[] writers){ //构造函数

		this.writers = writers;
		this.deck = new int[136]; //创建牌库对象
		this.top = 0;
		this.bottom = 135;
		this.zhuangNum = 0; //第一局“東”坐庄
		this.discardPile = new Hashtable<Integer,Integer>();
		this.turn = this.zhuangNum;
		//把136张牌按顺序放入牌库
		//一万到九万各四张
		int wan = 11;
		for(int i=0; i<9; i++){
			for(int j=0; j<4; j++){
				deck[top] = wan;
				top ++;
			}
			wan ++;
		}
		//一条到九条各四张
		int tiao = 21;
		for(int i=0; i<9; i++){
			for(int j=0; j<4; j++){
				deck[top] = tiao;
				top ++;
			}
			tiao ++;
		}
		//一筒到九筒各四张
		int tong = 31;
		for(int i=0; i<9; i++){
			for(int j=0; j<4; j++){
				deck[top] = tong;
				top ++;
			}
			tong ++;
		}
		//东南西北风各四张
		int feng = 41;
		for(int i=0; i<4; i++){
			for(int j=0; j<4; j++){
				deck[top] = feng;
				top ++;
			}
			feng += 2;
		}
		//中发白箭牌各四张
		int jian = 51;
		for(int i=0; i<3; i++){
			for(int j=0; j<4; j++){
				deck[top] = jian;
				top ++;
			}
			jian += 2;
		}
		//洗牌
		this.shuffle();
	}//构造函数结束

	public void shuffle(){ //成员函数，洗牌，重置牌库顶、底角标，重置弃牌堆
		System.out.println("正在洗牌");
		this.top = 0;
		this.bottom = 135;
		this.discardPile = new Hashtable<Integer,Integer>();
		int length = deck.length;
		Random rand = new Random();
		for(int i=length; i>0; i--){
			int randInd = rand.nextInt(i);
			swap(deck, randInd, i-1);
		}
	}

	private void swap(int[] arr, int i, int j){ //辅助函数，数组两元素换位
		int temp = arr[i];
		arr[i] = arr[j];
		arr[j] = temp;
	}

	// public boolean needHandle = false;
	// distribute pai to players
	public void distribute() {
		int round = 11;
		String cmd = "";
		while(round >= 0) {
			for(int i = 0; i < 4; i++) {
				if(deck[top] < 10) {
					cmd = turn + "" + FA + "0" + deck[top];
				}else {
					cmd = turn + "" + FA + "" + deck[top];
				}
				boradcast(cmd);
				top++;
			}
			turn++;
			if(turn >= 4) {
				turn = 0;
			}
			round--;
		}
		// tiao pai
		for(int i = 0; i < 4; i++) {
			if(deck[top] < 10) {
					cmd = turn + "" + FA + "0" + deck[top];
				}else {
					cmd = turn + "" + FA + "" + deck[top];
				}
			boradcast(cmd);
			top++;
			turn++;
			if(turn >= 4) {
				turn = 0;
			}
		}
		if(deck[top] < 10) {
			cmd = turn + "" + MO + "0" + deck[top];
		}else {
			cmd = turn + "" + MO + "" + deck[top];
		}

		//
		//等一会儿，让之前的-1都到达
		// System.out.println("开始等");
		// try{
		// 	Thread.sleep(10000);
		// }catch(InterruptedException e) {
		// 	e.printStackTrace();
		// }
		
		// System.out.println("结束等");
		// needHandle = true;
		//前13张发送完毕,马上发庄家的第14张
		boradcast(cmd);
		top++;
		
		System.out.println("结束发牌");
	}
	// draw pai for player
	public void draw() {
		String cmd = "";
		if(deck[top] < 10) {
			cmd = turn + "" + MO + "0" + deck[top];
		}else {
			cmd = turn + "" + MO + "" + deck[top];
		}
		top++;
		// boradcast(turn + "" + mo);
		boradcast(cmd);
	}

	String[] cmdMey = new String[4];
	String daMey = null;
	boolean isGang = false;
	// command handler
	public void cmdHandler(String cmd) {
		// if(!needHandle) {
		// 	return;
		// }
		// user id.
		int playerId = Integer.parseInt(cmd.substring(0, 1));
		// operation id.
		int operationId = Integer.parseInt(cmd.substring(1, 3));
		if(operationId >= 0) {
			int extraCode = operationId % 10;
			operationId -= extraCode;
		}
		switch(operationId) {
			case DA:
				turn--;
				if(turn == -1) {
					turn = 3;
				}
				daMey = cmd;
				cmdMey[playerId] = cmd;
				break;
			case CHI:
				cmdMey[playerId] = cmd;
				break;
			case PENG:
				cmdMey[playerId] = cmd;
				break;
			case GANG:
				cmdMey[playerId] = cmd;
				isGang = true;
				break;
			case HU:
				hasWinner = true;
				cmdMey[playerId] = cmd;
				break;
			case -1:
				cmdMey[playerId] = cmd;
				System.out.println("收到了 " + playerId);
				break;
			case -2:
				System.exit(0);
				break;
			default:
				break;
		}

		if(confirmed()) {
			if(allNeg()) {		// if all negative
				if(hasWinner) {
					System.out.println("准备新一局");
					hasWinner = false;
					// needHandle = false;
					top = 0;
					bottom = 135;
					zhuangNum++;
					if(zhuangNum >= 4) {
						zhuangNum = 0;
					}
					turn = zhuangNum;
					shuffle();
					distribute();
				}else {
					if(isGang) {
						isGang = false;
						String pCmd = "";
						if(deck[bottom] < 10) {
							pCmd = turn + "" + MO_FROM_BOTTOM + "0" + deck[bottom];
						}else {
							pCmd = turn + "" + MO_FROM_BOTTOM + "" + deck[bottom];
						}
						bottom--;
						boradcast(pCmd);
					}else {
						draw();
					}
				}
			}else {		// if not all negative
				String tempCmd = "";
				boolean isHu = false;
				String huCmd = "";
				for(int i = 0, j=0; j < 4; i++,j++) {
					if(i == 4) {
						i = 0;
					}
					String s = cmdMey[i];
					int tempOpe = Integer.parseInt(s.substring(1, 3));
					if(tempOpe >= 0) {
						int tempCode = tempOpe % 10;
						tempOpe -= tempCode;
					}
					if(tempOpe == DA) {
						tempCmd = s;
					}
					if(tempOpe == HU) {
						isHu = true;
						huCmd = s;
					}
					if(tempOpe == GANG || tempOpe == PENG) {
						tempCmd = s;
						turn = Integer.parseInt(s.substring(0, 1));
					}
					if(tempOpe == CHI && tempCmd.equals("")) {
						tempCmd = s;
						turn = Integer.parseInt(s.substring(0, 1));
					}
				}
				if(isHu) {
					tempCmd = huCmd;
				}
				boradcast(tempCmd);
			}


			// if(!tempCmd.equals("")) {
			// 	boradcast(tempCmd);

			// }else {
			// }
		}
		// }else if(daMey == null) {
		// 	for(int i = 0; i < 4; i++) {
		// 		if(cmdMey[i] != null) {
		// 			// System.out.println("yeah ya: " + cmdMey[i]);
		// 			if(Integer.parseInt(cmdMey[i].substring(1, 3)) == gang) {
		// 				String ttt = cmdMey[i];
		// 				for(int k = 0; k < 4; k++) {
		// 					cmdMey[k] = null;
		// 				}
		// 				boradcast(ttt);
		// 				String pCmd = "";
		// 				if(deck[bottom] < 10) {
		// 					pCmd = turn + "" + gmo + "0" + deck[bottom];
		// 				}else {
		// 					pCmd = turn + "" + gmo + "" + deck[bottom];
		// 				}
		// 				bottom--;
		// 				boradcast(pCmd);
		// 			}else if(Integer.parseInt(cmdMey[i].substring(1, 3)) == hu) {
		// 				String ttt = cmdMey[i];
		// 				for(int k = 0; k < 4; k++) {
		// 					cmdMey[k] = null;
		// 				}
		// 				boradcast(ttt);
		// 			}
		// 		}
		// 	}
		// }
		
	}

	//check if cmdMey is empyt or not
	private boolean confirmed() {
		for(int i = 0; i < 4; i++) {
			if(cmdMey[i] == null) {
				return false;
			}
		}
		return true;
	}

	// check if all -1
	private boolean allNeg() {
		for(int i = 0; i < 4; i++) {
			if(Integer.parseInt(cmdMey[i].substring(1, 3)) != -1) {
				return false;
			}
		}
		return true;
	}

	// boradcast
	public void boradcast(String cmd) {
		for(int i = 0; i < 4; i++) {
			cmdMey[i] = null;
		}
		System.out.println("server: " + cmd);
		for(int i = 0; i < 4; i++) {
			writers[i].println(cmd);
			writers[i].flush();
		}
	}

	// single cast
	public void singlecast(String cmd, int playerId) {
		writers[playerId].println(cmd);
		writers[playerId].flush();
	}

	// public static void main(String[] args) {
	// 	MahjongTable test = new MahjongTable(null);
	// 	test.cmdHandler("0-110");
	// }
}