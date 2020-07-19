import java.util.*;

public class Player{
	/*
	牌用数字表示

	数字 {11 ~ 19} 表示  {1 ~ 9} 万

	数字 {21 ~ 29} 表示  {1 ~ 9} 条

	数字 {31 ~ 39} 表示  {1 ~ 9} 筒

	数字 {41 43 45 47 } 表示 { 东 南 西 北 }

	数字 {51 53 55} 表示 {中 發 白}
	*/
	public static String[] n2p = { // 编码表 number to pai
		"无效","无效","无效","无效","无效","无效","无效","无效","无效","无效",
		"无效","一萬","二萬","三萬","四萬","五萬","六萬","七萬","八萬","九萬",
		"无效","一条","二条","三条","四条","五条","六条","七条","八条","九条",
		"无效","一筒","二筒","三筒","四筒","五筒","六筒","七筒","八筒","九筒",
		"无效","東风","无效","南风","无效","西风","无效","北风","无效","无效",
		"无效","红中","无效","發财","无效","白板"};
	public static String[] n2pe = { // 编码表 number to pai
		"null ","null ","null    ","null    ","null    ","null    ","null    ","null    ","null    ","null    ",
		"null    ","char1","char2","char3","char4","char5","char6","char7","char8","char9",
		"null    ","bamb1","bamb2","bamb3","bamb4","bamb5","bamb6","bamb7","bamb8","bamb9",
		"null    ","dot 1","dot 2","dot 3","dot 4","dot 5","dot 6","dot 7","dot 8","dot 9",
		"null    ","west ","null    ","south","null    ","west ","null    ","north","null    ","null    ",
		"null    ","red  ","null    ","green","null    ","white"};
	public static int[] tiles = { // 所以有效牌的数字码
		11,12,13,14,15,16,17,18,19, // 一到九万
		21,22,23,24,25,26,27,28,29, // 一到九条
		31,32,33,34,35,36,37,38,39, // 一到九筒
		41,43,45,47, //东南西北
		51,53,55}; // 中发白
	public static String[] n2z = {"東","南","西","北"}; //编码表 number to zhuang

	String name;
	int playerNum; // 0-3, 代表东南西北
	int tileRemains; // 有效牌还剩的张数
	public ArrayList<Integer> hands = null; //手牌 //测试代码！！记得改成private
	int[] hands_other; //所有玩家的手牌数
	ArrayList<ArrayList<Integer>> fulu = null; //每个人的副露：吃碰杠之后露在外面不可改变的手牌
	ArrayList<ArrayList<Integer>> discardTile = null; //每个人的弃牌堆：按顺序记录了该玩家所有的弃牌
	Hashtable<Integer,Integer> discardCounnt = null; //明牌计数器，记录了所有显露出来的牌的张数，包括你自己摸到的，所有人打、吃碰杠显露出来的
	Scanner sc; //从控制台接受指令的对象
	String[] plyNames; //按0123的顺序记录所有玩家的名字
	int zhuangNum; //庄家序号
	int[] points; //按顺序记录所有玩家得分，1000分初始
	int whosTurn;

