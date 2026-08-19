package com.vittorioprivitera.menu;
import java.util.ArrayList;
import java.util.List;
public class ordiniTutti {
    private static List<MenuItem> ordini=new ArrayList<>();
    public static List<MenuItem> getOrdini()
    {
        return ordini;
    }
    public static void addElem(MenuItem item)
    {
        ordini.add(item);
    }
    public static void svuota()
    {
        ordini.clear();
    }
}
