package nak.game.othello;

/**
 * 配置方向定義
 */
public enum PlaceVector
{
		/** 上 */
		UP(0, 1),
		/** 右上 */
		UPRIGHT(1, 1),
		/** 右 */
		RIGHT(1, 0),
		/** 右下 */
		BOTTOMRIGHT(1, -1),
		/** 下 */
		BOTTOM(0, -1),
		/** 左下 */
		BOTTOMLEFT(-1, -1),
		/** 左 */
		LEFT(-1, 0),
		/** 左上 */
		UPLEFT(-1, 1);

	public int _x, _y;

	private PlaceVector( int x, int y )
	{
		_x = x;
		_y = y;
	}
}
