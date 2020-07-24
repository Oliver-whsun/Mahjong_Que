package pers.weihengsun.mahjong.game;

public class Constant {
	/**
	 * operation code (OC)
	 * client 与 server 之间交互时用到的操作规范码
	 */
	public static final int OC_MO = 10; //draw a tile, 玩家摸牌
	public static final int OC_FA = 11; //distribute a tile, 系统发牌 
	public static final int OC_MO_FROM_BOTTOM = 12; //draw a tile from bottom, 杠底摸牌
	public static final int OC_DA = 20; //discard a tile, 玩家打牌
	public static final int OC_CHI = 30; //玩家吃牌
	public static final int OC_PENG = 40; //玩家碰牌
	public static final int OC_GANG = 50; //玩家杠牌
	public static final int OC_HU = 0; //玩家胡牌
	public static final int OC_NO_ACTION = -1; //玩家接收到信息，没有异议且没有动作
//	private static final int again = 60;
	
	/**
	 * constant related to rules (RL)
	 * 与国标规则相关的常量 
	 */
	public static final int RL_NUM_OF_TILES = 144; //麻将牌总数，144 = 9*3*4 + 7*4 + 8
	public static final int RL_NUM_OF_HANDS = 13; //标准手牌数，即无摸牌、无副露时应该有13张手牌
	
	/**
	 * 麻将牌文字与数字编号对应表
	 * 数字 {11 ~ 19} 表示  {1 ~ 9} 万
	 * 
	 *数字 {21 ~ 29} 表示  {1 ~ 9} 条
	 *
	 *数字 {31 ~ 39} 表示  {1 ~ 9} 筒
	 *
	 *数字 {41 43 45 47 } 表示 { 东 南 西 北 }
	 *
	 *数字 {51 53 55} 表示 {中 發 白}
	 *
	 *数字{61 ~ 68} 表示{春夏秋冬梅兰竹菊}
	 */
	public static final String[] NUMBER_2_TILE = { // 编码表
			"无效","无效","无效","无效","无效","无效","无效","无效","无效","无效",
			"无效","一萬","二萬","三萬","四萬","五萬","六萬","七萬","八萬","九萬",
			"无效","一条","二条","三条","四条","五条","六条","七条","八条","九条",
			"无效","一筒","二筒","三筒","四筒","五筒","六筒","七筒","八筒","九筒",
			"无效","東风","无效","南风","无效","西风","无效","北风","无效","无效",
			"无效","红中","无效","發财","无效","白板","无效","北风","无效","无效",
			"无效","壹春","贰夏","叁秋","肆冬","伍梅","陆兰","柒竹","捌菊","无效"};
	
	
	
}
