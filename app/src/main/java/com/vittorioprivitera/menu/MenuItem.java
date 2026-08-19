package com.vittorioprivitera.menu;
import java.io.Serializable;
public class MenuItem implements Serializable {
    private String nome;
    private String desc;
    private float prezzo;
    private int img;

    public MenuItem(String nome, String desc, float prezzo, int img)
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

    public String completo()
    {
        StringBuilder sb=new StringBuilder("---PIATTO----"+"\n");
        sb.append(this.nome+"\n");
        sb.append(this.desc+"\n");
        sb.append(this.prezzo+"\n");
        return sb.toString();
    }
}
