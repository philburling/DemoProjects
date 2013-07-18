package com.blahsoft.noughtsandcrosses;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import com.blah.designpatterns.ViewInterface;
import com.blahsoft.boardgame.BoardGameControllerInterface;
import com.blahsoft.boardgame.BoardGameViewModelInterface;
import com.blahsoft.boardgame.GameStateModelInterface;
import com.blahsoft.boardgame.Move;
import com.blahsoft.boardgame.Move2D;

public class NCView implements ViewInterface, ActionListener, MouseListener {
	//Program components
	BoardGameViewModelInterface viewModel;
	BoardGameControllerInterface controller;
	//UI components
	JFrame viewFrame;
	JPanel contentPanel;
	NCBoardPanel boardPanel;
	JPanel controls;
	JLabel opponentModeLabel;
	ButtonGroup opponentModeButtons;
	JRadioButton playAIButton;
	JRadioButton playHotSeatButton;
	JLabel startingPlayerLabel;
	ButtonGroup startingPlayerButtons;
	JRadioButton crossesStartsButton;
	JRadioButton noughtsStartsButton;
	JButton newGameButton;
	JLabel aiStrategyLabel;
	JComboBox aiStrategyCombobox;
	JMenuBar menuBar;
	Boolean gameOverMessageDisplayed;
	
	public NCView(BoardGameControllerInterface controller, BoardGameViewModelInterface viewModel) {
		this.controller = controller;
		this.viewModel = viewModel;
		viewModel.registerObserver(this);
		gameOverMessageDisplayed = false;
	}

    /* Create the GUI and show it */
	@Override
	public void createView() {
	
		viewFrame = new JFrame();
		/*Define the properties of the main GUI window*/
		viewFrame.setTitle("Noughts & Crosses");
		viewFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		viewFrame.setResizable(false);
		
		/*Build the JPanel the GUI window will use to host its content*/ 
		contentPanel = new JPanel(new BorderLayout());
		viewFrame.setContentPane(contentPanel);
		
		boardPanel = new NCBoardPanel();
		boardPanel.setOpaque(true);
		boardPanel.setBackground(Color.BLACK);
		boardPanel.setPreferredSize(new Dimension (400,400));
		boardPanel.addMouseListener(this);
		contentPanel.add(boardPanel, BorderLayout.LINE_START);
		
		contentPanel.add(createControlPanel());
		
		viewFrame.pack();
		viewFrame.setVisible(true);
	}
	
