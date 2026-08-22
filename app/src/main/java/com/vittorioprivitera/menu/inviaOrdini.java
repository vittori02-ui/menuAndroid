package com.vittorioprivitera.menu;
import android.os.Looper;
import android.os.Handler;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class inviaOrdini {
    private static final String urlScript="https://script.google.com/macros/s/AKfycbwpBM-sAxghUc5vtqiYZXx06EVZiOwJ6JzlSBfjrUFBikErSuPfExHSx4azg5UakknZ/exec";
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
                        + "\"sala\":\"" + sala + "\","
                        + "\"tavolo\":\"" + tavolo + "\","
                        + "\"piatto\":\"" + item.getNome() + "\","
                        + "\"prezzo\":\"" + item.getPrezzo() + "\""
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
