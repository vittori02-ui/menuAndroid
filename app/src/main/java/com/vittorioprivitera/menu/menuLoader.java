package com.vittorioprivitera.menu;
import android.content.Context;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
public class menuLoader {
    public static List<Object> caricaMenu(Context context)
    {
        List<Object>lista=new ArrayList<>();
        try
        {
            InputStream is=context.getAssets().open("menu.json");
            int size=is.available();
            byte[] buffer=new byte[size];
            is.read(buffer);
            is.close();
            String json=new String(buffer, StandardCharsets.UTF_8);
            JSONObject root=new JSONObject(json);
            String categoriaAtt="";
            JSONArray array=root.getJSONArray("menu");
            for(int i=0;i<array.length();i++)
            {
                JSONObject obj=array.getJSONObject(i);
                String categoria=obj.getString("categoria");
                String nome=obj.getString("nome");
                String desc=obj.getString("descrizione");
                float prez=(float)obj.getDouble("prezzo");
                String img=obj.getString("immagine");
                if(!categoria.equals(categoriaAtt))
                {
                    lista.add(new titolo(categoria));
                    categoriaAtt=categoria;
                }
                int id=context.getResources().getIdentifier(img,"drawable",context.getPackageName());
                lista.add(new MenuItem(nome,desc,prez,id));
                System.out.println("carico lista");
            }
        }catch (Exception e)
        {
            System.out.println("problema");
            System.out.println(e.fillInStackTrace());
        }
        System.out.println("lista caricata");
        return lista;
    }
}
