package com.blahsoft.boardgame;

/* This class contains a pair of coordinates that represents a position on a board in a board game. */
public class Location2D implements Location {
	private int x;
	private int y;
	
	public Location2D(int x, int y) {
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
		Location2D move = (Location2D)o;
		if (move.getX() == x && move.getY() == y) {
			return true;
		} else {
			return false;
		}
	}
}
