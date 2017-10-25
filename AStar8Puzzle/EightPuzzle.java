//8-Puzzle Solved with A* Search algorithm
//If you want to use a file of eight puzzles, pass them in the command line
import java.util.*;
import java.io.*;

public class EightPuzzle{

	private static int misplacedDist;
	private static int manhattanDist;
	private static int misplacedRunTime;
	private static int manhattanRunTime;

    public static void main(String[] args){
		String givenPuzzle = "";
		System.out.print("Enter 0 for random puzzles and 1 for custom submissions: ");
		Scanner kb = new Scanner(System.in);
		while(true){
			misplacedDist = 0;
			manhattanDist = 0;
			int i = 0;
			if(kb.nextInt() == 0){
				System.out.println("Generating 100 random puzzles: ");
				while(i != 100){
					if(randomPuzzleGenerator()) i++;
				}
				System.out.println("Manhattan average: " + manhattanDist/100);
				System.out.println("Misplaced average: " + misplacedDist/100);

			} else {
				while(true){
	   			System.out.print("Enter a puzzle sequence in one line (example: 012345678): ");
	   			Scanner cb = new Scanner(System.in);
	   			givenPuzzle = cb.nextLine();
	   			System.out.print("Choose a heuristic(0 for manhattan distance, 1 for misplaced tiles, ): ");
    			int heuristic = cb.nextInt();
	   			if (verifyPuzzle(givenPuzzle)){
	   				solvePuzzle(givenPuzzle, heuristic);
	   			} else{
	   				System.out.println("Not a valid puzzle");
	   			}
	   			if(heuristic == 0) System.out.println("Number of nodes: " + manhattanDist);
	   			if(heuristic == 1) System.out.println("Number of nodes: " + misplacedDist);
	   			//System.out.println("Manhattan runtime: " + manhattanRunTime);
	   			//System.out.println("Misplaced runtime: " + misplacedRunTime);
	   			}
	   		}
	   	}
    }

    public static boolean randomPuzzleGenerator(){
    	int[] values = {0,1,2,3,4,5,6,7,8,9};
    	int sum = Integer.MAX_VALUE;
    	boolean zeroCounter = true;
    	String puzzle = "";
    	Random rand = new Random(System.currentTimeMillis());
    	while(puzzle.length() != 9){
			int r = rand.nextInt(9);
			if( values[r] == 0 && zeroCounter){
				puzzle += '0';
				zeroCounter = false;
			} else if(values[r] != 0){
				puzzle += Integer.toString(values[r]);
				values[r] = 0;
			}
    	}
    	if(verifyPuzzle(puzzle)){
    		System.out.println("Solving " + puzzle + " with Manhattan distance as heuristic:");
    		manhattanDist += solvePuzzle(puzzle, 0);
    		System.out.println("Solving " + puzzle + " with misplaced tiles as heuristic:");
    		misplacedDist+=solvePuzzle(puzzle, 1);
    		return true;
    	} else {
    		System.out.println(puzzle + " is not a valid puzzle");
    		try{
    			Thread.sleep(100);
    		} catch(InterruptedException ex){
    			Thread.currentThread().interrupt();
    		}
    		return false;
    	}
    	
    }

    //Verify the puzzle entered is in correct format and solveable.
    public static boolean verifyPuzzle(String puzzle){
    	//First verify its of length 9 and has only integers
    	if(!puzzle.matches("^[0-8]{9}")) return false;
    	//Go through puzzle and count how many of each digit
    	int[] array = new int[9];
    	for(int i = 0; i < puzzle.length(); i++){
    		array[puzzle.charAt(i)-'0']++;
    	}
    	//Make sure there is one of each value in the string.
    	for(int i = 0; i < array.length; i++){
    		//return false if there is a digit that has anything other than 1.
			if(array[i] != 1) return false;
		}
		//Count inversions
		int inversions = 0;
		for(int i = 0; i < puzzle.length(); i++){
			for(int j = i+1; j < puzzle.length(); j++){
				if((puzzle.charAt(i)-'0' > puzzle.charAt(j)-'0') && (puzzle.charAt(i)-'0' != 0) && (puzzle.charAt(j)-'0') != 0){
					inversions++;
				}
			}
		}
		//If inversions are odd, return false (not solvable)
		return(inversions%2 != 1); 
    }

