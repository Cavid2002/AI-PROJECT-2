public class GCBacktracking extends GC
{
    GCBacktracking(String filename)
    {
        super(filename);
    }

    public boolean solve()
    {
        return backtracking(getNextVertex(graph.firstKey()));
    }
    
    protected boolean isSafe(int currentVertex, int color)
    {
        for(int neigbourVertex : graph.get(currentVertex))
        {
            if(colorList.get(neigbourVertex) == color)
            {
                return false;
            }
        }
        return true;

    }

    
    public int getNextVertex(int vertex)
    {
        
        if(graph.higherKey(vertex) != null)
        {
            return -1;
        }
        return graph.higherKey(vertex);
    }


    public boolean backtracking(int vertex)
    {
        if(vertex == -1)
        {
            return true;
        }

        for(int i = 1; i <= colorCount; i++)
        {
            if(isSafe(vertex, i))
            {
                colorList.put(vertex, i);
                
                if(backtracking(getNextVertex(vertex)))
                {
                    return true;
                }

                colorList.put(vertex, -1);
            }

        }

        return false;
    }
}
