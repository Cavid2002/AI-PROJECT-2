import java.util.Map.Entry;
import java.util.TreeMap;

public class Main
{
    public static void main(String[] args)
    {
        TreeMap<Integer, String> map = new TreeMap<>();

        map.put(10, "RED");
        map.put(5, "GREEN");
        map.put(1, "BLUE");

        System.out.println(map.size());

        for(Entry<Integer, String> entry : map.entrySet())
        {
            System.err.println(entry.getKey() + " " + entry.getValue());
        }
    }
}