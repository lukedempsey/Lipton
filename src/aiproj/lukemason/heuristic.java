package aiproj.lukemason;

import aiproj.squatter.*;

/**
 * 
 * @author lukedempsey
 * 
 * Currently three main factors
 * 
 * liberties (free spaces adjacent)
 * alive/dead
 * connecting the stones
 *
 */

public class Heuristic {
	
	static boolean debug = false;
	
	/**
	 * Calculates the alive/dead factor
	 * @return 
	 */
	private int aliveDead(){
		int white = LukeMason.getTallyW();
		int black = LukeMason.getTallyB();
		
		return white - black;
	}
	
	public static int liberties(Board board, LukeMason player){
		int[][] cells = board.getCells();
		int dim = board.getBoardDims();
		
		int lib_player = 0;
		int lib_opponent = 0;
		
		//Count liberties
		for(int i=0; i<dim; i++){
			for(int j=0; j<dim; j++){
				//Find player cell
				if(cells[i][j]==player.getPlayerColour()){
					//check adjacent
					
					for(int d_i=-1;d_i<=1;d_i++){
						for(int d_j=-1; d_j <=1; d_j++){
							try{
								if(cells[i+d_i][j+d_j]==Piece.EMPTY){
									lib_player++;
								}
							}catch (ArrayIndexOutOfBoundsException e){
								if(debug) System.out.println("Cell boundary encountered (player)");
							} finally {}
						}
					}
				}
				if(debug){
					System.out.println("Comparing:");
					System.out.println(cells[i][j]);
					System.out.println(player.getOpponentColour());
				}
				
				if(cells[i][j]==player.getOpponentColour()){
					//check adjacent
					if(debug) System.out.println("Found");
					
					for(int d_i=-1;d_i<=1;d_i++){
						for(int d_j=-1; d_j <=1; d_j++){
							try{
								
								if(cells[i+d_i][j+d_j]==Piece.EMPTY){
									lib_opponent++;
									if(debug) System.out.println("Opponent++");
								}
							}catch (ArrayIndexOutOfBoundsException e){
								if(debug) System.out.println("Cell boundary encountered (opponent)");
							} finally {}
						}
					}
				}
			}
		}
		return lib_player - lib_opponent;
	}

	
	
}
