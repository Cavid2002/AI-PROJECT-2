import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.TreeMap;

public class GCWithAc3 extends GCForwardCheck 
{
    GCWithAc3(String filename)
    {
        super(filename);
    }    


    @Override
    public boolean backtracking(int vertex)
    {
        if(vertex == -1)
        {
            return true;
        }

        for(int color : getLCV(vertex))
        {
            colorList.put(vertex, color);
            
            if(updateDomain(vertex, color) == false)
            {
                restoreDomain(vertex, color);
                colorList.put(vertex, -1);
                continue;
            }

            if(ac3() && backtracking(getMRVver()))
            {
                return true;
            }

            restoreDomain(vertex, color);
            colorList.put(vertex, -1);
        }
        

        return false;
    }


    private boolean ac3()
    {
        ArrayDeque<int[]> q = new ArrayDeque<>();

        for(int ver : graph.keySet())
        {
            for(int adj : graph.get(ver))
            {
                q.add(new int[]{ver, adj});
            }
        }


        int x, y;
        while(!q.isEmpty())
        {
            int[] arc = q.poll();
            x = arc[0];
            y = arc[1];

            if(revise(x, y))
            {
                if(domain.get(x).isEmpty()) return false;

                for(int z : graph.get(x))
                {
                    if(z != y)
                    {
                        q.add(new int[]{z, x});
                    }
                }
            }
        }

        return true;
    }



    private boolean revise(int x, int y)
    {
        boolean rev = false;
        boolean hasValid;
        for(int colorx : domain.get(x))
        {   
            hasValid = false;
            for(int colory : domain.get(y))
            {
                if(colorx != colory)
                {
                    hasValid = true;
                    break;
                }
            }

            if(hasValid == false)
            {
                domain.get(x).remove(colorx);
                rev = true;
            }
        }

        return rev;
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
