import java.io.*;
import java.net.*;

public class Receiver
{
	private static BufferedReader i;
	private static PrintWriter o;
	
    private static Socket socket;
        
    private static boolean terminate;
    private static boolean done;
    
    private static int state;
    private static int numReceived;
    
    public static void main(String[] args)
    {
        if(args.length != 2)
        {
            System.out.println("Error: Server URL or port number not found.");
            return;
        }
                
        String totalMessage = ""; //Total message added together
        String message = null; //Partial message received from network
        
        terminate = false;
        done = false;
        
        state = 0;
        numReceived = 0;
        
        //Attempt to connect with the network
        try
        {
        	System.out.println("Waiting to receive message.");
            socket = new Socket(args[0], Integer.parseInt(args[1])); //New socket with given URL and port number
            
            o = new PrintWriter(socket.getOutputStream(), true); //New output stream to network
            i = new BufferedReader(new InputStreamReader(socket.getInputStream())); //New input stream from network
        }
        catch(Exception e)
        {
            System.out.println("Error: Could not connect to server");
            System.exit(-1);
        }
        
        //Loop to receive message from sender via the network
        while(!terminate)
        {
            try
            {
                message = i.readLine();
            }
            catch(IOException e)
            {
                System.out.println("Error: message not received");
            }
            
            numReceived++;
            Packet packet = new Packet(message);
            
            System.out.printf("Waiting" + state + ", " + numReceived + ", " + message + ", ");
            
            if(packet.returnSeqNum() == -1)
            {
                terminate = true;
                o.println("-1");
                break;
            }
            
            if(state != packet.returnSeqNum())
            {
                packet = Packet.receiveACK(packet.returnSeqNum());
            }
            else if(packet.doubleCheckSum() == true)
            {
                totalMessage += packet.returnPacketContent() + " ";
                
                if(packet.returnPacketContent().indexOf(".") != -1)
                {
                    done = true;
                }
                
                packet = Packet.receiveACK(state);
                if(state == 0)
                {
                    state = 1;
                }
                else
                {
                    state = 0;
                }
            }
            else
            {
                packet = Packet.swapACK(state);
            }
            
            o.println(packet.stringToPacket());
            System.out.println(packet.regularString());
        }
        
        System.out.println();
        if(done)
        {
            System.out.println("Message is: " + totalMessage);
            totalMessage = "";
            done = false;
        }
        else
        {
        	System.out.println("Error: Please end message in '.'");
        }
        
        System.out.println("Receiver closed.");
    }
}