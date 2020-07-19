package pers.weihengsun.mahjong.game;

public class Test{
	public static void main(String[] args){
		// MahjongTable table = new MahjongTable();
		Player p1 = new Player("dian", 0);
		AIPlayer ai = new AIPlayer("ai",1);

		// //测试牌桌
		// for(int i=table.top; i<table.deck.length; i++){
		// 	System.out.print(MahjongTable.n2p[table.deck[i]]+",");
		// }//牌桌测试结束

		//测试抓牌
		// // Player p1 = new Player("dian", 0);
		// p1.draw_yourself(0,11);p1.draw_other(0,1);p1.draw_other(0,2);p1.draw_other(0,3);
		// p1.draw_yourself(0,12);p1.draw_other(0,1);p1.draw_other(0,2);p1.draw_other(0,3);
		// p1.draw_yourself(0,12);p1.draw_yourself(0,22);p1.draw_yourself(0,32);p1.draw_yourself(0,43);
		// //测试抓牌结束

		// //测试打牌
		// p1.discard_yourself(2); p1.discard_other(1,1); p1.discard_other(2,1); 
		// //测试打牌结束

		//测试吃牌
		//测试别人吃牌
		//左吃
		// p1.draw_other(3); p1.draw_other(3);
		// p1.draw_yourself(1); p1.discard_yourself(1); 
		// p1.chi_other(3,1,1);
		//中吃
		// p1.draw_other(3); p1.draw_other(3);
		// p1.draw_yourself(2); p1.discard_yourself(2); 
		// p1.chi_other(3,2,2);
		//右吃
		// p1.draw_other(3); p1.draw_other(3);
		// p1.draw_yourself(3); p1.discard_yourself(3); 
		// p1.chi_other(3,3,3);
		//测试自己能否吃牌
		// p1.draw_yourself(2); p1.draw_yourself(3); p1.draw_yourself(5);
		// p1.draw_other(3); p1.discard_other(3,4);
		// boolean res = p1.canChi(4);
		// System.out.println("能吃牌吗："+res);
		//测试自己吃牌
		// p1.draw_yourself(2); p1.draw_yourself(3); p1.draw_yourself(5);
		// p1.draw_other(3); p1.discard_other(3,1);
		// p1.chi_yourself(1,1);

		// //测试碰牌
		// p1.draw_yourself(1); p1.draw_yourself(2); p1.draw_yourself(2); 
		// boolean res = p1.canPeng(1);
		// System.out.println(p1.n2p[1]+"可以碰吗："+res);

		// p1.draw_yourself(1);
		// p1.draw_other(2); p1.draw_other(2); p1.draw_other(2); p1.draw_other(2);
		// p1.discard_yourself(1);
		// p1.peng_other(2,1); 

		// p1.draw_yourself(1); p1.draw_yourself(1); p1.draw_yourself(2); 
		// p1.discard_other(3,1);
		// p1.peng_yourself(1);
		//测试碰牌结束

		//测试杠牌
		//测试能否杠的判断
		//测试暗杠
		// p1.draw_yourself(1); p1.draw_yourself(1); p1.draw_yourself(1);
		// p1.draw_yourself(1);
		// int res = p1.canGang(true,1);
		// System.out.println("是否可以暗杠："+res);
		//测试加杠
		// p1.peng_yourself(1);
		// p1.draw_yourself(1);
		// int res = p1.canGang(true,1);
		// System.out.println("是否可以加杠："+res);
		// 测试明杠
		// p1.draw_yourself(1); p1.draw_yourself(1); p1.draw_yourself(1);
		// p1.discard_other(3,1);
		// int res = p1.canGang(false, 1);
		// System.out.println("是否可以明杠："+res);
		//测试别人杠牌
		//测试暗杠
		// p1.draw_other(3); p1.draw_other(3); p1.draw_other(3); p1.draw_other(3);
		// p1.gang_other(3,1,-1);
		//测试加杠
		// p1.draw_other(3); p1.draw_other(3);
		// p1.draw_yourself(1); p1.discard_yourself(1); p1.peng_other(3,1);
		// p1.draw_other(3); p1.gang_other(3,2,1);
		//测试明杠
		// p1.draw_other(3);p1.draw_other(3);p1.draw_other(3);
		// p1.draw_yourself(1);p1.discard_yourself(1);
		// p1.gang_other(3,3,1);
		//测试自己杠牌
		//暗杠
		// p1.draw_yourself(1);p1.draw_yourself(1);p1.draw_yourself(1);p1.draw_yourself(1);
		// p1.gang_yourself(1,1);
		//加杠
		// p1.draw_yourself(1); p1.draw_yourself(1); 
		// p1.draw_other(3); p1.discard_other(3,1); p1.peng_yourself(1);
		// p1.draw_yourself(1); p1.gang_yourself(2,1);
		//明杠
		// p1.draw_yourself(1); p1.draw_yourself(1); p1.draw_yourself(1); 
		// p1.draw_other(3); p1.discard_other(3,1);
		// p1.gang_yourself(3,1);

		//测试胡牌
		//测试胡十三幺
		// p1.draw_yourself(1); p1.draw_yourself(9); p1.draw_yourself(11); p1.draw_yourself(19); p1.draw_yourself(21); p1.draw_yourself(29);
		// p1.draw_yourself(31); p1.draw_yourself(33); p1.draw_yourself(35); p1.draw_yourself(37); p1.draw_yourself(41); p1.draw_yourself(43); p1.draw_yourself(45);
		// p1.draw_yourself(22);
		// boolean res = p1.huShiSanYao(p1.hands);
		// System.out.println("胡十三幺吗："+ res);
		//测试胡七对儿
		// p1.draw_yourself(1); p1.draw_yourself(1); p1.draw_yourself(3); p1.draw_yourself(3); p1.draw_yourself(7); p1.draw_yourself(7); p1.draw_yourself(9);
		// p1.draw_yourself(9); p1.draw_yourself(35); p1.draw_yourself(35); p1.draw_yourself(41); p1.draw_yourself(41); p1.draw_yourself(45); p1.draw_yourself(5);
		// boolean res = p1.huQiDuier(p1.hands);
		// System.out.println("胡七对儿吗："+ res);
		//测试胡标准形式
		// p1.draw_yourself(0,17); p1.draw_yourself(0,18); p1.draw_yourself(0,22); p1.draw_yourself(0,23); p1.draw_yourself(0,24); p1.draw_yourself(0,33); p1.draw_yourself(0,33); p1.draw_yourself(0,16);
		// boolean res = p1.huStandard(p1.hands);
		// System.out.println("胡标准型吗："+ res);
		// 测试是否胡牌
		// p1.draw_yourself(1); p1.draw_yourself(1); p1.draw_yourself(1); p1.draw_yourself(2); p1.draw_yourself(3); p1.draw_yourself(4); p1.draw_yourself(5);
		// p1.draw_yourself(6); p1.draw_yourself(7); p1.draw_yourself(8); p1.draw_yourself(9); p1.draw_yourself(9); p1.draw_yourself(9); 
		// int tile = 21;
		// boolean res = p1.canHu(tile);
		// System.out.println("是否可以胡【"+p1.n2p[tile]+"】："+ res);

		//测试解码
		// int[] decodeRes = p1.decode("3-141");
		// for(int i=0; i<decodeRes.length; i++){
		// 	System.out.print(decodeRes[i]+", ");
		// }
		// System.out.println();

		//测试反应函数
		// p1.draw_yourself(0,11); p1.draw_yourself(0,11);
		// String res = p1.react("32011");
		// System.out.println("收到了"+res);

		//测试ai的听牌检测功能
		// ai.draw_yourself(0,13); ai.draw_yourself(0,12); ai.draw_yourself(0,55); ai.draw_yourself(0,55);
		// ai.discard_other(0,11);
		// int tingNum = ai.ting();
		// System.out.println("听"+tingNum+"张牌");

		//测试ai的差1听功能
		// ai.draw_yourself(0,12); ai.draw_yourself(0,14); ai.draw_yourself(0,15); ai.draw_yourself(0,55);
		// int tingNum = ai.ting(ai.hands);
		// int x1tingNum = ai.x1ting(ai.hands);
		// System.out.println("听"+tingNum+"张牌");
		// System.out.println("有"+x1tingNum+"张牌可以使手牌进入听牌状态");

		//测试差2听功能
		// ai.draw_yourself(0,12); ai.draw_yourself(0,15); ai.draw_yourself(0,53); ai.draw_yourself(0,55);
		// int tingNum = ai.ting(ai.hands);
		// int x1tingNum = ai.x1ting(ai.hands);
		// int x2tingNum = ai.x2ting(ai.hands);
		// System.out.println("听"+tingNum+"张牌");
		// System.out.println("有"+x1tingNum+"张牌可以使手牌进入听牌状态");
		// System.out.println("有"+x2tingNum+"张牌可以使手牌进入差1听状态");

		//测试差3听状态
		// ai.draw_yourself(0,12); ai.draw_yourself(0,51); ai.draw_yourself(0,53); ai.draw_yourself(0,55);
		// int tingNum = ai.ting(ai.hands);
		// int x1tingNum = ai.x1ting(ai.hands);
		// int x2tingNum = ai.x2ting(ai.hands);
		// int x3tingNum = ai.x3ting(ai.hands);
		// System.out.println("听"+tingNum+"张牌");
		// System.out.println("有"+x1tingNum+"张牌可以使手牌进入听牌状态");
		// System.out.println("有"+x2tingNum+"张牌可以使手牌进入差1听状态");
		// System.out.println("有"+x3tingNum+"张牌可以使手牌进入差2听状态");

		//测试数顺子个数
		// ai.draw_yourself(0,12); ai.draw_yourself(0,13); ai.draw_yourself(0,14); ai.draw_yourself(0,15); ai.draw_yourself(0,16); ai.draw_yourself(0,17);
		// int countShun = ai.countShunzi(ai.hands);
		// System.out.println("当前牌包含"+countShun+"个顺子");

		//测试数刻子个数
		// ai.draw_yourself(0,12); ai.draw_yourself(0,12); ai.draw_yourself(0,12); ai.draw_yourself(0,16); ai.draw_yourself(0,16); ai.draw_yourself(0,16);
		// int countKe = ai.countKezi(ai.hands);
		// System.out.println("当前牌包含"+countKe+"个刻子");

		//测试数副露成型量
		//ai.peng_yourself(11); ai.chi_yourself(2,23);
		// int countFulu = ai.countFulu();
		// System.out.println("副露里有"+countFulu+"套成型牌");

		//测试数对子个数
		// ai.draw_yourself(0,12); ai.draw_yourself(0,12); ai.draw_yourself(0,12); ai.draw_yourself(0,12); ai.draw_yourself(0,16); ai.draw_yourself(0,16);
		// int countPair = ai.countPair(ai.hands);
		// System.out.println("当前牌包含"+countPair+"个对子");

		//测试数二连个数
		// ai.draw_yourself(0,12); ai.draw_yourself(0,13); ai.draw_yourself(0,14); ai.draw_yourself(0,15); ai.draw_yourself(0,16); ai.draw_yourself(0,17); ai.draw_yourself(0,18);
		// int countErlian = ai.countErlian(ai.hands);
		// System.out.println("当前牌包含"+countErlian+"个二连");

		//测试数空1个数
		// ai.draw_yourself(0,12); ai.draw_yourself(0,14); ai.draw_yourself(0,16); ai.draw_yourself(0,17); ai.draw_yourself(0,19); ai.draw_yourself(0,15); ai.draw_yourself(0,18);
		// int countKong1 = ai.countKong1(ai.hands);
		// System.out.println("当前牌包含"+countKong1+"个空1");

		//测试数空1个数
		// ai.draw_yourself(0,11); ai.draw_yourself(0,14); ai.draw_yourself(0,17); ai.draw_yourself(0,55); //ai.draw_yourself(0,19); ai.draw_yourself(0,15); ai.draw_yourself(0,18);
		// double singleScore = ai.calculateSingle(ai.hands);
		// System.out.println("当前牌单张得分"+singleScore+"分");

		//测试基本牌型启发式总分
		// ai.draw_yourself(0,43); ai.draw_yourself(0,45); ai.draw_yourself(0,47); //ai.draw_yourself(0,19); ai.draw_yourself(0,21); ai.draw_yourself(0,23); ai.draw_yourself(0,27);
		// // ai.draw_yourself(0,34); ai.draw_yourself(0,38); ai.draw_yourself(0,43); ai.draw_yourself(0,45); ai.draw_yourself(0,31); ai.draw_yourself(0,19);
		// double totalScore = ai.calculateRegular(ai.hands);
		// System.out.println("当前牌基本牌型得分"+totalScore+"分");
		// System.out.println(Double.MAX_VALUE>1687500000);

		//测试评价函数，即当前牌总分
		// ai.draw_yourself(0,11); ai.draw_yourself(0,14); ai.draw_yourself(0,17); ai.draw_yourself(0,55); ai.draw_yourself(0,19); ai.draw_yourself(0,15); ai.draw_yourself(0,18);
		// double totalScore = ai.evaluate(ai.hands);
		// System.out.println("当前牌牌型总分"+totalScore+"分");

		//测试思考打牌函数
		ai.draw_yourself(0,11); ai.draw_yourself(0,11); ai.draw_yourself(0,12); ai.draw_yourself(0,13); ai.draw_yourself(0,14); ai.draw_yourself(0,17); ai.draw_yourself(0,19);
		ai.draw_yourself(0,22); ai.draw_yourself(0,23); ai.draw_yourself(0,24); ai.draw_yourself(0,33); ai.draw_yourself(0,38); ai.draw_yourself(0,39); ai.draw_yourself(0,55);
		int moveIndex = ai.think_discard();
		System.out.println("思考结果：应该打"+ai.n2p[ai.hands.get(moveIndex)]);

		//测试思考是否碰
		// ai.draw_yourself(0,13); ai.draw_yourself(0,13); ai.draw_yourself(0,16); 
		// ai.draw_yourself(0,17); ai.draw_yourself(0,21); ai.draw_yourself(0,22); ai.draw_yourself(0,23);
		// boolean peng = ai.think_peng(13);
		// System.out.println("思考结果是否碰："+peng);

		//测试思考是否吃
		// ai.draw_yourself(0,43); ai.draw_yourself(0,15); ai.draw_yourself(0,16); 
		// ai.draw_yourself(0,17); ai.draw_yourself(0,21); ai.draw_yourself(0,22); ai.draw_yourself(0,23);
		// int chi = ai.think_chi(15);
		// System.out.println("思考结果是否吃："+chi);


	}
}