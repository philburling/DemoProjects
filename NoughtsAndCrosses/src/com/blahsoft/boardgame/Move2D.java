package com.blahsoft.boardgame;


/* This class contains a pair of coordinates for use as a move in a board game. */
public class Move2D implements Move {
	private int x;
	private int y;
	
	public Move2D(int x, int y) {
		this.x = x;
		this.y = y;
	}
	public int getX() {
		return x;
	}
	public int getY() {
		return y;
	}
	public void setX(int x) {
		this.x = x;
	}
	public void setY(int y) {
		this.y = y;
	}
	
	@Override
	public String toString() {
		return "(" + Integer.toString(x) + ", " + Integer.toString(y) + ")";
	}
	
	@Override
	public boolean equals(Object o) {
		Move2D move = (Move2D)o;
		if (move.getX() == x && move.getY() == y) {
			return true;
		} else {
			return false;
		}
	}
}
