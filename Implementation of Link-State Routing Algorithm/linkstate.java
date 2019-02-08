import java.io.*;

public class linkstate {

	private static int MAXVAL = Integer.MAX_VALUE;

	String file;
	String filename;
	String[] fileArray;

	int[][] nodeDistances;
	boolean[] visitedArray;

	int[] endMinDistance;
	
	public static void printHeader(int nodes) 
	{
		for (int i = 0; i < 80; i++) 
		{
			System.out.print("-");
		}
		System.out.println();
		String printline = " | ";
		printline += "Step	 ";
		printline += " | ";
		printline += "N'	 ";
		for (int i = 2; i < nodes; i++) 
		{
			printline += " | ";
			printline += "D(" + i + "),p(" + i + ")";
		}
		
		System.out.println(printline);
		
		for (int i = 0; i < 80; i++) 
		{
			System.out.print("-");
		}
		System.out.println();
	}

	private String parseFileToString(String fname) {
		BufferedReader r = createFileReader(fname);
		String f = "";
		String temp = "";

		while (temp != null) 
		{
			temp = readLine(r);
			if (temp == null) 
			{
				continue;
			} 
			else if (temp.equals("EOF.")) 
			{
				f += temp;
			} 
			else 
			{
				f += temp + "\n";
			}
		}
		return f;
	}

	private String[] parseFileStringToArray(String file) 
	{
		String[] arr = null;
		arr = file.split("\n");
		return arr;
	}

	
	private int[][] parseStringToInt(String[] array) 
	{
		int length = array.length - 1;
		int[][] Distances = initializeDistanceArray(length);
		String nodeString = "";
		String[] nodeSplit = null;
		try 
		{
			for (int node = 0; node < length; node++) 
			{
				nodeString = array[node];
				nodeString = nodeString.replace(".", "");
				nodeSplit = nodeString.split(",");
				if (nodeString.contains("EOF")) 
				{
					continue;
				}
				for (int Distance = 0; Distance < length; Distance++) 
				{
					int value = Integer.MAX_VALUE;
					if (!nodeSplit[Distance].contains("N")) 
					{
						value = Integer.parseInt(nodeSplit[Distance]);
					}
					Distances[node][Distance] = value;
				}
			}
		} 
		catch (NumberFormatException e) 
		{
			System.out.println(e.getMessage());
		}
		return Distances;
	}

	public static String printDistanceArray(int[][] array) 
	{
		String printline = "";

		for (int node = 0; node < array.length; node++) 
		{
			for (int Distance = 0; Distance < array[node].length; Distance++) 
			{
				if (array[node][Distance] == MAXVAL) 
				{
					printline += "N";
				} 
				else 
				{
					printline += array[node][Distance];
				}

				printline += " ";
			}
			
			if (node != array.length - 1) printline += "\n";
		}
		return printline;
	}

	public static int[] initMinDistanceArray(int nodes) 
	{
		int[] printline = new int[nodes];
		
		for (int i = 0; i < nodes; i++) 
		{
			printline[i] = MAXVAL;
		}
		return printline;
	}

	public static int findClosestNeighbor(int[] minDistance, boolean[] visited) 
	{
		int minimum = MAXVAL;
		int printline = MAXVAL;
		
		for (int i = 0; i < minDistance.length; i++) 
		{
			if (!visited[i]) 
			{
				if (minDistance[i] < minimum) 
				{
					minimum = minDistance[i];
					printline = i;
				}
			}
		}
		return printline;
	}

	public static boolean allNodesVisited(boolean[] visited) 
	{
		for (boolean node : visited) 
		{
			if (!node) return false;
		}
		
		return true;
	}

	public static String visitedNodes(boolean[] visited) 
	{
		String nodes = "";
		for (int i = 0; i < visited.length; i++) 
		{
			if (visited[i]) nodes += (i + 1) + ",";
		}
		nodes = nodes.substring(0, nodes.length() - 1);
		
		return nodes;
	}

	public static boolean[] findneighbor(int[][] Distances, int node) 
	{
		boolean[] neighbor = new boolean[Distances.length];
		int Distance;
		for (int i = 0; i < Distances.length; i++) 
		{
			Distance = Distances[node][i];
			
			if (Distance != MAXVAL && Distance != 0) neighbor[i] = true;
			else neighbor[i] = false;
		}
		return neighbor;
	}

