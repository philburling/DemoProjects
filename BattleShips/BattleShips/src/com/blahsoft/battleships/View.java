package com.blahsoft.battleships;
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
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import com.blahsoft.boardgame.ControllerInterface;
import com.blahsoft.boardgame.Location;
import com.blahsoft.boardgame.Location2D;
import com.blahsoft.boardgame.ViewInterface;

/* This class provides an implementation of a graphical-user-interface for a game of BattleShips. */
public class View implements ViewInterface, ActionListener, MouseListener {
	//Program components
	ModelInterface model;
	ControllerInterface controller;
	//UI components
	private JFrame viewFrame;
	private JPanel contentPanel;
	private BoardPanel currentPlayerBoardPanel;
	private BoardPanel opponentBoardPanel;
	private JPanel controls;
	private JLabel opponentModeLabel;
	private ButtonGroup opponentModeButtons;
	private JRadioButton playAIButton;
	private JRadioButton playHotSeatButton;
	private JButton newGameButton;
	private JButton instructionsButton;
	private JLabel aiStrategyLabel;
	private JComboBox aiStrategyCombobox;
	private static final int PANEL_SIZE = 400;

/*-------------------------------------------
 * 
 * View initialisation
 * 
 * -------------------------------------------	
 */
	
	public View(ControllerInterface controller, ModelInterface viewModel) {
		this.controller = controller;
		this.model = viewModel;
		viewModel.registerObserver(this);
		createView();
	}

    /* A helper method to create a GUI and show it. */
	private void createView() {
	
		viewFrame = new JFrame();
		/*Define the properties of the main GUI window*/
		viewFrame.setTitle("Battleships");
		viewFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		viewFrame.setResizable(false);
		
		/*Build the JPanel the GUI window will use to host its content*/ 
		contentPanel = new JPanel(new BorderLayout());
		viewFrame.setContentPane(contentPanel);
		
		currentPlayerBoardPanel = createBoardPanel(model.getCurrentPlayer().getBoard());
		opponentBoardPanel = createBoardPanel(model.getCurrentPlayer().getOpponent().getBoard());
		
		contentPanel.add(opponentBoardPanel, BorderLayout.CENTER);
		contentPanel.add(currentPlayerBoardPanel, BorderLayout.LINE_START);
		opponentBoardPanel.setBackground(Color.BLACK);
		currentPlayerBoardPanel.setUndiscoveredShipsDisplayed(true);
		
		contentPanel.add(createControlPanel(), BorderLayout.LINE_END);
		viewFrame.pack();
		viewFrame.setVisible(true);
	}
	
	/* A helper method for createView */
	private BoardPanel createBoardPanel(char[][] board) {
		BoardPanel boardPanel = new BoardPanel(board);
		boardPanel.setOpaque(true);
		boardPanel.setBackground(Color.DARK_GRAY);
		boardPanel.setPreferredSize(new Dimension (PANEL_SIZE,PANEL_SIZE));
		boardPanel.addMouseListener(this);
		return boardPanel;
	}

	/* A helper method for createView */
	private JPanel createControlPanel() {

		controls = new JPanel();
		controls.setLayout(new BoxLayout(controls, BoxLayout.Y_AXIS));
		controls.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
		
		opponentModeLabel = new JLabel("Opponent mode:");
		controls.add(opponentModeLabel);
		playAIButton = new JRadioButton("AI (player 2)");
		playAIButton.setSelected(true);
		controller.setOpponentIsAI(true);
		controls.add(playAIButton);
		playHotSeatButton = new JRadioButton("Human");
		controls.add(playHotSeatButton);
		
		controls.add(Box.createRigidArea(new Dimension(0,25)));
		
		aiStrategyLabel = new JLabel("AI strategy:");
		controls.add(aiStrategyLabel);
		
		String[] aiOptions = {"Random"};
		aiStrategyCombobox = new JComboBox(aiOptions);
		controller.setAIStrategy((String)aiStrategyCombobox.getSelectedItem());
		aiStrategyCombobox.setMaximumSize(new Dimension(100, 25));		
		controls.add(aiStrategyCombobox);

		controls.add(Box.createRigidArea(new Dimension(0,25)));
		
		instructionsButton  = new JButton("Instructions");
		
		controls.add(instructionsButton);

		controls.add(Box.createRigidArea(new Dimension(0,25)));
		
		newGameButton = new JButton("New game");
		controls.add(newGameButton);

		
		opponentModeButtons = new ButtonGroup();
		opponentModeButtons.add(playAIButton);
		opponentModeButtons.add(playHotSeatButton);
		
		contentPanel.add(controls, BorderLayout.LINE_END);
		
		playAIButton.addActionListener(this);
		playHotSeatButton.addActionListener(this);
		instructionsButton.addActionListener(this);
		newGameButton.addActionListener(this);		

		aiStrategyCombobox.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		return controls;
	}
	
/*---------------------------------------------------------------------------
 * 
 * User-generated events
 * 
 * ----------------------------------------------------------------------
 */
	
	@Override
	public void actionPerformed(ActionEvent e) { 
		Object source = e.getSource();
		if (source == playAIButton) {
			controller.setOpponentIsAI(true);
		} else if (source == playHotSeatButton) {
			controller.setOpponentIsAI(false);
		} else if (source == aiStrategyCombobox) {
			controller.setAIStrategy((String)aiStrategyCombobox.getSelectedItem());
		} else if (source == instructionsButton) {
			displayInstructions();
		} else if (source == newGameButton) {
			opponentBoardPanel.setUndiscoveredShipsDisplayed(false);
			controller.newGame();
		}
	}
	
