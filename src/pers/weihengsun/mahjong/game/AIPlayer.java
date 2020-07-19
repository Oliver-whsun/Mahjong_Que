package pers.weihengsun.mahjong.game;

import java.util.*;

public class AIPlayer extends Player{

	//父类不需要的成员变量
	int moves = 0; //记录当前局自己操作了多少手牌，不论输赢，每局游戏后清空
	int wins = 0; //记录本次运行以来本AI赢了多少局
	double averageWinSpeed = 0; //记录上一次计算出的胡牌速度

	AIPlayer(String name, int num){ //构造函数
		//首先调用父类的有参构造函数
		super(name,num);

	}//构造函数结束

	//覆写父类方法，增加AI的自动决策功能
	public String react_hu_other(int who){
		System.out.println(n2z[who]+"风玩家"+plyNames[who]+"胡牌啦，不要生气再接再厉！");
		System.out.println(plyNames[who]+" wins.");
		updatePoints(who,whosTurn); //计算分数，who是赢家,whosTurn是点炮者
		//询问是否再玩一局
		System.out.println("要继续游戏吗？Y/N");
		System.out.println("Continue? ");
		// System.out.println("AI表示不服并发誓要打败人类，血战到底！ BLOOD!!!!!");
		System.out.println("AI choose to continue.");
		moves = 0; //清空操作数
		newGame();
		return playerNum+"-1";//表示自己没有动作
	}
	public String react_discard_other(int who, int tile){
		discard_other(who,tile); //先进行打牌的基本操作
		//然后依次判断能否胡、吃、碰、杠
		if(canHu(tile)){ //判断是否能胡
			System.out.println("你可以胡这张【"+n2p[tile]+"】，要胡吗？Y/N");
			System.out.println("【"+n2pe[tile]+"】is a winning tile, AI claims winning.");
			// System.out.println("AI表示能胡还不胡，是不是傻！菜鸡人类，又输给我了。");
			return playerNum+"00"+tile;
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
				chiType = think_chi(tile);
				System.out.println("AI选择："+chiType);
				System.out.println("AI choose: "+chiType);
				if(chiType!=0){ 
					return playerNum+"3"+chiType+tile;
				} //end if 确定吃牌
			} //end if 能吃牌
		}// end if 上家打的
		// 不吃，进入碰的判断
		if(canPeng(tile)){
			System.out.println("你可以碰这张【"+n2p[tile]+"】，要碰吗？Y/N");
			System.out.println("【"+n2pe[tile]+"】is a peng tile，peng？Y/N");
			boolean peng = think_peng(tile);
			System.out.println("AI选择："+peng);
			System.out.println("AI choose ："+peng);
			if(peng){
				return playerNum+"40"+tile;
			}
		}
		// 不碰，进入杠的判断
		if(canGang(false,tile)==3){
			System.out.println("你可以明杠这张【"+n2p[tile]+"】，要杠吗？Y/N");
			System.out.println("【"+n2pe[tile]+"】is a kong tile，kong？Y/N");
			boolean gang = think_gang(3,tile);
			System.out.println("AI选择："+gang);
			System.out.println("AI choose："+gang);
			if(gang){
				return playerNum+"53"+tile;
			}
		}
		//什么都不做，没有动作
		return playerNum+"-1";//表示自己没有动作
	}
	public String react_draw_yourself(int drawType, int tile){
		switch(drawType){
		case 1: //摸牌类型为1，代表开局发牌，不用进行任何操作
			draw_yourself(drawType,tile); //进行摸牌的基本操作
			return playerNum+"-3"; //特殊协议，开局发牌
		case 0: //摸牌类型为0，代表对局中摸牌然后打牌，首先检测是否胡，然后检测加杠/暗杠
		case 2:
			//先检测是否可以胡牌
			boolean canHu = canHu(tile);
			if(canHu){
				System.out.println("你摸牌：摸到了【"+n2p[tile]+"】");
				System.out.println("You drew【"+n2pe[tile]+"】");
				System.out.println("你可以胡这张【"+n2p[tile]+"】，要胡吗？Y/N");
				System.out.println("【"+n2pe[tile]+"】is a winning tile, AI claims winning.");
				// System.out.println("AI表示能胡还不胡，是不是傻！菜鸡人类，又输给我了。");
				return playerNum+"00"+tile;
			}
			//如果不胡或者不能胡，继续检测是否可以加杠或者暗杠
			int gangType = canGang(true,tile);
			switch(gangType){
			case 1: //可以暗杠
				System.out.println("你可以暗杠这张【"+n2p[tile]+"】，要杠吗？Y/N");
				System.out.println("【"+n2pe[tile]+"】is a kong tile，kong？Y/N");
				boolean gang = think_gang(1,tile);
				System.out.println("AI选择："+gang);
				System.out.println("AI choose："+gang);
				if(gang) return playerNum+"51"+tile;
				break;
			case 2: //可以加杠
				System.out.println("你可以加杠这张【"+n2p[tile]+"】，要杠吗？Y/N");
				System.out.println("【"+n2pe[tile]+"】is a kong tile，kong？Y/N");
				gang = think_gang(2,tile);
				System.out.println("AI选择："+gang);
				System.out.println("AI choose："+gang);
				if(gang) return playerNum+"52"+tile;
				break;
			default: //不可以杠
				break;
			}
			//也不杠的话就进入正常摸牌打牌流程
			draw_yourself(drawType,tile); //进行摸牌的基本操作
			System.out.println("该你打牌了，你要打第几张牌？（从0开始数）");
			System.out.println("Please discard tile, type index:");
			int discardIndex = think_discard();
			System.out.println("AI选择："+discardIndex);
			System.out.println("AI choose："+discardIndex);
			moves++;//摸打一次，操作数+1
			return playerNum+"20"+hands.get(discardIndex);
		default: //如果进入这个选项说明代码出错了
			return playerNum+"-2";
		}
	}
	public String react_chi_yourself(int chiType, int tile){
		chi_yourself(chiType,tile); //吃牌操作
		//进入打牌流程
		System.out.println("该你打牌了，你要打第几张牌？（从0开始数）");
		System.out.println("Please discard tile, type index:");
		int discardIndex = think_discard();
		System.out.println("AI选择："+discardIndex);
		System.out.println("AI choose："+discardIndex);
		moves++;//摸打一次，操作数+1
		return playerNum+"20"+hands.get(discardIndex);
	}
	public String react_peng_yourself(int tile){
		peng_yourself(tile); //碰牌操作
		//进入打牌流程
		System.out.println("该你打牌了，你要打第几张牌？（从0开始数）");
		System.out.println("Please discard tile, type index:");
		int discardIndex = think_discard();
		System.out.println("AI选择："+discardIndex);
		System.out.println("AI choose："+discardIndex);
		moves++;//摸打一次，操作数+1
		return playerNum+"20"+hands.get(discardIndex);
	}
	public String react_hu_yourself(){
		System.out.println("恭喜！你胡啦！");
		System.out.println("You win!");
		updatePoints(playerNum,whosTurn); //计算分数，who是赢家,whosTurn是点炮者
		System.out.println("要继续游戏吗？Y/N");
		System.out.println("AI choose to continue.");
		// System.out.println("AI表示菜鸡人类，又输给我了。就勉强再陪他们玩会儿吧。");
		//增加AI计算自己平均胡牌速度的函数
		wins++;//自己赢了，胜利次数+1
		averageWinSpeed = calculateAverageWinSpeed();
		moves = 0;
		System.out.println("直至当前，本AI平均胡牌速度为"+averageWinSpeed+"张。");
		newGame();
		return playerNum+"-1";
	}

	//增加一些AI特有的行为函数
	public boolean canHu(ArrayList<Integer> pai, int tile){ //有两个参数形式的canHu函数，判断能否胡牌
		ArrayList<Integer> inspector = (ArrayList<Integer>) pai.clone(); //不能直接操作手牌，临时创建一个检查器
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
	public int ting(ArrayList<Integer> pai){ //判断是否听牌的函数
		//入口参数是一副牌，可能是手牌，可能是虚拟手牌，判断有没有听
		//返回值是int，代表听牌的张数（不是种类）：比如双面听25万，则返回场上未出现的25万有多少张
		//听牌，就是再来一张加入手牌就胡的状态，遍历所有的牌，假想把他们加入手牌，如果胡了，那这一种牌就是听的牌之一，记录这种牌还有多少
		int res=0;
		for(int i=0; i<tiles.length; i++){
			if(canHu(pai,tiles[i])){
				res += discardCounnt.get(tiles[i])==null ? 4 : 4-discardCounnt.get(tiles[i]);
				// discardCount记录的是明牌，剩下还能打出的是4-这个值
			}
		}
		return res;
	}
	public int x1ting(ArrayList<Integer> pai){ //判断是否差一张就听牌，返回多少张牌能让你进入听牌状态
		//判断逻辑：手牌不能直接操作，复制一份放入检查器
		//外循环遍历手牌，删除当前遍历的手牌，内循环放入所有有效牌，这之后的手牌如果是听牌状态，则之前就是差1听状态
		//记录下这种有效牌还剩的张数
		int res=0;
		HashSet<Integer> helpfulTile = new HashSet<Integer>();
		for(int i=0; i<pai.size(); i++){
			ArrayList<Integer> inspector = (ArrayList<Integer>) pai.clone(); //不能直接操作手牌，临时创建一个检查器
			// printInspector(inspector);
			inspector.remove(new Integer(pai.get(i)));//删除当前遍历的牌
			// printInspector(inspector);
			for(int j=0; j<tiles.length; j++){
				inspector.add(tiles[j]);//添加一张有效牌
				// printInspector(inspector);
				if(ting(inspector)!=0){ //这张牌可以使当前状态进入听
					// System.out.println(n2p[tiles[j]]+"可以使当前牌听牌");
					helpfulTile.add(new Integer(tiles[j])); //把该牌加入有用牌集合，最后统计总数
				}
				inspector.remove(new Integer(tiles[j]));//添加的这张牌检查完毕，撤销这个动作
			}
		}
		Iterator<Integer> iter = helpfulTile.iterator();
		while(iter.hasNext()){
			int tile = iter.next().intValue();
			res += discardCounnt.get(tile)==null ? 4 : 4-discardCounnt.get(tile);
		}
		return res;
	}
	public int x2ting(ArrayList<Integer> pai){ //判断差两张听牌，返回多少张牌让当前牌进入差1听状态
		//判断逻辑：手牌不能直接操作，复制一份放入检查器
		//外循环遍历手牌，删除当前遍历的手牌，内循环放入所有有效牌，这之后的手牌如果是差1听状态，则之前就是差2听状态
		//记录下这种有效牌还剩的张数
		int res=0;
		HashSet<Integer> helpfulTile = new HashSet<Integer>();
		for(int i=0; i<pai.size(); i++){
			ArrayList<Integer> inspector = (ArrayList<Integer>) pai.clone(); //不能直接操作手牌，临时创建一个检查器
			// printInspector(inspector);
			inspector.remove(new Integer(pai.get(i)));//删除当前遍历的牌
			// printInspector(inspector);
			for(int j=0; j<tiles.length; j++){
				inspector.add(tiles[j]);//添加一张有效牌
				// printInspector(inspector);
				if(x1ting(inspector)!=0){ //这张牌可以使当前状态进入差1听
					// System.out.println(n2p[tiles[j]]+"可以使当前牌进入差1听状态");
					helpfulTile.add(new Integer(tiles[j])); //把该牌加入有用牌集合，最后统计总数
				}
				inspector.remove(new Integer(tiles[j]));//添加的这张牌检查完毕，撤销这个动作
			}
		}
		Iterator<Integer> iter = helpfulTile.iterator();
		while(iter.hasNext()){
			int tile = iter.next().intValue();
			res += discardCounnt.get(tile)==null ? 4 : 4-discardCounnt.get(tile);
		}
		return res;
	}
	public int x3ting(ArrayList<Integer> pai){ //判断差3张听牌，返回多少张牌让当前牌进入差2听状态
		//判断逻辑：手牌不能直接操作，复制一份放入检查器
		//外循环遍历手牌，删除当前遍历的手牌，内循环放入所有有效牌，这之后的手牌如果是差2听状态，则之前就是差3听状态
		//记录下这种有效牌还剩的张数
		int res=0;
		HashSet<Integer> helpfulTile = new HashSet<Integer>();
		for(int i=0; i<pai.size(); i++){
			ArrayList<Integer> inspector = (ArrayList<Integer>) pai.clone(); //不能直接操作手牌，临时创建一个检查器
			// printInspector(inspector);
			inspector.remove(new Integer(pai.get(i)));//删除当前遍历的牌
			// printInspector(inspector);
			for(int j=0; j<tiles.length; j++){
				inspector.add(tiles[j]);//添加一张有效牌
				// printInspector(inspector);
				if(x2ting(inspector)!=0){ //这张牌可以使当前状态进入差2听
					System.out.println(n2p[tiles[j]]+"可以使当前牌进入差2听状态");
					helpfulTile.add(new Integer(tiles[j])); //把该牌加入有用牌集合，最后统计总数
				}
				inspector.remove(new Integer(tiles[j]));//添加的这张牌检查完毕，撤销这个动作
			}
		}
		Iterator<Integer> iter = helpfulTile.iterator();
		while(iter.hasNext()){
			int tile = iter.next().intValue();
			res += discardCounnt.get(tile)==null ? 4 : 4-discardCounnt.get(tile);
		}
		return res;
	}
	public int countShunzi(ArrayList<Integer> pai){ //数给定的手牌、虚拟手牌中有多少个三连（顺子）
		//判断逻辑：先排序，再遍历，第i张若为n，查看是否存在n+1和n+2
		//数完后删除当前发现的顺子，比如2345万算一个顺，要么算234要么算345
		int res = 0;
		ArrayList<Integer> inspector = (ArrayList<Integer>) pai.clone(); //不能直接操作手牌，临时创建一个检查器
		Collections.sort(inspector); // 排序
		for(int i=0; i<inspector.size(); i++){
			int current = inspector.get(i);
			if(inspector.contains(Integer.valueOf(current+1)) && inspector.contains(Integer.valueOf(current+2))){ //同时存在N,N+1,N+2;说明这是个顺子
				res++;
				inspector.remove(Integer.valueOf(current));
				inspector.remove(Integer.valueOf(current+1));
				inspector.remove(Integer.valueOf(current+2));
				i--; //中途删除过数据，角标回跳，避免检查遗漏或重复
			}
		}
		return res;
	}
	public int countKezi(ArrayList<Integer> pai){ ////数给定的手牌、虚拟手牌中有多少个三同样牌（刻子）
		//判断逻辑：复制，排序，遍历，看当前牌的后两张和当前是否一样
		//注意四张一样的算一个刻子，因为开杠后这只是一套成型的
		int res = 0;
		ArrayList<Integer> inspector = (ArrayList<Integer>) pai.clone(); //不能直接操作手牌，临时创建一个检查器
		Collections.sort(inspector); // 排序
		for(int i=0; i<inspector.size()-2; i++){
			Integer current = inspector.get(i);
			if(current.equals(inspector.get(i+1)) && current.equals(inspector.get(i+2))){
				res++;
				inspector.remove(current);
				inspector.remove(current);
				inspector.remove(current);
				i--; //中途删除过数据，角标回跳，避免检查遗漏或重复
			}
		}
		return res;
	}
	public int countFulu(){ //数出自己的副露中有几套成型牌组，不论顺子还是刻子
		ArrayList<Integer> myFulu = this.fulu.get(playerNum);
		if(myFulu==null) return 0;
		return myFulu.size()/3;
	}
	public int countPair(ArrayList<Integer> pai){ //数有多少对儿，刻子算一对儿，四张算两对儿
		int res = 0;
		ArrayList<Integer> inspector = (ArrayList<Integer>) pai.clone(); //不能直接操作手牌，临时创建一个检查器
		Collections.sort(inspector); // 排序
		for(int i=0; i<inspector.size()-1; i++){
			Integer current = inspector.get(i);
			if(current.equals(inspector.get(i+1))){
				res++;
				inspector.remove(current);
				inspector.remove(current);
				i--; //中途删除过数据，角标回跳，避免检查遗漏或重复
			}
		}
		return res;
	}
	public int countErlian(ArrayList<Integer> pai){ //数有多少二连，比如45万
		//不算顺子，比如345万不含二连
		//判断逻辑：遍历，含N和N+1，但是不含N+2和N-1
		int res = 0;
		ArrayList<Integer> inspector = (ArrayList<Integer>) pai.clone(); //不能直接操作手牌，临时创建一个检查器
		Collections.sort(inspector); // 排序
		removeShunzi(inspector);
		for(int i=0; i<inspector.size(); i++){
			int current = inspector.get(i);
			if(inspector.contains(Integer.valueOf(current+1)) && 
				!inspector.contains(Integer.valueOf(current+2)) && !inspector.contains(Integer.valueOf(current-1))){
					res++;
			}
		}
		return res;
	}
	public int countKong1(ArrayList<Integer> pai){ //数有多少中间嵌张的牌型，比如24万
		//不算顺子，
		//判断逻辑：删除顺子，遍历，含N和N+2，不含N+1
		//字牌跳过，字牌不连
		int res = 0;
		ArrayList<Integer> inspector = (ArrayList<Integer>) pai.clone(); //不能直接操作手牌，临时创建一个检查器
		Collections.sort(inspector); // 排序
		removeShunzi(inspector);
		for(int i=0; i<inspector.size(); i++){
			int current = inspector.get(i);
			if(current>40) continue;
			if(inspector.contains(Integer.valueOf(current+2)) && !inspector.contains(Integer.valueOf(current+1))){
					res++;
			}
		}
		return res;
	}
	public double calculateSingle(ArrayList<Integer> pai){ //启发式算分函数，计算单张得分函数
		//单张的判定方式：不成对儿、刻的字牌都是单张，每个记-2分
		//和左右间隔两张及以上的万条筒也是单张，每张记-1.x分，其中x是该牌数值和5的距离
		//比如单牌一万记-1.4分，因为1到5差4；单牌5万记-1.0分，以此类推
		double res = 0.0;
		ArrayList<Integer> inspector = (ArrayList<Integer>) pai.clone(); //不能直接操作手牌，临时创建一个检查器
		Collections.sort(inspector); // 排序
		for(int i=0; i<inspector.size(); i++){
			int current = inspector.get(i);
			if(current>=40){ //大于40说明是字牌
				//遍历一遍，看看有没有一样的，没有说明是单张
				int count = 0;
				for(int j=0; j<inspector.size(); j++){
					if(inspector.get(j).equals(new Integer(current))){
						count++;
					}
				}
				//如果singe还是true，说明是单张，要扣分
				boolean single = !(count>1);
				if(single) res -= 2.0;
			}
			else{ //不是字牌，用万条筒的单牌判断方式
				//1、9单独判断，因为1、9往左右两个就越界了
				boolean single = true;
				if(current%10==1){
					single = !(inspector.contains(Integer.valueOf(current+1))||inspector.contains(Integer.valueOf(current+2)));
				}
				else if(current%10==9){
					single = !(inspector.contains(Integer.valueOf(current-1))||inspector.contains(Integer.valueOf(current-2)));
				}
				else{
					single = !(inspector.contains(Integer.valueOf(current+1))||inspector.contains(Integer.valueOf(current+2))
								|| inspector.contains(Integer.valueOf(current-1))||inspector.contains(Integer.valueOf(current-2)));
				}
				if(single){ //如果是单牌，要计分
					double distance = Math.abs(current%10-5);
					res -= 1+distance/10;
				}
			}
		}
		return res;
	}
	public double calculateErlian(ArrayList<Integer> pai){ //启发式算分函数，对二连算分
		//每个二连记+10分
		return 10*countErlian(pai);
	}
	public double calculatePair(ArrayList<Integer> pai){ //启发式算分函数，对对子计分
		//首个对子记+100分，剩余对子+10每个
		int count = countPair(pai);
		if(count==0) return 0;
		return 100+10*(count-1);
	}
	public double calculateKong1(ArrayList<Integer> pai){ //启发式算分函数，对空1算分
		//每个空1记+5分
		return 5*countKong1(pai);
	}
	public double calculateShunzi(ArrayList<Integer> pai){ //启发式算分函数，对顺子算分
		//每个顺子+100分
		return 100*countShunzi(pai);
	}
	public double calculateKezi(ArrayList<Integer> pai){ //启发式算分函数，对刻子算分
		//每个刻子+100分
		return 100*countKezi(pai);
	}
	public double calculateFulu(){ //启发式算分函数，对副露算分
		//每套副露+100分
		return 100*countFulu();
	}
	public double calculateRegular(ArrayList<Integer> pai){ //启发式函数，对离胡牌还很远的牌型，算基本得分(总分)
		//也就是对上述计分方式做总和
		return calculateShunzi(pai)+calculateKezi(pai)+calculateFulu()+calculatePair(pai)+
				calculateErlian(pai)+calculateKong1(pai)+calculateSingle(pai);
	}
	public double evaluate(ArrayList<Integer> pai){ //启发式函数最终计算法，从听一直到x3听，最后是基本算分
		//算法简介：先判断是否听，如果听，直接算听得分，否则算x1听...以此类推
		//关于得分倍率：保证上一级的得分一定高于下一级的最高得分，所以可以只算上一级，优化计算量
		//由于基础得分以100为单位，最高分500；所以x3听的倍率是500
		//由于x3听最多有136张牌，简单记为150，所以x2听得分倍率500*150=75,000
		//以此类推，x1听：11,250,000
		//听：1,687,500,000
		double res = 0.0;
		int tingNum = ting(pai);
		// System.out.println("听牌数计算完毕");
		if(tingNum!=0){ //已经听牌了，得分是听牌数*倍率
			res = 11250000 * tingNum;
			return res;
		}else{
			int x1tingNum = x1ting(pai);
			// System.out.println("x1听数计算完毕");
			if(x1tingNum!=0){ //差1听
				res = 75000 * x1tingNum;
				return res;
			}else{
				// int x2tingNum = x2ting(pai);
				//x2听计算太慢了，先不算了
				int x2tingNum = 0;
				// System.out.println("x2听数计算完毕");
				if(x2tingNum!=0){ //差2听
					res = 500 * x2tingNum;
					return res;
				}else{
					res = calculateRegular(pai);
					// System.out.println("基础牌型得分计算完毕");
					return res;
				}
			}
		}
	}
	public int think_discard(){ //AI思考函数，适用于所以需要打出一张牌的情况，包括正常摸牌，吃碰杠后打牌
		//返回想要打出牌的索引
		//算法：遍历所有能打出的牌，看打出哪张之后得分最高，就打那种
		System.out.println("AI正在思考");
		System.out.println("AI is thinking...");
		ArrayList<Integer> inspector = (ArrayList<Integer>)hands.clone(); //不能直接操作手牌，临时创建一个检查器
		Collections.sort(inspector); // 排序
		double highestScore = 0.0;
		int bestMoveIndex = -1;
		for(int i=0; i<inspector.size(); i++){
			System.out.println("正在模拟打出第"+i+"张牌");
			System.out.println("Simulating discard the "+i+ "th tile.");
			//假想打出这张牌
			Integer current = inspector.get(i);
			inspector.remove(current);
			double score = evaluate(inspector);
			System.out.println("当前状态得分："+score);
			System.out.println("Status score: "+score);
			if(score > highestScore){
				highestScore = score;
				bestMoveIndex = i;
			}
			//把假想打出去的这张牌加回来
			inspector.add(current);
			Collections.sort(inspector);
		}
		return bestMoveIndex;
	}
	public boolean think_gang(int gangType, int tile){ //思考函数，思考要不要杠这张牌
		System.out.println("AI正在思考");
		System.out.println("AI is thinking...");
		//加杠不会影响手牌牌效率，加杠直接同意
		if(gangType==2) return true;
		//暗杠和明杠可能对成型手牌造成破坏，要进行思考
		//思考的依据就是：我要把这三张当刻子用，还是当别的用（比如一对儿+顺子中的一张）
		//另一条依据：如果杠之前的得分大于杠之后的得分，绝不杠
		//首先考虑明杠
		if(gangType==3){
			//用前后得分比较决定要不要杠
			double currentScore = evaluate(hands);
			ArrayList<Integer> inspector = (ArrayList<Integer>) hands.clone(); //不能直接操作手牌，临时创建一个检查器
			//假想我杠这张牌，则手牌中的这个暗刻要去掉，变为副露
			inspector.remove(new Integer(tile));
			inspector.remove(new Integer(tile));
			inspector.remove(new Integer(tile));
			//考虑此时的评分
			double scoreAfterGang = evaluate(inspector);
			//这个评分需要加上几部分：开杠后副露里有杠得分，杠之后你多一次摸打机会、杠开机会等等
			//补分原则：只有算分结果是基础牌型才要补分
			if(scoreAfterGang<500) scoreAfterGang += 100;
			//这50分一定要补，因为是摸打机会分
			scoreAfterGang += 50;
			//如果之后分更高，就要杠
			if(scoreAfterGang >= currentScore) return true;
		}
		//下面考虑暗杠
		//暗杠之后要多摸一张再打，由于这张是随机的，不方便用前后得分比较
		//这样考虑：如果不杠，那就是正常打出一张牌，我的最高得分是模拟打出一张牌后能得的最高分
		//如果杠了，我摸一张随机牌，然后打出一张牌，我的预估得分是摸到最差的一张牌但打出最优牌之后的得分
		if(gangType==1){
			//假如不杠
			// double scoreNoGang = 0.0;
			// ArrayList<Integer> inspector = (ArrayList<Integer>) hands.clone(); //不能直接操作手牌，临时创建一个检查器
			// for(int i=0; i<inspector.size(); i++){
			// 	Integer current = inspector.get(i);
			// 	inspector.remove(current);
			// 	double score = evaluate(inspector);
			// 	if(score>scoreNoGang) scoreNoGang = score;
			// 	inspector.add(current);
			// 	Collections.sort(inspector);
			// }
			// double scoreGang = 0.0;
			// //外循环，模拟又摸了一张牌，内循环，模拟打出一张牌
			// for(int i=0; i<tiles.length; i++){
			// 	if(tiles[i] == tile) continue; //不可能摸到杠的这张牌
			// 	inspector = (ArrayList<Integer>) hands.clone(); //不能直接操作手牌，临时创建一个检查器
			// 	inspector.add(new Integer(tiles[i]));

			// }
			//暗杠太复杂了，先默认暗杠吧
			return true;
		}
		return false; //不该进到这个分支，之前就该返回了
	}
	public boolean think_peng(int tile){ //思考要不要碰
		System.out.println("AI正在思考");
		System.out.println("AI is thinking...");
		//判断逻辑：前后分数比较
		double scoreNoPeng = evaluate(hands); //不碰的分数
		// System.out.println("不碰得分："+scoreNoPeng);
		//思考碰之后的分数
		double scorePeng = 0.0;
		ArrayList<Integer> inspector = (ArrayList<Integer>) hands.clone(); //不能直接操作手牌，临时创建一个检查器
		//碰之后，这个对子转换为副露，还要打出一张牌
		inspector.remove(new Integer(tile));
		inspector.remove(new Integer(tile));
		//模拟打出一张牌
		for(int i=0; i<inspector.size(); i++){
			// System.out.println("正模拟打第"+i+"张牌");
			Integer current = inspector.get(i);
			inspector.remove(current);
			double score = evaluate(inspector);
			// System.out.println("碰了再打得分："+score);
			if(score>scorePeng) scorePeng=score;
			inspector.add(current);
			Collections.sort(inspector);
		}
		//还要补上副露里刻子的100分
		if(scorePeng<500) scorePeng += 100;
		if(scorePeng>=scoreNoPeng) return true;
		return false;
	}
	public int think_chi(int tile){ //思考要不要吃，怎么吃
		boolean[] canChi = canChi(tile);
		double scoreNoChi = 0.0;
		double scoreZuoChi = 0.0;
		double scoreZhongChi = 0.0;
		double scoreYouChi = 0.0;
		scoreNoChi = evaluate(hands);
		// System.out.println("不吃得分："+scoreNoChi);
		//模拟左吃
		if(canChi[1]){
			ArrayList<Integer> inspector = (ArrayList<Integer>) hands.clone(); //不能直接操作手牌，临时创建一个检查器
			inspector.remove(new Integer(tile+1));
			inspector.remove(new Integer(tile+2));
			scoreZuoChi = bestScore(inspector);
			if(scoreZuoChi<500) scoreZuoChi += 100; //补分
			// System.out.println("左吃得分："+scoreZuoChi);
		}
		//中吃
		if(canChi[2]){
			ArrayList<Integer> inspector = (ArrayList<Integer>) hands.clone(); //不能直接操作手牌，临时创建一个检查器
			inspector.remove(new Integer(tile+1));
			inspector.remove(new Integer(tile-2));
			scoreZhongChi = bestScore(inspector);
			if(scoreZhongChi<500) scoreZhongChi += 100; //补分
		}
		//右吃
		if(canChi[3]){
			ArrayList<Integer> inspector = (ArrayList<Integer>) hands.clone(); //不能直接操作手牌，临时创建一个检查器
			inspector.remove(new Integer(tile-1));
			inspector.remove(new Integer(tile-2));
			scoreYouChi = bestScore(inspector);
			if(scoreYouChi<500) scoreYouChi += 100; //补分
		}
		double max = 0.0;
		max = scoreNoChi>scoreZuoChi? scoreNoChi : scoreZuoChi;
		max = max>scoreZhongChi? max : scoreZhongChi;
		max = max>scoreYouChi? max : scoreYouChi;
		if(max==scoreNoChi) return 0;
		else if(max == scoreZuoChi) return 1;
		else if(max == scoreZhongChi) return 2;
		else return 3;
	}

	private void removeShunzi(ArrayList<Integer> pai){ //帮助函数，去除成型顺子
		for(int i=0; i<pai.size(); i++){
			int current = pai.get(i); //查看第一张牌
			if(pai.contains(Integer.valueOf(current+1)) && pai.contains(Integer.valueOf(current+2))){ //同时存在N,N+1,N+2;说明这是个顺子
				// 剔除这个顺子，继续检查
				pai.remove(Integer.valueOf(current));
				pai.remove(Integer.valueOf(current+1));
				pai.remove(Integer.valueOf(current+2));
				i--; //中途删除过数据，角标回跳，避免检查遗漏或重复
			}
		}
	}
	private double bestScore(ArrayList<Integer> pai){ //帮助函数，模拟所有能打出的牌，返回打出后能得的最高分
		double bestScore = 0.0;
		ArrayList<Integer> inspector = (ArrayList<Integer>) pai.clone();
		for(int i=0; i<inspector.size(); i++){
			Integer current = inspector.get(i);
			inspector.remove(current);
			double score = evaluate(inspector);
			// System.out.println("碰了再打得分："+score);
			if(score>bestScore) bestScore=score;
			inspector.add(current);
			Collections.sort(inspector);
		}
		return bestScore;
	}
	public double calculateAverageWinSpeed(){ //计算平均胡牌速度的函数
		//想计算平均胡牌速度（平均多少手牌后胡牌），需要总操作张数以及胡牌次数
		return (averageWinSpeed*(wins-1)+moves)/wins;
	}
}