	@Override
	public void update() {
		NCGameState gameState =  (NCGameState)viewModel.getGameState();
		boardPanel.setBoard(gameState.getBoard());
		boardPanel.repaint();
		if (viewModel.gameIsOver() && !gameOverMessageDisplayed) {
			int utility = (gameState.getUtility('X','O'));
			if (utility == GameStateModelInterface.WIN) {
				JOptionPane.showMessageDialog(boardPanel, "Crosses has won");
			} else if (utility == GameStateModelInterface.LOSS) {
				JOptionPane.showMessageDialog(boardPanel, "Noughts has won");
			} else if (utility == GameStateModelInterface.DRAWN) {
				JOptionPane.showMessageDialog(boardPanel, "The game is drawn");
			} else if (utility == GameStateModelInterface.GAME_UNFINISHED) {
				return;
			}
			gameOverMessageDisplayed = true;
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) { 
		Object source = e.getSource();
		if (source == playAIButton) {
			controller.setOpponentMode("AI");
		} else if (source == playHotSeatButton) {
			controller.setOpponentMode("HotSeat");
		} else if (source == crossesStartsButton) {
			controller.setStartingPlayer('X');
		} else if (source == noughtsStartsButton) {
			controller.setStartingPlayer('O');
		} else if (source == aiStrategyCombobox) {
			controller.setAIStrategy((String)aiStrategyCombobox.getSelectedItem());
		} else if (source == newGameButton) {	
			controller.newGame();
			if (viewModel.getStartingPlayer() == 'X') {
				JOptionPane.showMessageDialog(boardPanel, "Crosses starts");
			} else {
				JOptionPane.showMessageDialog(boardPanel, "Noughts starts");
			}
			controller.startGame();
			gameOverMessageDisplayed = false;
		}
	}
	
	@Override
	public void mouseReleased(MouseEvent e) {
		//Translate the location clicked into board coordinates
		int x = e.getX();
		int y = e.getY();
		int boardWidth = boardPanel.getWidth();
		int boardHeight = boardPanel.getHeight();
		int size = Math.min(boardWidth,boardHeight);
	    int xBorder = boardWidth - size;
	    int yBorder = boardHeight - size;
	    int xBoardPosition = (x-(xBorder/2))/(size/3);
	    int yBoardPosition = (y-(yBorder/2))/(size/3);
	    /* Send these board coordinates as a new request for a move to the controller. The player it is requested for will depend 
	     * on whose turn it is in the model. It will be granted or denied depending on whose turn it is, whether that turn is to 
	     * be handled by AI instead, and whether a game is currently in progress (i.e. started and not finished */
	    Move boardLocationClicked = new Move2D(xBoardPosition, yBoardPosition);
	    controller.requestMove(boardLocationClicked);
	}	
	
	@Override
	public void mouseClicked(MouseEvent e) {}
	@Override
	public void mouseEntered(MouseEvent e) {}
	@Override
	public void mouseExited(MouseEvent e) {}
	@Override
	public void mousePressed(MouseEvent e) {}
	
	private JPanel createControlPanel() {

		controls = new JPanel();
		controls.setLayout(new BoxLayout(controls, BoxLayout.Y_AXIS));
		controls.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
		
		opponentModeLabel = new JLabel("Opponent mode:");
		controls.add(opponentModeLabel);
		playAIButton = new JRadioButton("AI (noughts)");
		playAIButton.setSelected(true);
		controller.setOpponentMode("ai");
		controls.add(playAIButton);
		playHotSeatButton = new JRadioButton("Human");
		controls.add(playHotSeatButton);
		
		controls.add(Box.createRigidArea(new Dimension(0,25)));
		
		aiStrategyLabel = new JLabel("AI strategy:");
		controls.add(aiStrategyLabel);
		
		String[] aiOptions = {"Simple rules", "Minimax"};
		aiStrategyCombobox = new JComboBox(aiOptions);
		controller.setAIStrategy((String)aiStrategyCombobox.getSelectedItem());
		aiStrategyCombobox.setMaximumSize(new Dimension(100, 25));		
		controls.add(aiStrategyCombobox);

		controls.add(Box.createRigidArea(new Dimension(0,25)));
		
		startingPlayerLabel = new JLabel("Who starts?");
		crossesStartsButton = new JRadioButton("Crosses");
		crossesStartsButton.setSelected(true);
		controller.setStartingPlayer('X');
		noughtsStartsButton = new JRadioButton("Noughts");		
		controls.add(startingPlayerLabel);
		controls.add(crossesStartsButton);
		controls.add(noughtsStartsButton);
		
		controls.add(Box.createRigidArea(new Dimension(0,25)));

		newGameButton = new JButton("New game");
		controls.add(newGameButton);

		opponentModeButtons = new ButtonGroup();
		opponentModeButtons.add(playAIButton);
		opponentModeButtons.add(playHotSeatButton);
		
		startingPlayerButtons = new ButtonGroup();
		startingPlayerButtons.add(crossesStartsButton);
		startingPlayerButtons.add(noughtsStartsButton);

		contentPanel.add(controls, BorderLayout.LINE_END);
		
		playAIButton.addActionListener(this);
		playHotSeatButton.addActionListener(this);
		crossesStartsButton.addActionListener(this);
		noughtsStartsButton.addActionListener(this);
		newGameButton.addActionListener(this);		

		aiStrategyCombobox.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		return controls;
	}
}