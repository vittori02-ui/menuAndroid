package com.vittorioprivitera.menu;
public class menuItem {
    private String nome;
    private String desc;
    private float prezzo;
    private int img;

    public menuItem(String nome,String desc,double prezzo,int img)
    {
        this.nome=nome;
        this.desc=desc;
        this.prezzo=prezzo;
        this.img=img;
    }
    public String getNome()
    {
        return this.nome;
    }
    public String getDesc()
    {
        return this.desc;
    }

    public float getPrezzo() {
        return prezzo;
    }

    public int getImg() {
        return img;
    }
}
