

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

/*
 * This class represents a graph that has vertex from ID = 1 to ID = n.
 * A DFS-based algorithm (Kosaraju's Two-Pass Algorithm)
 * is implemented to find the strongly connected components (SCC).
 *
 * Algorithm time complexity: O(m+n) where n is the number of vertices and m is the number of edges
 *
 * @author: Zitao He
 * */
/**
 * Project: Coursera Algorithms by Standford University: Graph Search, Shortest Paths, and Data Structures
 * Description: Week 2 Programming Assignment.
 *              This class represents a graph that has vertex from ID = 1 to ID = n.
 *              A DFS-based algorithm (Kosaraju's Two-Pass Algorithm)
 *              is implemented to find the strongly connected components (SCC).
 * @author : Zitao He
 * @date : 2021-01-22 20:47
 **/

public class GraphSCCFinder {
    //All first integer in below hashmaps is the ID of vertex
    private HashMap<Integer, ArrayList<Integer>> vertices; //Graph representation
    private HashMap<Integer, ArrayList<Integer>> reversed; //Reversed graph representation
    private int[] leader; //Track leader vertex for each vertex in graph (only used in second DFS loop)
    private boolean [] explored; //Track whether or not the vertex is explored
    private int [] finishTime; //Track the finish time for each vertex.
                               //This will be used as ordering in second DFS loop.
                               //Note: the index of finishTime array is finish time value.
                               //The value of finishTime array is vertex ID.
    private int finishTimer; //Track the finish time for each DFS loop
    private int s; //Temp vertex ID used in DFSLoop method

    /**
     * Construct the directed graph from external .txt file. First integer in each line is start(tail) node.
     * Second integer in each line is end(head) node.
     * @param inputFileName string of the file name
     * @throws FileNotFoundException when file not found, throw exception
     */
    public GraphSCCFinder(String inputFileName) throws FileNotFoundException {
        vertices = new HashMap<Integer, ArrayList<Integer>>();
        reversed = new HashMap<Integer, ArrayList<Integer>>();
        finishTimer = 0;

        int idx = 0;
        try{
            Scanner fileScanner = new Scanner(new File(inputFileName));
            //add vertices
            while(fileScanner.hasNextInt()){

                int startId = fileScanner.nextInt();
                int endId = fileScanner.nextInt();

                //add a vertex using the first int value of each row in file as vertex ID
                if(!vertices.containsKey(startId)){
                    vertices.put(startId, new ArrayList<Integer>());
                }
                if(!vertices.containsKey(endId)){
                    vertices.put(endId, new ArrayList<Integer>());
                }
                if(!reversed.containsKey(startId)){
                    reversed.put(startId, new ArrayList<Integer>());
                }
                if(!reversed.containsKey(endId)){
                    reversed.put(endId, new ArrayList<Integer>());
                }
                //add edge to the graph. Each line (pair of two integers) represent one directed edge
                //that has 1st integer as start vertex and 2nd integer as end vertex
                vertices.get(startId).add(endId);
                reversed.get(endId).add(startId);
                System.out.println("Loading file " + (double)100*(double)idx++/(double)5000000 + "%...");
            }
        }
        catch (IOException e) {
            throw new FileNotFoundException("Error: Input file is not found.");
        }
    }



    /**
     * Implement the first DFS loop DFS-based algorithm (Kosaraju's Two-Pass Algorithm)
     * The purpose of this loop is to find finishing time for each vertex
     * This loop uses reversed graph(not normal graph)
     */
    public void firstDFSLoop(){
        finishTime = new int[reversed.size()];
        explored = new boolean[reversed.size()];
        Arrays.fill(explored, Boolean.FALSE); //set all explored value to false

        //finishTime also represents # of nodes processed so far
        int numOfVertices = reversed.size();
        for (int i = numOfVertices; i >= 1; i--){
            if (explored[i-1] == false){
                firstDFS(i);
            }
        }
    }

    /**
     * Implement the first DFS loop DFS-based algorithm (Kosaraju's Two-Pass Algorithm)
     * The purpose of this loop is to find SCC. Each SCC has vertices that all have the same leader
     * This loop uses normal graph(not reversed graph)
     */
    public void secondDFSLoop(){
        leader = new int[vertices.size()];
        Arrays.fill(leader, -1); //set all leader value to -1 (invalid negative)

        explored = new boolean[vertices.size()];
        Arrays.fill(explored, Boolean.FALSE); //set all explored value to false
        s = -1; //negative value to initialize an invalid vertex. This is used for leaders in 2nd loop
        int numOfVertices = vertices.size();
        for (int i = numOfVertices; i >= 1; i--){
            int currVertex = finishTime[i-1]; //Get the vertex that has i as finish time
                                              //Note: finish time starts with 0(index) in finishTime array!
            if (explored[currVertex-1] == false){
                s = currVertex;
                secondDFS(currVertex, s);
            }
        }
    }

