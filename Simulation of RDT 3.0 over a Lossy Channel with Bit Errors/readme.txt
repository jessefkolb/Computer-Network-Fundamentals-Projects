---------------------------------------Compiling instructions---------------------------------------

javac network.java packet.java receiver.java sender.java

Running instructions:

STORM SERVER:

java Network 9017

THUNDER SERVER:

java Receiver storm.cise.ufl.edu 9017

SAND SERVER:

java Sender storm.cise.ufl.edu 9017 message.txt

-------------------------------------------Code Structure-------------------------------------------

This project is separated into the three required java files (network, sender, and receiver) and one supporting java file (packet). Packet was added in to perform certain operations such as converting a string to a packet, calculating the checksum, keeping track of the sequence and ID numbers, and many more small helpful methods. The sender program begins by accepting the port number, URL of the network server, and message text file and creating a socket to communicate with the network. Next, the message from the text file is parsed into an array in preparation to be sent off into the network in individual packets. It then runs through a while loop as long as there are "packets" in the array, and sends and resends packets based on the outlined conditions in the project.

The network program begins by accepting the port number and creating new sockets through which it can send and receive code. The program now runs its packet transportation method -- run(); -- that spawns two threads (sendThread and receiveThread), allowing for two incoming connections with both sender and receiver. Packets are chosen randomly to be either PASS, CORRUPT, or DROP. If PASS gets chosen, the packet is delivered as normal to the receiver with either ACK0 or ACK1. If CORRUPT gets chosen, the supporting packet java file runs a method to corrupt the checksum, then the packet is delivered to the receiver with ACK0 or ACK1. If DROP gets chosen, ACK2 is delivered back to the sender. 

Finally, the receiver program begins by accepting the port number and URL of the network server and creating a socket to communicate with the network. Next, it runs a while loop as long as there are still packets to be received, determined by the sequence number. If the checksum checks out, the packet is accepted and added to the message and the state is changed. When complete, receiver prints out the final message.


------------------------------------------Execution Results------------------------------------------ 

(message.txt contains "hello there friend."):

Network:

Success! Waiting for sender and receiver...
Get connection from receiver 127.0.0.1
Get connection from sender 127.0.0.1
Received: Packet0, 1, DROP
Received: Packet0, 2, DROP
Received: Packet0, 3, PASS
Received: ACK0, PASS
Received: Packet1, 4, PASS
Received: ACK1, PASS
Received: Packet0, 5, DROP
Received: Packet0, 6, CORRUPT
Received: ACK1, PASS
Received: Packet0, 7, PASS
Received: ACK0, PASS
Network closed.

Receiver:

Waiting to receive message.
Waiting0, 1, 0 3 532 hello, ACK0
Waiting1, 2, 1 4 536 there, ACK1
Waiting0, 3, 0 6 679 friend., ACK1
Waiting0, 4, 0 7 678 friend., ACK0
Waiting1, 5, -1,
Message is: hello there friend.
Receiver closed.

Sender:

Reading message...
Waiting ACK0, 1, DROP, resend Packet0 ...
Waiting ACK0, 2, DROP, resend Packet0 ...
Waiting ACK0, 3, ACK0, send Packet0
Waiting ACK1, 4, ACK1, send Packet1
Waiting ACK0, 5, DROP, resend Packet0 ...
Waiting ACK0, 6, ACK1, resend Packet0 ...
Waiting ACK0, 7, ACK0, Message sent.
Sender closed.
