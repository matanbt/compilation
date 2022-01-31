package LIVENESS;

import TEMP.TEMP;

import java.util.HashSet;
import java.util.Set;

public class Liveness
{

    /**
     * Perform a single iteration of liveness analysis.
     * @param graph The CFG to run the analysis on.
     * @return True if the any IN/OUT set changed, false otherwise.
     */
    private static boolean livenessIteration(CFGraph graph)
    {
        boolean changed = false;

        for (Vertex v: graph.vertices)
        {
            /* Backup old sets, used to check if there was a change */
            Set<TEMP> old_in_set = v.ir.in_set;
            Set<TEMP> old_out_set = v.ir.out_set;

            /* Update OUT set */
            Set<TEMP> out_set = new HashSet<TEMP>();
            for (Vertex u: v.outgoing_edges)
            {
                out_set.addAll(u.ir.in_set);
            }

            v.ir.out_set = out_set;

            /* Update IN set */
            v.ir.updateInSet();
            Set<TEMP> in_set = v.ir.in_set;

            /* Check for a difference in the old and new sets */
            if (!old_in_set.equals(in_set) || !old_out_set.equals(out_set))
            {
                changed = true;
            }
        }

        return changed;
    }

    /**
     * Perform liveness analysis on the graph.
     * @param graph The CFG to analyze.
     */
    public static void analyze(CFGraph graph)
    {
        boolean changed = true;

        while (changed)
        {
            changed = livenessIteration(graph);
        }
    }
}
