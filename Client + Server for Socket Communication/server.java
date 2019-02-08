import java.net.*;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.Arrays;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class server {

	private static boolean close = false;
	private static String[] requests = {"bye", "add", "multiply", "subtract", "terminate"};
	
	//Takes the input in the form of an array, parses it, and performs calculations
	public static int calculator(String[] arr) 
	{
		int answer = Integer.parseInt(arr[1]); //start at position 1 because 0 holds the operative
		
		if(arr[0].toLowerCase().equals("add"))
		{
			for (int i = 2; i < arr.length; i++) 
			{
				answer += Integer.parseInt(arr[i]);
			}
		}
		
		if(arr[0].toLowerCase().equals("subtract"))
		{
			for (int i = 2; i < arr.length; i++) 
			{
				answer -= Integer.parseInt(arr[i]);
			}
		}
		
		if(arr[0].toLowerCase().equals("multiply"))
		{
			for (int i = 2; i < arr.length; i++) 
			{
				answer *= Integer.parseInt(arr[i]);
			}
		}
		return answer;
	}

	//Figures out if the input after the operative contains only numbers
	public static boolean onlyNumbers(String str) {
		try 
		{
			Integer.parseInt(str);
		} 
		catch (NumberFormatException nfe) 
		{
			return false;
		}
		return true;
	}

	//Parses the second half of input (presumably numbers) after the operative
	public static boolean numParser(String[] arr) 
	{
		for (int i = 1; i < arr.length; i++) 
		{
			if (!onlyNumbers(arr[i]))
			{
				return false;
			}
		}
		return true;
	}

	// Parses the first half of input (presumably the operative) and
	// assigns error codes to unaccepted input
	public static int opParser(String[] arr) 
	{
		int answer = 0;
		arr[0] = arr[0].toLowerCase();

		if (arr[0].equals("bye") || arr[0].equals("terminate")) 
		{
			answer = -5;
		} 
		else if (!Arrays.asList(requests).contains(arr[0])) 
		{
			answer = -1;
		} 
		else if (arr.length <= 2) 
		{
			answer = -2;
		} 
		else if (arr.length > 5) 
		{
			answer = -3;
		} 
		else if (!numParser(arr)) 
		{
			answer = -4;
		}
		else 
		{
			answer = calculator(arr);
		}

		return answer;
	}

	public static void main(String[] args) throws IOException 
	{
		int port;
		
		if(!(args[0] == null))
		{
			port = Integer.parseInt(args[0]);
		}
		else
		{
			port = 9017;
		}
		
		ServerSocket server = new ServerSocket(port);
		System.out.println("./server " + port);
		
		try 
		{
			while (true) 
			{
				Socket s = server.accept();
				System.out.println("get connection from " + s.getInetAddress().getHostName());

				try 
				{
					BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream())); //Input stream
					PrintWriter o = new PrintWriter(s.getOutputStream(), true); //Output stream
					o.println("Hello!"); //Server is ready for communication

					while (true) 
					{
						String i = in.readLine();
						String[] arr = i.split(" "); //Parse input string into array to send to methods
						
						if (i.toLowerCase().equals("terminate"))
						{
							close = true;
						}	

						int answer = opParser(arr); //Parse, error check, then calculate
						
						System.out.println("get: " + i + ", return: " + answer);
						
						o.println(answer);
						
						if (answer == -5)
						{
							break;
						}	
					}

				} 
				
				finally 
				{
					// Close socket but not server
					s.close();
					if (close)
					{
						break;
					}
				}
			}
		} 
		
		finally 
		{
			// Close server upon user termination request
			// System.out.println("Closing server");
			server.close();
		}
	}
}