	Player(String name, int num){ //构造函数
		this.name = name;
		this.playerNum = num;
		this.tileRemains = 136; // 136
		this.hands = new ArrayList<Integer>();
		this.hands_other = new int[4]; //初始情况下0号玩家“東”坐庄
		this.fulu = new ArrayList<ArrayList<Integer>>();
		fulu.add(new ArrayList<Integer>());
		fulu.add(new ArrayList<Integer>());
		fulu.add(new ArrayList<Integer>());
		fulu.add(new ArrayList<Integer>());
		this.discardTile = new ArrayList<ArrayList<Integer>>();
		discardTile.add(new ArrayList<Integer>());
		discardTile.add(new ArrayList<Integer>());
		discardTile.add(new ArrayList<Integer>());
		discardTile.add(new ArrayList<Integer>());
		this.discardCounnt = new Hashtable<Integer,Integer>();
		this.sc = new Scanner(System.in);
		this.plyNames = new String[4];
		this.zhuangNum = 0; //庄家永远从0号开始
		this.points = new int[4];
		points[0] = 1000; points[1] = 1000; points[2] = 1000; points[3] = 1000; //初始分
		this.whosTurn = 0;
	}//构造函数结束
	public void setNames(String message){ //记录各个玩家的名字
		String[] splitRes = message.split("_");
		for(int i=0; i<4; i++){
			this.plyNames[i] = splitRes[i];
		}
	}
	public void newGame(){ //开始一局新的游戏，清空各种记录器
		this.tileRemains = 136; // 136
		this.hands = new ArrayList<Integer>();
		this.hands_other = new int[4]; //初始情况下0号玩家“東”坐庄
		this.fulu = new ArrayList<ArrayList<Integer>>();
		fulu.add(new ArrayList<Integer>());
		fulu.add(new ArrayList<Integer>());
		fulu.add(new ArrayList<Integer>());
		fulu.add(new ArrayList<Integer>());
		this.discardTile = new ArrayList<ArrayList<Integer>>();
		discardTile.add(new ArrayList<Integer>());
		discardTile.add(new ArrayList<Integer>());
		discardTile.add(new ArrayList<Integer>());
		discardTile.add(new ArrayList<Integer>());
		this.discardCounnt = new Hashtable<Integer,Integer>();
		//更新庄家序号
		updateZhuang();
		//显示现有分数
		printPoints();
		this.whosTurn = zhuangNum; //庄家是14张牌，首先打牌
	}
	public String draw_other(int drawType, int num){ // 别的玩家摸了一张牌
		//drawType为0,正常摸打；1，开局发牌只摸不打；2，杠底摸牌，牌库余量不减
		hands_other[num] ++; //该玩家手牌数+1
		whosTurn = num;
		if(drawType==0){
			System.out.println(n2z[num]+"风玩家"+plyNames[num]+"摸了一张牌，当前手牌数"+hands_other[num]);
			System.out.println(plyNames[num]+" drew a tile.");
			tileRemains --; //总牌余量-1
			// System.out.println("牌库剩余"+tileRemains+"张。");
		}else if(drawType==1){
			System.out.println("牌库给"+n2z[num]+"风玩家"+plyNames[num]+"发了一张牌，当前手牌数"+hands_other[num]);
			System.out.println(plyNames[num]+" drew a tile.");
			tileRemains --; //总牌余量-1
			// System.out.println("牌库剩余"+tileRemains+"张。");
		}else if(drawType==2){
			System.out.println(n2z[num]+"风玩家"+plyNames[num]+"从杠底摸了一张牌，当前手牌数"+hands_other[num]);
			System.out.println(plyNames[num]+" drew a tile.");
		}
		return(num+"01"); //反馈信息，玩家编号+摸牌操作代码01
	}
	public String draw_yourself(int drawType ,int tile){ // 你抓了一张牌
		hands.add(tile); //这张牌放入手牌
		whosTurn = playerNum;
		Collections.sort(hands); //对手牌进行排序
		if(drawType==0){
			System.out.println("你摸牌：摸到了【"+n2p[tile]+"】");
			System.out.println("You draw【"+n2pe[tile]+"】");
		}else if(drawType==1){
			System.out.println("牌库发牌：发给你一张【"+n2p[tile]+"】");
			System.out.println("You draw【"+n2pe[tile]+"】");
		}else if(drawType==2){
			System.out.println("你从杠底摸牌：摸到了【"+n2p[tile]+"】");
			System.out.println("You draw【"+n2pe[tile]+"】");
		}
		printHands();
		hands_other[playerNum]++; //玩家手牌数操作
		// printHandsNumber(playerNum);
		if(drawType!=2){ //非杠底摸牌才会改变牌库余量
			tileRemains --; //总牌余量-1
			// printTileRemains();
		}
		changeCount(tile);
		// printDiscardCount(tile);
		return(""+playerNum+"01"+tile); //反馈信息，玩家编号+摸牌操作代码01+摸到的牌号码
	}
	public String discard_other(int num, int tile){ //别人打牌
		System.out.println(n2z[num]+"风玩家"+plyNames[num]+"打了一张【"+n2p[tile]+"】");
		System.out.println(plyNames[num]+" discarded【"+n2pe[tile]+"】");
		hands_other[num] --; //对手牌数进行操作
		// System.out.println(num+"号玩家当前手牌数"+hands_other[num]);
		discardTile.get(num).add(tile); //该玩家个人弃牌堆加上这张牌
		// printDiscardTile(num);
		changeCount(tile); //对弃牌计数器进行操作
		// printDiscardCount(tile);
		return(num+"02"+tile); //反馈信息，玩家编号+打牌操作码02+打出的牌
	}
	public String discard_yourself(int tile){ //自己打了一张牌
		System.out.println("你打出了【"+n2p[tile]+"】");
		System.out.println("You discard【"+n2pe[tile]+"】");
		hands.remove(new Integer(tile)); //手牌去掉这张牌
		printHands();
		hands_other[playerNum]--; //对所有玩家手牌数进行操作
		// System.out.println(playerNum+"号玩家当前手牌数"+hands_other[playerNum]);
		discardTile.get(playerNum).add(tile); //玩家个人弃牌堆加上这张牌
		// printDiscardTile(playerNum);
		return(playerNum+"02"+tile); //反馈信息，玩家编号+打牌操作码02+打出的牌
	}
	public boolean[] canChi(int tile){ //是否能吃,返回一个数组，0代表能不能吃，123代表能不能左中右吃
		boolean leftChi = hands.contains(Integer.valueOf(tile+1)) && hands.contains(Integer.valueOf(tile+2));
		boolean midChi = hands.contains(Integer.valueOf(tile+1)) && hands.contains(Integer.valueOf(tile-1));
		boolean rightChi = hands.contains(Integer.valueOf(tile-1)) && hands.contains(Integer.valueOf(tile-2));
		boolean finalRes = leftChi || midChi || rightChi;
		boolean[] res = {finalRes,leftChi,midChi,rightChi};
		return res;
	}
	public String chi_other(int num, int chiType, int tile){ //别人吃牌，吃的类型：1左吃，2中吃，3右吃
		whosTurn = num;
		if(chiType==1){ //左吃
			System.out.println(n2z[num]+"风玩家"+plyNames[num]+"左吃【"+n2p[tile]+"】");
			System.out.println(plyNames[num]+" chow【"+n2pe[tile]+"】");
			hands_other[num] -= 2; //手牌数操作
			// printHandsNumber(num);
			fulu.get(num).add(tile); //副露添加
			fulu.get(num).add(tile+1);
			fulu.get(num).add(tile+2);
			// printFulu(num);
			changeCount(tile+1); //明牌计数器
			// printDiscardCount(tile+1);
			changeCount(tile+2);
			// printDiscardCount(tile+2);
		}else if(chiType==2){
			System.out.println(n2z[num]+"风玩家"+plyNames[num]+"中吃【"+n2p[tile]+"】");
			System.out.println(plyNames[num]+" chow【"+n2pe[tile]+"】");
			hands_other[num] -= 2; //手牌数操作
			// printHandsNumber(num);
			fulu.get(num).add(tile-1); //副露添加
			fulu.get(num).add(tile);
			fulu.get(num).add(tile+1);
			// printFulu(num);
			changeCount(tile-1); //明牌计数器
			// printDiscardCount(tile-1);
			changeCount(tile+1);
			// printDiscardCount(tile+1);
		}else if(chiType==3){
			System.out.println(n2z[num]+"风玩家"+plyNames[num]+"右吃【"+n2p[tile]+"】");
			System.out.println(plyNames[num]+" chow【"+n2pe[tile]+"】");
			hands_other[num] -= 2; //手牌数操作
			// printHandsNumber(num);
			fulu.get(num).add(tile-2); //副露添加
			fulu.get(num).add(tile-1);
			fulu.get(num).add(tile);
			// printFulu(num);
			changeCount(tile-2); //明牌计数器
			// printDiscardCount(tile-2);
			changeCount(tile-1);
			// printDiscardCount(tile-1);
		}
		return(""+num+3+chiType+tile);
	}
	public String chi_yourself(int chiType, int tile){ //自己吃牌
		whosTurn = playerNum;
		if(chiType==1){
			System.out.println("你左吃【"+n2p[tile]+"】");
			System.out.println("You chow from left【"+n2pe[tile]+"】");
			hands.remove(new Integer(tile+1)); //手牌操作
			hands.remove(new Integer(tile+2));
			printHands();
			hands_other[playerNum] -= 2; //手牌数操作
			// printHandsNumber(playerNum);
			fulu.get(playerNum).add(tile); //副露操作
			fulu.get(playerNum).add(tile+1); 
			fulu.get(playerNum).add(tile+2); 
			// printFulu(playerNum);
		}else if(chiType==2){
			System.out.println("你中吃【"+n2p[tile]+"】");
			System.out.println("You chow from middle【"+n2pe[tile]+"】");
			hands.remove(new Integer(tile+1)); //手牌操作
			hands.remove(new Integer(tile-1));
			printHands();
			hands_other[playerNum] -= 2; //手牌数操作
			// printHandsNumber(playerNum);
			fulu.get(playerNum).add(tile-1); //副露操作
			fulu.get(playerNum).add(tile); 
			fulu.get(playerNum).add(tile+1); 
			// printFulu(playerNum);
		}else if(chiType==3){
			System.out.println("你右吃【"+n2p[tile]+"】");
			System.out.println("You chow from right【"+n2pe[tile]+"】");
			hands.remove(new Integer(tile-1)); //手牌操作
			hands.remove(new Integer(tile-2));
			printHands();
			hands_other[playerNum] -= 2; //手牌数操作
			// printHandsNumber(playerNum);
			fulu.get(playerNum).add(tile-2); //副露操作
			fulu.get(playerNum).add(tile-1); 
			fulu.get(playerNum).add(tile); 
			// printFulu(playerNum);
		}
		return ""+playerNum+3+chiType+tile;
	}
	public boolean canPeng(int tile){ //是否能碰
		int count = 0;
		for(int i=0; i<hands.size(); i++){
			if(hands.get(i).intValue() == tile){
				count++;
			}
		}
		return count==2;
	}
	public String peng_other(int num, int tile){ //别人碰牌
		whosTurn = num;
		System.out.println(n2z[num]+"风玩家"+plyNames[num]+"碰了【"+n2p[tile]+"】");
		System.out.println(plyNames[num]+" peng【"+n2pe[tile]+"】");
		hands_other[num] -= 2; // 手牌数操作
		// System.out.println(num+"号玩家当前手牌数"+hands_other[num]);
		fulu.get(num).add(tile); //副露操作
		fulu.get(num).add(tile);
		fulu.get(num).add(tile);
		// printFulu(num);
		changeCount(tile); //弃牌计数器操作
		changeCount(tile);
		// printDiscardCount(tile);
		return num+"04"+tile; //碰04
	}
	public String peng_yourself(int tile){
		whosTurn = playerNum;
		System.out.println("你碰了【"+n2p[tile]+"】");
		System.out.println("You peng【"+n2pe[tile]+"】");
		hands.remove(new Integer(tile)); //手牌去掉这张牌两次
		hands.remove(new Integer(tile)); 
		printHands();
		hands_other[playerNum] -= 2; //手牌数操作
		// printHandsNumber(playerNum);
		fulu.get(playerNum).add(tile); //副露操作
		fulu.get(playerNum).add(tile);
		fulu.get(playerNum).add(tile);
		// printFulu(playerNum);
		// printDiscardCount(tile);
		return playerNum+"04"+tile;
	}
	public int canGang(boolean drawByYou, int tile){ // 能否杠牌
		// 杠：分为暗杠，明杠，加杠。需要考虑这是一张摸牌还是别的的打牌
		int res;
		if(drawByYou){ // 如果你摸来这张牌，可能是暗杠或者加杠
			//检查是否可以暗杠
			int count = 0;
			for(int i=0; i<hands.size(); i++){
				if(hands.get(i).intValue() == tile){
					count++;
				}
			}
			if(count==4) return 1; //暗杠为1
			//检查是否可以加杠
			count = 0;
			for(int i=0; i<fulu.get(playerNum).size(); i++){
				if(fulu.get(playerNum).get(i).intValue() == tile){
					count++;
				}
			}
			if(count==3) return 2; //加杠为2
		}else{ // 如果是别人打的牌，那就检查是否可以明杠
			int count = 0;
			for(int i=0; i<hands.size(); i++){
				if(hands.get(i).intValue() == tile){
					count++;
				}
			}
			if(count==3) return 3; //明杠为3
		}
		return -1; // 不能杠返回-1
	}
	public String gang_other(int num, int gangType, int tile){ //别人杠牌
		if(gangType==1){ //暗杠
			System.out.println(n2z[num]+"风玩家"+plyNames[num]+"暗杠");
			System.out.println(plyNames[num]+" kong");
			hands_other[num] -= 4; //手牌数操作
			// printHandsNumber(num);
		}else if(gangType==2){ //加杠
			System.out.println(n2z[num]+"风玩家"+plyNames[num]+"加杠【"+n2p[tile]+"】");
			System.out.println(plyNames[num]+" kong【"+n2pe[tile]+"】");
			hands_other[num] --; //手牌数操作
			// printHandsNumber(num);
			fulu.get(num).add(tile); //副露操作
			Collections.sort(fulu.get(num));
			// printFulu(num);
			changeCount(tile); //弃牌计数器操作
			// printDiscardCount(tile);
		}else if(gangType==3){ //明杠
			System.out.println(n2z[num]+"风玩家"+plyNames[num]+"明杠【"+n2p[tile]+"】");
			System.out.println(plyNames[num]+" kong【"+n2pe[tile]+"】");
			hands_other[num] -= 3; //手牌数操作
			// printHandsNumber(num);
			fulu.get(num).add(tile); //副露操作
			fulu.get(num).add(tile);
			fulu.get(num).add(tile);
			fulu.get(num).add(tile);
			// printFulu(num);
			changeCount(tile); //弃牌计数器操作
			changeCount(tile); 
			changeCount(tile); 
			// printDiscardCount(tile);
		}
		return(""+num+5+gangType+tile);
	}
	public String gang_yourself(int gangType, int tile){
		if(gangType==1){ // 暗杠
			System.out.println("你暗杠了【"+n2p[tile]+"】");
			System.out.println("You kong【"+n2pe[tile]+"】");
			hands.remove(new Integer(tile)); //手牌去掉这张牌四次
			hands.remove(new Integer(tile));
			hands.remove(new Integer(tile));
			hands.remove(new Integer(tile));
			printHands();
			hands_other[playerNum] -= 4; //手牌数操作
			// printHandsNumber(playerNum);
			// printDiscardCount(tile);
		}else if(gangType==2){ //加杠
			System.out.println("你加杠了【"+n2p[tile]+"】");
			System.out.println("You kong【"+n2pe[tile]+"】");
			hands.remove(new Integer(tile)); //手牌去掉这张牌
			printHands();
			hands_other[playerNum] --; //手牌数操作
			// printHandsNumber(playerNum);
			fulu.get(playerNum).add(tile); //加杠的这张牌显示在副露中
			Collections.sort(fulu.get(playerNum)); //副露排序
			// printFulu(playerNum);
			// printDiscardCount(tile);
		}else if(gangType==3){ //明杠
			System.out.println("你明杠了【"+n2p[tile]+"】");
			System.out.println("You kong【"+n2pe[tile]+"】");
			hands.remove(new Integer(tile)); //手牌去掉这张牌三次
			hands.remove(new Integer(tile));
			hands.remove(new Integer(tile));
			printHands();
			hands_other[playerNum] -= 3; //手牌数操作
			// printHandsNumber(playerNum);
			fulu.get(playerNum).add(tile); //副露中添加这个杠
			fulu.get(playerNum).add(tile);
			fulu.get(playerNum).add(tile);
			fulu.get(playerNum).add(tile);
			// printFulu(playerNum);
			// printDiscardCount(tile);
		}
		return(""+playerNum+gangType+tile);
	}
	public boolean canHu(int tile){ //是否胡牌，输入一张可能点炮或自摸的牌
		ArrayList<Integer> inspector = (ArrayList<Integer>) hands.clone(); //不能直接操作手牌，临时创建一个检查器
		inspector.add(tile);// 把这张有可能使之胡的牌加入检查器
		Collections.sort(inspector); // 排序
		//先检查特殊胡牌牌型
		//首先检查十三幺（国士无双）：三种一九牌，七种字牌全都有，再加其中任意一张
		if(huShiSanYao(inspector)) return true;
		//再检查胡七对儿
		if(huQiDuier(inspector)) return true;
		//最后检查标准胡牌形式，即3n+2型
		if(huStandard(inspector)) return true;
		//全都不能胡，返回假
		return false;
	}
	public String react(String message){ //反应函数，最重要的函数
		//收到一个服务器发来的消息message，player解码并调用自己的函数做出反应
		int[] decodeRes = decode(message);
		int who = decodeRes[0];
		int operation = decodeRes[1];
		int tile = decodeRes[2];
		if(who==playerNum){//说明这个代码是你自己，你操作了自己的牌
			return react_yourself(operation,tile);
		}else{ //别人操作了一些动作
			return react_other(who,operation,tile);
		}
	}



