import java.util.*;
/*Program that uses two different types of local search algorithms to solve the NQueens problem with a 21x21 board*/
public class NQueens{
	public static void main(String[] args){
		//Call puzzle handler
		newPuzzle();
	}
	//Creates a new puzzle and either uses that puzzle for the Steepest Ascent search, or calls the genetic
	//algorithm which will generate its own states.
	public static void newPuzzle(){
		float totalSolved = 0;
		long totalTime = 0;
		for(int i = 0; i < 100; i++){
			BoardState thisState = new BoardState(21);
			System.out.println("heuristic: " + thisState.getHeuristic());
			/*Comment this out for genetic solution, or uncomment it for hillclimbing*/
			//if(hillClimbingSolution(thisState)) totalSolved++;
			totalTime +=geneticSolution();

		}
		totalTime /= 100;
		System.out.println("Average time: " + totalTime);
		//System.out.println("Average Solved: " + (totalSolved / 100));
	}

	//Helper method that creates a new board state from the parent state. (moves a queen.)
	public static BoardState generateNewBoardState(BoardState board, int n, int newPos, int col){
	//Uses size and previous board constructor then uses the make state method to translate queen.
		BoardState tempState = new BoardState(n, board);
		tempState.makeState(newPos, col);
		return tempState;
	}

	//Hill cimbing algorithm, returns a true or false whether it could find a solution
	public static boolean hillClimbingSolution(BoardState thisState){
		long timeBegin = System.currentTimeMillis();
		//If the current state is not the goal state loop
		while(thisState.getHeuristic() != 0){
			//Get the next best board state and set the current state to it.
			BoardState next = getBestNextState(thisState);
			//If the next best state is better than the current, the algorithm has failed to solve it and will return.
			if(next.getHeuristic() >= thisState.getHeuristic()){
				System.out.println("Hit a peak or platuea, cant continue.");
				long time = System.currentTimeMillis() - timeBegin;
				System.out.println("Stuck in " + time + " milliseconds.");
				return false;
			}
			//If the next best state has a lower heuristic, set it as this state and repeat.
			thisState = next;
			//System.out.println("Heuristic: " + thisState.getHeuristic());
		}
		long time = System.currentTimeMillis() - timeBegin;
		System.out.println("Solved in " + time + " milliseconds.");
		thisState.printBoard();

		return true;
	}
	//Genetic Algorithm solution
	public static long geneticSolution(){
		long timeBegin = System.currentTimeMillis();
		boolean solved = false;
		//Population to hold parents and children
		BoardState[] population = new BoardState[100];
		BoardState[] temp = new BoardState[100];
		//Create 100 parents check for any already solved states
		for(int i = 0; i < 100; i++){
			population[i] = new BoardState(21);
			if(population[i].getHeuristic() == 0){
				System.out.println("Solution found.");
				return 0;
			}
		}
		//Loop until a goal state is found
		while(!solved){
			Random rnd = new Random(System.nanoTime());
			//Build next population
			for(int i = 0; i < 100; i++){
				int rn1 = rnd.nextInt(100);
				int rn2 = rnd.nextInt(100);
				//Select two parents randomly, if they are the same, pick two more parents else continue.
				if(Arrays.equals(population[rn1].getBoard(), population[rn2].getBoard())){
					i--;
					continue;
				} else {
				//If the parents arent equal, breed them and generate a new state with it.
					BoardState bsc = crossOver(population[rn1], population[rn2]);
				//If the offspring has a better heuristic than at least one of the parents, it can join the next population
					if(bsc.getHeuristic() > population[rn1].getHeuristic() || bsc.getHeuristic() > population[rn2].getHeuristic()){
						i--;
						continue;
					} else {
						temp[i] = bsc;
					}
				}
				//If a random number is less then 30 (30/100), there is a 30% chance a random mutation will occur to the child
				if(rn1 < 30) temp[i].mutate();
				//Check if the child is the goal solution. If not continue.
				if(temp[i].getHeuristic() == 0){
					temp[i].printBoard();
					solved = true;
				}
			}
			//Transfer the child population to parent population.
			for(int i = 0; i < 100; i++){
				population[i] = temp[i];
			}
		}
		long timeEnd = System.currentTimeMillis();
		long total = timeEnd - timeBegin;
		System.out.println("Solved w/ genetic algorithm in " + total + " milliseconds");
		return total;
	}

	//Method that randomly selects a point, and creates a new boardstate that is a random mix of the previous boardstates
	public static BoardState crossOver(BoardState b1, BoardState b2){
		int[] newChild = new int[b1.getSize()];
		int rand = b1.getRandomIndex();
		Random rng = new Random(System.nanoTime());
		int rngI = rng.nextInt(1);
		//Create new boardstate baased on our random number.
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

	//Used for the Steepest Ascent algorithm, returns the best board state possible from moving one of the queens
	//On the current boardstate.
	public static BoardState getBestNextState(BoardState thisState){
		//Custom comparator to compaare heuristic
		Comparator<BoardState> comparator = new Comparator<BoardState>(){
			@Override
			public int compare(BoardState n1, BoardState n2){
				return n1.getHeuristic() - n2.getHeuristic();
			}
		};
		//Priority queue that will hold our boardstates
 		PriorityQueue<BoardState> setOfNext = new PriorityQueue<BoardState>(100, comparator);
 		//Does every possible move and puts it into the priority queue.
		for(int i = 0; i < thisState.getSize(); i++){
			for(int j = 0; j < thisState.getSize(); j++){
				BoardState tempState = new BoardState(thisState.getSize(), thisState);
				tempState.makeState(i,j);
				setOfNext.add(tempState);
			}
		}
		//Return top of priority queue.
		return setOfNext.poll();
	}

}


//Boardstate class to build boardstate objects
class BoardState{
	private int[] board;
	private int heuristic;
	private int size;

	//Generic randomly generated board of specified size
	public BoardState(int size){
		this.size = size;
		this.board = new int[size];
		randomizeBoard();
		this.heuristic = generateHueristic();
	}

	//Boardstate built with a given array
	public BoardState(int size, int[] arr){
		this.size = size;
		this.board = new int[size];
		for(int i = 0; i < size; i++) this.board[i] = arr[i];
		this.heuristic = generateHueristic();
	}

	//Boardstate built with given board
	public BoardState(int size, BoardState board){
		this.size = size;
		this.board = new int[size];
		for(int i = 0; i < size; i++) this.board[i] = board.board[i];
		this.heuristic = generateHueristic();
	}

	//Randomly changes one value of boardstate
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