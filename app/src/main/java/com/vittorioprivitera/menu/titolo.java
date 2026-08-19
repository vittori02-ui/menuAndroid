package com.vittorioprivitera.menu;
import java.io.Serializable;
public class titolo implements Serializable {
    private String titolo;
    public titolo(String titolo)
    {
        if(titolo.isEmpty()||titolo==null)return;
        else this.titolo=titolo;
    }
    public String getTitolo()
    {
        return this.titolo;
    }
}
