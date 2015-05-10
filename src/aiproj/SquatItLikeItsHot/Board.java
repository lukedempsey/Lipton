package aiproj.squatItLikeItsHot;
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
	private static String[][] cells;
	
	private boolean debug = true;
	
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
		
		//printBoard(this);

	}
	
	
	/** Initialises the cells array
	 * @param boardDims The dimensions of the board size
	 */
	public static void initBoard(int boardDims, String[][] cells){
		Board.cells = new String[boardDims][boardDims];
		
	}	
	

	/** Reads in information from system input and fills cells array
	 * with this data
	 * @param cells Array to store board data
	 */
	public static void fillBoard(int boardDims, String[][] cells) {
		
		int col = 0;
		int row = 0;
		
		while(row<boardDims){
			while(col<boardDims){
				cells[row][col] = Integer.toString(Piece.EMPTY);
				col++;
			}
			row++;
			col=0;
		}
	}

	/** Finds the current game state by searching through the board
	 * @param debug Boolean to turn debugging mode on and off
	 * @param board Data will be examined about this board
	 * @param gameOver Whether or not the game has finished or not
	 */
	public static void state(Boolean debug, Board board, Boolean gameOver) {
		String lastCol=null;
		
		//Search for a captured point
		//Skip cells on bottom & right edges
		for(int row = 0; row < (board.getBoardDims()-1); row++){
			for(int col = 0; col < (board.getBoardDims()-1); col++){
				
				//debug
				if(debug){System.out.println("Checking "+row+","+col+":"+board.getCells()[row][col]);}
				
				//Look to see if game is finished
				if (board.getCells()[row][col].equals("+")) {
					
					//debug
					if(debug){System.out.println("Found empty cell");}
					
					//change game state to not over
					squatItLikeItsHot.setGameOver(false);
				}
				
				//Check if captured
				if (board.getCells()[row][col].equals("-")){
					
					//debug
					if(debug){System.out.println("Found a captured cell: "+row+","+col);}
					
					//Add to tally
					if(lastCol.equals("B")){
						squatItLikeItsHot.setTallyB(squatItLikeItsHot.getTallyB() + 1);
					} else {
						squatItLikeItsHot.setTallyW(squatItLikeItsHot.getTallyW() + 1);
					}
					
					//debug
					if(debug){System.out.println("TallyW: "+squatItLikeItsHot.getTallyW()+", TallyB: "+squatItLikeItsHot.getTallyB());}
					
				//If not captured, set to latest colour
				} else {
					
					lastCol = board.getCells()[row][col];
					
					//debug
					if(debug){System.out.println("Captured cell not found, last Colour set to "+lastCol);}
				}
			}
		}
	}

	public static void printBoard(Board board){
		int dims = board.getBoardDims();
		for(int i=0; i<dims; i++){
			for(int j=0; j<dims; j++){
				System.out.print(board.getCells()[i][j]);
			}
		System.out.print("\n");
		}
	}
	
/** Prints the current state of the board
	 * @param gameOver Whether the game has finished or not
	 * @param tallyB How many cells Black player has claimed 
	 * @param tallyW How many cells White player has claimed
	 */
	public static int returnState(Boolean gameOver, int tallyB, int tallyW){
		if(gameOver) {
			if(tallyB==tallyW) {
				return Piece.DEAD;
			} else if(tallyB>tallyW) {
				return Piece.BLACK;
			} else {
				return Piece.WHITE;
			}
		}else{
			return Piece.EMPTY;
		}
	}
	
	
	/** Setter for cells
	 * @param cells Array of data about cells on the board
	 */
	public void setCells(String[][] cells) {
		this.cells = cells;
	}
	
	/** Getter for board dimensions
	 */
	public int getBoardDims() {
		return boardDims;
	}
	
	/** Getter for cells data string
	 */
	public String[][] getCells() {
		return cells;
	}
}
