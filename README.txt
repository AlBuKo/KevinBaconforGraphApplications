CSE4030 – Graph Theory and Applications
Project: Small-World Phenomenon (Kevin Bacon Numbers)

--------------------------------------------------
Student Information
--------------------------------------------------
Student Name : [ALPEREN BURAK KOÇYİĞİT]
Student ID   : [150121035]

--------------------------------------------------
Project Description
--------------------------------------------------
This project models the Small-World Phenomenon using a graph-based
approach. Actors are represented as vertices, and an undirected edge
exists between two actors if they have appeared in the same movie.

The program computes Kevin Bacon numbers (or similar actor numbers)
by finding the shortest path between a selected source actor and all
other actors using Breadth-First Search (BFS).

--------------------------------------------------
Files Included
--------------------------------------------------
- KevinBacon.java   : Main Java source file
- KevinBacon.class  : Compiled class file
- movies.txt        : Movie cast dataset
- test-sample.txt   : Query actor list
- README.txt        : Project documentation

--------------------------------------------------
Compilation
--------------------------------------------------
This project is written in Java and requires Java 17 or later.

Compile the program using:
javac KevinBacon.java

--------------------------------------------------
Execution
--------------------------------------------------
Run the program using:
java KevinBacon

The following files must be present in the same directory:
- movies.txt
- test-sample.txt

--------------------------------------------------
Program Usage
--------------------------------------------------
1. The program loads movie data from the file named "movies.txt".
2. It reads query actors from the file named "test-sample.txt".
3. The user selects a source actor from the provided list.
4. The program computes shortest paths using Breadth-First Search.
5. A histogram of actor numbers is printed.
6. Detailed shortest actor–movie–actor chains are printed.

--------------------------------------------------
Assumptions and Design Decisions
--------------------------------------------------
- The graph is undirected and unweighted.
- Actors appearing in the same movie are fully connected.
- Breadth-First Search (BFS) is used since all edges have equal weight.
- Actors not connected to the source actor are assigned infinite distance.
- All files are read from the current directory.
- No external libraries are used.
- The default Java package is used.

--------------------------------------------------
Output
--------------------------------------------------
- A histogram showing the distribution of actor numbers.
- For each actor in the test file, one shortest connection chain
  to the source actor is printed.
----------------------------------------------------