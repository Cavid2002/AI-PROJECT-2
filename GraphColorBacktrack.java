public class GraphColorBacktrack extends GraphColor
{
    GraphColorBacktrack(String filename)
    {
        super(filename);
    }
    
    private boolean canColor(int currentVertex, int color)
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
    

    public boolean backtracking(int vertex)
    {
        if(vertex == -1)
        {
            return true;
        }

        for(int i = 1; i <= colorCount; i++)
        {
            if(canColor(vertex, i))
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
