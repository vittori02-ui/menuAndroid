package com.vittorioprivitera.menu;
import android.os.Looper;
import android.os.Handler;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class inviaOrdini {
    private static final String urlScript="https://script.google.com/macros/s/AKfycbwtXgjlMSf3rOzSN1v8fsax4urrPiGzWIuQMoI45R1CQAnkrChpmKk7ZalZ3dIa4MHm/exec";
    public interface OnIdRicevutoListener
    {
        void onId(int id);
        void onErrore(String mess);
    }
    public static void richiediId(OnIdRicevutoListener listener)
    {
        new Thread(()->
        {
            try
            {
                URL url=new URL(urlScript);
                HttpURLConnection conn=(HttpURLConnection)url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type","application/json; utf-8");
                conn.setDoOutput(true);
                conn.setInstanceFollowRedirects(false);
                String json="{\"azione\":\"nuovoOrdine\"}";
                OutputStream os=conn.getOutputStream();
                os.write(json.getBytes(StandardCharsets.UTF_8));
                os.close();
                int codice=conn.getResponseCode();

                if(codice==HttpURLConnection.HTTP_MOVED_TEMP||codice==HttpURLConnection.HTTP_MOVED_PERM)
                {
                    String url2=conn.getHeaderField("Location");
                    URL urlNuovo=new URL(url2);
                    HttpURLConnection conn2=(HttpURLConnection)urlNuovo.openConnection();
                    conn2.setRequestMethod("GET");
                    BufferedReader read=new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String risp=read.readLine();
                    read.close();
                    if(risp==null||risp.trim().isEmpty())
                    {
                        new Handler(Looper.getMainLooper()).post(()-> listener.onErrore("risposta vuota dal server"));
                        System.out.println("non andata a buon fine");
                        return;
                    }
                    int id=Integer.parseInt(risp.trim());
                    new Handler(Looper.getMainLooper()).post(()->listener.onId(id));
                }
                else
                {
                    BufferedReader read=new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String risposta=read.readLine();
                    read.close();
                    if(risposta==null||risposta.trim().isEmpty())
                    {
                        new Handler(Looper.getMainLooper()).post(()-> listener.onErrore("risposta guota dal server"));
                        System.out.println("non andata a buon fine");
                        return;
                    }
                    int id=Integer.parseInt(risposta.trim());
                    new Handler(Looper.getMainLooper()).post(()-> listener.onId(id));
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
                new Handler(Looper.getMainLooper()).post(()-> listener.onErrore(e.getMessage()));
            }
        }).start();
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
