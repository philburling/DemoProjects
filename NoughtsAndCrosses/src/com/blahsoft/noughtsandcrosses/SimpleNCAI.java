package com.blahsoft.noughtsandcrosses;

import java.util.ArrayList;
import java.util.Random;

import com.blahsoft.ai.AIStrategy;
import com.blahsoft.boardgame.GameStateModelInterface;
import com.blahsoft.boardgame.Move;
import com.blahsoft.boardgame.Move2D;

public class SimpleNCAI extends AIStrategy {
	
	@Override
	public String toString() {
		return "simplerulesnc";
	}
	
	@Override
	// Pre-condition: There should be at least one available move.
	public Move getMove(GameStateModelInterface state, char playerToken, char opponentToken) {

		this.playerToken = playerToken;
		this.opponentToken = opponentToken;

		ArrayList<Move> availableMoves = state.getAvailableMoves();

		// Check each move to see if there is a winning move.
		for (Move move : availableMoves) {
			if (state.isMoveType(move, GameStateModelInterface.WINNING_MOVE, playerToken, opponentToken)) {
				return move;
			}
		}
		// If no winning move. Check if an imminent opponent win needs to be blocked.
		for (Move move : availableMoves) {
			if (state.isMoveType(move, GameStateModelInterface.BLOCKS_OPPONENT_WIN, playerToken, opponentToken)) {
				return move;
			}
		}
		// If the middle square is not occupied. Then take it.
		char[][] board = ((NCGameState) state).getBoard();
		/* N.B. If the board size were ever to change then this rule will need alteration. */
		if (!(board[1][1] == playerToken || board[1][1] == opponentToken)) {
			return new Move2D(1, 1);
		} else {
			// If the middle is taken, pick an available move at random.
			Random randomNumberGenerator = new Random(System.nanoTime());
			return availableMoves.get(randomNumberGenerator.nextInt(availableMoves.size()));
		}
	}
}
