/*
 * Author: Tan Kwan Hong
 * Student ID: 23442423
 * Date: 26 May 2026
 * Description: Implements the city graph, BFS, DFS cycle detection, and Dijkstra shortest paths.
 * The graph stores a linked list of GraphNode objects, and each GraphNode
 * stores a linked-list adjacency list.
 */

public class Graph
{
    public static class PathResult
    {
        public final int time;
        public final String path;
        public final boolean reachable;

        PathResult(int time, String path, boolean reachable)
        {
            this.time = time;
            this.path = path;
            this.reachable = reachable;
        }

        public int getTime()
        {
            return time;
        }

        public String getPath()
        {
            return path;
        }

        public boolean getReachable()
        {
            return reachable;
        }
    }

    public static class CycleResult
    {
        public final boolean hasCycle;
        public final String cycle;

        CycleResult(boolean hasCycle, String cycle)
        {
            this.hasCycle = hasCycle;
            this.cycle = cycle;
        }

        public boolean getHasCycle()
        {
            return hasCycle;
        }

        public String getCycle()
        {
            return cycle;
        }
    }

    private LinkedList nodes;

    public Graph()
    {
        // The graph owns a linked list of nodes.
        nodes = new LinkedList();
    }

    public boolean addLocation(String name)
    {
        validateName(name);
        boolean added = false;

        // addRoad() also calls this method, so duplicate locations are ignored here.
        if (findNode(name) == null)
        {
            nodes.insertLast(new GraphNode(name));
            sortNodeList();
            added = true;
        }

        return added;
    }

    public void addRoad(String a, String b, int weight)
    {
        validateName(a);
        validateName(b);
        if (a.equals(b) || weight <= 0)
        {
            throw new IllegalArgumentException("Road endpoints must differ and weight must be positive.");
        }

        addLocation(a);
        addLocation(b);

        GraphNode fromNode = getNode(a);
        GraphNode toNode = getNode(b);

        // Because the graph is undirected, I store the road in both directions.
        fromNode.addOrUpdateEdge(toNode, weight);
        toNode.addOrUpdateEdge(fromNode, weight);
    }

    public String bfsLevels(String source)
    {
        GraphNode[] nodeArray = getSortedNodes();
        int sourceIndex = indexOf(source, nodeArray);

        if (sourceIndex == -1)
        {
            throw new IllegalArgumentException("BFS source was not found.");
        }

        int[] level = new int[nodeArray.length];
        Queue queue = new Queue();

        // I reset the visited flags before BFS. The level array is used only to
        // group the output into Level 0, Level 1, and so on.
        for (int i = 0; i < nodeArray.length; i++)
        {
            nodeArray[i].setVisited(false);
            level[i] = -1;
        }

        nodeArray[sourceIndex].setVisited(true);
        level[sourceIndex] = 0;
        // I put GraphNode objects into the queue directly.
        queue.enqueue(nodeArray[sourceIndex]);

        while (!queue.isEmpty())
        {
            GraphNode currentNode = (GraphNode)queue.dequeue();
            // The level array is index-based, so I find the current node's index
            // before updating its neighbours.
            int current = indexOf(currentNode.getLabel(), nodeArray);
            GraphEdge[] edges = nodeArray[current].getAdjacentEdges();

            for (int i = 0; i < edges.length; i++)
            {
                GraphNode nextNode = edges[i].getDestination();
                int next = indexOf(nextNode.getLabel(), nodeArray);

                if (next != -1 && !nextNode.getVisited())
                {
                    // The first BFS visit gives the shortest number of roads from
                    // the source to this location.
                    nextNode.setVisited(true);
                    level[next] = level[current] + 1;
                    queue.enqueue(nextNode);
                }
            }
        }

        int maxLevel = -1;
        // Isolated nodes stay at level -1, so they are not printed from this source.
        for (int i = 0; i < level.length; i++)
        {
            if (level[i] > maxLevel)
            {
                maxLevel = level[i];
            }
        }

        String out = "";
        // I print each level on its own line to match the assignment output.
        for (int currentLevel = 0; currentLevel <= maxLevel; currentLevel++)
        {
            out = out + "Level " + currentLevel + ": ";
            boolean first = true;

            for (int i = 0; i < nodeArray.length; i++)
            {
                if (level[i] == currentLevel)
                {
                    if (!first)
                    {
                        out = out + ", ";
                    }
                    out = out + nodeArray[i].getLabel();
                    first = false;
                }
            }

            out = out + System.lineSeparator();
        }

        return out.trim();
    }

