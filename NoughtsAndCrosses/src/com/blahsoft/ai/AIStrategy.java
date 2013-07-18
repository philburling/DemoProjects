package com.blahsoft.ai;

import com.blahsoft.boardgame.GameStateModelInterface;
import com.blahsoft.boardgame.Move;

/* This interface is for getting appropriate AI moves in a two-player game */
/* For games with multiple opponents, consider extending this with an array for multiple opponent tokens
 * and an overload of getMove() with an array of characters instead of a single character. If a game has alliances, it may be useful
 * to have an additional character array for ally tokens */

public abstract class AIStrategy {
	protected char playerToken; /* A token to represent the player who the move is being made on behalf of */
	protected char opponentToken;
	public abstract String toString(); /* This method should be overridden from Object to provide the name of the AI in lower-case */
	public abstract Move getMove(GameStateModelInterface state, char playerToken, char opponentToken);
	// N.B. 'null' will be returned if, for whatever reason, no move is possible
}
