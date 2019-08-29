package nak.game.othello;

/**
 * オセロ石
 */
public class OthelloStone
{
	/** オセロ石の表・裏の色 */
	public enum OthelloColor
	{
			BLACK, WHITE;

		/** 逆の色を取得する */
		public OthelloColor opposite()
		{
			return this == BLACK ? WHITE : BLACK;
		}

		@Override
		public String toString()
		{
			return this == OthelloColor.BLACK ? "●" : "〇";
		}
	}

	/** 表示されている色 */
	private OthelloColor _color;

	/** 配置されたX座標 */
	private int posX = -1;
	/** 配置されたY座標 */
	private int posY = -1;

	/** 全ての方向に石が配置済か？ */
	private boolean isSorrounded = false;

	/** コンストラクタ */
	public OthelloStone( OthelloColor color )
	{
		_color = color;
	}

	/** コンストラクタ */
	public OthelloStone( OthelloColor color, int x, int y )
	{
		_color = color;
		posX = x;
		posY = y;
	}

	/**
	 * オセロの色を逆転させます。
	 */
	public void reverse()
	{
		_color = getColor() == OthelloColor.BLACK ? OthelloColor.WHITE : OthelloColor.BLACK;
	}

	/**
	 * 表示されている色を取得します。
	 * @return
	 */
	public OthelloColor getColor()
	{
		return _color;
	}

	/**
	 * 石の指定座標を設定します。
	 * @param posX
	 * @param posY
	 */
	public void setPos( int posX, int posY )
	{
		this.posX = posX;
		this.posY = posY;
	}

	/**
	 * 石のX座標を取得します。
	 * @return
	 */
	public int getPosX()
	{
		return posX;
	}

	/**
	 * 石のY座標を取得します。
	 * @return
	 */
	public int getPosY()
	{
		return posY;
	}

	/**
	 * 全ての方向に石が配置済。
	 * @return
	 */
	public boolean isSorrounded()
	{
		return isSorrounded;
	}

	/**
	 * 全ての方向に石が配置済か設定します。
	 * @return
	 */
	public void setSorrounded( boolean isSorrounded )
	{
		this.isSorrounded = isSorrounded;
	}

	@Override
	public String toString()
	{
		return getColor().toString();
	}
}
