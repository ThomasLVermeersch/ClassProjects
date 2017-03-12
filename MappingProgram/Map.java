import java.util.*;
import java.io.*;

public class Map{
	//Initialize our adjacency matrix, and arrays to hold data
	private static String[][] cityArray = new String[20][7];
	private static int[][] dataArray = new int[400][3];
	private static int[][] adjacencyMatrix = new int[20][21];
	//Map constructor
	public Map(){
		//Create adjacency matrix from values in cityArray and dataArray
		for(int i = 0; i < 400; i++){
			int from = dataArray[i][0];
			int to = dataArray[i][1];
			int dist = dataArray[i][2];
			adjacencyMatrix[from][to] = dist;
		}
	}
	//Build our city array
	public static void buildCityArray(){
		int li = 0;
		try{
			//load file into scanner
			Scanner kb = new Scanner(new FileReader("city.dat"));
			//Go line by line
			while(li < cityArray.length){
				//Store line and tokenize it into an array of parts
				String line = kb.nextLine();
				String[] brokenLine = line.trim().split("\\s+");
				//Load from line, into the cityArray matrix
				for(int i = 0; i < brokenLine.length; i++){
					cityArray[li][i] = brokenLine[i];
				}
				li++;
				
			}
		} catch(FileNotFoundException e){
			System.out.println("File not found");
		}
	}
	//Build data array
	public static void buildDataArray(){
		int li = 0;
		try{
			//Load file into scanner
			Scanner kb = new Scanner(new FileReader("road.dat"));
			//Line by line go through file
			while(li < 77){
				//Tokenize Lines into array of values
				String line = kb.nextLine();
				String[] brokenLine = line.trim().split("\\s+");
				//Store values into dataArray after parsing into integers
				for(int i = 0; i < brokenLine.length; i++){
					dataArray[li][i] = Integer.parseInt(brokenLine[i]);
				}
				li++;
			}
		} catch(FileNotFoundException e){
			System.out.println("File not found");
		}
	}
	//Get the info about each city from the city code
	public static void getCityInfo(String cc){
		//Find city code in city array
		for(int i = 0; i < cityArray.length; i++){
			if(cityArray[i][1].equals(cc)){
				//Print each value of city if it isnt null
				for(int j = 0; j < cityArray[0].length; j++){
					if(cityArray[i][j] != null){
						System.out.print(cityArray[i][j] + " ");
					}
				}
				System.out.println();
			}
		}
	}
	//Insert a road
	public static void insertRoad(String c1, String c2, int dist){
		//Convert city codes to their respective numbers using codeToNumber()
		int from = codeToNumber(c1);
		int to = codeToNumber(c2);
		//Check if a road already exists there
		if(doesRoadExist(from,to)){
			System.out.println("Road already exists.");
			return;
		}
		//If not set their distance in adjacency matrix
		adjacencyMatrix[from][to] = dist;
		System.out.println("You have inserted a road from " + codeToFullCityName(c1) +
			" to " + codeToFullCityName(c2) + " with a distance of " + dist + ".");
	}
	//Remove a road
	public static void removeRoad(String c1, String c2){
		//convert city codes to their respective integer codes
		int from = codeToNumber(c1);
		int to = codeToNumber(c2);
		//Check if road exists
		if(doesRoadExist(from, to)){
			//If so set the distance to 0.
			adjacencyMatrix[from][to] = 0;
			System.out.println("The road from " + codeToFullCityName(c1) +
				" to " + codeToFullCityName(c2) + " has been deleted.");
		}
	}
	//Find shortest distance between each node
	public static int shortestDistance(String c1, String c2){
		//Convert city codes to respective integer codes
		int start = codeToNumber(c1);
		int finish = codeToNumber(c2);
		int shortPath[] = new int[21];
		//Set Starting value to 0
		shortPath[start] = 0;
		//Set all other values to 'infinity'
		for(int i = 1; i < shortPath.length; i++){
			if(i != start){
				shortPath[i] = 100000;
			}
		}
		//Loop through adjacency matrix
		for(int i = 1; i < 20; i++){
			for(int j = 1; j < 20; j++){
				//Check each edge
				int edge = adjacencyMatrix[i][j];
				//if the road exists (not zero)
				if(edge != 0){
					//if the shortest path so far is greater than the path to it + edge
					if(shortPath[j] > shortPath[i] + edge){
						//Update the shortest path.
						shortPath[j] = shortPath[i] + edge;
					}
				}

			}
		}
		//return shortest path
		return shortPath[finish];
	}
	//Convert the city code to the full name
	public static String codeToFullCityName(String city){
		String ct = "Not a city code";
		//find city codes
		for(int i = 0; i < cityArray.length; i++){
			if(cityArray[i][1].equals(city)){
				//if the city arrays 5th value is not null, there are 2 words in name
				if(cityArray[i][5] != null){
					ct = cityArray[i][2] + " " + cityArray[i][3];
				} else {
					ct = cityArray[i][2];
				}
				break;
			}
		}
		//return name
		return ct;
	}
	//convert integer city code to full city name
	public static String codeToFullCityName(int city){
		String ct = "Not a city code";
		city++;
		//Find city code
		System.out.println(cityArray[city].length);
		if(cityArray[city].length > 5){
			ct = cityArray[city][2] + " " + cityArray[city][3];
		} else {
			ct = cityArray[city][2];
		}
		return ct;
	}
	//Converts city code to its adjacency matrix vertex number
	public static int codeToNumber(String code){
		for(int i = 0; i < cityArray.length; i++){
			if(cityArray[i][1].equals(code)){
				return Integer.parseInt(cityArray[i][0]);
			}
		}
		System.out.println("City code doesnt exist.");
		return -1;
	}
	//Checks if road exists
	public static boolean doesRoadExist(int from, int to){
		if(adjacencyMatrix[from][to] == 0){
			System.out.println("The road from " + codeToFullCityName(from) +
			" to " + codeToFullCityName(to) + " does not exist");
			return false;
		}
		return true;
	}
}