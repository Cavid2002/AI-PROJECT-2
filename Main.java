
public class Main
{
    public static void main(String[] args)
    {
        GCForwardCheckWithOrdering a = new GCForwardCheckWithOrdering("./samples/sample2.txt");
        GCBacktracking b = new GCBacktracking("./samples/sample2.txt");
        GCWithAc3 c = new GCWithAc3("./samples/sample2.txt");
        a.printGraphContent();

        boolean res = a.backtracking(a.getMRVver());
        b.backtracking(b.getNextVertex());
        c.backtracking(c.getMRVver());

        if(res == false)
        {
            System.out.println("Imposible");
            System.exit(0);
        }

        
        a.printColorContent();
        b.printColorContent();
        c.printColorContent();
    }
}