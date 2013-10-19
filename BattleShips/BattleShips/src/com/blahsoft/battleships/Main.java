package com.blahsoft.battleships;

import com.blahsoft.ai.AIStrategy;
import com.blahsoft.boardgame.ControllerInterface;

/**
 * @author Phil Burling
 *
 */

/* This class creates Model, View, Controller and AI components in order to run a game of BattleShips */
public class Main {

	public static void main(String[] args) {

		AIStrategy aiStrategy = new AIRandom();
		ModelInterface modelInterface = new Model(aiStrategy);
		/* The View object is initialised in the controller's constructor and maintains it's existence.
		 * The JFrame in the View object will terminate the entire program when the window is terminated by the user */
		ControllerInterface controller = new Controller(modelInterface);
	}
}
