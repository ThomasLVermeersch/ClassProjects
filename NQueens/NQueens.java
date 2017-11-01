import java.util.*;

public class NQueens{
	public static void main(String[] args){
		newPuzzle();
	}

	public static void newPuzzle(){
		float totalSolved = 0;
		for(int i = 0; i < 100; i++){
			BoardState thisState = new BoardState(21);
			System.out.println("heuristic: " + thisState.getHeuristic());
			if(hillClimbingSolution(thisState)) totalSolved++;
		}
		System.out.println("Average Solved: " + (totalSolved / 100));
	}

	public static BoardState generateNewBoardState(BoardState board, int n, int newPos, int col){
		BoardState tempState = new BoardState(n, board);
		tempState.makeState(newPos, col);
		return tempState;
	}

	public static boolean hillClimbingSolution(BoardState thisState){
		while(thisState.getHeuristic() != 0){
			BoardState next = getBestNextState(thisState);
			if(next.getHeuristic() >= thisState.getHeuristic()){
				System.out.println("Same heuristic, problem cant be solved");
				return false;
			}
			thisState = next;
			System.out.println("Heuristic: " + thisState.getHeuristic());
		}
		System.out.println("Solved");
		return true;
	}
	public static BoardState getBestNextState(BoardState thisState){
		Comparator<BoardState> comparator = new Comparator<BoardState>(){
			@Override
			public int compare(BoardState n1, BoardState n2){
				return n1.getHeuristic() - n2.getHeuristic();
			}
		};

 		PriorityQueue<BoardState> setOfNext = new PriorityQueue<BoardState>(100, comparator);

		for(int i = 0; i < thisState.getSize(); i++){
			for(int j = 0; j < thisState.getSize(); j++){
				BoardState tempState = new BoardState(thisState.getSize(), thisState);
				tempState.makeState(i,j);
				setOfNext.add(tempState);
			}
		}
		return setOfNext.poll();
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

	public BoardState(int size, BoardState board){
		this.size = size;
		this.board = new int[size];
		for(int i = 0; i < size; i++){
			this.board[i] = board.board[i];
		}
		this.heuristic = generateHueristic();
	}

	public void randomizeBoard(){
		for(int i = 0; i < this.board.length; i++) addQueen(getRandomIndex(), i);
	}

	public void makeState(int newPos, int col){
		this.board[col] = newPos;
		this.heuristic = generateHueristic();
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
	public int[] getBoard(){
		return this.board;
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