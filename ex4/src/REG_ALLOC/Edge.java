package REG_ALLOC;

public class Edge
{
    /*
     * `u` and `v` are vertices connected by this edge.
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
}
