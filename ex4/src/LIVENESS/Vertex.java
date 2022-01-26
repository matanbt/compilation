package LIVENESS;

import IR.IRcommand;

import java.util.ArrayList;
import java.util.List;

public class Vertex
{
    /*
     * ir is the IR command associated with this vertex.
     * outgoing edges are the edges coming out of this vertex.
     */
    public IRcommand ir;
    public List<Vertex> outgoing_edges;

    public Vertex(IRcommand ir)
    {
        this.ir = ir;
        outgoing_edges = new ArrayList<>();
    }
}