    public CycleResult detectCycle(String source)
    {
        GraphNode[] nodeArray = getSortedNodes();
        int sourceIndex = indexOf(source, nodeArray);

        if (sourceIndex == -1)
        {
            throw new IllegalArgumentException("DFS source was not found.");
        }

        int[] parent = new int[nodeArray.length];

        // parent records how DFS entered each node, so I can rebuild a cycle path.
        for (int i = 0; i < nodeArray.length; i++)
        {
            parent[i] = -1;
            nodeArray[i].setVisited(false);
        }

        String cycle = dfsCycle(sourceIndex, parent, nodeArray);
        boolean hasCycle;
        String cycleText;
        if (cycle == null)
        {
            hasCycle = false;
            cycleText = "";
        }
        else
        {
            hasCycle = true;
            cycleText = cycle;
        }
        return new CycleResult(hasCycle, cycleText);
    }

    /*
     * Dijkstra's shortest-path algorithm follows the free OpenDSA
     * shortest-path explanation cited in the report:
     * https://opendsa.org/OpenDSA/Books/Catalog/html/GraphShortest.html
     * I implemented it from scratch using arrays and a linear scan instead of
     * Java's built-in priority queue.
     */
    public PathResult dijkstra(String source, String destination)
    {
        GraphNode[] nodeArray = getSortedNodes();
        int start = indexOf(source, nodeArray);
        int end = indexOf(destination, nodeArray);

        if (start == -1 || end == -1)
        {
            throw new IllegalArgumentException("Dijkstra location was not found.");
        }

        int infinity = 1_000_000_000;
        int[] distance = new int[nodeArray.length];
        int[] previous = new int[nodeArray.length];

        // distance stores the best known driving time. previous stores where that
        // best route came from, so I can rebuild the path at the end.
        for (int i = 0; i < nodeArray.length; i++)
        {
            distance[i] = infinity;
            previous[i] = -1;
            nodeArray[i].setVisited(false);
        }

        distance[start] = 0;

        for (int count = 0; count < nodeArray.length; count++)
        {
            // Instead of a priority queue, I scan all nodes and choose the unvisited
            // node with the smallest distance.
            int current = smallestUnvisited(distance, nodeArray);

            if (current == -1)
            {
                break;
            }

            nodeArray[current].setVisited(true);
            GraphEdge[] edges = nodeArray[current].getAdjacentEdges();

            for (int i = 0; i < edges.length; i++)
            {
                int next = indexOf(edges[i].getDestination().getLabel(), nodeArray);
                int newDistance = distance[current] + edges[i].getWeight();

                if (next != -1 && !nodeArray[next].getVisited() && newDistance < distance[next])
                {
                    // If this route is shorter, I update the best time and remember
                    // where the route came from.
                    distance[next] = newDistance;
                    previous[next] = current;
                }
            }
        }

        PathResult result;
        if (distance[end] == infinity)
        {
            result = new PathResult(0, "", false);
        }
        else
        {
            result = new PathResult(distance[end], rebuildPath(previous, end, nodeArray), true);
        }

        return result;
    }

    public String display()
    {
        String result = "";
        GraphNode[] nodeArray = getSortedNodes();

        // This prints the adjacency list in a simple readable form.
        for (int i = 0; i < nodeArray.length; i++)
        {
            result = result + nodeArray[i].getLabel() + " -> ";
            GraphEdge[] edges = nodeArray[i].getAdjacentEdges();

            for (int j = 0; j < edges.length; j++)
            {
                result = result + edges[j].getDestination().getLabel() + "(" + edges[j].getWeight() + ")";

                if (j < edges.length - 1)
                {
                    result = result + ", ";
                }
            }

            result = result + "\n";
        }

        return result.trim();
    }

    private GraphNode getNode(String label)
    {
        GraphNode node = findNode(label);

        if (node == null)
        {
            throw new IllegalArgumentException("Location was not found: " + label);
        }

        return node;
    }

    private GraphNode findNode(String label)
    {
        ListNode current = nodes.getHead();
        GraphNode found = null;

        // Manual linked-list search.
        while (current != null && found == null)
        {
            GraphNode node = (GraphNode)current.getValue();
            if (node.getLabel().equals(label))
            {
                found = node;
            }
            current = current.getNext();
        }

        return found;
    }

