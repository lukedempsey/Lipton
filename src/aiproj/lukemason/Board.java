package aiproj.lukemason;
import aiproj.squatter.*;
/* COMP30024 Artificial Intelligence - Squat it like it's hot
 * Modified from Project Part A: Testing for a Win
 * Authors: Luke Dempsey <ldempsey - 638407>, Mason Rose-Campbell <mrose1 - 638370>
 */


/** Board Class to be used in conjunction with squatItLikeItsHot class.
 * For Finding the current state of the game based on input.
 */
public class Board {
	
	//Board variables
	private int boardDims;
	private static int[][] cells;
	
	private static boolean debug = false;
	
	/** Creates a new Board object
	 * @param n 
	 * @param cells The status of each tile on the board.
	 */
	public Board(int n) {
		
		//check to validate input
		boardDims = n;
		
		//initialise and fill the board
		initBoard(boardDims, cells);

		fillBoard(boardDims, cells);
		
		if(debug)printBoard(this);

	}
		
	/** Initialises the cells array
	 * @param boardDims The dimensions of the board size
	 */
	public static void initBoard(int boardDims, int[][] cells){
		Board.cells = new int[boardDims][boardDims];
		
	}	
	
	/** Reads in information from system input and fills cells array
	 * with this data
	 * @param cells Array to store board data
	 */
	public static void fillBoard(int boardDims, int[][] cells) {
		
		int col = 0;
		int row = 0;
		
		while(row<boardDims){
			while(col<boardDims){
				cells[row][col] = Piece.EMPTY;
				col++;
			}
			row++;
			col=0;
		}
	}
	
	/** Checks if the current board state satisfys an end game condition (someone has won)
	 * @param board The board from which the game state is checked
	 * @return A boolean for whether the game has ended or not
	 */
	public static boolean checkGameOver(Board board){
		for(int row = 0; row < board.getBoardDims(); row++){
			for(int col = 0; col < board.getBoardDims(); col++){
				
				//debug
				if(debug){System.out.println("Checking "+row+","+col+":"+board.getCells()[row][col]);}
				
				//Look to see if game is finished
				if (board.getCells()[row][col]==Piece.EMPTY) {
					
					//debug
					if(debug){System.out.println("Found empty cell");}
					
					//game state is not over
					return false;
				}
			}
		}
		//game state is over
		return true;
	}

	/** Finds the current game state (the scores of each player) by searching through the board
	 * @param debug Boolean to turn debugging mode on and off
	 * @param board Data will be examined about this board
	 * @param gameOver Whether or not the game has finished or not
	 */
	public static void state(Boolean debug, Board board) {
		int lastCol = 0;
		
		//Update whether or not the game is over
		LukeMason.setGameOver(checkGameOver(board));
		
		//Search for a captured point
		//Skip cells on bottom & right edges
		for(int row = 0; row < (board.getBoardDims()-1); row++){
			for(int col = 0; col < (board.getBoardDims()-1); col++){
				
				//Check points based on captured piece in each cell
				//Check captured spaces
				if (board.getCells()[row][col]==(CustomPiece.DEADSPACE)) {
					
					//debug
					if(debug){System.out.println("Found a captured cell: "+row+","+col);}
					
					//Add to tally
					if(lastCol==CustomPiece.BLACK){
						LukeMason.setTallyB(LukeMason.getTallyB() + 1);
					} else {
						LukeMason.setTallyW(LukeMason.getTallyW() + 1);
					}
					
					//debug
					if(debug){System.out.println("TallyW: "+LukeMason.getTallyW()+", TallyB: "+LukeMason.getTallyB());}
				
				//Check captured white pieces
				} else if (board.getCells()[row][col]==(CustomPiece.DEADWHITE)) {
					
					if(lastCol==CustomPiece.BLACK){
						LukeMason.setTallyB(LukeMason.getTallyB() + 1);
					}
				
				//Check captured black pieces
				} else if (board.getCells()[row][col]==(CustomPiece.DEADBLACK)) {
					
					if(lastCol==CustomPiece.WHITE){
						LukeMason.setTallyW(LukeMason.getTallyW() + 1);
					}
					
				//If not captured, set to latest colour
				} else {
					
					lastCol = board.getCells()[row][col];
					
					//debug
					if(debug){System.out.println("Captured cell not found, last Colour set to "+lastCol);}
				}
			}
		}
	}

