package pers.weihengsun.mahjong.game;

import java.io.*;
import java.util.*;

public class MahjongTable{
	
	private boolean hasWinner = false; //当前局是否有玩家胡牌

	private int[] deck = null; //牌库：长度144，内容是牌的编号
	private int top; //牌库顶指针(角标)
	private int bottom; //牌库底指针(角标)
	private String[] cmdMey = new String[4];
	private String daMey = null;
	private boolean isGang = false;
	public int whoIsZhuang; //庄家玩家编号
	public int whoseTurn; // turn of player, 当前轮次玩家编号
	/*
	 * 花牌堆
	 * 每位玩家都拥有一个花牌堆，里面存放着花牌的编号
	 */
	public List<Integer>[] huaPiles = null;
	/*
	 * 副露堆
	 * 每位玩家都拥有一个副露堆，里面存放着副露对象
	 */
	public List<Fulu>[] fuluPiles = null;
	/**
	 * 弃牌堆
	 * 每位玩家自己面前拥有弃牌堆
	 * 按时间顺序记录打出的牌
	 * 但是被吃碰杠拿走的牌进入他人的副露而非弃牌堆
	 */
	public List<Integer>[] discardPiles = null; 
	// writers
	public PrintWriter[] writers;

	MahjongTable(PrintWriter[] writers){ //构造函数

		this.writers = writers;
		this.deck = new int[Constant.RL_NUM_OF_TILES]; //创建牌库对象
		this.top = 0;
		this.bottom = Constant.RL_NUM_OF_TILES - 1;
		this.whoIsZhuang = 0; //第一局“東”坐庄
		this.whoseTurn = this.whoIsZhuang;
		deckInit();
		//洗牌
		this.shuffle();
	}//构造函数结束

	/**
	 * 洗牌
	 * 重置牌库顶、底角标
	 * 重置弃牌堆、副露堆、花牌堆
	 */
	public void shuffle(){ //成员函数，洗牌，重置牌库顶、底角标，重置弃牌堆
		System.out.println("正在洗牌");
		this.top = 0;
		this.bottom = Constant.RL_NUM_OF_TILES -1;
		discardPilesInit();
		fuluPilesInit();
		huaPilesInit();
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
	
	// distribute tile to players, 发牌函数
	public void distribute() {
		//先发 NUM_OF_HANDS-1 张牌，最后一轮次为跳牌轮次（庄两张，他人一张）
		int rounds = Constant.RL_NUM_OF_HANDS -1;
		String cmd = "";
		while(rounds > 0) {
			for(int i = 0; i < 4; i++) {
				cmd = whoseTurn + "" + Constant.OC_FA + "" + deck[top];
				boradcast(cmd);
				top++;
			}
			whoseTurn++;
			if(whoseTurn >= 4) {
				whoseTurn = 0;
			}
			rounds--;
		}
		// tiao pai
		for(int i = 0; i < 4; i++) {
			if(deck[top] < 10) {
					cmd = whoseTurn + "" + Constant.OC_FA + "0" + deck[top];
				}else {
					cmd = whoseTurn + "" + Constant.OC_FA + "" + deck[top];
				}
			boradcast(cmd);
			top++;
			whoseTurn++;
			if(whoseTurn >= 4) {
				whoseTurn = 0;
			}
		}
		if(deck[top] < 10) {
			cmd = whoseTurn + "" + Constant.OC_MO + "0" + deck[top];
		}else {
			cmd = whoseTurn + "" + Constant.OC_MO + "" + deck[top];
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
			cmd = whoseTurn + "" + Constant.OC_MO + "0" + deck[top];
		}else {
			cmd = whoseTurn + "" + Constant.OC_MO + "" + deck[top];
		}
		top++;
		// boradcast(turn + "" + mo);
		boradcast(cmd);
	}

	// command handler
	/**
	 * 命令处理器：
	 * 收到某玩家发来的一条指令后，解析指令，分析合理性，改变牌桌状态，将信息广播
	 * @param cmd
	 */
	public void cmdHandler(String cmd) {
		// if(!needHandle) {
		// 	return;
		// }
		
		/**
		 * 命令格式：userId-operationCode-
		 */
		// user id.
		int playerId = Integer.parseInt(cmd.substring(0, 1));
		// operation id.
		int operationId = Integer.parseInt(cmd.substring(1, 3));
		if(operationId >= 0) {
			int extraCode = operationId % 10;
			operationId -= extraCode;
		}
		switch(operationId) {
			case Constant.OC_DA:
				whoseTurn--;
				if(whoseTurn == -1) {
					whoseTurn = 3;
				}
				daMey = cmd;
				cmdMey[playerId] = cmd;
				break;
			case Constant.OC_CHI:
				cmdMey[playerId] = cmd;
				break;
			case Constant.OC_PENG:
				cmdMey[playerId] = cmd;
				break;
			case Constant.OC_GANG:
				cmdMey[playerId] = cmd;
				isGang = true;
				break;
			case Constant.OC_HU:
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
					whoIsZhuang++;
					if(whoIsZhuang >= 4) {
						whoIsZhuang = 0;
					}
					whoseTurn = whoIsZhuang;
					shuffle();
					distribute();
				}else {
					if(isGang) {
						isGang = false;
						String pCmd = "";
						if(deck[bottom] < 10) {
							pCmd = whoseTurn + "" + Constant.OC_MO_FROM_BOTTOM + "0" + deck[bottom];
						}else {
							pCmd = whoseTurn + "" + Constant.OC_MO_FROM_BOTTOM + "" + deck[bottom];
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
					if(tempOpe == Constant.OC_DA) {
						tempCmd = s;
					}
					if(tempOpe == Constant.OC_HU) {
						isHu = true;
						huCmd = s;
					}
					if(tempOpe == Constant.OC_GANG || tempOpe == Constant.OC_PENG) {
						tempCmd = s;
						whoseTurn = Integer.parseInt(s.substring(0, 1));
					}
					if(tempOpe == Constant.OC_CHI && tempCmd.equals("")) {
						tempCmd = s;
						whoseTurn = Integer.parseInt(s.substring(0, 1));
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
	
	@SuppressWarnings("unchecked")
	private void huaPilesInit() {
		this.huaPiles = new List[4];
		for(int i=0; i<4; i++) {
			huaPiles[i] = new LinkedList<Integer>();
		}
	}
	
	@SuppressWarnings("unchecked")
	private void fuluPilesInit() {
		this.fuluPiles = new List[4];
		for(int i=0; i<4; i++) {
			fuluPiles[i] = new LinkedList<Fulu>();
		}
	}
	
	@SuppressWarnings("unchecked")
	private void discardPilesInit() {
		this.discardPiles = new List[4];
		for(int i=0; i<4; i++) {
			discardPiles[i] = new LinkedList<Integer>();
		}
	}
	
	private void deckInit() {
		//把144张牌按顺序放入牌库
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
				//花牌总计八张
				int hua = 61;
				for(int i=0; i<8; i++) {
					deck[top] = hua;
					top++;
					hua++;
				}
	}
}