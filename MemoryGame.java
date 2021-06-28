package ex1;

import java.io.Serializable;

/**
 * 
 * this class represent the memory game info
 * it contains the board cards and a boolean matrix "show" when true means the card was matched
 *
 */

public class MemoryGame implements Serializable {
	
	private int boardSize;
	private String[][] board;
	private boolean[][] show;
	
	// constructor 
	
	public MemoryGame(int size) {
		
		// creating a cards generator to randomly choose cards 
		
		CardsGenerator cG = new CardsGenerator();
		// set the board size and create the matrices
		boardSize = size;
		board = new String[boardSize][boardSize];
		show = new boolean[boardSize][boardSize];
		
		// get the cards from the generator class
		
		String[] cards = cG.getCards(boardSize);
		int k = 0;
		// fill in the board
		for(int i = 0; i < boardSize; i++) {
			for(int j = 0; j < boardSize; j++) {
				board[i][j] = cards[k++];
			}
		}
	}
	
	// gets i and j and set the show matrix in this location as true 
	
	public void show(int i, int j) {
		show[i][j] = true;
	}
	
	// return true if two cards are a match
	
	public boolean tryMatch(int[] a, int[] b) {
		if(board[a[0]][a[1]].equals(board[b[0]][b[1]])) {
			return true;
		}
		else {
			show[a[0]][a[1]] = false;
			show[b[0]][b[1]] = false;
			return false;
		}
	}
	// return the show matrix 
	public boolean[][] getShow(){
		return show;
	}
	
	// return the board matrix 
	public String[][] getBoard(){
		return board;
	}
	
	// return true if all the cards are shown
	public boolean isFinished() {
		for(int i = 0; i < boardSize; i++) {
			for(int j = 0; j < boardSize; j++) {
				if(!show[i][j])
					return false;
			}
		}
		return true;
	}
}
			
			
		
		
	