	public static CaptureNode[] findLoop(int[][] currentBoard, CaptureNode[] deadcells, Move move, int colour, int direction) {
		
			
		
		//Keep locations of dead cells up to date
		if ((deadcells[move.Row].getMax() == -1)|(deadcells[move.Row].getMax()>move.Col)) {
			deadcells[move.Row].setMax(move.Col);
		} if ((deadcells[move.Row].getMin() == -1)|(deadcells[move.Row].getMin()>move.Col)) {
			deadcells[move.Row].setMin(move.Col);
		}
		
		// make small formulas for each adjacent cell, keeping in mind direction
		// make sure lateral cells have prefernce to diagonal ones
		// make sure to check if the cell is within the bounds of the board (coordinates dont exceed dims or go below 0)
		// also check that the cell has a piece of the same colour
		// keep recurring until the same initial point is reached {BASE CASE}
		
		return deadcells;
	}
	
	public static int[][] findCaptured(CaptureNode[] deadcells, int[][] currentBoard, int dims) {
		
		// change all required cells to dead
		for(int i=0; i<dims; i++){
			for(int j = deadcells[i].getMin(); j<= deadcells[i].getMax(); j++){
				if(j==Piece.BLACK) {
					currentBoard[i][j] = CustomPiece.DEADBLACK;
				} else if(j==Piece.WHITE) {
					currentBoard[i][j] = CustomPiece.DEADWHITE;
				} else if(j==Piece.EMPTY) {
					currentBoard[i][j] = CustomPiece.DEADSPACE;
				}
			}
		}
		
		//return the updated board cells
		return currentBoard;
	}
	
	/** Prints the current game state to standard output
	 * @param board The board from which the data is printed
	 */
	public static void printBoard(Board board){
		int dims = board.getBoardDims();
		for(int i=0; i<dims; i++){
			for(int j=0; j<dims; j++){
				int tmp = board.getCells()[i][j];
				switch(tmp) {
				case CustomPiece.EMPTY:
					System.out.print("+");
					break;
				case CustomPiece.WHITE:
					System.out.print("W");
					break;
				case CustomPiece.BLACK:
					System.out.print("B");
					break;
				case CustomPiece.DEAD:
					System.out.print("-");
					break;
				case CustomPiece.DEADWHITE:
					System.out.print("w");
					break;
				case CustomPiece.DEADBLACK:
					System.out.print("b");
					break;
				case CustomPiece.INVALID:
					System.out.print("Invalid board data. Probs Check that"); 	//Need to sort out when an invalid piece of data would get as far as this method
					break;
				}
			}
		System.out.print("\n");
		}
	}
	
	/** Returns the results of the game
	 * @param gameOver Whether the game has finished or not
	 * @param tallyB How many cells Black player has claimed 
	 * @param tallyW How many cells White player has claimed
	 */
	public static int returnWinner(Boolean gameOver, int tallyB, int tallyW){
		//TODO check whenever an illegal move is made that Piece.INVALID is returned
		if(gameOver) {
			if(tallyB==tallyW) {
				return Piece.DEAD;
			} else if(tallyB>tallyW) {
				return Piece.BLACK;
			} else {
				return Piece.WHITE;
			}
		} else {
			return Piece.EMPTY;
		}
	}
	
	/** Setter for cells
	 * @param cells Array of data about cells on the board
	 */
	public void setCells(int[][] cells) {
		Board.cells = cells;
	}
	
	/** Getter for board dimensions
	 */
	public int getBoardDims() {
		return boardDims;
	}
	
	/** Getter for cells data string
	 */
	public int[][] getCells() {
		return cells;
	}
}
