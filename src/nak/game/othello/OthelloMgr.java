package nak.game.othello;

import java.util.*;

import nak.game.othello.OthelloStone.OthelloColor;

/**
 * オセロのルール管理クラス
 */
public class OthelloMgr
{
	/** 盤面を表すオセロ石 */
	private OthelloStone[][] _board;

	/** 配置済の石 */
	private Set<OthelloStone> placedStones = new HashSet<>();

	/** プレイヤー */
	private List<PlayerIf> _players;
	private int playerIdx = -1;

	/**
	 * コンストラクタ
	 * @param size
	 */
	public OthelloMgr( int size, PlayerIf... players )
	{
		initBoard( size );

		_players = new ArrayList<>( Arrays.asList( players ) );
	}

	/** 座標を表すクラス */
	public class Pos
	{
		int x, y;

		public Pos( int x, int y )
		{
			this.x = x;
			this.y = y;
		}
	}

	/** オセロ盤の初期状態設定 */
	private void initBoard( int size )
	{
		if( size % 2 != 0 )
		{
			throw new IllegalArgumentException( "オセロ版の大きさは偶数でなければいけません。" );
		}

		_board = new OthelloStone[ size ][ size ];

		// 初期配置
		int centerPos = ( size / 2 );
		placeStone( new OthelloStone( OthelloColor.WHITE, centerPos - 1, centerPos - 1 ) );
		placeStone( new OthelloStone( OthelloColor.BLACK, centerPos - 1, centerPos ) );
		placeStone( new OthelloStone( OthelloColor.BLACK, centerPos, centerPos - 1 ) );
		placeStone( new OthelloStone( OthelloColor.WHITE, centerPos, centerPos ) );
	}

	/**
	 * 次の手番のプレイヤーを取得します
	 * @return
	 */
	public PlayerIf getNextPlayer()
	{
		playerIdx = playerIdx == -1 ? 0 : _players.size() - 1 <= playerIdx ? 0 : ++playerIdx;
		return _players.get( playerIdx );
	}

	/**
	 * １手進めます
	 */
	public void play()
	{
		PlayerIf player = getNextPlayer();
		Pos pos = player.getPos( this );
		if( pos == null )
		{
			// 場所指定なしはパス扱い
			player.setPassed( true );
			System.out.println( player.getColor().toString() + "パス！" );
			return;
		}
		OthelloStone stone = new OthelloStone( player.getColor(), pos.x, pos.y );

		// 置けない場合はパス扱い
		boolean isPlaceable = isPlaceableCell( stone );
		if( isPlaceable )
		{
			System.out.println(
				String.format( "(%s,%s)%s", String.valueOf( stone.getPosX() + 1 ), String.valueOf( stone.getPosY() + 1 ), stone.toString() ) );
			placeStone( stone );
		}
		player.setPassed( !isPlaceable );
	}

	/**
	 * ゲーム続行可能かの判定
	 * @return
	 */
	public boolean isEndGame()
	{
		// 全てのマス目が埋まっている
		int edgeLength = _board.length;
		if( Math.pow( edgeLength, 2 ) == placedStones.size() )
		{
			return true;
		}

		// 両者とも置ける場所が無くなった（パスをしている）
		if( _players.stream().allMatch( p -> p.isPassed() ) )
		{
			return true;
		}

		return false;
	}

	/**
	 * 最初に見つかった配置できる座標を返却します
	 * @param color
	 * @return
	 */
	public Pos anyPlaceableCell( OthelloColor color )
	{
		for( OthelloStone stone : placedStones )
		{
			for( PlaceVector v : PlaceVector.values() )
			{
				int posX = stone.getPosX() + v._x;
				int posY = stone.getPosY() + v._y;
				if( isPlaceableCell( new OthelloStone( color, posX, posY ) ) )
				{
					return new Pos( posX, posY );
				}
			}
			stone.setSorrounded( true );
		}
		return null;
	}

	public List<Pos> allPlaceableCell( OthelloColor color )
	{
		return null;
	}

	/**
	 * オセロ盤の範囲外の領域が指定されていないか？
	 * @param x
	 * @param y
	 * @return
	 */
	public boolean isOutOfBoard( int x, int y )
	{
		int boardSize = _board.length - 1;
		return x < 0 || y < 0 || boardSize < x || boardSize < y;
	}

