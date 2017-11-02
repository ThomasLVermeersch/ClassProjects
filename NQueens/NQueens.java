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
			//if(hillClimbingSolution(thisState)) totalSolved++;
			if(geneticSolution()) totalSolved++;

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
				//System.out.println("Same heuristic, problem cant be solved");
				return false;
			}
			thisState = next;
			//System.out.println("Heuristic: " + thisState.getHeuristic());
		}
		System.out.println("Solved");
		return true;
	}

	public static boolean geneticSolution(){
		boolean solved = false;
		//Will hold parents
		BoardState[] population = new BoardState[100];
		BoardState[] temp = new BoardState[100];
		//Create 100 parents check for any already solved states
		for(int i = 0; i < 100; i++){
			population[i] = new BoardState(21);
			if(population[i].getHeuristic() == 0){
				System.out.println("Solution found.");
				return true;
			}
		}
		while(!solved){
			Random rnd = new Random(System.nanoTime());
			//Build next population
			for(int i = 0; i < 100; i++){
				int rn1 = rnd.nextInt(100);
				int rn2 = rnd.nextInt(100);
				if(Arrays.equals(population[rn1].getBoard(), population[rn2].getBoard())){
					i--;
					continue;
				} else {
					BoardState bsc = crossOver(population[rn1], population[rn2]);
					if(bsc.getHeuristic() > population[rn1].getHeuristic() || bsc.getHeuristic() > population[rn2].getHeuristic()){
						i--;
						continue;
					} else {
						temp[i] = bsc;
					}
				}
				if(rn1 < 30) temp[i].mutate();
				if(temp[i].getHeuristic() == 0){
					System.out.println("Solution found.");
					return true;
				}
			}
			for(int i = 0; i < 100; i++){
				population[i] = temp[i];
			}
		}
		System.out.println("Solved");
		return true;
	}

	public static BoardState crossOver(BoardState b1, BoardState b2){
		int[] newChild = new int[b1.getSize()];
		int rand = b1.getRandomIndex();
		Random rng = new Random(System.nanoTime());
		int rngI = rng.nextInt(1);
		for(int i = 0; i < b1.getSize(); i++){
			if(rngI == 0){
				if(i < rand) newChild[i] = b1.getBoardValue(i);
				if(i >= rand) newChild[i] = b2.getBoardValue(i);
			} else {
				if(i < rand) newChild[i] = b2.getBoardValue(i);
				if(i >= rand) newChild[i] = b1.getBoardValue(i);
			}
		}
		BoardState child = new BoardState(b1.getSize(), newChild);
		//System.out.println("Child:" + Arrays.toString(child.getBoard()) + "\nh: " + child.getHeuristic());
		return child;
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

	public BoardState(int size, int[] arr){
		this.size = size;
		this.board = new int[size];
		for(int i = 0; i < size; i++) this.board[i] = arr[i];
		this.heuristic = generateHueristic();
	}

	public BoardState(int size, BoardState board){
		this.size = size;
		this.board = new int[size];
		for(int i = 0; i < size; i++) this.board[i] = board.board[i];
		this.heuristic = generateHueristic();
	}

	public void mutate(){
		Random rand = new Random(System.nanoTime());
		int rng = rand.nextInt(21);
		this.board[rng] = rand.nextInt(21);
	}

	public int getBoardValue(int index){
		return this.board[index];
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