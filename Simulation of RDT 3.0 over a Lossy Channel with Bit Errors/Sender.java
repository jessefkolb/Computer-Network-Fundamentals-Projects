import java.io.*;
import java.net.*;

public class Sender
{
	private static BufferedReader i;
	private static PrintWriter o;
	private static BufferedReader fileReader;
	
    private static Socket socket;
    
    private static boolean terminate;
    private static boolean done;
    private static boolean ACKd;
    
    private static int state;
    private static int numSent;
    private static int count;
     
    public static void main(String[] args)
    {
        Packet resendMessage = null;
        if(args.length != 3)
        {
            System.out.println("Error: Server URL, port number, or text file not found.");
            return;
        }
        
        String[] messageParsed = null;
        String message = null;
               
        state = 0;
        numSent = 0;
        count = 0;
        terminate = false;
        done = false;
        ACKd = false;
        
        //Attempt to connect with the network
        try
        {
            socket = new Socket(args[0], Integer.parseInt(args[1]));
            o = new PrintWriter(socket.getOutputStream(), true);
            i = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            fileReader = new BufferedReader(new FileReader(args[2]));
        }
        catch(FileNotFoundException e)
        {
            System.out.println("Error 404: File not found");
            System.exit(-1);
        }
        catch(Exception e)
        {
            System.out.println("Error: Could not connect to server");
            System.exit(-1);
        }
        
        System.out.println("Success! Connected to server");
        
        try
        {
            message = fileReader.readLine();
            System.out.println("Reading message...");
        }
        catch(IOException e)
        {
            System.out.println("Error: Could not find message.");
        }
        
        //parses file message into array
        messageParsed = message.split(" ");
        
        while(!terminate)
        {
        	//All packets sent
            if(messageParsed.length <= count)
            {
                done = true;
            }
            
            if(ACKd == true)
            {
                String sendMessage = null;
                
                try
                {
                    sendMessage = i.readLine();
                }
                catch(IOException e)
                {
                    System.out.println("Error: Could not find message.");
                }
                
                Packet sender = new Packet(sendMessage);
                System.out.print("Waiting ACK" + state + ", " + numSent + ", " +  sender.regularString() + ", ");
                
                if(sender.returnSeqNum() == 2) //dropped packet
                {
                	System.out.println("resend Packet" + state + " ...");
                	
                    numSent++;
                    
                    resendMessage.idNumber(numSent);
                    resendMessage.booleanACK(false);
                    
                    o.println(resendMessage.stringToPacket());
                }
                
                else if(sender.returnSeqNum() != state) //bad ACK
                {
                	System.out.println("resend Packet" + state + " ...");
                	
                    numSent++;
                    
                    resendMessage.idNumber(numSent);
                    
                    o.println(resendMessage.stringToPacket());
                }
                else if(sender.returnCheckSum() != 0) //packet corrupted
                {
                	System.out.println("CORRUPT, resend Packet" + state + " ...");
                	
                    numSent++;
                    
                    resendMessage.idNumber(numSent);
                    
                    o.println(resendMessage.stringToPacket());
                }
                else //sent packet
                {
                    ACKd = false;
                    
                    if(done == true)
                    {
                        System.out.println("Message sent.");
                        o.println("-1");
                        terminate = true;
                    }
                    else
                    {
                        System.out.println("send Packet" + state);
                    }  
                    
                    //State change
                    if(state == 0)
                    {
                        state = 1;
                    }
                    else
                    {
                        state = 0;
                    }
                }
            }
            else
            {
            	ACKd = true;
            	
                Packet send = new Packet();
                String sendMsg = messageParsed[count];
                
                send.sequenceNumber(state);
                send.idNumber(numSent);
                send.sumNumber(send.calculateSum(sendMsg));
                send.contentString(sendMsg);
                send.booleanACK(false);
                
                count++;
                numSent++;
                
                resendMessage = send;
                resendMessage.idNumber(numSent);
                
                o.println(resendMessage.stringToPacket()); 
            }
        }
        System.out.println("Sender closed.");
    }
    

}