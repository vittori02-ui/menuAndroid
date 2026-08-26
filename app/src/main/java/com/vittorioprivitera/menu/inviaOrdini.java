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
    private static final String urlScript="https://script.google.com/macros/s/AKfycbyZKPo3hHHVYs2U_VdUhgb90nNM-WoFWyLTHup6qGvsjk8C0l08Pnc2CEoVy3rz1_GN/exec";
    public interface OnIdRicevutoListener
    {
        void onId(int id);
        void onErrore(String mess);
    }
    public interface OnStatoRicevutoListener
    {
        void onStato(String testo);
        void onErrore(String mess);
    }

    public interface OnRispostaListener
    {
        void onRisposta(String risp);
        void onErrore(String mess);
    }
    private static void mandaRichiesta(String json,OnIdRicevutoListener listener)
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
                conn.setDoOutput(true);
                conn.setInstanceFollowRedirects(false);
                OutputStream os=conn.getOutputStream();
                os.write(json.getBytes(StandardCharsets.UTF_8));
                os.close();
                int codice=conn.getResponseCode();
                HttpURLConnection conn2=conn;
                if(codice==HttpURLConnection.HTTP_MOVED_TEMP||codice==HttpURLConnection.HTTP_MOVED_PERM)
                {
                    String url2 = conn.getHeaderField("Location");
                    URL urlNuovo = new URL(url2);
                    conn2 = (HttpURLConnection) urlNuovo.openConnection();
                    conn2.setRequestMethod("GET");
                }
                BufferedReader read=new BufferedReader(new InputStreamReader(conn2.getInputStream()));
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
            catch (Exception e)
            {
                e.printStackTrace();
                new Handler(Looper.getMainLooper()).post(()-> listener.onErrore(e.getMessage()));
            }
        }).start();
    }

    public static void richiediId(OnStatoRicevutoListener listener)
    {
        mandaRichiesta("{\"azione\":\"nuovoOrdine\"}", new OnRispostaListener(){
            @Override
            public void onRisposta(String risp)
            {
                listener.onId(Integer.parseInt(risp));
            }
        }
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
