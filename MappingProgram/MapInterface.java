import java.util.*;

public class MapInterface{
	//Boolean to loop through our program
	public static boolean commandLoop = true;

	public static void main(String[] args){
		//Loop our program command interface
		while(commandLoop){
			//Compile the city data and road data into matrices
			Map.buildCityArray();
			Map.buildDataArray();
			//Initialize map
			Map ourMap = new Map();
			//command Prompt
			System.out.print("Command? ");
			Scanner kb = new Scanner(System.in);
			String cmd = kb.nextLine();
			char command = Character.toUpperCase(cmd.charAt(0));
			//Pass command to our menu
			menu(command);
		}
	}
	public static void menu(char cmd){
		//Simply match the command to the case and call respective function
		switch(cmd){
			case 'Q': query(); 
			break;
			case 'D': distance();
			break;
			case 'I': insert();
			break;
			case 'R': removeRoad();
			break;
			case 'H': message();
			break;
			case 'E': commandLoop = false;
			break;
		}
	}
	//Function to ask for city information
	public static void query(){
		//Prompt city codes
		System.out.print("City code: ");
		Scanner kb = new Scanner(System.in);
		String cc = kb.nextLine();
		//call getCityInfo function with city requested
		Map.getCityInfo(cc);

	}
	//Function to return shortest distance between two cities
	public static void distance(){
		//Prompt city codes
		System.out.print("City codes: ");
		Scanner kb = new Scanner(System.in);
		String cc = kb.nextLine();
		//tokenize line into city codes
		String[] brokenLine = cc.split("\\s+");
		//Call distance function and print distance
		System.out.println("Shortest distance from " + Map.codeToFullCityName(brokenLine[0]) +
			" to " + Map.codeToFullCityName(brokenLine[1]) + " is " + Map.shortestDistance(brokenLine[0],brokenLine[1]));

	}
	//Function to insert road
	public static void insert(){
		//Prompt codes and distance
		System.out.print("City codes and distance: ");
		Scanner kb = new Scanner(System.in);
		String cc = kb.nextLine();
		//tokenize into codes and distance
		String[] brokenLine = cc.split("\\s+");
		//Call insert
		Map.insertRoad(brokenLine[0],brokenLine[1], Integer.parseInt(brokenLine[2]));
	}
	//Function to remove roads
	public static void removeRoad(){
		//Prompt city codes
		System.out.print("City codes: ");
		Scanner kb = new Scanner(System.in);
		String cc = kb.nextLine();
		//Tokenize string into codes
		String[] brokenLine = cc.split("\\s+");
		//Call remove function
		Map.removeRoad(brokenLine[0],brokenLine[1]);
	}
	//Function to print our menu when requested.
	public static void message(){
		System.out.println("\n" +
			"Q Query the city information by entering the city code.\n" +
			"D Find the minimum distance between two cities.\n" +
			"I Insert a road by entering two city codes and distance.\n" +
			"R Remove an existing road by entering two city codes.\n" + 
			"H Display this message. \n" + 
			"E Exit."
			);
	}

}