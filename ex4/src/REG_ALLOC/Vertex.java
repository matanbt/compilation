package REG_ALLOC;

import TEMP.TEMP;

import java.util.HashSet;
import java.util.Set;

public class Vertex
{
    /*
     * `t` is the temporary associated with this vertex.
     * `is_on` is a flag, which is on if the vertex is in the graph, and off if we remove it from the graph.
     * `edges` is a list of edges connected to this vertex.
     */
    public TEMP t;
    public boolean is_on;
    public Set<Vertex> neighbors;

    public Vertex(TEMP t)
    {
        this.t = t;
        this.is_on = true;
        this.neighbors = new HashSet<>();
    }

    /**
     * Count how many connected edges are on for this vertex. See Edge.isOn for further explanation.
     * @return Count of edges turned on.
     */
    public int edgesCount()
    {
        int i = 0;
        for (Vertex neighbor: this.neighbors)
        {
            if (neighbor.is_on)
            {
                i += 1;
            }
        }

        return i;
    }

    /**
     * Add a new edge to this vertex. Doesn't allow self loops!
     * @param v The vertex to connect to this vertex.
     */
    public void addEdge(Vertex v)
    {
        if (this != v)
        {
            neighbors.add(v);
        }
    }
}