    public static int solvePuzzle(String puzzle, int heuristic){
    	int[] puzzleArray = new int[9];
    	for(int i = 0; i < puzzleArray.length; i++){
    		puzzleArray[i] = puzzle.charAt(i)-'0';
    	}
    	if(heuristic == 1){
    		return aStar(puzzleArray, 1);
    	} else {
    		return aStar(puzzleArray, 0);
    	}

    }

    //Heuristic based on how many misplaced tiles there are
    public static int getMisplacedTiles(int[] puzzle){
    	int misplacedTiles = 0;
    	for(int i = 0; i < puzzle.length; i++){
    		if(puzzle[i] != i) misplacedTiles++;
    	}
    	return misplacedTiles;
    }

    //Heuristic for the sum of manhattan distances to goal state(vertical and horizontal moves only)
    public static int getManhattanDistance(int[] puzzle){
    	int sumOfDistances = 0;
    	int value = 0;
    	for(int i = 0; i < puzzle.length; i++){
    		value = puzzle[i];
    		if(value == 0) continue;
    		int currentY = i / 3;
    		int currentX = i % 3;
    		int goalY = value / 3;
    		int goalX = value % 3;
    		sumOfDistances += Math.abs(goalY - currentY) + Math.abs(goalX - currentX);
    	}
    	return sumOfDistances;
    }

    //Get Zero Position
    public static int getZeroPosition(int[] puzzle){
    	int position = 0;
    	for(int i = 0; i < puzzle.length; i++){
    		if (puzzle[i] == 0) return i;
    	}
    	return -1;
    }

    public static ArrayList<int[]> nodeSuccessors(int[] puzzle){
    	ArrayList<int[]> successors = new ArrayList<int[]>();
    	int[] successor = Arrays.copyOf (puzzle, puzzle.length);
    	int zPos = getZeroPosition(puzzle);
    	switch(zPos){
    		case 0:
    		//Swap 0 and first tile
    			successors.addAll(Arrays.asList(swapTiles(zPos,1,puzzle)));
    		//Swap 0 and 3rd tile.
    			successors.addAll(Arrays.asList(swapTiles(zPos,3,puzzle)));
    			break;
    		case 1:
    		//Swap 1 and 0 tile
    			successors.addAll(Arrays.asList(swapTiles(zPos,0,puzzle)));
    			successors.addAll(Arrays.asList(swapTiles(zPos,2,puzzle)));
    			successors.addAll(Arrays.asList(swapTiles(zPos,4,puzzle)));
    			break;

    		case 2:
    			successors.addAll(Arrays.asList(swapTiles(zPos,1,puzzle)));
    			successors.addAll(Arrays.asList(swapTiles(zPos,5,puzzle)));
    			break;

    		case 3:
    			successors.addAll(Arrays.asList(swapTiles(zPos,0,puzzle)));
    			successors.addAll(Arrays.asList(swapTiles(zPos,4,puzzle)));
    			successors.addAll(Arrays.asList(swapTiles(zPos,6,puzzle)));
    			break;

    		case 4:
    			successors.addAll(Arrays.asList(swapTiles(zPos,1,puzzle)));
    			successors.addAll(Arrays.asList(swapTiles(zPos,3,puzzle)));
    			successors.addAll(Arrays.asList(swapTiles(zPos,5,puzzle)));
    			successors.addAll(Arrays.asList(swapTiles(zPos,7,puzzle)));
    			break;
    		
    		case 5:
    			successors.addAll(Arrays.asList(swapTiles(zPos,2,puzzle)));
    			successors.addAll(Arrays.asList(swapTiles(zPos,4,puzzle)));
    			successors.addAll(Arrays.asList(swapTiles(zPos,8,puzzle)));

    		case 6:
    			successors.addAll(Arrays.asList(swapTiles(zPos,3,puzzle)));
    			successors.addAll(Arrays.asList(swapTiles(zPos,7,puzzle)));
    			break;

    		case 7:
    			successors.addAll(Arrays.asList(swapTiles(zPos,4,puzzle)));
    			successors.addAll(Arrays.asList(swapTiles(zPos,6,puzzle)));
    			successors.addAll(Arrays.asList(swapTiles(zPos,8,puzzle)));

    		case 8:
    			successors.addAll(Arrays.asList(swapTiles(zPos,5,puzzle)));
    			successors.addAll(Arrays.asList(swapTiles(zPos,7,puzzle)));

    	}
    	return successors;
    }

