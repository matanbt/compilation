package LIVENESS;

import IR.IRcommand;
import IR.IRcommand_Jump_If_Eq_To_Zero;
import IR.IRcommand_Jump_Label;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CFGraph
{
    /*
     * The graph is just a list of the vertices
     */
    public List<Vertex> vertices;

    protected CFGraph()
    {
        this.vertices = new ArrayList<>();
    }

    /**
     * Add vertex v to the graph. Note that it is inserted in a LIFO manner, for
     * liveness analysis.
     * @param v Vertex to add.
     */
    protected void addVertex(Vertex v)
    {
        this.vertices.add(0, v);
    }

    /**
     * Given a list of IRcommands of a single function, create the control flow graph
     * of the function.
     * @param commands List of commands of a function.
     * @return the CFG of the function.
     */
    public static CFGraph createCFG(List<IRcommand> commands)
    {
        CFGraph g = new CFGraph();
        Map<IRcommand, Vertex> map = new HashMap<>();

        /* Add all of the IRcommands as vertices to the graph */
        for (IRcommand ir: commands)
        {
            Vertex v = new Vertex(ir);
            g.addVertex(v);
            map.put(ir, v);
        }

        /* Connect the different vertices in the graph */
        int size = commands.size();
        for (int i = 0; i < size; i++)
        {
            IRcommand ir = commands.get(i);

            Vertex v_in = map.get(ir);
            /* Handle jumps differently */
            if (ir instanceof IRcommand_Jump_Label)
            {
                Vertex v_out = map.get(((IRcommand_Jump_Label) ir).jump_dst);
                v_in.outgoing_edges.add(v_out);
            }
            else if (ir instanceof IRcommand_Jump_If_Eq_To_Zero)
            {
                Vertex v_out1 = map.get(((IRcommand_Jump_If_Eq_To_Zero) ir).jump_dst);
                v_in.outgoing_edges.add(v_out1);

                /* If there is a next command, add it */
                if (i < size - 1)
                {
                    IRcommand next_ir = commands.get(i + 1);
                    Vertex v_out2 = map.get(next_ir);
                    v_in.outgoing_edges.add(v_out2);
                }
            }
            else
            {
                /* If there is a next command, add it */
                if (i < size - 1)
                {
                    IRcommand next_ir = commands.get(i + 1);
                    Vertex v_out = map.get(next_ir);
                    v_in.outgoing_edges.add(v_out);
                }
            }
        }

        /* Return the newly constructed CFG */
        return g;
    }
}
