/**
 * Project: Coursera Algorithms by Standford University
 * Description: Helper class for class DijkstraShortestPathFinder.
 *              This class represents an edge in graph. It has start and end node, along with the length.
 *
 * @author : Zitao He
 * @date : 2021-01-24 12:36
 **/
public class GraphEdge {
    private int startID;
    private int endID;
    private int length;

    public GraphEdge(int startID, int endID, int length){
        this.startID = startID;
        this.endID = endID;
        this.length = length;
    }
    public int getStartID() {
        return startID;
    }

    public int getEndID() {
        return endID;
    }

    public int getLength() {
        return length;
    }
}
