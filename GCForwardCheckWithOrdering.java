import java.util.ArrayList;
import java.util.Comparator;
import java.util.TreeMap;

public class GCForwardCheckWithOrdering extends GCForwardCheck
{
    GCForwardCheckWithOrdering(String filename)
    {
        super(filename);
    }   


    @Override
    public boolean solve()
    {
        return backtracking(getMRVver());
    }

    
    @Override
    public boolean backtracking(int vertex)
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

            if(backtracking(getMRVver()))
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
        int count1 = 0;
        int count2 = 0;
        for(int i : graph.get(vertex1))
        {
            if(colorList.get(i) == -1) count1++;
        }

        for(int i : graph.get(vertex2))
        {
            if(colorList.get(i) == -1) count2++;
        }

        return count1 < count2;
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

    
    public ArrayList<Integer> getLCV(int vertex) {
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
