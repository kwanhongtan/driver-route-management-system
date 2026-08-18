/*
 * Author: Tan Kwan Hong
 * Date: 26 May 2026
 * Description: Represents one road in the graph adjacency list.
 */

public class GraphEdge
{
    private GraphNode source;
    private GraphNode destination;
    private int weight;

    public GraphEdge(GraphNode source, GraphNode destination, int weight)
    {
        // source is the node this road starts from, destination is the neighbour,
        // and weight is the driving time in minutes.
        this.source = source;
        this.destination = destination;
        this.weight = weight;
    }

    public GraphNode getSource()
    {
        return source;
    }

    public void setSource(GraphNode source)
    {
        this.source = source;
    }

    public GraphNode getDestination()
    {
        return destination;
    }

    public void setDestination(GraphNode destination)
    {
        this.destination = destination;
    }

    public int getWeight()
    {
        return weight;
    }

    public void setWeight(int weight)
    {
        this.weight = weight;
    }
}
