public class GCBacktracking extends GC
{
    GCBacktracking(String filename)
    {
        super(filename);
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

    
    public int getNextVertex()
    {
        for(int i : graph.keySet())
        {
            if(colorList.get(i) == -1)
            {
                return i;
            }
        }
        return -1;
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
                    
                if(backtracking(getNextVertex()))
                {
                    return true;
                }

                colorList.put(vertex, -1);
            }

        }

        return false;
    }
}
