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
	private int[][] cells = null;
	private int[] deadcells = null;
	
	private static boolean debug = false;
	
	/** Creates a new Board object
	 * @param n 
	 * @param cells The status of each tile on the board.
	 */
	public Board(int n) {
		
		//check to validate input
		boardDims = n;
		
		//Initialises the cells and dead cells arrays
		cells = new int[boardDims][boardDims];
		deadcells = new int[(boardDims-2)*(boardDims-2)];

		//Fill the cells and deadcell arrays
		fillBoard(boardDims, cells, deadcells);
		
		if(debug)printBoard(this);

	}
	
	/** Reads in information from system input and fills cells array
	 * with this data
	 * @param cells Array to store board data
	 */
	public static void fillBoard(int boardDims, int[][] cells, int[] deadcells) {
		
		// initialise board elements to empty
		for(int row=0;row<boardDims; row++){
			for(int col=0;col<boardDims; col++){
				cells[row][col] = Piece.EMPTY;
			}
		}
		
		// initialise deadcell hashtable to be invalid
		for(int i=0; i<deadcells.length; i++) {
			deadcells[i] = Piece.INVALID;
		}
		
	}
	
	/** Checks if the current board state satisfys an end game condition 
	 * @param board The board from which the game state is checked
	 * @return A boolean for whether the game has ended or not
	 */
	public static boolean checkGameOver(Board board){
		for(int row = 0; row < board.getBoardDims(); row++){
			for(int col = 0; col < board.getBoardDims(); col++){
				
				//debug
				if(debug){System.out.println("Checking "+row+","+col+":"+board.getCells()[row][col]);}
				
				//Look to see if game is finished by looking for empty cells
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

	/** Finds the current game state (the scores of each player) by searching
	 *  through the board
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

	/** Inspired by the floodfill algorithm, finds the dead cells and stores
	 * their location in a hash table called deadcells
	 * @param currentCells current state of cells on the board
	 * @param row row location of current cell being analysed
	 * @param col column location of current cell being analysed
	 * @param colour colour of the boundary (capturing) cells
	 * @param board the board of the current game
	 * @return a number based on whether a boundary or board edge was found
	 */
	public static int floodfill(int[][] currentCells, int row, int col,
			int colour, Board board) {
		//TODO test this, because it probably doesn't work at all
		int dim = currentCells.length;
		if(row<0|col<0|row>=dim|col>=dim) {
			return -1;
		} else if (currentCells[row][col] == colour) {
			return 0;
		} else {
			int up = floodfill(currentCells, row-1, col, colour, board);
			int down = floodfill(currentCells, row+1, col, colour, board);
			int left = floodfill(currentCells, row, col-1, colour, board);
			int right = floodfill(currentCells, row, col+1, colour, board);
			if(up==0 & down==0 & left==0 & right==0) {
				board.setDeadCells((row-1)*(dim-2)+(col-1), 1);
				return 0;
			}
			return -1;
		}
	}
	
	/** updates the board data to include the newly captured (dead) cells
	 * @param currentCells state of the current cells of the board
	 * @param deadcells the array containing the captured cells
	 * @return returns the updated cell data, with captured cells now marked as dead
	 */
	public static int[][] updateDead(int[][] currentCells, int[] deadcells){
		
		for(int i=1; i<currentCells.length-1; i++) {
			for(int j =1; j<currentCells.length-1; j++) {
				int tmp = deadcells[(i-1)*(currentCells.length-2)+(j-1)];
				if( tmp != Piece.INVALID) {
					if(j==Piece.BLACK) {
						currentCells[i][j] = CustomPiece.DEADBLACK;
					} else if(j==Piece.WHITE) {
						currentCells[i][j] = CustomPiece.DEADWHITE;
					} else if(j==Piece.EMPTY) {
						currentCells[i][j] = CustomPiece.DEADSPACE;
					}
				}	
			}
		}
		
		return currentCells;
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
		this.cells = cells;
	}
	
	/** Getter for cells data string
	 */
	public int[][] getCells() {
		return cells;
	}
	
	/** Getter for board dimensions
	 */
	public int getBoardDims() {
		return boardDims;
	}
	
	/** Setter for dead cells
	 * @param i index in the deadcells array
	 * @param x element to be set at index i
	 */
	public void setDeadCells(int i, int x) {
		this.deadcells[i] = x;
	}
	
	/** Getter for deadcells
	 */
	public int[] getDeadCells(){
		return this.deadcells;
	}
}
