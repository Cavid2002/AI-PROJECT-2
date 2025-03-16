import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class GCWithAc3 extends GCBacktracking 
{
    GCWithAc3(String filename)
    {
        super(filename);
    }    



    /**
     * Solves the graph coloring problem using AC-3 for constraint propagation and backtracking.
     * 
     * @return true if a valid coloring is found, false otherwise.
     */
    @Override
    public boolean solve()
    {
        return backtracking(getMRVver());
    }


    /**
     * Recursive backtracking method to assign colors to vertices using MRV and LCV heuristics, propagating constraints using AC-3.
     * 
     * @param vertex The current vertex to be colored.
     * @return true if a valid coloring is found for the remaining vertices, false otherwise.
     */
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


    /**
     * Checks if a color assignment for a vertex is consistent with its adjacent vertices.
     * 
     * @param colorx The color to be checked.
     * @param ydom The domain of colors for the adjacent vertex.
     * @return true if the color assignment is consistent, false otherwise.
     */
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

    /**
     * Implements the AC-3 algorithm for constraint propagation.
     * 
     * @return true if the constraints are consistent, false otherwise.
     */
    private boolean ac3()
    {
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

    /**
     * Revises the domain of a vertex based on the constraints of its adjacent vertex.
     * 
     * @param x The vertex whose domain is being revised.
     * @param y The adjacent vertex.
     * @return true if the domain of x is revised, false otherwise.
     */
    private boolean revise(int x, int y) 
    {
        boolean rev = false;
        Set<Integer> xdom = new HashSet<>(domain.get(x));
    
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

    /**
     * Breaks ties between vertices with the same domain size by choosing the one with fewer uncolored neighbors.
     * 
     * @param vertex1 The first vertex to compare.
     * @param vertex2 The second vertex to compare.
     * @return true if vertex1 has fewer uncolored neighbors than vertex2, false otherwise.
     */
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


    /**
     * Selects the vertex with the Minimum Remaining Values (MRV) heuristic.
     * 
     * @return The vertex with the smallest domain size, or -1 if all vertices are colored.
     */
    public int getMRVver()
    {
        int minDomain = Integer.MAX_VALUE;
        int mrvVer = -1;
        for(int i : graph.keySet())
        {
            if(colorList.get(i) != -1) continue;

            int remainDomain = domain.get(i).size();
            
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


     /**
     * Orders the colors for a vertex using the Least Constraining Value (LCV) heuristic.
     * 
     * @param vertex The vertex for which to order the colors.
     * @return A list of colors ordered by how few constraints they impose on neighboring vertices.
     */
    public List<Integer> getLCV(int vertex) 
    {
        Map<Integer, Integer> lcv = new TreeMap<>();
        int count;

        for (int color : domain.get(vertex)) 
        {
            count = 0;
            for (int neighbor : graph.get(vertex)) 
            {
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
