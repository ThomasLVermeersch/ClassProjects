//Scheduler for Processes implemented with:
// First Come First Serve
// Shortest Job First
//Round-Robin with Time quantum of 30
//Round-Robin with Time Quantum of 60
//Lottery with time quantum of 50

import java.util.*;
import java.io.*;

public class ProcessScheduler{

    public static void main(String args[]){
    	try{
    		PrintStream printStream = new PrintStream(new FileOutputStream("ProjectOneOutput.txt"));
			System.setOut(printStream);
		} catch(FileNotFoundException e){
			
		}

    //Show interface
		while(true){
 	    	System.out.println("Which Algorithm would you like to use?\n"+
	    	"[0] First-Come-First-Serve\n"+
	    	"[1] Shortest Job First\n"+
	    	"[2] Round Robin with Time Quantum of 30\n"+
	    	"[3] Round Robin with Time Quantum of 60\n"+
	    	"[4] Lottery with Time Quantum of 50\n" +
	    	"[5] Exit");
	    	System.out.print("Choice: ");
	//Get choice from user and based on choice, call the getFileName function and execute proper function with it.
	    	Scanner kb = new Scanner(System.in);
	    	String filename;
	    	int choice = kb.nextInt();
	    	switch(choice){
	    		case 0:
	    			filename = getFileName();	
	    			firstComeFirstServe(filename);
					break;
				case 1:
					filename = getFileName();
					shortestJob(filename);
					break;
				case 2: 
					filename = getFileName();
					roundRobin(30, filename);
					break;
				case 3: 
					filename = getFileName();
					roundRobin(60, filename);
					break;
				case 4:
					filename = getFileName(); 
					lottery(50, filename);
					break;
				case 5:
					System.out.println("Adios, mi amigo.");
					System.exit(0);
					break;
				default: System.out.println("That is not a choice");
	    	}
		}
    }
    //Function to request and return a filename from the user and test to make sure it is a legitimate file.
    public static String getFileName(){
		String filename = "";
		Boolean didYouActuallyEnterAProperFileName = false;
		while(!didYouActuallyEnterAProperFileName){
		    System.out.println("Which File would you like to test? ");
		    Scanner kb = new Scanner(System.in);
	    	filename = kb.nextLine();
	    	File f = new File(filename);
	    	if(f.exists()){
			didYouActuallyEnterAProperFileName = true;
	    	}
		}
		return filename;
    }

    //First Come First Serve algorithm
    public static void firstComeFirstServe(String filename){
    	int cpuTime = 0;
    	int swapSpeed = 3;
    	int totalProcesses = 0;
		System.out.println("First-Come-First-Serve algorithm initiated on file " + filename);
		try{
	//Load file
	    	Scanner kb = new Scanner(new FileReader(filename));
	    	while(kb.hasNextLine()){
	//Store necessary data from the file and count how many processes.
	    		String processId = kb.nextLine();
	    		String burst_time = kb.nextLine();
	    		String priority = kb.nextLine();
	    		totalProcesses++;
	//Specify what process has begun, add its burst time to the cputime.
	    		System.out.println("Process " + processId + " initiated at CPU time: " + cpuTime);
	    		cpuTime += Integer.parseInt(burst_time);
	    		System.out.println("Process " + processId + " completed at " + cpuTime);
	//Process completes in one go, increment by the swap speed to take into account next swap
	    		cpuTime += swapSpeed;
			}
	//Print average time for processes
			System.out.println("Average time per process: " + cpuTime / totalProcesses);
		}catch(FileNotFoundException e){
	    	System.out.println("File not found");
    	}
	}

	//Function to handle round robin algorithm
	public static void roundRobin(int tq, String filename){
		int cpuTime = 0;
		int swapSpeed = 3;
		int priority = 0;
		int burst_time = 0;
		int processId = 0;
		int totalProcesses = 0;
	//Print what file is being used and initialize a new queue.
		System.out.println("Round Robin with time quantum of " + tq + " initiated on file " + filename);
		Queue<Integer> queue = new LinkedList<Integer>();
		try{
	//Open file and read through it and populate the queue
			Scanner kb = new Scanner(new FileReader(filename));
			while(kb.hasNextLine()){
				queue.add(Integer.parseInt(kb.nextLine()));
				totalProcesses++;
			}
			int counter = 0;
	//Iterate through the queue until it is empty
			while(!queue.isEmpty()){
	//Every third item in the queue is a new process, so we handle each item in queue differently
				switch(counter % 3){
	//Case 0 means the front of the queue has a process Id, so we remove and store that and announce its initiation.
					case 0:
						processId = queue.remove();
						System.out.println("Process " + processId + " initiated at CPU time: " + cpuTime);
						break;
	//Case 1 means burst time is at the front of the queue. Remove the burst time from the queue and check if it can finish
	//in the alloted time given by the time quantum, if it is we set the burst_time to zero, otherwise it needs to go back in
	//the queue
					case 1:
						burst_time = queue.remove();
						if(burst_time <= tq){
							cpuTime += burst_time;
							burst_time = 0;
							System.out.println("Process " + processId + " completed at CPU time: " + cpuTime);
						} else {
							System.out.println("Process " + processId + " has been swapped out");
							burst_time -= tq;
							cpuTime += tq;
						}
						break;
	//Case 2 means it is the lottery number (which is irrelevant) and signifies the end of the process data
	//Check if the process is complete, if not put back in the queue.
					case 2:
						priority = queue.remove();
						cpuTime+=swapSpeed;
						if(burst_time > 0){
							queue.add(processId);
							queue.add(burst_time);
							queue.add(priority);
						}
						break;
				}
				counter++;
			}
	//Print the average time per process. (divide by 3 to account for the data that was read too)
			System.out.println("Average time per process: " + cpuTime / (totalProcesses/3));
		}catch(FileNotFoundException e){
			System.out.println("File not Found");
		}

	}

