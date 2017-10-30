import java.util.*;

public class NQueens{
	public static void main(String[] args){
		puzzleDriver();
	}

	public static void puzzleDriver(){
		BoardState thisState = new BoardState(21);
		thisState.printBoard();
		System.out.println("heuristic: " + thisState.getHeuristic());
	}

}

class BoardState{
	private int[] board;
	private int heuristic;
	private int size;

	public BoardState(int size){
		this.size = size;
		this.board = new int[size];
		randomizeBoard();
		this.heuristic = generateHueristic();
	}

	public void randomizeBoard(){
		for(int i = 0; i < this.board.length; i++) addQueen(getRandomIndex(), i);
	}

	public void printBoard(){
		for(int i = 0; i < this.board.length; i++){
			for(int j = 0; j < this.board.length; j++){
				if(this.board[j] == i){
					System.out.print(" Q ");
				} else {
					System.out.print(" _ ");
				}
			}
			System.out.println();
		}
	}

	public void addQueen(int row, int col){
		this.board[col] = row;
	}

	public int generateHueristic(){
		int heuristic = 0;
		for(int i = 0; i < this.board.length; i++){
			for(int j = i + 1; j < this.board.length; j++){
				//Compare rows
				if(this.board[i] == this.board[j]) heuristic++;
				//Compare diagonals
				if(Math.abs(j - i) == Math.abs(board[j] - board[i])) heuristic++;
			}
		}
		return heuristic;
	}

	public int getHeuristic(){
		return this.heuristic;
	}

	public int getSize(){
		return this.size;
	}

	public int getRandomIndex(){
		Random rand = new Random(System.nanoTime());
		return rand.nextInt(this.getSize());
	}

}