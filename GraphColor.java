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
        try
        {
            File file = new File(filename);
            Scanner scan = new Scanner(file);

            this.readColorCount(scan);
            this.readGraph(scan);


            scan.close();
        }
        catch(Exception e)
        {
            System.err.println("Graph Color Contructor");   
            System.err.println(e.getStackTrace());
            System.exit(1);
        }

    }



    private void readColorCount(Scanner scan)
    { 
        String line;
        String[] splitLine;
        while(scan.hasNextLine())
        {
            line = scan.nextLine();
            if(line.charAt(0) == '#') continue;   
            
            splitLine = line.split(" ");
            
            if(splitLine[0].equals("colors"))
            {
                colorCount = Integer.parseInt(splitLine[2]);
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

    private void readGraph(Scanner scan)
    {
        String line;
        String[] splitLine;
        int a, b;
        while(scan.hasNextLine())
        {
            line = scan.nextLine();
            if(line.charAt(0) == '#') continue;   
            
            splitLine = line.split(",");
            
            a = Integer.parseInt(splitLine[0]);
            b = Integer.parseInt(splitLine[1]);

            insertToGraph(a, b);
            insertToGraph(b, a);

            initDomain(a);
            initDomain(b);

            colorList.put(a, -1);
            colorList.put(b, -1);

            
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
    
    
    public boolean backtracking(Integer vertex)
    {
        if(vertex == null)
        {
            return true;
        }

        for(int i = 0; i < colorCount; i++)
        {
            if(canColor(vertex, i))
            {
                colorList.put(vertex, i);

                if(backtracking(graph.higherKey(vertex)))
                {
                    return true;
                }

                colorList.put(vertex, -1);
            }

        }

        return false;
    }

    
    public boolean forwardChecking(Integer vertex)
    { 
        if(vertex == -1)
        {
            return true;
        }


        for(int i = 1; i <= colorCount; i++)
        {
            
        }

        return false;
    }


}
