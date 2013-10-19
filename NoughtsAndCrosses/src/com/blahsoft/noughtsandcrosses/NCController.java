package com.blahsoft.noughtsandcrosses;

import com.blahsoft.ai.Minimax;
import com.blahsoft.boardgame.ControllerInterface;
import com.blahsoft.boardgame.Model;
import com.blahsoft.boardgame.Move;
import com.blahsoft.designpatterns.ViewInterface;

public class NCController implements ControllerInterface {

	Model model;
	ViewInterface view;
	
	public NCController (Model model) {
		this.model = model;
		this.view = new NCView(this, model);
		view.createView();
	}
	@Override
	public void setOpponentMode(String mode) {
		if (mode.equalsIgnoreCase("ai")) {
			model.setOpponentMode(Model.AI_PLAYER);			
		} else if (mode.equalsIgnoreCase("hotseat")) {
			model.setOpponentMode(Model.HUMAN_PLAYER);
		} else {
			throw new IllegalArgumentException("Unexpected value passed to setOpponentMode");
		}
	}
	@Override
	public void setAIStrategy(String mode) {
		
		if (mode.equalsIgnoreCase("minimax")) {
			model.setAIStrategy(new SimpleNCAI());
		} else if (mode.equalsIgnoreCase("minimax")) {
			model.setAIStrategy(new Minimax());
		}
		
	}
	@Override
	public void setStartingPlayer(char player) {
		model.setStartingPlayer(player);
	}
	@Override
	public void newGame() {
		model.resetBoard();
	}
	@Override
	public void startGame() {
		model.startGame();
	}
	@Override
	//A request from the GUI to add a counter in a particular location.
	// Which move this player is for is determined by the variable in the model representing whose turn it is.
	public void requestMove(Move location) {
		model.requestMove(location);
	}
}
