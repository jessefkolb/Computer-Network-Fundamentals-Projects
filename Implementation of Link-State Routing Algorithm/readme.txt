------------------------------------Project Summary-------------------------------	

This project reads a file named "network.txt" that contains a simulated network setup, then runs Dijkstra's (Link-State Routing) Algorithm to find the shortest path between two nodes in a network. The results are then printed in a table, detailing the path taken each step, along with the total number of steps to get from point A to point B.

----------------------------------Compile instructions----------------------------	

javac linkstate.java

----------------------------------Running instructions----------------------------	

java linkstate network.txt

----------------------------------Code Structure----------------------------------

The entire project is compact enough to be held within one class: linkstate. To begin, the method init() is called, where the header is printed, the textfile is parsed into a string, said string is parsed into an array delimited by spaces, and the new array is cleared of all non-ints to quickly determine distances between nodes in Dijkstra's algorithm. Additionally, a boolean array is initialized to store values that know if a node has been visited (all values are initially false). This is so the algorithm knows not to go to a node that has already been visited.

Next, DijkstraAlgorithm() is called, which determines the shortest distance/path to be taken between nodes in a graph. It begins by setting the current node to the initial one and setting its visited value in the array to "true," and then assigns a value of infinity (using Integer.MAX_VALUE) to the other nodes. Next, the minimum distance is calculated by iterating through a for loop and comparing two distances and keeping the minimum value. If the node with this distance is unvisited, it is now where we will visit. Repeat this until the final destination is reached. Once the final destination node is marked visited the algorithm stops.

----------------------------------Execution Results--------------------------------

The Network:

0 2 5 1 N N
2 0 3 2 N N
5 3 0 3 1 5
1 2 3 0 1 N
N N 1 1 0 2
N N 5 N 2 0

The Output Table:

-----------------------------------------------------------------------------------------
 | Step   | N'              | D(2),p(2) | D(3),p(3) | D(4),p(4) | D(5),p(5) | D(6),p(6) |
-----------------------------------------------------------------------------------------
 |      0 |     1           |    2,1    |    5,1    |    1,1    |     N     |     N     |
-----------------------------------------------------------------------------------------
 |      1 |     1,4         |    2,1    |    4,4    |    1,1    |    2,4    |     N     |
-----------------------------------------------------------------------------------------
 |      2 |     1,2,4       |    2,1    |    4,4    |    1,1    |    2,4    |     N     |
-----------------------------------------------------------------------------------------
 |      3 |     1,2,4,5     |    2,1    |    3,5    |    1,1    |    2,4    |    4,5    |
-----------------------------------------------------------------------------------------
 |      4 |     1,2,3,4,5   |    2,1    |    3,5    |    1,1    |    2,4    |    4,5    |
-----------------------------------------------------------------------------------------
 |      5 |     1,2,3,4,5,6 |    2,1    |    3,5    |    1,1    |    2,4    |    4,5    |
-----------------------------------------------------------------------------------------
