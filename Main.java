
public class Main
{
    public static void main(String[] args)
    {
        GraphColor gc1 = new GraphColor("./samples/sample2.txt");
        GraphColor gc2 = new GraphColor("./samples/sample2.txt");

        gc1.printGraphContent();
        gc2.printGraphContent();

        boolean res = gc1.backtracking(gc1.getNextVertex());
        gc2.forwardChecking(gc2.getNextVertex());
        
        if(res == false)
        {
            System.out.println("Impossible");
            return;
        }

        gc1.printColorContent();
        gc2.printColorContent();

    }
}