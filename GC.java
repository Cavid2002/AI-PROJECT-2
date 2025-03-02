import java.io.File;
import java.util.HashSet;
import java.util.Scanner;
import java.util.TreeMap;


public class GC
{
    protected int colorCount;
    protected TreeMap<Integer, HashSet<Integer>> graph;
    protected TreeMap<Integer, Integer> colorList;
    protected TreeMap<Integer, HashSet<Integer>> domain;

    GC(String filename)
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

            insertToGraph(a, b);
            insertToGraph(b, a);

            initDomain(a);
            initDomain(b);

            colorList.put(a, -1);
            colorList.put(b, -1);

            
        }

    }


    protected void readColorCount(Scanner scan)
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

    protected void insertToGraph(int a, int b)
    {
        if(graph.containsKey(a) == false)
        {
            graph.put(a, new HashSet<>());
        }
        graph.get(a).add(b);
    }


    protected void initDomain(int a)
    {
        if(domain.containsKey(a)) return;

        domain.put(a, new HashSet<>());

        for(int i = 1; i <= colorCount; i++)
        {
            domain.get(a).add(i);
        }
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
