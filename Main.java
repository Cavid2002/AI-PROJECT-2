
public class Main
{
    public static void main(String[] args)
    {
        GCBacktracking c = new GCWithAc3("./samples/sample6.txt");

        GCForwardCheck a = new GCForwardCheckWithOrdering("./samples/sample6.txt");
        a.printGraphContent();

        a.solve();
        c.solve();

        
        a.printColorContent();
        c.printColorContent();
    }
}