 	public static int[] swapTiles(int from, int to, int[] puzzle){
 		int[] successor = Arrays.copyOf (puzzle, puzzle.length);
 		successor[from] = puzzle[to];
    	successor[to] = 0;
    	return successor;
 	}

 	public static boolean isSolved(int[] puzzle){
 		if(getMisplacedTiles(puzzle) == 0) return true;
 		return false;
 	}

 	public static int aStar(int[] puzzle, int heuristic){
 		int counter = 0;
 		long startTime = System.currentTimeMillis();

 		Comparator<Node> comparator = new Comparator<Node>(){
			@Override
			public int compare(Node n1, Node n2){
				return n1.getfValue() - n2.getfValue();
			}
		};

 		PriorityQueue<Node> openSet = new PriorityQueue<Node>(1000, comparator);
 		HashSet<Integer> visitedStates = new HashSet<Integer>();
 		Node state = new Node(puzzle, getHeuristic(puzzle,heuristic), 0, null);
 		while(!isSolved(state.getState())){
 			counter++;
 			//Add current state to visited states
 			visitedStates.add(Arrays.hashCode(state.getState()));
 			//get list of successors and add them as children
 			ArrayList<int[]> successors = nodeSuccessors(state.getState());
 			counter += successors.size();
 			for(int[] s : successors){
 				int pc = state.getPathCost();
 				state.addChild(new Node(s, getHeuristic(s, heuristic), ++pc, state));
 				if(heuristic == 0){
 					manhattanDist++;
 				} else {
 					misplacedDist++;
 				}
 			}
 			//Get children for current state
 			List<Node> children = state.getChildren();
 			//If the child != parent, add it to pq.
 			for(Node child : children){
 				if(!visitedStates.contains(Arrays.hashCode(child.getState()))) openSet.add(child);
 			}
 			if(!openSet.isEmpty()) state = openSet.poll();

 			
 		}
 		getPathTo(state);
 		long endTime = System.currentTimeMillis();
 		if(heuristic == 0){
 			manhattanRunTime+= (endTime - startTime);
 		} else {
			misplacedRunTime+= (endTime - startTime);
 		}
 		System.out.println("solved with depth " + state.getPathCost());
 		return counter;
 	}

 	public static void getPathTo(Node n){
 		if( n.getParent() != null){
 			getPathTo(n.getParent());
 		}
 		printBoard(n);
 	}

 	public static void printBoard(Node state){
 		int[] board = state.getState().clone();
 		System.out.println("-------");
 		for(int i = 0; i < 9; i++){
 			if(i % 3 == 0 && i != 0) System.out.println("\n");
 			System.out.print(board[i] + " ");
 		}
 		System.out.println();
 	}

 	public static int getHeuristic(int[] puzzle, int hchoice){
 		if(hchoice == 0){
 			return getManhattanDistance(puzzle);
 		} else {
 			return getMisplacedTiles(puzzle);
 		}
 	}

}

class Node{
	private List<Node> children = null;
	private int[] state;
	private int heuristic;
	private int pathcost;
	private int fValue;
	private Node parent;

	public Node(int[] state, int heuristic, int pathcost, Node parent){
		this.children = new ArrayList<>();
		this.state = state;
		this.heuristic = heuristic;
		this.pathcost = pathcost;
		this.fValue = this.heuristic + this.pathcost;
		this.parent = parent;
	}

	public int getPathCost(){
		return this.pathcost;
	}
	public int getfValue(){
		return this.fValue;
	}
	public Node getParent(){
		return this.parent;
	}

	public void addChild(Node successor){
		children.add(successor);
	}
	public int getHeuristic(){
		return this.heuristic;
	}
	public int[] getState(){
		return this.state;
	}
	public List<Node> getChildren(){
		return this.children;
	}

}