	/**
	 * 指定座標に石を配置します。
	 * @param x
	 * @param y
	 * @param stone
	 */
	public void placeStone( OthelloStone stone )
	{
		int x = stone.getPosX();
		int y = stone.getPosY();

		if( isOutOfBoard( x, y ) )
		{
			return;
		}
		_board[ y ][ x ] = stone;
		placedStones.add( stone );

		// 周りの石をひっくり返す
		reverseStone( stone );
	}

	/**
	 * 石を置いた場合に周りの石を反転させます
	 * @param stone
	 */
	private void reverseStone( OthelloStone stone )
	{
		int posX = stone.getPosX();
		int posY = stone.getPosY();

		for( PlaceVector v : PlaceVector.values() )
		{
			// 指定方向の同色の石を取得
			OthelloStone nextSameStone = getNextStone( posX, posY, v, stone.getColor() );
			if( nextSameStone == null || isContiguous( stone, nextSameStone ) )
			{
				continue;
			}
			OthelloStone from = stone;
			while( ( from = getNextStone( from.getPosX(), from.getPosY(), v ) ) != nextSameStone )
			{
				from.reverse();
			}
		}
	}

	/**
	 * 指定位置のオセロ石を取得します。
	 * @param x
	 * @param y
	 * @return
	 */
	public OthelloStone getStone( int x, int y )
	{
		return isOutOfBoard( x, y ) ? null : _board[ y ][ x ];
	}

	/**
	 * 指定ベクトルに移動した位置の石を取得します
	 * @param x
	 * @param y
	 * @param v
	 * @return
	 */
	public OthelloStone getNextStone( int x, int y, PlaceVector v )
	{
		int posX = x + v._x;
		int posY = y + v._y;
		return getStone( posX, posY );
	}

	/**
	 * 指定方向に向かって最初に見つかった指定色の石を返却します
	 * @param x
	 * @param y
	 * @param v
	 * @param color
	 * @return
	 */
	public OthelloStone getNextStone( int x, int y, PlaceVector v, OthelloColor color )
	{
		OthelloStone stone = getNextStone( x, y, v );
		return stone == null ? null : stone.getColor() == color ? stone : getNextStone( stone.getPosX(), stone.getPosY(), v, color );
	}

	/**
	 * 隣接している石どうしか？
	 * @param a
	 * @param b
	 * @return
	 */
	public boolean isContiguous( OthelloStone a, OthelloStone b )
	{
		return Math.abs( a.getPosX() - b.getPosX() ) <= 1 && Math.abs( a.getPosY() - b.getPosY() ) <= 1;
	}

	/**
	 * 配置可能な場所か
	 * @param x
	 * @param y
	 * @param stone
	 * @return
	 */
	public boolean isPlaceableCell( OthelloStone stone )
	{
		int x = stone.getPosX();
		int y = stone.getPosY();

		// 置ける範囲か
		if( isOutOfBoard( x, y ) )
		{
			return false;
		}

		// 既に置かれていないか
		if( getStone( x, y ) != null )
		{
			return false;
		}

		for( PlaceVector v : PlaceVector.values() )
		{
			// 指定方向の同色の石を取得
			OthelloStone nextSameStone = getNextStone( x, y, v, stone.getColor() );
			if( nextSameStone == null )
			{
				continue;
			}
			// 隣接していた場合は不可
			if( isContiguous( stone, nextSameStone ) )
			{
				continue;
			}

			return true;
		}

		return false;
	}

	/**
	 * 現在の盤面を標準出力に表示します。
	 */
	public void showBoard()
	{
		// 盤面表示
		int boardSize = _board.length;
		StringBuilder sb = new StringBuilder();
		for( int i = 0; i < boardSize + 1; i++ )
		{
			for( int j = 0; j < boardSize + 1; j++ )
			{
				String s = "　";
				if( i == 0 || j == 0 )
				{
					s = i != 0 ? "" + i : j != 0 ? "" + j : s;
					if( !"　".equals( s ) )
					{
						// 半角数字を全角数字へ
						s = String.valueOf( ( (char) ( s.charAt( 0 ) + 0xFEE0 ) ) );
					}
				}
				else
				{
					OthelloStone stone = _board[ i - 1 ][ j - 1 ];
					if( stone != null )
					{
						s = stone.toString();
					}
				}
				sb.append( s ).append( "|" );
			}
			sb.append( "\n" );
		}
		System.out.println( sb.toString() );

		// 枚数表示
		int black = (int)placedStones.stream().filter( s -> s.getColor() == OthelloColor.BLACK ).count();
		int white = placedStones.size() - black;
		System.out.println( OthelloColor.BLACK.toString() + ":" + black + "、" + OthelloColor.WHITE.toString() + ":" + white + "\n" );
	}

}
