----------------------------Project summary----------------------------

This project contains a server program and a client program developed to run on two separate machines. They communicate with each other using socket communication on a TCP/IP protocol suite. The process is as follows: After initialization, the server process listens and
responds to the clients’ requests. A client will try to connect to the server and send out a message containing a command and inputs. The command will be arithmetic calculation commands of adding, subtracting and multiplying and is followed by inputs of one or more numbers (e.g. add 12 7). Upon receiving the command and inputs, the server will respond to the client with the calculating result (e.g. 19).

Note: This program was set up to only accept port number 9017 to avoid conflicts. This code has been tested on both the CISE machines remotely through PuTTy, and Windows 10.

-------------------------Compiling Instructions-------------------------

Commands to compile server.java on storm.cise.ufl.edu server:
javac server.java
java server 9017

Commands to compile client.java on thunder.cise.ufl.edu server:
javac client.java
java client storm.cise.ufl.edu 9017

-----------------------------Code structure------------------------------

My server.java code is structured into multiple different methods. In main, the server/client relationship is established, and the IO streams are set up. The client input is parsed into an array, which is then sent to the first parsing method (opParser), which parses the request and determines if it is valid, making use of another method (numParser) to ensure the input after the
operative are numbers. If the request is not valid, an error code is sent back. 

Additionally, if the user decides to terminate or say "bye", the code -5 is sent back and the communication between the client and server ceases. If it is valid, the next method (calculator) is called to calculate the request. After calculated, answer is returned from the method, and the answer is sent back to the client via the output stream.

My client.java code is fairly straightforward. After the IO streams are set up and the client establishes communication with the server, a while loop is entered to take in input from the client. It is only exited when the client requests termination of communication with the server through "bye" or "terminate". The input is then sent to the server for the server to perform its
parsing and calculations.

------------------------------Sample Results-----------------------------

Server side:
./server 9017
get connection from 127.0.0.1
get: add 4 5 2, return: 11
get: subtract 8 4, return: 4
get: multiply 3 4 5 6, return: 360
get: dkghdg 4 5, return: -1
get: add 4 5 6 7 8, return: -3
get: add 1, return: -2
get: terminate, return: -5

Client side:
get: Hello!
add 4 5 2
receive: 11
subtract 8 4
receive: 4
multiply 3 4 5 6
receive: 360
dkghdg 4 5
receive: incorrect operation command.
add 4 5 6 7 8
receive: number of inputs is more than four.
add 1
receive: number of inputs is less than two.
terminate

These results were expected. The client sent over multiple requests to add, subtract, multiply, and the server did just that with no issues. When introduced to bad input, the server handled them and sent back errors corresponding to their codes. After multiple tests, there are no abnormal results or bugs that I am aware of in this program.
