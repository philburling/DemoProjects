package com.blahsoft.noughtsandcrosses;

import java.util.ArrayList;

import com.blahsoft.boardgame.GameStateModelInterface;
import com.blahsoft.boardgame.Move;
import com.blahsoft.boardgame.Move2D;


public class NCGameState implements GameStateModelInterface {
	
	private char[][] board; 
	private static final int BOARD_SIZE = 3;
	private boolean crossesTurn;
	
	public NCGameState() {
		board = new char[BOARD_SIZE][BOARD_SIZE]; //A char representation of the board has been chosen for an intuitive representation and to allow for easy potential console representation in place of a GUI.
		crossesTurn = true; //Default value. Normally this should be initialised by a command originating from the GUI.
	} 

	@Override
	public NCGameState clone() {
		NCGameState clone = new NCGameState();
		System.arraycopy(board,  0, clone.board, 0, BOARD_SIZE);
		clone.crossesTurn = crossesTurn;
		return clone;
	}	
	
	/* Changes to the board should be made only through addMove and Reset methods.*/
	public char[][] getBoard() {
		return board; 
	}
	
	@Override
	public char getPlayerTurn() {
		if (crossesTurn) {
			return 'X';
		} else {
			return 'O';
		}
	}

	
	@Override
	public ArrayList<Move> getAvailableMoves() {
		
		ArrayList<Move> availableMoves = new ArrayList<Move>();
		
		for (int x=0; x<BOARD_SIZE; x++) {
			for(int y=0; y<BOARD_SIZE; y++) {
				if (board[x][y] == '\u0000' || (board[x][y] == ' ')) {
					availableMoves.add(new Move2D(x,y));
				}
			}
		}
		return availableMoves;
	}

	@Override
	public int getUtility(char playerToken, char opponentToken) {

		/* This implementation checks to see if all entries in any row, column, or diagonal are equal to the one of the two player tokens
		 * to determine a winner. If there is no winner it will determine if there are any positions left on the board (GAME_UNFINISHED)
		 * or if they have been taken (DRAW). 
		 * N.B. This implementation will only work for games where a whole row, column or diagonal must be occupied on the board.
		 */

		//Look for a diagonal of matching consecutive tokens
		{
			int sumPlayerToken = 0;
			int sumOpponentToken = 0;
			for (int i=0; i<BOARD_SIZE; i++) {
				if ((sumPlayerToken > 0 && board[i][i] == opponentToken) || 
					(sumOpponentToken > 0 && board[i][i] == playerToken)  ||
					(board[i][i] != playerToken && board[i][i] != opponentToken)) {
					break;
				} else {
					if (board[i][i] == playerToken) { 
						sumPlayerToken++; 
					}else if (board[i][i] == opponentToken) { 
						sumOpponentToken++;
					}
				}
			}
			if (sumPlayerToken>=BOARD_SIZE) {
				return WIN;
			} else if (sumOpponentToken>=BOARD_SIZE) {
				return LOSS;
			}
			sumPlayerToken = 0;
			sumOpponentToken = 0;
			for (int x=(BOARD_SIZE-1), y=0; y<BOARD_SIZE; x--, y++) {
				if ((sumPlayerToken > 0 && board[x][y] == opponentToken) || 
					(sumOpponentToken > 0 && board[x][y] == playerToken)  ||
					(board[x][y] != playerToken && board[x][y] != opponentToken)) {
						break;
				} else {
					if (board[x][y] == playerToken) { 
						sumPlayerToken++; 
					} else if (board[x][y] == opponentToken) { 
						sumOpponentToken++;
					}
				}
			}
			if (sumPlayerToken>=BOARD_SIZE) {
				return WIN;
			} else if (sumOpponentToken>=BOARD_SIZE) {
				return LOSS;
			}
		}
		
		//Look for a column of matching tokens
		for (char[] column : board) {
			int sumPlayerToken = 0;
			int sumOpponentToken = 0;
			for (char cell : column ) {
				if ((sumPlayerToken>0 && cell == opponentToken) || 
					(sumOpponentToken>0 && cell == playerToken)  ||
					(cell != playerToken && cell != opponentToken)) {
					break; //go to the next row.
				} else {
					if (cell == playerToken) { 
						sumPlayerToken++; 
					}else if (cell == opponentToken) { 
						sumOpponentToken++;
					}
				}
			}
			if (sumPlayerToken >= BOARD_SIZE) {
				return WIN;
			} else if (sumOpponentToken >= BOARD_SIZE) {
				return LOSS;
			}
		}

		//Look for a row of matching tokens. Also check to see if there is an unoccupied board position (to save a future board-scan if no win is detected) 
		for (int row=0; row<BOARD_SIZE; row++) {
			int sumPlayerToken = 0;
			int sumOpponentToken = 0;
			for (int cell=0; cell<BOARD_SIZE; cell++) {
				if ((sumPlayerToken > 0 && board[cell][row] == opponentToken) || 
					(sumOpponentToken > 0 && board[cell][row] == playerToken)) {
					break; //go to the next row.
				} else {
					if (board[cell][row] == playerToken) { 
						sumPlayerToken++; 
					}else if (board[cell][row] == opponentToken) { 
						sumOpponentToken++;
					}
				}
			}
			if (sumPlayerToken>=BOARD_SIZE) {
				return WIN;
			} else if (sumOpponentToken>=BOARD_SIZE) {
				return LOSS;
			}
		}
		//As no win is detected, check there are no free spaces
		if (getAvailableMoves().isEmpty()) { return DRAWN; } else { return GAME_UNFINISHED; }
	}
	
