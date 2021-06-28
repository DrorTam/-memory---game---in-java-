package ex1;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/** 
 * 
 * this is the panel for the client side
 * it is also runnable so the GUI will work simultaneously
 *
 */


public class MemoryGamePanel extends JPanel implements Runnable {
	
	// the cards pictures size 
	private final int cardHeight = 92, cardWidth = 66;
	
	// the board size 
	private int boardSize;
	
	// panel components
	private JPanel cardsPanel = null;
	private JTextArea textArea = null;
	
	// streams 
	private ObjectOutputStream out = null;
	private ObjectInputStream in = null;
	
	// boolean for turns
	private boolean myTurn = false;
	
	// for the back side of the cards
	private Image blankImg  = null;
	
	// the show and  board matrices
	private boolean[][] show;
	private String[][] board;
	
	// boolean to set true when the game ends 
	private boolean gameOver = false;
	
	
	// constructor
	public MemoryGamePanel() {
		
		String serverName;
		int port;
		serverName = JOptionPane.showInputDialog("Please enter Host Name\n(Press cancel for default:\"localhost\")");
		if(serverName == null || serverName.isEmpty())
			serverName = "localhost";
		try {
			port = Integer.parseInt(JOptionPane.showInputDialog("Please enter port\n(Press cancel for default:\"7777\")"));
		}catch(NumberFormatException e) {
			port = 7777;
		}
		
		// create a socket and set in and out streams
		Socket socket = null;
		try {
			socket = new Socket(serverName, port);
			out = new ObjectOutputStream(socket.getOutputStream());
			in = new ObjectInputStream(socket.getInputStream());
		}catch(IOException e) {
			System.out.println("couldn't connect");
			System.exit(1);
		}
		
		
		// get first input
		Object input = null;
		try {
			input = in.readObject();
		}catch(IOException e) {
			System.out.println("couldn't get game");
			System.exit(1);
		}catch(ClassNotFoundException e) {
			System.out.println("couldn't get game");
			System.exit(1);
		}
		
		boardSize = (int)input;
		// set the matrices
		show = new boolean[boardSize][boardSize];
		board = new String[boardSize][boardSize];
		// set the back side of cards
		blankImg = this.getToolkit().getImage("blank/blank.gif");
		// set the layout of the panel
		this.setLayout(new BorderLayout());
		cardsPanel = new JPanel(new GridLayout(boardSize, boardSize));
		this.add(cardsPanel, BorderLayout.CENTER);
		textArea = new JTextArea(8, 30);
		textArea.setEditable(false);
		JScrollPane jsp = new JScrollPane(textArea);
		this.add(jsp, BorderLayout.SOUTH);
		// run this thread
		new Thread(null, this).start();
		}
	
	@Override
	public void run() {
		Object input = null;
		// while game not over get input through stream
		while(!gameOver) {
			try {
				input = in.readObject();
			}catch(IOException e) {
				System.out.println("couldn't get input");
				System.exit(1);
			}catch(ClassNotFoundException e) {
				System.out.println("Couldn't get input");
				System.exit(1);
			}
			// send input to process method
			processInput(input);
		}
	}
	
	// process the input object
	public void processInput(Object input) {
		if(input instanceof String[][]) {
			board = (String[][])input;
		}
		else if(input instanceof String) {
			String msg = null;
			msg = (String)input;
			switch(msg) {
			case "Finised": // game over
				gameOver = true;
				break;
			case "Wait for second player":
				textArea.setText(msg);
				break;
			case "Game Started":
				textArea.setText(msg+"\n");
				break;
			case "Your Turn":
				myTurn = true;
				textArea.append(msg+"\n");
				break;
			case "Opponent's Turn":
				myTurn = false;
				textArea.append(msg+"\n");
				break;
			default:
				textArea.append(msg+"\n");
			}
		}
		else if(input instanceof boolean[][]) {
			show = null;
			show = (boolean[][])input;
			paintPanel();
		}
	}
	// paints the panel 
	public void paintPanel() {
		// in case of repaint first clear
		cardsPanel.removeAll();
		Image cardImg = null;
		// width and height for cards pictures
		int w, h;
		w = cardWidth;
		h = cardHeight;
		for(int i = 0; i < boardSize; i++) {
			for(int j = 0; j < boardSize; j++) {
				if(show[i][j]) {
					cardImg = this.getToolkit().getImage(board[i][j]);
					if(cardsPanel.getWidth()>boardSize){w = cardsPanel.getWidth()/boardSize;
						h = cardsPanel.getHeight()/boardSize;
				}
				Image resizedCard = cardImg.getScaledInstance(w, h, Image.SCALE_DEFAULT);
				ImageIcon icon = new ImageIcon(resizedCard);
				JButton button = new JButton(icon);
				String buttonActionCommand = i + "," +j;
				button.setActionCommand(buttonActionCommand);
				cardsPanel.add(button);
				}
			
			else	if(cardsPanel.getWidth()<boardSize){w = cardsPanel.getWidth()/boardSize;
				h = cardsPanel.getHeight()/ boardSize;
			}
			Image resizedCard = blankImg.getScaledInstance(w, h, Image.SCALE_DEFAULT);
			ImageIcon icon = new ImageIcon(resizedCard);
			JButton button = new JButton(icon);
			String buttonActionCommand = i + "," +j;
			button.setActionCommand(buttonActionCommand);
			button.addActionListener(new BtnListener());
			cardsPanel.add(button);
			
				
			}
			}
		
	}
 



protected void paintComponent(Graphics g) {

		super.paintComponent(g);
}

public class BtnListener implements ActionListener{
	@Override
	public void actionPerformed(ActionEvent e) {
		// if my turn is false user can't click
		if(myTurn) {
			String[] action = e.getActionCommand().split(",");
			int i = Integer.parseInt(action[0]);
			int j = Integer.parseInt(action[1]);
			int[] myPlay = new int[2];
			myPlay[0] = i;
			myPlay[1] = j;
			
			try {
				out.writeObject(myPlay);	
			}catch(IOException e1) {
				System.out.println("couldnt send my play");
				System.exit(1);
			}
		}
	}
	}
}
	



