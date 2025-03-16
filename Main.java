
public class Main
{
    public static void main(String[] args)
    {
        GCWithAc3 d = new GCWithAc3("./samples/sample4.txt");
        boolean res = d.solve();

        if(res == false)
        {
            System.out.println("Impossible");
            System.exit(1);
        }
        
        d.printColorContent();
    }
}