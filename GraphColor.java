import java.io.File;
import java.util.HashSet;
import java.util.Scanner;
import java.util.TreeMap;


public class GraphColor
{
    private int colorCount;
    private TreeMap<Integer, HashSet<Integer>> graph;
    private TreeMap<Integer, Integer> colorList;
    private TreeMap<Integer, HashSet<Integer>> domain;

    GraphColor(String filename)
    {
        graph = new TreeMap<>();
        colorList = new TreeMap<>();
        domain = new TreeMap<>();

        try
        {
            File file = new File(filename);
            Scanner scan = new Scanner(file);

            this.readColorCount(scan);
            this.readGraph(scan);


            scan.close();
        }
        catch(NullPointerException e)
        {
            System.err.println("NULL execption");
            System.exit(1);
        }
        catch(Exception e)
        {
            System.err.println("Graph Color Contructor Error");   
            System.exit(1);
        }


    }

    
    private void readGraph(Scanner scan)
    {
        String line;
        String[] splitLine;
        int a, b;
        while(scan.hasNextLine())
        {
            line = scan.nextLine();
            if(line.isEmpty() || line.charAt(0) == '#') continue;   
            
            splitLine = line.split(",");
            
            a = Integer.parseInt(splitLine[0]);
            b = Integer.parseInt(splitLine[1]);

            System.out.println(a + " " + b);

            insertToGraph(a, b);
            insertToGraph(b, a);

            initDomain(a);
            initDomain(b);

            colorList.put(a, -1);
            colorList.put(b, -1);

            
        }

    }


    private void readColorCount(Scanner scan)
    { 
        String line;
        String[] splitLine;
        while(scan.hasNextLine())
        {
            line = scan.nextLine();
            if(line.isEmpty() || line.charAt(0) == '#') continue;   
            
            splitLine = line.split(" ");
            
            if(splitLine[0].equals("colors"))
            {
                colorCount = Integer.parseInt(splitLine[2].trim());
                return;
            }

        }


    }

    private void insertToGraph(int a, int b)
    {
        if(graph.containsKey(a) == false)
        {
            graph.put(a, new HashSet<>());
        }
        graph.get(a).add(b);
    }


    private void initDomain(int a)
    {
        if(domain.containsKey(a)) return;

        domain.put(a, new HashSet<>());

        for(int i = 1; i <= colorCount; i++)
        {
            domain.get(a).add(i);
        }
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

    public boolean AC3(int vertex)
    {


        return false;
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

        int color = getLCV(vertex);

        colorList.put(vertex, color);
        
        if(updateDomain(vertex, color) == false)
        {
            restoreDomain(vertex, color);
            colorList.put(vertex, -1);
            return false;
        }
        
        if(forwardCheckingMRVandLCV(getMRVver()))
        {
            return true;
        }

        restoreDomain(vertex, color);
        colorList.put(vertex, -1);

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

    

    
    private int getLCV(int vertex)
    {
        TreeMap<Integer, Integer> lcv = new TreeMap<>();
        int count = 0;
        for(int color : domain.get(vertex))
        {
            count = 0;
            for(int neigbour : graph.get(vertex))
            {
                if(colorList.get(neigbour) != -1) continue;

                if(domain.get(neigbour).contains(color)) count++;
            }
            lcv.put(color, count);  

        }
        

        return lcv.firstEntry().getValue();
    }

    public void printGraphContent()
    {
        System.out.println("Color Count:" + colorCount);
        for(int i : graph.keySet())
        {
            System.out.print(i + " -> ");
            for(int j : graph.get(i))
            {
                System.out.print(j + " ");
            }
            System.out.println();
        }
    }

    public void printColorContent()
    {
        System.out.println("Coloring Result");
        for(int i : colorList.keySet())
        {
            System.out.println(i + " -> " + colorList.get(i));
        }
    }




}
