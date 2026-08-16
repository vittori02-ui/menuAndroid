package com.vittorioprivitera.menu;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class menuLoader {
    public static List<menuItem> caricaMenu(Context context)
    {
        List<menuItem>lista=new ArrayList<>();
        try
        {
            InputStream is=context.getAssets().open("menu.json");
            int size=is.available();
            byte[] buffer=new byte[size];
            is.read(buffer);
            is.close();

            String json=new String(buffer, StandardCharsets.UTF_8);
            JSONArray array=new JSONArray(json);
            for(int i=0;i<array.length();i++)
            {
                JSONObject obj=array.getJSONObject(i);
                String nome=obj.getString("nome");
                String desc=obj.getString("desc");
                double prez=obj.getDouble("prezzo");
                String img=obj.getString("immagine");
                int id=context.getResources().getIdentifier(img,"drawable",context.getPackageName());
                lista.add(new menuItem(nome,desc,prez,id));
            }
        }catch (Exception e)
        {
            System.out.println("problema");
        }
        return lista;
    }
}
