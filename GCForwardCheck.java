
public class GCForwardCheck extends GCBacktracking
{
    GCForwardCheck(String filename)
    {
        super(filename);
    }
    
    @Override
    public boolean solve()
    {
        return backtracking(graph.firstKey());
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

    /**
    * Updates the domain of neighboring vertices by removing a specific color after assigning it to the current vertex.
    * This ensures that the neighboring vertices cannot use the same color, maintaining the graph coloring constraints.
    *
    * @param currentVertex The vertex that has been assigned a color.
    * @param color The color assigned to the current vertex.
    * @return true if the domain update is successful for all neighbors, false if any neighbor's domain becomes empty.
    */
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



    /**
    * Restores the domain of neighboring vertices by re-adding a specific color when backtracking.
    * This is used to undo the domain changes made during the `updateDomain` method when a color assignment
    * leads to a dead end in the backtracking process.
    *
    * @param currentVertex The vertex whose color assignment is being backtracked.
    * @param color The color that was previously assigned to the current vertex.
    */
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