	public static String printMinsAndVisits(int[] previous, int[] minDistance) 
	{
		String printline = "";
		for (int i = 1; i < previous.length; i++) 
		{
			if (minDistance[i] == MAXVAL) 
			{
				printline += "N" + " | ";
			} 
			else 
			{
				printline += minDistance[i];
				printline += "," + (previous[i] + 1) + " | ";
			}
		}
		return printline;
	}

	private int minimum(int x, int y) 
	{
		if (x < y) 
		{
			if (x < 0) return y;
			else return x;
		} 
		else 
		{
			if (y < 0) return x;
			else return y;
		}
	}

	public static int[][] initializeDistanceArray(int nodes) 
	{
		int[][] DistanceArray = new int[nodes][nodes];

		for (int node = 0; node < nodes; node++) 
		{
			for (int Distance = 0; Distance < nodes; Distance++) 
			{
				DistanceArray[node][Distance] = MAXVAL;
			}
		}
		return DistanceArray;
	}

	private String readLine(BufferedReader reader) 
	{
		String line = "";
		try 
		{
			line = reader.readLine();
		} 
		catch (FileNotFoundException e) 
		{
			System.out.println("Error: File Not Found");
		} 
		catch (IOException e) 
		{
			System.out.println("Error: Bad Input");
		}
		return line;
	}

	private BufferedReader createFileReader(String name) 
	{
		BufferedReader reader = null;
		try 
		{
			reader = new BufferedReader(new FileReader(name));
		} 
		catch (FileNotFoundException e) 
		{
			System.out.println("Error: File Not Found");
		}
		return reader;
	}
	
	public void init() 
	{
		file = parseFileToString(filename); // Parses the text file into a string
		fileArray = parseFileStringToArray(file); // Parses the new string separated by return space into an array

		nodeDistances = parseStringToInt(fileArray); // Parses out the non-ints and converts to int array to access distances
		visitedArray = new boolean[fileArray.length - 1]; //Array to determine if a node has been visited or not

		System.out.println("");
		System.out.println("The Network:");
		System.out.println("");

		String temp = printDistanceArray(nodeDistances);

		System.out.println(temp);
		System.out.println("");
		System.out.println("The Output Table:");
		System.out.println("");

		printHeader(fileArray.length);

		//Perform Dijkstra's algorithm to find the shortest path
		DijkstraAlgorithm(nodeDistances, visitedArray); 
	}
	
	private void DijkstraAlgorithm(int[][] Distances, boolean[] visited) 
	{
		int step = 0;
		int currentNode = 0;
		
		int numNodes = Distances.length;
		int[] previousNodes = new int[numNodes];
		int[] minDistance = initMinDistanceArray(numNodes);
		
		int[][] tempDistance = Distances;

		for (int i = 0; i < numNodes; i++) 
		{
			minDistance[i] = Distances[0][i];
		}

		String printline = "";
		
		while (!allNodesVisited(visited)) 
		{
			currentNode = findClosestNeighbor(minDistance, visited); //Find the node closest that has not been visited yet

			printline = " | 	";
			printline += step++;
			printline += " | 	";

			visited[currentNode] = true;

			for (int i = 0; i < numNodes; i++)
			{
				if (!visited[i]) 
				{
					int change = minimum(minDistance[i], minDistance[currentNode] + tempDistance[currentNode][i]);
					
					if (change != minDistance[i]) 
					{
						minDistance[i] = change;
						previousNodes[i] = currentNode;
					}
				}
			}

			printline += visitedNodes(visited);
			printline += " | ";
			printline += printMinsAndVisits(previousNodes, minDistance);

			System.out.println(printline);
			
			for (int i = 0; i < 80; i++) 
			{
				System.out.print("-");
			}
			System.out.println();
		}

		endMinDistance = minDistance;
	}

	public static void main(String[] args) 
	{
		String textfile = args[0];
		linkstate ls = new linkstate();
		ls.filename = textfile;
		ls.init();
	}
}