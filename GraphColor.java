import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public class GraphColor
{
    public static int maxSize = 50;


    private int vertexCount;
    private int visitedCount;
    private int colorCount;
    private ArrayList<ArrayList<Integer>> graph;
    private ArrayList<Integer> colorList;

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

    private void readGraph(Scanner scan)
    {
        String line;
        String[] splitLine;
        int a, b, max = 0;
        while(scan.hasNextLine())
        {
            line = scan.nextLine();
            if(line.charAt(0) == '#') continue;   
            
            splitLine = line.split(",");
            
            a = Integer.parseInt(splitLine[0]);
            b = Integer.parseInt(splitLine[1]);
            
            max = Math.max(max, a);
            max = Math.max(max, b);

            this.graph.get(a).add(b);
            this.graph.get(b).add(a);
        }

        this.vertexCount = max;
    }
    

    boolean canColor(int currentVertex, int color)
    {
        int neigbourVertex;
        for(int i = 0; i < graph.get(currentVertex).size(); i++)
        {
            neigbourVertex = graph.get(currentVertex).get(i);

            if(colorList.get(neigbourVertex) == color)
            {
                return false;
            }

        }

        return true;
    }
    
    public boolean backtracking(int vertex)
    {
        for(int i = 0; i < colorCount; i++)
        {
            if(canColor(vertex, i) == true)
            {
                colorList.set(vertex, i);
            }
        }

        if(colorList.get(vertex) == -1) return false;

        visitedCount++;

        if(visitedCount == vertexCount) return true;

        int neigbourVertex;
        for(int i = 0; i < graph.get(vertex).size(); i++)
        {
            neigbourVertex = graph.get(vertex).get(i);
            
            if(colorList.get(neigbourVertex) != -1) continue;
            
            if(backtracking(neigbourVertex) == true)
            {
                return true;
            }
            
        }

        visitedCount--;
        colorList.set(vertex, -1);
        return false;
    }

}
