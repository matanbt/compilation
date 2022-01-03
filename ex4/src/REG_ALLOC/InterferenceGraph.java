package REG_ALLOC;

import IR.IRcommand;
import TEMP.TEMP;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InterferenceGraph
{
    /*
     * The graph is just a list of the vertices.
     */
    public List<Vertex> vertices;

    protected InterferenceGraph()
    {
        this.vertices = new ArrayList<>();
    }

    /**
     * Add vertex to the graph.
     * @param v Vertex to add.
     */
    protected void addVertex(Vertex v)
    {
        this.vertices.add(v);
    }


    /**
     * Count how many turned on vertices are in this graph.
     * @return Number of turned on vertices.
     */
    public int verticesCount()
    {
        int i = 0;
        for (Vertex v: this.vertices)
        {
            if (v.is_on)
            {
                i += 1;
            }
        }

        return i;
    }

    public static InterferenceGraph CreateInterferenceGraph(List<IRcommand> commands)
    {
        InterferenceGraph g = new InterferenceGraph();
        Map<TEMP, Vertex> map = new HashMap<TEMP, Vertex>();

        /* Add vertices to the graph */
        for (IRcommand ir : commands)
        {
            for (TEMP t : ir.out_set)
            {
                /* Add only temporaries we haven't already added to the graph */
                if (!map.containsKey(t))
                {
                    Vertex v = new Vertex(t);
                    g.addVertex(v);
                    map.put(t, v);
                }
            }
        }

        /* Add edges for all the vertices */
        for (IRcommand ir : commands)
        {
            for (TEMP t1 : ir.out_set)
            {
                Vertex v_t1 = map.get(t1);
                /*
                 * For each temporary, connect it with all the other temporaries
                 * in the OUT set. Notice `addEdge` doesn't connect a vertex to itself,
                 * so we won't have self loops.
                 */
                for (TEMP t2: ir.out_set)
                {
                    Vertex v_t2 = map.get(t2);
                    v_t1.addEdge(v_t2);
                }
            }
        }

        /* Return the newly constructed interference graph */
        return g;
    }
}
