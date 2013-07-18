package com.blahsoft.noughtsandcrosses;

import com.blahsoft.ai.AIStrategy;
import com.blahsoft.boardgame.BoardGameControllerInterface;
import com.blahsoft.boardgame.BoardGameViewModelInterface;

/**
 * @author Phil
 *
 */

/* This class creates the MVC components necessary to run the Noughts & Crosses application */
public class NCMain {

	public static void main(String[] args) {

		AIStrategy simpleNCAI = new SimpleNCAI();
		BoardGameViewModelInterface model = new NCViewModel(simpleNCAI);
		BoardGameControllerInterface controller = new NCController(model); //The view is initialised in the controller's constructor.
	}
}
