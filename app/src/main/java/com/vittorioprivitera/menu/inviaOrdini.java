package com.vittorioprivitera.menu;
import android.os.Looper;
import android.os.Handler;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class inviaOrdini {
    private static final String urlScript="https://script.google.com/macros/s/AKfycbyLqLekN8fpcLZcW2MbgbB-nPxBvSNCNhHiuehHduOmX6KvlBfaAsrX9A86snxm1hP6/exec";
    public interface OnVersioneListener
    {
        void onVersione(int versione);
        void onErrore(String mees);
    }
    public interface OnIdRicevutoListener
    {
        void onId(int id);
        void onErrore(String mess);
    }
    public interface OnRispostaListener
    {
        void onRisposta(String risp);
        void onErrore(String mess);
    }
    public interface OnStatoMultiploListener
    {
        void onRis(Map<String,Boolean>ris);
        void onErrore(String mess);
    }
    public static void inviaPag(String sala,String tav,List<MenuItem> lista,float tot,OnInviatoListener listener)
    {
        new Thread(()->
        {
            try
            {
                StringBuilder piatti=new StringBuilder("[");
                for(int i=0;i<lista.size();i++)
                {
                    MenuItem item=lista.get(i);
                    piatti.append("{\"nome\":\"").append(item.getNome()).append("\",\"prezzo\":").append(item.getPrezzo()).append("}");
                    if(i<lista.size()-1)piatti.append(",");
                }
                piatti.append("]");
                String json = "{"
                        + "\"azione\":\"pagamento\","
                        + "\"sala\":\"" + sala + "\","
                        + "\"tavolo\":\"" + tav + "\","
                        + "\"totale\":" + tot + ","
                        + "\"piatti\":" + piatti
                        + "}";
                mandaRichiesta(json, new OnRispostaListener() {
                    @Override
                    public void onRisposta(String risp) {
                        if(risp.trim().equalsIgnoreCase("ok"))listener.onSuccesso();
                    }
                    @Override
                    public void onErrore(String mess) {
                        listener.onErrore("risposta no "+mess);
                    }
                });
            }
            catch(Exception e)
            {
                new Handler(Looper.getMainLooper()).post(()->listener.onErrore(e.getMessage()));
            }
        }).start();
    }
    public static void richiediVersione(OnVersioneListener listener)
    {
        mandaRichiesta("{\"azione\":\"getVersione\"}", new OnRispostaListener() {
            @Override
            public void onRisposta(String risp) {
                String pulito=risp.trim();
                if(pulito.startsWith("<"))
                {
                    listener.onErrore("ha risposto con html");
                    return;
                }
                try {
                    listener.onVersione(Integer.parseInt(risp.trim()));
                }
                catch (Exception e)
                {
                    listener.onErrore("è nel catch");
                }
            }
            @Override
            public void onErrore(String mess) {
                listener.onErrore(mess);
            }
        });
    }
    public static void richiediStatiMult(List<MenuItem> items,OnStatoMultiploListener listener)
    {
        StringBuilder json=new StringBuilder("[");
        for(int i=0;i<items.size();i++)
        {
            MenuItem it=items.get(i);
            json.append("{\"id\":").append(it.getId()).append(",\"piatto\":\"").append(it.getNome()).append("\"}");
            if(i<items.size()-1)json.append(",");
        }
        json.append("]");
        String manda="{\"azione\":\"statoMultiplo\",\"items\":"+json+"}";
        mandaRichiesta(manda, new OnRispostaListener() {
            @Override
            public void onRisposta(String risp) {
                try
                {
                    JSONObject obj=new JSONObject(risp);
                    Map<String,Boolean> ris=new HashMap<>();
                    Iterator<String> chiavi=obj.keys();
                    while(chiavi.hasNext())
                    {
                        String chiave=chiavi.next();
                        JSONObject sing=obj.getJSONObject(chiave);
                        boolean pronto=sing.getBoolean("pronto");
                        ris.put(chiave,pronto);
                    }
                    listener.onRis(ris);
                }
                catch (Exception e)
                {
                    listener.onErrore("errore nel parsing");
                }
            }

            @Override
            public void onErrore(String mess) {
                listener.onErrore(mess);
            }
        });
    }
    private static void mandaRichiesta(String json,OnRispostaListener listener)
    {
        new Thread(()->
        {
            try
            {
                int tent=0;
                URL url=new URL(urlScript);
                HttpURLConnection conn=(HttpURLConnection)url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type","application/json; utf-8");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setInstanceFollowRedirects(false);
                OutputStream os=conn.getOutputStream();
                os.write(json.getBytes(StandardCharsets.UTF_8));
                os.close();
                int codice=conn.getResponseCode();
                while((codice==HttpURLConnection.HTTP_MOVED_TEMP||codice==HttpURLConnection.HTTP_MOVED_PERM||codice==303)&&tent<5)
                {
                    String local=conn.getHeaderField("Location");
                    System.out.println("verso "+local);
                    URL url2=new URL(local);
                    conn=(HttpURLConnection)url2.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setInstanceFollowRedirects(false);
                    codice=conn.getResponseCode();
                    tent++;
                }
                System.out.println("codice finale "+codice);
                HttpURLConnection conn2=conn;
                if(codice==HttpURLConnection.HTTP_MOVED_TEMP||codice==HttpURLConnection.HTTP_MOVED_PERM)
                {
                    String url2 = conn.getHeaderField("Location");
                    URL urlNuovo = new URL(url2);
                    conn2 = (HttpURLConnection) urlNuovo.openConnection();
                    conn2.setRequestMethod("GET");
                }
                BufferedReader read;
                int codice2=conn2.getResponseCode();
                System.out.println("codice: "+codice);
                if(codice2>=200&&codice2<300)read=new BufferedReader(new InputStreamReader(conn2.getInputStream()));
                else read=new BufferedReader(new InputStreamReader(conn2.getErrorStream()));
                String risp=read.readLine();
                read.close();
                System.out.println("risposta conn2 "+risp);
                if(risp==null||risp.trim().isEmpty())
                {
                    new Handler(Looper.getMainLooper()).post(()-> listener.onErrore("risposta vuota dal server"));
                    System.out.println("non andata a buon fine");
                    return;
                }
                String finale=risp.trim();
                new Handler(Looper.getMainLooper()).post(()->listener.onRisposta(finale));

            }
            catch (Exception e)
            {
                e.printStackTrace();
                new Handler(Looper.getMainLooper()).post(()-> listener.onErrore(e.getMessage()));
            }
        }).start();
    }

    public static void richiediId(OnIdRicevutoListener listener)
    {
        mandaRichiesta("{\"azione\":\"nuovoOrdine\"}",new OnRispostaListener()
        {
           @Override
           public void onRisposta(String risp)
           {
               String pulito=risp.trim();
               if(pulito.startsWith("<"))
               {
                   listener.onErrore("il seerver ha risp con html da id");
                   return;
               }
               try
               {
                   listener.onId(Integer.parseInt(risp));
               }
               catch (Exception e)
               {
                   listener.onErrore("formato non valido");
               }
           }
           @Override
            public void onErrore(String mess)
           {
               listener.onErrore(mess);
           }
        });
    }

    public interface OnInviatoListener
    {
        void onSuccesso();
        void onErrore(String messaggio);
    }
    public interface OnTuttiInviatiListener
    {
        void onCompletato();
    }

    public static void invialista(String sala, String tavolo, List<MenuItem> lista,OnTuttiInviatiListener listenerFinale)
    {
        inviaUno(sala,tavolo,lista,0,listenerFinale);
    }

    private static void inviaUno(String sala,String tavolo,List<MenuItem>lista,int indice,OnTuttiInviatiListener listenerFinale)
    {
        if(indice>=lista.size())
        {
            listenerFinale.onCompletato();
            return;
        }
        MenuItem item=lista.get(indice);
        invia(sala, tavolo, item, new OnInviatoListener() {
            @Override
            public void onSuccesso() {
                inviaUno(sala,tavolo,lista,indice+1,listenerFinale);
            }

            @Override
            public void onErrore(String messaggio) {
                inviaUno(sala,tavolo,lista,indice+1,listenerFinale);
            }
        });
    }

    public static void invia(String sala,String tavolo,MenuItem item,OnInviatoListener listener)
    {
        new Thread(()->
        {
            try
            {
                URL url=new URL(urlScript);
                HttpURLConnection conn=(HttpURLConnection)url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type","application/json; utf-8");
                conn.setDoInput(true);
                String json="{"
                        +"\"azione\":\"invioPiatto\","
                        +"\"id\":\""+item.getId()+"\","
                        + "\"sala\":\"" + sala + "\","
                        + "\"tavolo\":\"" + tavolo + "\","
                        + "\"piatto\":\"" + item.getNome() + "\","
                        + "\"prezzo\":\"" + item.getPrezzo() + "\","
                        +"\"pronto\":\""+item.getPronto()+"\""
                        + "}";
                OutputStream os=conn.getOutputStream();
                os.write(json.getBytes(StandardCharsets.UTF_8));
                os.close();
                int codiceRisp= conn.getResponseCode();

                new Handler(Looper.getMainLooper()).post(()->
                {
                    if(codiceRisp==200)listener.onSuccesso();
                    else listener.onErrore("codice risposta"+codiceRisp);
                });
            }
            catch (Exception e)
            {
                new Handler(Looper.getMainLooper()).post(() ->
                    listener.onErrore(e.getMessage())
                );
            }
        }).start();
    }
}
