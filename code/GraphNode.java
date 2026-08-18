/*
 * Author: Tan Kwan Hong
 * Date: 26 May 2026
 * Description: Represents one location in the ZipRide city graph.
 */

public class GraphNode
{
    private String label;
    private LinkedList adjacencyList;
    private boolean visited;

    public GraphNode(String label)
    {
        // Each graph node owns a linked-list adjacency list. For ZipRide, each
        // item in that list is a weighted edge.
        this.label = label;
        this.adjacencyList = new LinkedList();
        this.visited = false;
    }

    public String getLabel()
    {
        return label;
    }

    public void setLabel(String label)
    {
        this.label = label;
    }

    public boolean getVisited()
    {
        return visited;
    }

    public void setVisited(boolean visited)
    {
        this.visited = visited;
    }

    public void addOrUpdateEdge(GraphNode destination, int weight)
    {
        GraphEdge edge = findEdge(destination);

        if (edge == null)
        {
            // New neighbour: add a weighted edge to this node's adjacency list.
            adjacencyList.insertLast(new GraphEdge(this, destination, weight));
            sortAdjacencyList();
        }
        else
        {
            // Existing neighbour: update the road time instead of storing a duplicate.
            edge.setWeight(weight);
        }
    }

    public GraphEdge findEdge(GraphNode destination)
    {
        ListNode current = adjacencyList.getHead();
        GraphEdge found = null;

        while (current != null && found == null)
        {
            GraphEdge edge = (GraphEdge)current.getValue();
            if (edge.getDestination().equals(destination))
            {
                found = edge;
            }
            current = current.getNext();
        }

        return found;
    }

    public GraphEdge[] getAdjacentEdges()
    {
        Object[] edgeObjects = adjacencyList.toArray();
        GraphEdge[] edges = new GraphEdge[edgeObjects.length];

        // Convert from Object[] because LinkedList stores general Object values.
        for (int i = 0; i < edgeObjects.length; i++)
        {
            edges[i] = (GraphEdge)edgeObjects[i];
        }

        return edges;
    }

    public boolean equals(Object obj)
    {
        boolean equal = false;

        if (obj instanceof GraphNode)
        {
            GraphNode other = (GraphNode)obj;
            equal = label.equals(other.label);
        }

        return equal;
    }

    public String toString()
    {
        return label;
    }

    private void sortAdjacencyList()
    {
        GraphEdge[] edges = getAdjacentEdges();

        // Keep neighbours alphabetical so BFS/DFS output stays predictable.
        for (int i = 1; i < edges.length; i++)
        {
            GraphEdge temp = edges[i];
            int j = i - 1;

            while (j >= 0 && edges[j].getDestination().getLabel().compareTo(temp.getDestination().getLabel()) > 0)
            {
                edges[j + 1] = edges[j];
                j--;
            }

            edges[j + 1] = temp;
        }

        adjacencyList = new LinkedList();
        for (int i = 0; i < edges.length; i++)
        {
            adjacencyList.insertLast(edges[i]);
        }
    }
}
