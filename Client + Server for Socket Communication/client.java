import java.net.*;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.Arrays;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class client {
	
	public static void main(String[] args) throws IOException
	{
		String clientInput;
		String server = args[0];
		int port = Integer.parseInt(args[1]);
		Socket s = new Socket(server, port);
		
		BufferedReader i = new BufferedReader(new InputStreamReader(s.getInputStream()));
		BufferedReader consoleInput = new BufferedReader(new InputStreamReader(System.in));
		PrintWriter o = new PrintWriter(s.getOutputStream(), true);
			
		String request = i.readLine();
		System.out.println("get: " + request);		
		
		while((clientInput = consoleInput.readLine()) != null)
		{
			
			o.println(clientInput.toLowerCase());
			request = i.readLine();
			
			if(request.equals("-1"))
			{
				request = "incorrect operation command.";				
			}
			else if(request.equals("-2"))
			{
				request = "number of inputs is less than two.";
			}
			else if(request.equals("-3"))
			{
				request = "number of inputs is more than four.";
			}
			else if(request.equals("-4"))
			{
				request = "one or more of the inputs contain(s) non-number(s).";
			}
			else if(request.equals("-5"))
			{
				request = "exit.";
				break;
			}
			
			System.out.println("receive: " + request);
			
		}
		
	}
}
