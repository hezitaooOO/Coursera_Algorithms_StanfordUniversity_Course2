import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

/**
 * Project: Coursera Algorithms by Standford University: Graph Search, Shortest Paths, and Data Structures
 * Description: Week 2 Programming Assignment.
 *              This class implements Dijkstra algorithm to find shortest path in the graph
 * @author : Zitao He
 * @date : 2021-01-24 12:05
 **/
public class DijkstraShortestPathFinder {
    private int[] shortestPath; //Integer array to store shortest path for each node. Array index is node ID - 1
    private HashMap<Integer, ArrayList<Integer>> vertices; //Graph representation. List is the adjacency list
    private ArrayList<GraphEdge> edges; //Edges in this graph
    private ArrayList<Integer> visited; //Track the visited elements in path search
    private ArrayList<Integer> unvisited; //Track the unvisited elements in path search
    private final int noPathLength = 1000000; //represents that there is no path between given vertex and source vertex

    /**
     * Construct the graph from .txt file.
     * The file contains an adjacency list representation of an undirected weighted graph with 200 vertices
     * labeled 1 to 200.
     * Each row consists of the node tuples that are adjacent to that particular vertex
     * along with the length of that edge.
     * For example, the 6th row has 6 as the first entry indicating that this row corresponds
     * to the vertex labeled 6. The next entry of this row "141,8200" indicates that
     * there is an edge between vertex 6 and vertex 141 that has length 8200.
     * The rest of the pairs of this row indicate the other vertices adjacent to vertex 6
     * and the lengths of the corresponding edges.
     * @param inputFileName the file name from which to construct graph
     * @throws FileNotFoundException
     */
    public DijkstraShortestPathFinder(String inputFileName) throws FileNotFoundException{
        vertices = new HashMap<Integer, ArrayList<Integer>>();
        edges = new ArrayList<GraphEdge>();
        visited = new ArrayList<Integer>();
        unvisited = new ArrayList<Integer>();
        try{
            Scanner fileScanner = new Scanner(new File(inputFileName));
            //add vertices
            while(fileScanner.hasNextLine()){
                Scanner lineScanner = new Scanner(fileScanner.nextLine());
                int startID = lineScanner.nextInt();
                vertices.put(startID, new ArrayList<Integer>());
            }
            shortestPath = new int[vertices.size()];//initialize array that stores shortest path
            Arrays.fill(shortestPath, 0);
            for (Integer id : vertices.keySet()){
                unvisited.add(id);
            }
            //add edges and distances to edges
            fileScanner = new Scanner(new File(inputFileName));
            while (fileScanner.hasNextLine()){
                Scanner lineScanner = new Scanner(fileScanner.nextLine());
                ArrayList<String> edgeDistList = new ArrayList<String>();
                int startID = lineScanner.nextInt(); //skip first integer that represents node ID
                while (lineScanner.hasNext()){
                    edgeDistList.add(lineScanner.next()); //Add each edge (with distance) to a list
                }
                for (int i=0; i<edgeDistList.size(); i++){ //Iterate over edge list and construct adjacency list
                    String[] tokens = edgeDistList.get(i).split(",");
                    int endID = Integer.parseInt(tokens[0]);
                    int length = Integer.parseInt(tokens[1]);
                    GraphEdge edge = new GraphEdge(startID, endID, length);
                    vertices.get(startID).add(endID);
                    edges.add(edge);
                }
            }
        }
        catch (IOException e) {
            throw new FileNotFoundException("Error: Input file is not found.");
        }
    }

    public void dijkstraSearch(int startID){
        int nextID = startID;
        while(visited.size() < vertices.size()){ //while there exists un-visited node
            visited.add(nextID);
            unvisited.remove(Integer.valueOf(nextID));
            if (visited.size() == vertices.size()){break;}
            int minDijkstraScore = noPathLength;
            for (GraphEdge edge : edges){
                if (visited.contains(edge.getStartID()) && unvisited.contains(edge.getEndID())){
                    if (shortestPath[edge.getStartID()-1] + edge.getLength() < minDijkstraScore){
                        minDijkstraScore = shortestPath[edge.getStartID()-1] + edge.getLength();
                        nextID = edge.getEndID();
                    }
                }
            }
            if (minDijkstraScore == noPathLength){
                break;
            }
            shortestPath[nextID - 1] = minDijkstraScore;
        }
        for (Integer unvisitedID : unvisited){
            shortestPath[unvisitedID - 1] = noPathLength;
        }
    }

    /**
     * Print the adjacency list of the graph
     */
    public void printGraph(){
        System.out.println("Printing graph...");
        for (Map.Entry<Integer, ArrayList<Integer>> entry : vertices.entrySet()) {
            System.out.println("Vertex " + entry.getKey() + " has outgoing vertices: " + entry.getValue().toString());
        }
        System.out.println("Total number of vertices in graph is: " + vertices.size());
    }

    /**
     * This method is to compute the assignment result only.
     * The assignment asks to compute the shortest path between vertex 1 and each of ten vertices
     * in array id.
     */
    public void printAssignmentAnswer(){
        int[] id = {7,37,59,82,99,115,133,165,188,197};
        int[] answer = new int[id.length];

        for (int i = 0; i < id.length; i++){
            answer[i] = shortestPath[id[i]-1];
        }
        System.out.println("Final answer for assignment is: " + Arrays.toString(answer));
    }

    public static void main(String[] args) throws FileNotFoundException {

        DijkstraShortestPathFinder test = new DijkstraShortestPathFinder("data/dijkstraData.txt");
        test.dijkstraSearch(1);
        test.printAssignmentAnswer();
    }

}


