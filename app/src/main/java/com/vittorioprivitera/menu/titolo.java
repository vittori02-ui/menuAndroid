package com.vittorioprivitera.menu;

public class titolo {
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
