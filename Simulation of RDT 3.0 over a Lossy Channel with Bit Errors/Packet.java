public class Packet
{
    private int seqNum;
    private int id;
    private int checkSum;
    private String packetContent;
    private boolean checkACK;
    
    public Packet()
    {
        checkSum = -2;
        checkACK = true;
    	
        seqNum = -2;
        id = 0;
    }
    
    public Packet(String parseMessage)
    {
        this();
        String[] parse = parseMessage.split(" ");
        
        if(parse.length >= 3)
        {
            seqNum = Integer.parseInt(parse[0]);
            id = Integer.parseInt(parse[1]);
            checkSum = Integer.parseInt(parse[2]);
            packetContent = parse[3];
            checkACK = false;
        }
        else if(parse.length >= 2)
        {
            seqNum = Integer.parseInt(parse[0]);
            checkSum = Integer.parseInt(parse[1]);
        }
        else
        {
            seqNum = Integer.parseInt(parse[0]);
        }
    }
    
    public String stringToPacket()
    {
        String s = "";
        if(checkACK == false)
        {
            s += seqNum + " " + id + " " + checkSum + " " + packetContent;
        }
        else
        {
            s += seqNum + " " + checkSum;
        }
        return s;
    }
    
    public String regularString()
    {
        String rs = "";
        if(checkACK == false)
        {
            rs += seqNum + " " + id + " " + checkSum + " " + packetContent;
        }
        else
        {
            if(seqNum < 2)
            {
                rs += "ACK" + seqNum;
            }
            else
            {
                rs += "DROP";
            }
        }
        return rs;
    }
    
    public int calculateSum(String content)
    {
        int sum = 0;
        for(int i = 0; i < content.length(); i++)
        {
            sum += (int) content.charAt(i);
        }
        return sum;
    }
    
    public boolean doubleCheckSum()
    {
        if(checkSum == calculateSum(this.packetContent))
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    
    public void checkSumAdder()
    {
        checkSum += 1;
    }
    
    public void drop()
    {
        seqNum = 2;
        checkACK = true;
    }
    
    public static Packet receiveACK(int sequenceNum)
    {
        Packet p = new Packet();
        p.seqNum = sequenceNum;
        p.checkSum = 0;
        return p;
    }
    
    public static Packet swapACK(int sequenceNum)
    {
        if(sequenceNum == 0)
        {
            sequenceNum = 1;
        }
        else
        {
            sequenceNum = 0;
        }
        
        Packet p = new Packet();
        p.seqNum = sequenceNum;
        p.checkSum = 0;
        return p;
    }  
    
    public void sequenceNumber(int seqNum)
    {
        this.seqNum = seqNum;
    }
    
    public void idNumber(int id)
    {
        this.id = id;
    }
    
    public void sumNumber(int checkSum)
    {
        this.checkSum = checkSum;
    }
    
    public void contentString(String packetContent)
    {
        this.packetContent = packetContent;
    }
    
    public void booleanACK(boolean checkACK)
    {
        this.checkACK = checkACK;
    }
    
    public boolean checkingACK()
    {
        return checkACK;
    }
    
    public int returnSeqNum()
    {
        return seqNum;
    }
    
    public int returnID()
    {
        return id;
    }
    
    public int returnCheckSum()
    {
        return checkSum;
    }
    
    public String returnPacketContent()
    {
        return packetContent;
    }
    
}