    private String dfsCycle(int sourceIndex, int[] parent, GraphNode[] nodeArray)
    {
        Stack stack = new Stack();
        String foundCycle = null;

        nodeArray[sourceIndex].setVisited(true);
        stack.push(nodeArray[sourceIndex]);

        // Iterative DFS pops a node, inspects neighbours, and pushes newly
        // discovered neighbours.
        while (!stack.isEmpty() && foundCycle == null)
        {
            GraphNode currentNode = (GraphNode)stack.pop();
            int current = indexOf(currentNode.getLabel(), nodeArray);
            GraphEdge[] edges = currentNode.getAdjacentEdges();

            for (int i = 0; i < edges.length && foundCycle == null; i++)
            {
                GraphNode nextNode = edges[i].getDestination();
                int next = indexOf(nextNode.getLabel(), nodeArray);

                if (next != -1 && !nextNode.getVisited())
                {
                    parent[next] = current;
                    nextNode.setVisited(true);
                    stack.push(nextNode);
                }
                else if (next != -1 && next != parent[current])
                {
                    // In an undirected graph, a visited neighbour is a cycle only
                    // when it is not the node we just came from.
                    foundCycle = buildCycle(current, next, parent, nodeArray);
                }
            }
        }

        return foundCycle;
    }

    private String buildCycle(int current, int neighbour, int[] parent, GraphNode[] nodeArray)
    {
        int[] temp = new int[nodeArray.length + 1];
        int count = 0;
        temp[count++] = neighbour;
        int walker = current;

        // I walk backwards through parent links until the repeated neighbour is reached.
        while (walker != -1 && walker != neighbour && count < temp.length)
        {
            temp[count++] = walker;
            walker = parent[walker];
        }

        temp[count++] = neighbour;
        String result = "";

        for (int i = count - 1; i >= 0; i--)
        {
            result = result + nodeArray[temp[i]].getLabel();

            if (i > 0)
            {
                result = result + " -> ";
            }
        }

        return result;
    }

    private int smallestUnvisited(int[] distance, GraphNode[] nodeArray)
    {
        int bestIndex = -1;
        int bestDistance = 1_000_000_000;

        for (int i = 0; i < distance.length; i++)
        {
            if (!nodeArray[i].getVisited() && distance[i] < bestDistance)
            {
                bestDistance = distance[i];
                bestIndex = i;
            }
        }

        return bestIndex;
    }

    private String rebuildPath(int[] previous, int end, GraphNode[] nodeArray)
    {
        int[] reverse = new int[nodeArray.length];
        int count = 0;
        int walker = end;

        // Dijkstra stores paths backwards through previous[], so I collect first
        // and then print from source to destination.
        while (walker != -1)
        {
            reverse[count++] = walker;
            walker = previous[walker];
        }

        String out = "";
        for (int i = count - 1; i >= 0; i--)
        {
            out = out + nodeArray[reverse[i]].getLabel();

            if (i > 0)
            {
                out = out + " -> ";
            }
        }

        return out;
    }

    private GraphNode[] getSortedNodes()
    {
        Object[] nodeObjects = nodes.toArray();
        GraphNode[] nodeArray = new GraphNode[nodeObjects.length];

        // I convert the linked list to an array only when indexed access is useful.
        for (int i = 0; i < nodeObjects.length; i++)
        {
            nodeArray[i] = (GraphNode)nodeObjects[i];
        }

        sortNodes(nodeArray);
        return nodeArray;
    }

    private void sortNodeList()
    {
        GraphNode[] nodeArray = getSortedNodes();
        nodes = new LinkedList();

        // I rebuild the linked list in alphabetical order for consistent output.
        for (int i = 0; i < nodeArray.length; i++)
        {
            nodes.insertLast(nodeArray[i]);
        }
    }

    private void sortNodes(GraphNode[] nodeArray)
    {
        // Insertion sort is enough here because the test graph is small.
        for (int i = 1; i < nodeArray.length; i++)
        {
            GraphNode temp = nodeArray[i];
            int j = i - 1;

            while (j >= 0 && nodeArray[j].getLabel().compareTo(temp.getLabel()) > 0)
            {
                nodeArray[j + 1] = nodeArray[j];
                j--;
            }

            nodeArray[j + 1] = temp;
        }
    }

    private int indexOf(String label, GraphNode[] nodeArray)
    {
        int index = -1;

        for (int i = 0; i < nodeArray.length; i++)
        {
            if (nodeArray[i].getLabel().equals(label))
            {
                index = i;
            }
        }

        return index;
    }

    private void validateName(String name)
    {
        if (name == null || name.trim().isEmpty())
        {
            throw new IllegalArgumentException("Location name must be non-empty.");
        }
    }
}