	//Implement algorithm for shortest job.
	public static void shortestJob(String filename){
		int swapSpeed = 3;
		int cpuTime = 0;
		int totalProcesses = 0;
		System.out.println("Shortest job initiated on file " + filename);
	//Treemap with burst_time as key, id as value
	//Treemap sorts by key value, so shortest burst_time will always be top of tree.
		Map<Integer, Integer> treemap = new TreeMap<Integer, Integer>();
		try{
			Scanner kb = new Scanner(new FileReader(filename));
	//read each line of file
			while(kb.hasNextLine()){
	//Sort the data and store in the treemap
				int processId = Integer.parseInt(kb.nextLine());
				int burst_time = Integer.parseInt(kb.nextLine());
				String throwaway = kb.nextLine();
				totalProcesses++;
				treemap.put(burst_time, processId);
			}
	//Iterate through the treemap by key and run them until they are complete. Update cpu time as we go.
			for(Integer key : treemap.keySet()){
				int processId = treemap.get(key);
				int burst_time = key;
				System.out.println("Process " + processId + " has been initiated at cputime: " + cpuTime);
				cpuTime += burst_time;
				System.out.println("Process " + processId + " completed at cputime: " + cpuTime);
				cpuTime += swapSpeed;
			}
			System.out.println("Average time per process: " + cpuTime / totalProcesses);


		} catch(FileNotFoundException e){
			System.out.println("File not found");
		}

	}

	//Function that implements lottery algorithm
	public static void lottery(int tq, String filename){
		int swapSpeed = 3;
		int cpuTime = 0;
		int priorityTotal = 0;
		int processesToHandle = 0;
		int totalProcesses = 0;
		try{
	//Create 3d array to store processes and their data. (array size 30 as specified is max processes in a file)
			int[][] array = new int[30][3];
			Scanner kb = new Scanner(new FileReader(filename));
	//Read 3 lines and store them into the proper array indices
	//Take note of the total priority count to know the lottery pool size
			while(kb.hasNextLine()){
				int processId = Integer.parseInt(kb.nextLine());
				int burst_time = Integer.parseInt(kb.nextLine());
				int priority = Integer.parseInt(kb.nextLine());
				array[processId][0] = burst_time;
				array[processId][1] = priorityTotal;
				priorityTotal+= priority;
				array[processId][2] = priorityTotal;
				processesToHandle++;
			}
			totalProcesses = processesToHandle;
	//Loop while there are still processes to handle
			while(processesToHandle != 0){
	//Get new random number between 0 and the lottery pool size.
				Random rand = new Random();
				int lotteryNumber = rand.nextInt(priorityTotal);
	//Go through the total processes
				for(int i = 1; i <= totalProcesses; i++){
					//This is the process that won the lottery
					int burst_time = array[i][0];
					int lower_bound = array[i][1];
					int upper_bound = array[i][2];
	//Pick a process to handle based on what the random lottery number was
					if(lotteryNumber >= lower_bound && lotteryNumber < upper_bound && burst_time != 0){
						System.out.println("Process " + i + " initiated at cpu time: " + cpuTime);
	//If burst time is less than the time quantum, run it til completion and take not of its completion
	//Otherwise subtract the time quantum and go again.
						if(burst_time <= tq){
							cpuTime += burst_time;
							array[i][0] = 0;
							System.out.println("Process " + i + " completed at cpu time: " + cpuTime);
							cpuTime += swapSpeed;
							processesToHandle--;
						} else {
							array[i][0] -= tq;
							cpuTime += tq;
							System.out.println("Process " + i + " swapped out at cpu time: " + cpuTime);
							cpuTime += swapSpeed;
						}
					}
				}
			}
	//Print the average time per process.
			System.out.println("Average time per process: " + cpuTime / totalProcesses);
		} catch(FileNotFoundException e){
			System.out.println("file not found");
		}


	}

}