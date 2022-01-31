package REG_ALLOC;


import EXCEPTIONS.ColoringException;

import java.util.ArrayDeque;
import java.util.Deque;

public class KColor
{
    /*
     * `k` is the amount of colors to use to color the interference graph.
     */
    public int k;

    /*
     * Can color using $t0 - $t9, so in total 10 colors.
     */
    private static final int DEFAULT_K = 10;

    public KColor(int k)
    {
        this.k = k;
    }

    public KColor()
    {
        this(DEFAULT_K);
    }

    /**
     * Create stack of vertices to color, according to the slides in the recitation.
     * @param graph The graph to color.
     * @return A stack of all of the vertices, in order that will allow us to color them.
     */
    private Deque<Vertex> createVerticesStack(InterferenceGraph graph) throws ColoringException
    {
        Deque<Vertex> stack = new ArrayDeque<>();
        boolean stuck;

        while (true)
        {
            stuck = true;

            /*
             * Stopping condition: We have added all the vertices in the graph
             * to the stack
             */
            if (graph.verticesCount() == 0)
            {
                break;
            }

            /* Go over turned on vertices, and add them to the stack if possible */
            for (Vertex v: graph.vertices)
            {
                if (v.is_on && v.edgesCount() < this.k)
                {
                    stuck = false;
                    v.is_on = false;
                    stack.addFirst(v);
                }
            }

            /*
             * If we didn't add any vertex to the stack in this iteration,
             * that means we can't color the graph at all using K colors.
             */
            if (stuck)
            {
                throw new ColoringException("Couldn't color graph: Couldn't create vertex stack");
            }
        }

        return stack;
    }

    /**
     * Color a single vertex. The newly added color would be added to the Palette of colors.
     * @param v Vertex to color.
     * @return True if succeeded in coloring the vertex, false otherwise.
     */
    private boolean colorVertex(Vertex v)
    {
        boolean found_color;
        Integer u_color;

        /* Go over all of the colors */
        for (int i = 0; i < this.k; i++)
        {
            found_color = true;

            /* For each connected vertex of v, check their color */
            for (Vertex u: v.neighbors)
            {
                /* We ignore edges turned off */
                if (!u.is_on)
                {
                    continue;
                }

                /* Check if the other vertex is colored in the color of `i` */
                u_color = Palette.getInstance().getColor(u.t.getSerialNumber());
                if (i == u_color)
                {
                    /* If it is, we can't use this color */
                    found_color = false;
                    break;
                }
            }

            /* If no collision was found for `i`, we can use it to color the vertex */
            if (found_color)
            {
                Palette.getInstance().addColor(v.t.getSerialNumber(), i);
                return true;
            }
        }

        /* Couldn't find a color for this vertex */
        return false;
    }

    /**
     * Color the graph using K colors.
     * @param graph Graph to color.
     */
    public void colorGraph(InterferenceGraph graph) throws ColoringException
    {
        Deque<Vertex> stack = this.createVerticesStack(graph);

        while (!stack.isEmpty())
        {
            Vertex v = stack.removeFirst();

            /* Turn the vertex back on */
            v.is_on = true;

            /* Search for a color */
            boolean found_color = this.colorVertex(v);
            if (!found_color)
            {
                throw new ColoringException("Couldn't color graph");
            }
        }
    }
}
