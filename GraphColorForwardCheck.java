import java.util.ArrayList;
import java.util.Comparator;
import java.util.TreeMap;

public class GraphColorForwardCheck extends GraphColor
{
    GraphColorForwardCheck(String filename)
    {
        super(filename);
    }

    private boolean updateDomain(int currentVertex, int color)
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


    private void restoreDomain(int currentVertex, int color)
    {
        for(int neigbourVertex : graph.get(currentVertex))
        {
            if(colorList.get(neigbourVertex) == -1)
            {
                domain.get(neigbourVertex).add(color);
            }
        }
    }
    

    
    public boolean forwardChecking(int vertex)
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

            if(forwardChecking(getNextVertex()))
            {
                return true;
            }


            restoreDomain(vertex, i);
            colorList.put(vertex, -1);
        }

        return false;
    }
    
    public boolean forwardCheckingMRVandLCV(int vertex)
    {
        if(vertex == -1)
        {
            return true;
        }

        ArrayList<Integer> colors = getLCV(vertex);

        for(int color : colors)
        {
            colorList.put(vertex, color);
        
            if(updateDomain(vertex, color) == false)
            {
                restoreDomain(vertex, color);
                colorList.put(vertex, -1);
                continue;
            }

            if(forwardCheckingMRVandLCV(getMRVver()))
            {
                return true;
            }

            restoreDomain(vertex, color);
            colorList.put(vertex, -1);

        }

        
        return false;
    }

    private boolean tiebreaker(int vertex1, int vertex2)
    {
        return graph.get(vertex1).size() < graph.get(vertex2).size();
    }

    public int getMRVver()
    {
        int minDomain = Integer.MAX_VALUE;
        int mrvVer = -1;
        int remainDomain;
        for(int i : graph.keySet())
        {
            if(colorList.get(i) != -1) continue;

            remainDomain = domain.get(i).size();
            
            if(remainDomain < minDomain)
            {
                mrvVer = i;
                minDomain = remainDomain;
            }
            else if(remainDomain == minDomain && tiebreaker(mrvVer, i) && mrvVer != -1)
            {
                mrvVer = i;
            }
            
        }

        return mrvVer;
    }

    
    private ArrayList<Integer> getLCV(int vertex) {
        TreeMap<Integer, Integer> lcv = new TreeMap<>();
        int count;

        for (int color : domain.get(vertex)) {
            count = 0;
            for (int neighbor : graph.get(vertex)) {
                if (colorList.get(neighbor) != -1) continue;
                if (domain.get(neighbor).contains(color)) count++;
            }
            lcv.put(color, count);
        }

        ArrayList<Integer> sortedColors = new ArrayList<>(lcv.keySet());
        sortedColors.sort(Comparator.comparingInt(lcv::get));
        return sortedColors;
    }    

}