    /**
     * First DFS for first DFS loop in Kosaraju's Two-Pass Algorithm
     * @param startNode the vertex(note) to start first DFS
     */
    public void firstDFS(int startNode){
        explored[startNode-1] = true; //mark start node as explored
        //System.out.println("1st DFS ongoing...Current vertex is: " + startNode);
        for (int neighbor : reversed.get(startNode)){
            if (explored[neighbor-1] == false){
                firstDFS(neighbor);
            }
            //helper method to track path of DFS
//            if(explored[neighbor-1] == true){
//                System.out.println("Oops, find the vertex that explored before: " + neighbor);
//            }
        }
        finishTime[finishTimer] = startNode; //finish time starts with zero!
        finishTimer ++;
    }

    /**
     * Second DFS for second DFS loop in Kosaraju's Two-Pass Algorithm
     * @param startNode the vertex(note) to start second DFS
     * @param leaderNode the leader vertex(note) of the startNode
     */
    public void secondDFS(int startNode, int leaderNode){
        explored[startNode-1] = true;
        leader[startNode-1] = leaderNode;
        //System.out.println("2nd DFS ongoing...Current vertex is: " + startNode);
        for (int neighbor : vertices.get(startNode)){
            if (explored[neighbor-1] == false){
                secondDFS(neighbor, leaderNode);
            }
            //helper method to track path of DFS
//            if(explored[neighbor-1] == true){
//                System.out.println("Oops, find the vertex that explored before: " + neighbor);
//            }
        }
    }

    /**
     * Compute the SCCs
     * @return an integer array that has the number of vertices in top 5 largest SCCs, in decreasing order
     *         If there are less than 5 SCCs, fill the rest array values with 0.
     */
    public int[] getSCC(){
        firstDFSLoop();
        secondDFSLoop();
        HashMap<Integer, Integer> leaderStats = new HashMap<Integer, Integer>();
        for (int i=0; i<leader.length; i++){
            if (!leaderStats.containsKey(leader[i])){
                leaderStats.put(leader[i], 1);
            }
            else{
                int temp = leaderStats.get(leader[i]) + 1;
                leaderStats.put(leader[i], temp);
            }
        }
        //System.out.println(Arrays.toString(leader));
        ArrayList<Integer> allSCC = new ArrayList<Integer>();
        for (int key : leaderStats.keySet()){
            allSCC.add(leaderStats.get(key));
        }
        Collections.sort(allSCC);
        Collections.reverse(allSCC);
        int[] topFive = new int[5];
        for (int i=0; i<5; i++){
            if (i < allSCC.size()){
                topFive[i] = allSCC.get(i);
            }
            else{
                topFive[i] = 0;
            }
        }
        return topFive;
    }

    public void printGraph(){
        System.out.println("Printing graph...");
        for (Map.Entry<Integer, ArrayList<Integer>> entry : vertices.entrySet()) {
            System.out.println("Vertex " + entry.getKey() + " has outgoing vertices: " + entry.getValue().toString());
        }
        System.out.println("Total number of vertices in graph is: " + vertices.size());
    }

    public void printReversedGraph(){
        System.out.println("Printing REVERSED graph...");
        for (Map.Entry<Integer, ArrayList<Integer>> entry : reversed.entrySet()) {
            System.out.println("Vertex " + entry.getKey() + " has outgoing vertices: " + entry.getValue().toString());
        }
        System.out.println("Total number of vertices in reversed graph is: " + reversed.size());
    }

    //helper method for de-bugging
    public void printGraphInfo(){
        System.out.println("Printing other graph info...");
        System.out.println("Variable leader is: " + Arrays.toString(leader));
        System.out.println("Variable explored is: " + Arrays.toString(explored));

    }

    //helper method for de-bugging
    public void printFinishTime(){
        for (int i=0; i<vertices.size(); i++){
            System.out.println("Printing vertex ID for finish time " + (i + 1) + ": " + finishTime[i]);
        }
    }

    public static void main(String[] args) throws FileNotFoundException {

        GraphSCCFinder test = new GraphSCCFinder("data/SCC.txt");
        System.out.println("Top five SCC is: " + Arrays.toString(test.getSCC()));
        //correct answer is: 434821,968,459,313,211
    }
}