	@Override
	public void setPlayerTurn(char playerToken) {
		if (playerToken == 'X') {
			crossesTurn = true;
		} else {
			crossesTurn = false;
		}
	}
	@Override
	public void nextTurn() {
		if (crossesTurn) {
			crossesTurn = false;
		} else {
			crossesTurn = true;
		}
	}

	@Override //Returns this GameStateInterface but altered in accordance with the input move. 
	public GameStateModelInterface addMove(Move move, char playerToken) {
		Move2D  moveXY = (Move2D)move;
		board[moveXY.getX()][moveXY.getY()] = playerToken;
		return this;
	}
	
	@Override
	public boolean isMoveType (Move move, int type, char playerToken, char opponentToken) {
		Move2D move2D = (Move2D)move;
		return (isMoveTypeForRow(move2D, type, playerToken, opponentToken) || isMoveTypeForColumn(move2D, type, playerToken, opponentToken) || isMoveTypeForDiagonal(move2D, type, playerToken, opponentToken));
	}
	
	private boolean isMoveTypeForRow(Move2D move, int condition, char playerToken, char opponentToken) {
		int rowValue = 0;
		for (int x=0; x<BOARD_SIZE; x++) {
			if (board[x][move.getY()] == opponentToken) {
				rowValue -= 1;
			} else if (board[x][move.getY()] == playerToken) {
				rowValue += 1;
			}
			
		}
		return rowValue==condition;
	}

	private boolean isMoveTypeForColumn(Move2D move, int condition, char playerToken, char opponentToken) {
		int columnValue = 0;
		for (int y=0; y<BOARD_SIZE; y++) {
			if (board[move.getX()][y] == opponentToken) {
				columnValue -= 1;
			} else if (board[move.getX()][y] == playerToken) {
				columnValue += 1;
			}
		}
		return columnValue==condition;
	}

	private boolean isMoveTypeForDiagonal(Move2D move, int condition, char playerToken, char opponentToken) {
		
		int diagonalValue = 0;
		
		if ((move.getX()==0 && move.getY()==0) || (move.getX()==1 && move.getY()==1) || ( move.getX()==2 && move.getY()==2)) { //This logic makes assumptions about board size
			for (int i=0; i<BOARD_SIZE; i++) {
				if (board[i][i] == opponentToken) {
					diagonalValue -= 1;
				} else if (board[i][i] == playerToken) {
					diagonalValue += 1;
				}
			}
			if (diagonalValue==condition ) {
				return diagonalValue==condition;
			} else {
				diagonalValue = 0; //For the case where x and y are both 1 (the middle square) 
			}
		}
		
		if ((move.getX()==2 && move.getY()==0) || (move.getX()==1 && move.getY()==1) || (move.getX()==0 && move.getY()==2)) { //This logic makes assumptions about board size
			for (int x=BOARD_SIZE-1, y=0; y<BOARD_SIZE; x--, y++) {
				if (board[x][y] == opponentToken) {
					diagonalValue -= 1;
				} else if (board[x][y] == playerToken) {
					diagonalValue += 1;
				}
			}
		}
		return diagonalValue==condition;
	}
}