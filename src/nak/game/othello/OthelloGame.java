package nak.game.othello;

import nak.game.othello.OthelloMgr.Pos;
import nak.game.othello.OthelloStone.OthelloColor;

public class OthelloGame
{
	public static void main( String[] args )
	{

		PlayerIf playerA = new PlayerAbs( OthelloColor.BLACK )
		{
			@Override
			public Pos getPos( OthelloMgr mgr )
			{
				// 最初に見つかった置ける場所を返す
				return mgr.anyPlaceableCell( getColor() );
			}
		};

		PlayerIf playerB = new PlayerAbs( playerA.getColor().opposite() )
		{
			@Override
			public Pos getPos( OthelloMgr mgr )
			{
				//                // 置けるものの中で一番反転できるものを返す
				//                List<Pos> posList = mgr.allPlaceableCell( getColor() );
				//                return null;
				return mgr.anyPlaceableCell( getColor() );
			}
		};

		OthelloMgr mgr = new OthelloMgr( 8, playerA, playerB );
		mgr.showBoard();

		while( !mgr.isEndGame() )
		{
			mgr.play();
			mgr.showBoard();
			if( mgr.isEndGame() )
			{
				break;
			}
		}

	}
}
