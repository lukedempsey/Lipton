package aiproj.SquatItLikeItsHot;
/* COMP30024 Artificial Intelligence - Squat it like it's hot
 * Project Part A: Testing for a Win
 * Authors: Luke Dempsey <ldempsey - 638407>, Mason Rose-Campbell <mrose1 - 638370>
 */

import java.util.Scanner;

/** Board Class to be used in conjunction with SquatItLikeItsHot class.
 * For Finding the current state of the game based on input.
 */
public class Board {
	
	//Scanner for input
	private static Scanner input = new Scanner(System.in);
	
	//Board variables
	private int boardDims;
	private String[][] cells;
	
	/** Creates a new Board object
	 * @param n 
	 * @param cells The status of each tile on the board.
	 */
	public Board(int n) {
		
		//check to validate input
		boardDims = n;
		
		//initialise and fill the board
		initBoard(this, boardDims, cells);
		fillBoard(boardDims, cells);
	}
	
	/** Initialises the cells array
	 * @param boardDims The dimensions of the board size
	 */
	public static void initBoard(Board board, int boardDims, String[][] cells){
		cells = new String[boardDims][boardDims];
		board.setCells(cells);
	}	

	/** Reads in information from system input and fills cells array
	 * with this data
	 * @param cells Array to store board data
	 */
public static void fillBoard(int boardDims, String[][] cells) {
		
		int col = 0;
		int row = 0;
		String line;
		String temp;
		String temp_2;
		
		while(row<boardDims) {
			
			line = input.nextLine();
			temp = line.replaceAll("\\s","");
			
			if(temp.length()!= boardDims){
				System.out.println("Error, Board dimensions or cell dimensions were incorrect input.");
				System.exit(0);
			}
			
			for(col=0;col<temp.length();col++) {
				temp_2 = Character.toString(temp.charAt(col));
				if(!temp_2.equals("B")&&!temp_2.equals("W")&&!temp_2.equals("-")&&!temp_2.equals("+")){
					System.out.println("Invalid input data for cell/s. Please input correct characters only.");
					System.exit(0);
				}
				cells[row][col] = temp_2;
			}
			row++;
			col=0;
			
		}
		if(row!=boardDims){
			System.out.println("Error, Board dimensions or cell dimensions were incorrect input.");
			System.exit(0);
		}
	}

	/** Finds the current game state by searching through the board
	 * @param debug Boolean to turn debugging mode on and off
	 * @param board Data will be examined about this board
	 * @param gameOver Whether or not the game has finished or not
	 */
	public void state(Boolean debug, Board board, Boolean gameOver) {
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
					SquatItLikeItsHot.setGameOver(false);
				}
				
				//Check if captured
				if (board.getCells()[row][col].equals("-")){
					
					//debug
					if(debug){System.out.println("Found a captured cell: "+row+","+col);}
					
					//Add to tally
					if(lastCol.equals("B")){
						SquatItLikeItsHot.setTallyB(SquatItLikeItsHot.getTallyB() + 1);
					} else {
						SquatItLikeItsHot.setTallyW(SquatItLikeItsHot.getTallyW() + 1);
					}
					
					//debug
					if(debug){System.out.println("TallyW: "+SquatItLikeItsHot.getTallyW()+", TallyB: "+SquatItLikeItsHot.getTallyB());}
					
				//If not captured, set to latest colour
				} else {
					
					lastCol = board.getCells()[row][col];
					
					//debug
					if(debug){System.out.println("Captured cell not found, last Colour set to "+lastCol);}
				}
			}
		}
	}

	
/** Prints the current state of the board
	 * @param gameOver Whether the game has finished or not
	 * @param tallyB How many cells Black player has claimed 
	 * @param tallyW How many cells White player has claimed
	 */
	public void printState(Boolean gameOver, int tallyB, int tallyW){
		if(gameOver) {
			if(tallyB==tallyW) {
				System.out.println("Draw");
			} else if(tallyB>tallyW) {
				System.out.println("Black");
			} else {
				System.out.println("White");
			}
		}else{
			System.out.println("None");
		}
		System.out.println(tallyW);
		System.out.println(tallyB);
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