	@Override
	public void mouseReleased(MouseEvent e) {
		//Translate the location clicked into board coordinates
		int x = e.getX();
		int y = e.getY();
	    int xBoardPosition = (x/(PANEL_SIZE/model.getGridSize()));
	    int yBoardPosition = (y/(PANEL_SIZE/model.getGridSize()));
	    /* Send these board coordinates as a new request for a move to the controller. The player it is requested for will depend 
	     * on whose turn it is in the model. It will be granted or denied depending on whose turn it is, whether that turn is to 
	     * be handled by AI instead, and whether a game is currently in progress (i.e. started and not finished */
	    Location boardLocationClicked = new Location2D(xBoardPosition, yBoardPosition);
	    if (e.getSource() == opponentBoardPanel) {
	    	controller.requestMove(boardLocationClicked);
	    } else if (e.getSource() == currentPlayerBoardPanel) {
	    	controller.requestPlacement(boardLocationClicked);

	    }
	    
	}	

/*-----------------------------------------------------------------------
 * 	
 * Visual display updates
 * 
 * ----------------------------------------------------------------------
 */
	
	@Override
	public void update() {
		currentPlayerBoardPanel.setBoard(model.getCurrentPlayer().getBoard());
		opponentBoardPanel.setBoard(model.getCurrentPlayer().getOpponent().getBoard());
		currentPlayerBoardPanel.repaint();
		opponentBoardPanel.repaint();
	}
	

	@Override
	public void hideBoards() {
		currentPlayerBoardPanel.setMarkersVisible(false);
		opponentBoardPanel.setMarkersVisible(false);
		currentPlayerBoardPanel.repaint();
		opponentBoardPanel.repaint();	
	}
	@Override
	public void showBoards() {
		currentPlayerBoardPanel.setMarkersVisible(true);
		opponentBoardPanel.setMarkersVisible(true);
		currentPlayerBoardPanel.repaint();
		opponentBoardPanel.repaint();	
	}

	@Override
	public void disableControls() {
		playAIButton.setEnabled(false);
		playHotSeatButton.setEnabled(false);
		aiStrategyCombobox.setEnabled(false);	
	}
	
	@Override
	public void enableControls() {
		playAIButton.setEnabled(true);
		playHotSeatButton.setEnabled(true);
		aiStrategyCombobox.setEnabled(true);	
	}
	
	/*--------------------------------------------------
	 * 
	 * Communication to the user
	 * 
	 * -------------------------------------------------
	 */
	@Override
	public void displayWelcomeMessage() {
		JOptionPane.showMessageDialog(contentPanel, "Welcome to BattleShips!");
		JOptionPane.showMessageDialog(contentPanel, "If you need any help, just click on 'Instructions'");		
	}
	@Override
	public void displayInstructions() {
		JOptionPane.showMessageDialog(contentPanel, 
										"BattleShips is a two-player game and is conducted in two phases:" + 
										"\n\n1) Each player in turn will have to place all of their available ships onto" +
										"\ntheir own grid (coloured grey). These are marked by white circles." + 
										"\n\n2) Players will take it in turns to guess the position of a ship on their opponent's" +
										"\ngrid (coloured black) and click on it to attack." +
										"\n\n-If it was occupied with an enemy ship, then that ship has been sunk and is shown in red." +
										"\n\n-If the space was unoccupied, it will be crossed off with a white 'X'." +
										"\n\n-You can watch your opponent's progress on your own grid." + 
										"\n\nTry to sink all of their ships first!");
	}
	@Override
	public void notifyPlayerSetUp() {
		JOptionPane.showMessageDialog(contentPanel, model.getCurrentPlayer().getId() + 
				" please place your ships\nYou have " + model.getCurrentPlayer().getPiecesRemaining() + " ships to place.");
	}	
	@Override
	public void notifyAlreadyPlacedHere() {
		JOptionPane.showMessageDialog(contentPanel, "You have already placed a ship here.\nYou must place a ship in a new location");
	}
	@Override
	public void notifySetUpComplete() {
		JOptionPane.showMessageDialog(contentPanel, "All ships have been placed");
	}
	@Override
	public void notifyPlayerTurn() {
		JOptionPane.showMessageDialog(contentPanel, model.getCurrentPlayer().getId() + "'s turn" + "\n" + model.getCurrentPlayer().getOpponent().getId() + " please look away");
	}
	@Override
	public void notifyAlreadyMovedHere() {
		JOptionPane.showMessageDialog(contentPanel, "You have already fired on this location.\nYou must choose another");
	}
	@Override
	public void fetchNextPlayer() {
		JOptionPane.showMessageDialog(contentPanel, "Please fetch the next player");		
	}
	@Override
	public void notifyGameOver(String player) {
		opponentBoardPanel.setUndiscoveredShipsDisplayed(true);
		JOptionPane.showMessageDialog(contentPanel, player + " has won!");
	}

	/*-----------------------------------------------------------------------------
	 * 
	 * Required interface overrides that need no implementation
	 * 
	 * -----------------------------------------------------------------------------
	 */
	
	@Override
	public void mouseClicked(MouseEvent e) {}
	@Override
	public void mouseEntered(MouseEvent e) {}
	@Override
	public void mouseExited(MouseEvent e) {}
	@Override
	public void mousePressed(MouseEvent e) {}
}