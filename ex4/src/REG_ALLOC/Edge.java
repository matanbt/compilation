package REG_ALLOC;

public class Edge
{
    /*
     * `u` and `v` are vertices connected by this (undirected) edge.
     */
    public Vertex u;
    public Vertex v;

    public Edge(Vertex u, Vertex v)
    {
        this.u = u;
        this.v = v;
    }

    /**
     * Return if the edge is turned on or not. An edge is on, if both of its vertices
     * are not removed from the graph.
     * @return If the edge is on or not.
     */
    public boolean isOn()
    {
        return u.is_on && v.is_on;
    }

    /**
     * Return the other side of the edge, according to a given vertex.
     * @param self A vertex in this edge.
     * @return The other vertex, or null if `self` isn't a part of this edge.
     */
    public Vertex getOtherVertex(Vertex self)
    {
        if (this.u == self)
        {
            return this.v;
        }
        else if (this.v == self)
        {
            return this.u;
        }
        return null;
    }
}
