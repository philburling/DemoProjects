package com.blahsoft.ai;

import java.util.ArrayList;

import com.blahsoft.boardgame.GameStateModelInterface;
import com.blahsoft.boardgame.Move;

/* This is an implementation of the minimax algorithm for a two player zero-sum game. */
/* To extend the number of players, re-factor opponentToken as the ArrayList passed into the constructor. */
/* This class is not currently designed to accommodate evaluating the game-tree to a specific number of ply or Alpha-Beta pruning.
 * If you want those features you should implement a new subclass of AIModule for it.
 */
public class Minimax extends AIStrategy {

	/*
	 * This method conducts a depth-first exploration of possible moves and their resulting states and returns the move which maximises the
	 * chances of winning for the player given the expectation that the opponent will always make the optimal move in response.
	 */
	
	@Override
	public String toString() {
		return "minimax";
	}
	
	@Override
	public Move getMove(GameStateModelInterface state, char playerToken, char opponentToken) {
		this.playerToken = playerToken;
		this.opponentToken = opponentToken;

		ArrayList<Move> availableMoves = state.getAvailableMoves();

		if (availableMoves.isEmpty()) {
			return null;
		}

		Move bestMove = null;
		int bestMoveUtility = 0;

		for (Move move : availableMoves) {
			if (bestMove == null) {
				bestMove = move;
				bestMoveUtility = min(state.clone().addMove(move, playerToken));
			} else {
				int moveUtility = min(state.clone().addMove(move, playerToken));
				if (moveUtility > bestMoveUtility) {
					bestMove = move;
					bestMoveUtility = moveUtility;
				}
			}
		}
		return bestMove;
	}

	// Find the utility of the best move the AI could make, given the best subsequent opponent move.
	private int max(GameStateModelInterface state) {
		int utility = state.getUtility(playerToken, opponentToken);
		if (utility != GameStateModelInterface.GAME_UNFINISHED) {
			return utility;
		} else {
			ArrayList<Move> availableMoves = state.getAvailableMoves();
			Move bestMove = null;
			int bestMoveUtility = -999;
			// Find maximum utility returned by min function;
			for (Move move : availableMoves) {
				if (bestMove == null) {
					bestMove = move;
					bestMoveUtility = min(state.clone().addMove(move, playerToken));
				} else {
					int moveUtility = min(state.clone().addMove(move, playerToken));
					if (moveUtility > bestMoveUtility) {
						bestMove = move;
						bestMoveUtility = moveUtility;
					}
				}
			}
			return bestMoveUtility;
		}
	}

	// Find the utility (for the AI) of the best move the AI's opponent could make, given the best subsequent AI move.
	private int min(GameStateModelInterface state) {
		int utility = state.getUtility(playerToken, opponentToken);
		if (utility != GameStateModelInterface.GAME_UNFINISHED) {
			return utility;
		} else {
			ArrayList<Move> availableMoves = state.getAvailableMoves();
			Move bestMove = null;
			int bestMoveUtility = 999;
			// Find the minimum utility returned by the max function;
			for (Move move : availableMoves) {
				if (bestMove == null) {
					bestMove = move;
					bestMoveUtility = max(state.clone().addMove(move, playerToken));
				} else {
					int moveUtility = max(state.clone().addMove(move, playerToken));
					if (moveUtility < bestMoveUtility) {
						bestMove = move;
						bestMoveUtility = moveUtility;
					}
				}
			}
			return bestMoveUtility;
		}
	}
}