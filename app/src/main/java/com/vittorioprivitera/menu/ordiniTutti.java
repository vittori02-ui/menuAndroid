package com.vittorioprivitera.menu;
import java.util.ArrayList;
import java.util.List;
public class ordiniTutti {
    private static List<MenuItem> ordini=new ArrayList<>();
    private static List<MenuItem> ordiniInviati=new ArrayList<>();
    public static List<MenuItem> getOrdini()
    {
        return ordini;
    }
    public static List<MenuItem> getInviati(){
        return ordiniInviati;
    }
    public static void addElem(MenuItem item)
    {
        ordini.add(item);
    }
    public static void addElemInviato(MenuItem item){
        ordiniInviati.add(item);
    }
    public static void svuota()
    {
        ordini.clear();
    }
    public static void svuota2(){ordiniInviati.clear();}
}
