package nak.game.othello;

import java.util.concurrent.atomic.AtomicBoolean;

import nak.game.othello.OthelloMgr.Pos;
import nak.game.othello.OthelloStone.OthelloColor;

/**
 * プレイヤーインターフェース
 * @since Vxxxx
 * @pkg
 */
public interface PlayerIf
{
	/** 前ターンにパスしたか */
	final AtomicBoolean _passed = new AtomicBoolean();

	default void setPassed( boolean isPass )
	{
		_passed.set( isPass );
	}

	default boolean isPassed()
	{
		return _passed.get();
	}

	/**
	 * 自分が打つ座標を取得します
	 * @param mgr
	 * @return 配置する座標を指定する。
	 */
	public Pos getPos( OthelloMgr mgr );

	/**
	 * プレイヤーの色を取得する
	 * @return
	 */
	public OthelloColor getColor();

}
