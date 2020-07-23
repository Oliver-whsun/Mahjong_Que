package pers.weihengsun.mahjong.game;

/**
 * 副露的抽象
 * 副露，可以被认为是每个人手牌的一部分，是吃、碰、杠之后从手牌中脱离的牌组
 * 副露一般为三张一组，但杠为四张一组
 * @author weiheng.sun
 *
 */
public class Fulu {
	//每一组副露必为吃碰杠三种之一
	public FuluType type = null;
	
	//每一组副露必然由3或4张牌组成，记录这些牌的编号
	public int[] tileNumbers = null;
	
	/*
	 * 对一组吃出来的副露，其中一张牌是特殊的
	 * 用该角标记录哪张是吃的
	 * 比如三四五条，chiIndex=0，则说明三条是吃来的
	 */
	public int chiIndex = -1;
	
	/**
	 * 对于碰出来的副露，用该值记录碰牌的来源
	 * 比如pengSource=0，则说明是東风玩家打出来的碰牌
	 */
	public int pengSource = -1;
	
	/**
	 * 对于杠出来的副露，还要具体区分明暗杠
	 * 明杠：不论大明杠还是加杠，如碰一样应该有来源
	 * 用该值记录来源的玩家编号
	 * 比如gangSource=0，则说明東风玩家打出的牌被杠
	 * 如果该编号等于自身编号，则说明这是一个暗杠
	 */
	public int gangSource = -1;
	
	Fulu(FuluType type, int[] tileNumbers, int chiIndex, int pengSource, int gangSource){
		this.type = type;
		this.tileNumbers = tileNumbers;
		this.chiIndex = chiIndex;
		this.pengSource = pengSource;
		this.gangSource = gangSource;
	}
}

