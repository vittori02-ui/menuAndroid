package com.vittorioprivitera.menu;
import java.io.Serializable;
public class MenuItem implements Serializable {
    private String nome;
    private String desc;
    private float prezzo;
    private int img;
    private int id;
    private boolean pronto=false;

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

    public int getId(){return id;}
    public boolean getPronto(){return pronto;}
    public void setId(int id)
    {
        this.id=id;
    }

    public String completo()
    {
        StringBuilder sb=new StringBuilder("---PIATTO----"+"\n");
        sb.append(this.nome+"\n");
        sb.append(this.desc+"\n");
        sb.append(this.prezzo+"\n");
        sb.append(id+"\n");
        sb.append(pronto+"\n");
        return sb.toString();
    }
}
