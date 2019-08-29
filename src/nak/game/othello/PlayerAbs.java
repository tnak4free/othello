package nak.game.othello;

import nak.game.othello.OthelloStone.OthelloColor;

public abstract class PlayerAbs implements PlayerIf
{
	private OthelloColor color;

	public PlayerAbs( OthelloColor color )
	{
		this.color = color;
	}

	@Override
	public OthelloColor getColor()
	{
		return this.color;
	}
}
