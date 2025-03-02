import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.TreeMap;

public class GCWithAc3 extends GCBacktracking 
{
    GCWithAc3(String filename)
    {
        super(filename);
    }    


    public boolean solve()
    {
        return backtracking(getMRVver());
    }


    @Override
    public boolean backtracking(int vertex) 
    {
        if (vertex == -1) 
        {
            return true;
        }

        
        for (int color : getLCV(vertex))
        {
            colorList.put(vertex, color);

            TreeMap<Integer, HashSet<Integer>> domainCopy = new TreeMap<>();
            for (int i : domain.keySet()) 
            {
                domainCopy.put(i, new HashSet<>(domain.get(i)));
            }


            domain.get(vertex).clear();
            domain.get(vertex).add(color);

            if (ac3()) 
            {
                int nextVertex = getMRVver();
                if (backtracking(nextVertex))
                {
                    return true;
                }
            }

            domain = domainCopy;
            colorList.put(vertex, -1); 
        }

        return false;
    }
    

    private boolean isConsistent(int colorx, HashSet<Integer> ydom)
    {
        for(int colory : ydom)
        {
            if(colorx != colory)
            {
                return true;
            }
        }
        return false;
    }

    
    private boolean ac3() {
        ArrayDeque<int[]> q = new ArrayDeque<>();
    
        for (int ver : graph.keySet()) 
        {
            for (int adj : graph.get(ver))
            {
                q.add(new int[]{ver, adj});
            }
        }
    
        while (!q.isEmpty()) 
        {
            int[] arc = q.poll();
            int x = arc[0];
            int y = arc[1];
    
            if (revise(x, y)) {
                if (domain.get(x).isEmpty()) {
                    return false; 
                }
    
                for (int z : graph.get(x)) {
                    if (z != y) {
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
        HashSet<Integer> xdom = new HashSet<>(domain.get(x));
    
        for (int colorx : xdom)
        {
            if (!isConsistent(colorx, domain.get(y))) 
            {
                domain.get(x).remove(colorx);
                rev = true;
                if(domain.get(x).isEmpty())
                {
                    return false;
                }
            }
        }
        return rev;
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
