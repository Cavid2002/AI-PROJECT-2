
public class GCForwardCheck extends GCBacktracking
{
    GCForwardCheck(String filename)
    {
        super(filename);
    }
    
    @Override
    public boolean solve()
    {
        return backtracking(getNextVertex());
    }

    @Override
    public boolean backtracking(int vertex)
    { 
        if(vertex == -1)
        {
            return true;
        }


        for(int i : domain.get(vertex))
        {
            colorList.put(vertex, i);

            if(updateDomain(vertex, i) == false)
            {
                restoreDomain(vertex, i);
                colorList.put(vertex, -1);
                continue;
            }

            if(backtracking(getNextVertex()))
            {
                return true;
            }


            restoreDomain(vertex, i);
            colorList.put(vertex, -1);
        }

        return false;
    }

    /** constraint propogation: forward checking **/
    protected boolean updateDomain(int currentVertex, int color)
    {
        for(int neigbourVertex : graph.get(currentVertex))
        {
            if(colorList.get(neigbourVertex) == -1)
            {
                domain.get(neigbourVertex).remove(color);

                if(domain.get(neigbourVertex).isEmpty()) return false;
            }
        }

        return true;
    }


    protected void restoreDomain(int currentVertex, int color)
    {
        for(int neigbourVertex : graph.get(currentVertex))
        {
            if(colorList.get(neigbourVertex) == -1)
            {
                domain.get(neigbourVertex).add(color);
            }
        }
    }
      

}
