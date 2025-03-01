
public class Main
{
    public static void main(String[] args)
    {
        GCForwardCheckWithOrdering a = new GCForwardCheckWithOrdering("./samples/sample2.txt");

        GCWithAc3 c = new GCWithAc3("./samples/sample2.txt");
        a.printGraphContent();

        boolean res = a.solve();
        c.solve();

        if(res == false)
        {
            System.out.println("Imposible");
            System.exit(0);
        }

        
        a.printColorContent();
        c.printColorContent();
    }
}