	//****************************************************


	private void printHands(){ //辅助函数，打印手牌
		System.out.println("你的手牌如下:");
		System.out.println("Printing Hands:");
		System.out.println();
		for(int i=0; i<hands.size(); i++){
			System.out.print(n2p[hands.get(i)]+" ");
		}
		System.out.println();
		for(int i=0; i<hands.size(); i++){
			System.out.print(i+"    ");
		}
		System.out.println();
		for(int i=0; i<hands.size(); i++){
			System.out.print(n2pe[hands.get(i)]+" ");
		}
		System.out.println();
		System.out.println();
		// System.out.println("-------------------------------");
	}
	private void printTileRemains(){ //辅助函数，打印牌库余量
		System.out.println("牌库剩余"+tileRemains+"张。");
	}
	private void printHandsNumber(int num){ // 辅助函数，打印手牌数
		System.out.println(n2z[num]+"风玩家"+plyNames[num]+"当前手牌数"+hands_other[num]);
	}
	private void printDiscardTile(int num){ // 辅助函数，打印某玩家个人弃牌堆
		System.out.println(n2z[num]+"风玩家"+plyNames[num]+"个人弃牌堆如下：");
		for(int i=0; i<discardTile.get(num).size(); i++){
			System.out.print(n2p[discardTile.get(num).get(i)]);
		}
		System.out.println();
		System.out.println("-------------------------------");
	}
	private void changeCount(int tile){ //辅助函数，对弃牌计数器进行操作
		if(!discardCounnt.containsKey(tile)){ //如果计数器里没有这张牌
			discardCounnt.put(tile,1); //放入1
		}else{
			int cur = discardCounnt.get(tile);
			cur ++;
			discardCounnt.put(tile,cur);
		}
	}
	private void printDiscardCount(int tile){ //辅助函数，打印弃牌计数器
		System.out.println(n2p[tile]+"已经出现了"+discardCounnt.get(tile)+"张。");
	}
	private void printFulu(int num){ //辅助函数，打印某玩家副露
		ArrayList<Integer> arl = fulu.get(num);
		System.out.println(n2z[num]+"风玩家"+plyNames[num]+"副露如下：");
		for(int i=0; i<arl.size(); i++){
			System.out.print(n2p[arl.get(i)]);
		}
		System.out.println();
		System.out.println("-------------------------------");
	}
	public boolean huShiSanYao(ArrayList<Integer> inspector){
		if(inspector.size()!=14) return false; //不是十四张一定不能胡十三幺
		//再看包不包含一对儿一样的
		boolean containsPair = containsPair(inspector);
		if(!containsPair) return false;
		//再看是不是六种一九、七种字牌都有
		boolean containsShiSan = 
			inspector.contains(Integer.valueOf(1)) && inspector.contains(Integer.valueOf(9)) //一九万
			&& inspector.contains(Integer.valueOf(11)) && inspector.contains(Integer.valueOf(19)) //一九条
			&& inspector.contains(Integer.valueOf(21)) && inspector.contains(Integer.valueOf(29)) //一九筒
			&& inspector.contains(Integer.valueOf(31)) && inspector.contains(Integer.valueOf(33)) && inspector.contains(Integer.valueOf(35)) && inspector.contains(Integer.valueOf(37)) //东南西北
			&& inspector.contains(Integer.valueOf(41)) && inspector.contains(Integer.valueOf(43)) && inspector.contains(Integer.valueOf(45));//中发白
		return containsShiSan;
	}
	public boolean huQiDuier(ArrayList<Integer> inspector){
		if(inspector.size()!=14) return false; //不是十四张一定不能胡
		//胡七对儿的逻辑：01张一样，23张一样...以此类推
		for(int i=0; i<inspector.size(); i+=2){
			if(!inspector.get(i).equals(inspector.get(i+1))) return false;
		}
		return true;
	}
	public boolean huStandard(ArrayList<Integer> inspector){
		//标准胡牌形式的判定，即3n+2的形式
		//第一步，检查有没有一对儿做将，没有将一定不胡
		if(!containsPair(inspector)) return false;
		//接下来，把一对儿将剔除出去，检查剩下的能不能全部组成顺子和刻子
		//因为一副牌可能含有很多对儿，任何一对都有做将的可能，所以要把所有情况都讨论
		// printInspector(inspector);
		ArrayList<ArrayList<Integer>> ins_PairRemoved = removePair(inspector);
		//对已经剔除了对儿的多种情况，分别检查
		//检查逻辑如下：如果是空的，能胡；如果含刻子或顺子，剔除后再检查；如果剩下杂乱的，不能胡
		for(int i=0; i<ins_PairRemoved.size(); i++){
			// System.out.println("正在检查第"+i+"种剔除将之后的情况");
			ArrayList<Integer> remain = ins_PairRemoved.get(i);
			// printInspector(remain);
			remain = removeShunKe(remain);
			if(remain.size()==0) return true;
			// System.out.println("第"+i+"种剔除将之后的情况【不胡】");
		}
		//全部检查完毕还不能胡，返回不能胡
		return false;
	}
	private boolean containsPair(ArrayList<Integer> inspector){
		//检查一副牌是否含有至少一对儿（做将）
		//以这副牌已排序为前提，至少含有一对儿的条件就是第0张和第1张一样，或第1张和第2张一样...以此类推
		for(int i=0; i<inspector.size()-1; i++){
			if(inspector.get(i).equals(inspector.get(i+1))) return true;
		}
		return false;
	}
	private ArrayList<ArrayList<Integer>> removePair(ArrayList<Integer> inspector){
		// 帮助函数，给我一副牌，我找出所有可能的将，剔除出去，返给你很多副牌
		// 比如给你一副牌：1,1,1,2,2,2,3,4,11,12,12,13,13,14
		// 我返给你四副牌，分别是剔除了对儿1，对儿2，对儿12，对儿13之后的牌
		// 写得不好，重写
		ArrayList<ArrayList<Integer>> res = new ArrayList<ArrayList<Integer>>();
		// System.out.println("inspector的长度是"+inspector.size());
		for(int i=0; i<inspector.size()-1; i++){
			if(inspector.get(i).equals(inspector.get(i+1))){
				// System.out.println("此时的i="+i);
				ArrayList<Integer> insWithoutPair = (ArrayList<Integer>) inspector.clone();
				// System.out.println("insWithoutPair的长度是"+insWithoutPair.size());
				insWithoutPair.remove(i);
				// 不看下面这种方案，有问题
				// i--; //特别注意！这是Arraylist变迭代边删除时的常见问题！删除了一个元素，size就变了，而循环变量不作处理就会导致遍历不完全或者越界
				insWithoutPair.remove(i); //重要信息！因为删除后角标变了，所以再删除这个位置就等同于删除它的下一个元素
				res.add(insWithoutPair);
			}
		}
		return res;
		// 开始重写
		// ArrayList<ArrayList<Integer>> res = new ArrayList<ArrayList<Integer>>();
		// for(int i=0; i<inspector.size(); i++){
		// 	int current = inspector.get(i);
		// 	if()
		// }
		// 算了吧，不好写
	}
	private ArrayList<Integer> removeShunKe(ArrayList<Integer> insWithoutPair){
		// 帮助函数，且是递归循环调用的
		// 给我一副已经剔除了将的牌，此函数不断剔除其中成型的顺子和刻子
		// 对于能胡的牌，最后结果是空手牌；对于不能胡的牌，结果是3n张无关联的牌
		if(insWithoutPair.size()<=2) return insWithoutPair; //base case,空手牌
		if(insWithoutPair.get(0).equals(insWithoutPair.get(1)) && insWithoutPair.get(1).equals(insWithoutPair.get(2))){ //前三张牌相同，剔除这个刻子，然后继续检查
			// printInspector(insWithoutPair);
			int firstTile = insWithoutPair.get(0); //查看第一张牌
			insWithoutPair.remove(Integer.valueOf(firstTile));
			insWithoutPair.remove(Integer.valueOf(firstTile));
			insWithoutPair.remove(Integer.valueOf(firstTile));
			// System.out.println("剔除了一个刻子");
			// printInspector(insWithoutPair);
			return removeShunKe(insWithoutPair);
		}else{ //前三张不同，那么有可能是个顺子，有可能是杂牌
			int firstTile = insWithoutPair.get(0); //查看第一张牌
			if(insWithoutPair.contains(Integer.valueOf(firstTile+1)) && insWithoutPair.contains(Integer.valueOf(firstTile+2))){ //同时存在N,N+1,N+2;说明这是个顺子
				// 剔除这个顺子，继续检查
				insWithoutPair.remove(Integer.valueOf(firstTile));
				insWithoutPair.remove(Integer.valueOf(firstTile+1));
				insWithoutPair.remove(Integer.valueOf(firstTile+2));
				// System.out.println("剔除了一个顺子");
				// printInspector(insWithoutPair);
				return removeShunKe(insWithoutPair);
			}
		}
		//既不是刻子也不是顺子，就是杂牌，没有再检查的必要了
		//返回它自己，因为自己不是空手牌，所以意味着胡不了
		// System.out.println("这是一个杂牌");
		return insWithoutPair;
	}
	public void printInspector(ArrayList<Integer> inspector){ //辅助函数，打印检查器的牌
		System.out.println("检查器内牌如下：");
		for(int i=0; i<inspector.size(); i++){
			System.out.print(n2p[inspector.get(i)]+" ");
		}
		System.out.println();
	}
	private int[] decode(String message){
		String head = message.substring(0,1); //消息第一位，玩家代码
		String body = message.substring(1,3); //消息第二三位，操作代码
		String tail = message.substring(3,5); //消息第四五位，牌的号码
		int who = Integer.parseInt(head);
		int operation = Integer.parseInt(body);
		int tile = Integer.parseInt(tail);
		int[] res = {who, operation, tile};
		return res;// 解码函数
	}
	public boolean discardFromShangjia(int num){// 判断是不是上家打的
		if(playerNum+1<4){ //我的玩家号码加一还是有效的玩家号码，那就直接比较
			return(playerNum+1==num);
		}else{ //我的玩家号码加一大于四，说明我是3号玩家，我的上家是0
			return(num==0);
		}
	}
	private void updateZhuang(){ //更新庄家信息并显示
		if(zhuangNum<=2){ //0,1,2做庄，更新直接+1
			zhuangNum++;
		}else{
			zhuangNum = 0; //否则就是3坐庄，更新为0
		}
		System.out.println(n2z[zhuangNum]+"风玩家 "+plyNames[zhuangNum]+"坐庄。");
		System.out.println(plyNames[zhuangNum]+" is now Banker.");
	}
	private void printPoints(){ //显示所有玩家得分情况
		for(int i=0; i<4; i++){
			System.out.println(n2z[i]+"风玩家 "+plyNames[i]+"现在得分："+points[i]);
		}
		for(int i=0; i<4; i++){
			System.out.println(plyNames[i]+" have points："+points[i]);
		}
	}
	public void updatePoints(int winner, int dianpao){ //更新分数，赢家得分，点炮者减分，自摸另算
		if(winner!=dianpao){ //点炮和赢家不是一个人，点炮者支付赢家分数
			points[dianpao] -= 100;
			points[winner] += 100;
		}else{ //如果赢家和点炮者是同一个人，说明是自摸，其他所有人支付赢家点数
			for(int i=0; i<4; i++){
				if(winner==i){
					points[i] += 300;
				}else{
					points[i] -= 100;
				}
			}
		}
		printPoints();
	}
	private String react_yourself(int operation, int tile){
		int oper = operation/10; //取第一位
		int type = operation%10; //取第二位
		switch (oper) {
		case 0: //胡牌
			return react_hu_yourself();
		case 1: //摸牌
			return react_draw_yourself(type,tile);
		case 2: //打牌
			return react_discard_yourself(tile);
		case 3: //吃牌
			return react_chi_yourself(type,tile);
		case 4: //碰牌
			return react_peng_yourself(tile);
		case 5: //杠牌
			return react_gang_yourself(type,tile);
		default: //不该进入这个分支，进入说明出错
			return playerNum+"-2";
		}
	}
	private String react_other(int who, int operation, int tile){
		int oper = operation/10; //取第一位
		int type = operation%10; //取第二位
		switch (oper) {
		case 0: //胡牌
			return react_hu_other(who);
		case 1: //摸牌
			return react_draw_other(who,type);
		case 2: //打牌
			return react_discard_other(who,tile);
		case 3: //吃牌
			return react_chi_other(who,type,tile);
		case 4: //碰牌
			return react_peng_other(who,tile);
		case 5: //杠牌
			return react_gang_other(who,type,tile);
		default: //不该进入这个分支，进入说明出错
			return playerNum+"-2";
		}
	}
	public String react_hu_other(int who){
		System.out.println(n2z[who]+"风玩家"+plyNames[who]+"胡牌啦，不要生气再接再厉！");
		System.out.println(plyNames[who]+" wins.");
		updatePoints(who,whosTurn); //计算分数，who是赢家,whosTurn是点炮者
		//询问是否再玩一局
		System.out.println("要继续游戏吗？Y/N");
		System.out.println("Continue？  Y/N");
		String respond = getRespond_YN();
		if(respond.equals("Y")){
			newGame();
			return playerNum+"-1";//表示自己没有动作
		}else return playerNum+"-2"; //表示退出游戏
	}
	public String react_draw_other(int who, int drawType){
		draw_other(drawType,who); //摸牌操作
		if(drawType!=1) return(playerNum+"-1"); //表示自己没有动作
		else return(playerNum+"-3"); //特殊交流协议，表示开局发牌，知道了
	}
	public String react_discard_other(int who, int tile){
		discard_other(who,tile); //先进行打牌的基本操作
		//然后依次判断能否胡、吃、碰、杠
		if(canHu(tile)){ //判断是否能胡
			System.out.println("你可以胡这张【"+n2p[tile]+"】，要胡吗？Y/N");
			System.out.println("【"+n2pe[tile]+"】is a winning tile，claim winning？Y/N");
			String respond = getRespond_YN();
			if(respond.equals("Y")){
				return playerNum+"00"+tile;
			}
		}
		//不能胡或不胡，进入吃的判断
		if(discardFromShangjia(who)){//首先判断是不是上家打的，上家打的才要判断吃
			boolean[] canChi = canChi(tile); //判断这张牌能不能吃,总的答案存储在数组首位
			if(canChi[0]){ //能吃，进而询问要吃哪张
				sc.reset();
				System.out.println("你可以吃这张【"+n2p[tile]+"】，要吃吗？");
				System.out.println("【"+n2pe[tile]+"】is a chow tile，chow？");
				System.out.println("左吃回复1，中吃回复2，右吃回复3，不吃回复0。");
				System.out.println("left  1， middle 2，right  3，don't chow 0。");
				int chiType = -1;
				do{
					chiType = sc.nextInt();
				}while((chiType<0 || chiType>3) || !canChi[chiType]); //健壮性判断，吃的种类必须合法
				if(chiType!=0){ 
					return playerNum+"3"+chiType+tile;
				} //end if 确定吃牌
			} //end if 能吃牌
		}// end if 上家打的
		// 不吃，进入碰的判断
		if(canPeng(tile)){
			System.out.println("你可以碰这张【"+n2p[tile]+"】，要碰吗？Y/N");
			System.out.println("【"+n2pe[tile]+"】is a peng tile，peng？Y/N");
			String respond = getRespond_YN();
			if(respond.equals("Y")){
				return playerNum+"40"+tile;
			}
		}
		// 不碰，进入杠的判断
		if(canGang(false,tile)==3){
			System.out.println("你可以明杠这张【"+n2p[tile]+"】，要杠吗？Y/N");
			System.out.println("【"+n2pe[tile]+"】is a kong tile，kong？Y/N");
			String respond = getRespond_YN();
			if(respond.equals("Y")){
				return playerNum+"53"+tile;
			}
		}
		//什么都不做，没有动作
		return playerNum+"-1";//表示自己没有动作
	}
	private String react_chi_other(int who, int chiType, int tile){
		chi_other(who,chiType,tile);
		return(playerNum+"-1");//表示自己没有动作
	}
	private String react_peng_other(int who, int tile){
		peng_other(who,tile);
		return(playerNum+"-1");//表示自己没有动作
	}
	private String react_gang_other(int who, int gangType, int tile){
		gang_other(who,gangType,tile);
		return(playerNum+"-1");//表示自己没有动作
	}
	public String react_draw_yourself(int drawType, int tile){
		switch(drawType){
		case 1: //摸牌类型为1，代表开局发牌，不用进行任何操作
			draw_yourself(drawType,tile); //进行摸牌的基本操作
			return playerNum+"-3"; //特殊交流协议，-3表示开局发牌，我知道了
		case 0: //摸牌类型为0，代表对局中摸牌然后打牌，首先检测是否胡，然后检测加杠/暗杠
		case 2:
			//先检测是否可以胡牌
			boolean canHu = canHu(tile);
			if(canHu){
				System.out.println("你摸牌：摸到了【"+n2p[tile]+"】");
				System.out.println("You drew【"+n2pe[tile]+"】");
				System.out.println("你可以胡这张【"+n2p[tile]+"】，要胡吗？Y/N");
				System.out.println("【"+n2pe[tile]+"】is a winning tile，claim winning？Y/N");
				String respond = getRespond_YN();
				if(respond.equals("Y")){
					whosTurn = playerNum;
					return playerNum+"00"+tile;
				}
			}
			//如果不胡或者不能胡，继续检测是否可以加杠或者暗杠
			int gangType = canGang(true,tile);
			switch(gangType){
			case 1: //可以暗杠
				System.out.println("你可以暗杠这张【"+n2p[tile]+"】，要杠吗？Y/N");
				System.out.println("【"+n2pe[tile]+"】is a kong tile，kong？Y/N");
				String respond = getRespond_YN();
				if(respond.equals("Y")) return playerNum+"51"+tile;
				break;
			case 2: //可以加杠
				System.out.println("你可以加杠这张【"+n2p[tile]+"】，要杠吗？Y/N");
				System.out.println("【"+n2pe[tile]+"】is a kong tile，kong？Y/N");
				respond = getRespond_YN();
				if(respond.equals("Y")) return playerNum+"52"+tile;
				break;
			default: //不可以杠
				break;
			}
			//也不杠的话就进入正常摸牌打牌流程
			draw_yourself(drawType,tile); //进行摸牌的基本操作
			System.out.println("该你打牌了，你要打第几张牌？（从0开始数）");
			System.out.println("Please discard tile, type index:");
			int discardIndex = getRespond_dicardIndex();
			return playerNum+"20"+hands.get(discardIndex);
		default: //如果进入这个选项说明代码出错了
			return playerNum+"-2";
		}
	}
	private String react_discard_yourself(int tile){
		discard_yourself(tile); //打牌操作
		return playerNum+"-1"; //没有其他动作
	}
	public String react_chi_yourself(int chiType, int tile){
		chi_yourself(chiType,tile); //吃牌操作
		//进入打牌流程
		System.out.println("该你打牌了，你要打第几张牌？（从0开始数）");
		System.out.println("Please discard tile, type index:");
		int discardIndex = getRespond_dicardIndex();
		return playerNum+"20"+hands.get(discardIndex);
	}
	public String react_peng_yourself(int tile){
		peng_yourself(tile); //碰牌操作
		//进入打牌流程
		System.out.println("该你打牌了，你要打第几张牌？（从0开始数）");
		System.out.println("Please discard tile, type index:");
		int discardIndex = getRespond_dicardIndex();
		return playerNum+"20"+hands.get(discardIndex);
	}
	private String react_gang_yourself(int gangType, int tile){
		gang_yourself(gangType,tile); //杠牌操作
		//杠完成后，服务器会给你发一张杠底牌，因此此时不需要额外操作
		return playerNum+"-1"; //没有其他动作
	}
	public String react_hu_yourself(){
		System.out.println("恭喜！你胡啦！");
		System.out.println("You win!");
		updatePoints(playerNum,whosTurn); //计算分数，who是赢家,whosTurn是点炮者
		System.out.println("要继续游戏吗？Y/N");
		System.out.println("Continue? Y/N");
		String respond = getRespond_YN();
		if(respond.equals("Y")){
			newGame();
			return playerNum+"-1";
		}else return playerNum+"-2";
	}
	private String getRespond_YN(){ //获得一个用户输入的Y/N字符串
		sc.reset();
		String respond = null;
		do{
			respond = sc.nextLine();
		}while(!respond.equals("Y")&&!respond.equals("N"));
		return respond;
	}
	private int getRespond_dicardIndex(){ //获得一个用户输入的合法弃牌索引
		int discardIndex = -1;
		do{
			discardIndex = sc.nextInt();
		}while(discardIndex<0 || discardIndex >= hands.size());
		return discardIndex;
	}
}