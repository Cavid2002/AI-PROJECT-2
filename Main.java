
public class Main
{
    public static void main(String[] args)
    {
        GraphColor gc = new GraphColor("./samples/sample2.txt");

        gc.printGraph();

        gc.backtracking(gc.getNextVertex());
    }
}