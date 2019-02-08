import java.io.*;
import java.net.*;


public class Network
{
	private Packet dropped;
	
    private BufferedReader sendIn;
    private BufferedReader receiveIn;
	
    private PrintWriter receiveOut;
    private PrintWriter sendOut;

    private NetworkThread sendThread;
    private NetworkThread receiveThread;
    
    private ServerSocket serverSocket;
    private Socket receiveSocket;
    private Socket sendSocket;
        
    public Network(int portNum)
    {
        dropped = null;
        
        try
        {
            serverSocket = new ServerSocket(portNum);
        }
        
        catch(Exception e)
        {
            System.out.println("Could not listen on port: " + portNum);
            System.exit(-1);
        }
        
        if(receiveSocket == null && sendSocket == null)
        {
        	System.out.println("Success! Waiting for sender and receiver...");

        	try
            {
                receiveSocket = serverSocket.accept();
                receiveOut = new PrintWriter(receiveSocket.getOutputStream(), true);
                receiveIn = new BufferedReader(new InputStreamReader(receiveSocket.getInputStream()));
                
                System.out.println("Get connection from receiver " + receiveSocket.getInetAddress().getHostAddress());
                
                sendSocket = serverSocket.accept();
                sendOut = new PrintWriter(sendSocket.getOutputStream(), true);
                sendIn = new BufferedReader(new InputStreamReader(sendSocket.getInputStream()));
                
                System.out.println("Get connection from sender " + sendSocket.getInetAddress().getHostAddress());
            }
            catch(Exception e)
            {
                System.out.println("Connection failed");
            }        
        }
    }
        
    public void run()
    {
    	
        receiveThread = new NetworkThread(receiveIn, sendOut); 
        sendThread = new NetworkThread(sendIn, receiveOut);
        
        receiveThread.start();
        sendThread.start();
        
        while(!sendThread.Terminated())
        {
            if(dropped != null)
            {
                receiveThread.SendPacket(dropped);
                dropped = null;
            }
            
            try
            {
                Thread.sleep(1000);
            }
            catch(InterruptedException e)
            {
                System.out.println("no timeout");
            }
        }
        receiveThread.Terminate();
        System.out.println("Network closed.");  
    }
       
    public static void main(String[] args)
    {
        if(args.length<1)
        {
            System.out.println("Port number not found");
            return;
        }
        else if(args.length>1)
        {
        	System.out.println("Please only enter port number");
        	return;
        }
        
        int portNum = Integer.parseInt(args[0]);
        Network port = new Network(portNum);
        port.run();             
    }
    
    //build a thread for both sender and receiver
    public class NetworkThread extends Thread
    {
    	private BufferedReader i;
        private PrintWriter o;
        private boolean terminate;
        
        public NetworkThread(BufferedReader in, PrintWriter out)
        {            
            i = in;
            o = out;
            terminate = false;
        }
                
        public void Terminate()
        {
        	terminate = true;
        }
        
        public boolean Terminated()
        {
            return terminate;
        }
        
        public void SendPacket(Packet p)
        {
            o.println(p.stringToPacket());
        }
        
        //Convert text file to packet, send the packet
        public void run()
        {
            while(!terminate)
            {
                String str = null;
                
                try
                {
                    str = i.readLine();
                }
                catch(IOException e)
                {
                    System.out.println("Error: Message could not be read");
                }
                
                if(str != null)
                {
                    Packet p = new Packet(str);
                    
                    //Is it done receiving
                    if(p.returnSeqNum() == -1)
                    {
                        o.println("-1");
                        terminate = true;
                        return;
                    }
                    
                    //Checks if the message has been received
                    if(p.checkingACK())
                    {
                        System.out.print("Received: " + p.regularString() + ", ");
                    }
                    
                    else
                    {
                        System.out.print("Received: Packet" + p.returnSeqNum() + ", " + p.returnID() + ", ");
                    }
                    
                    double x = Math.random();
                    
                    if(x < 0.5)
                    {
                        o.println(p.stringToPacket());
                        System.out.println("PASS");
                    }
                    else if(x <= 0.75)
                    {
                        p.checkSumAdder();
                        o.println(p.stringToPacket());
                        System.out.println("CORRUPT");
                    }
                    else
                    {
                        p.drop();
                        dropped = p;
                        System.out.println("DROP");
                    }
                } 
                else
                {
                    System.out.println("Error: Message was empty");
                }
            }
        }
    }
}