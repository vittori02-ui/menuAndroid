package com.vittorioprivitera.menu;
import android.icu.util.Output;
import android.os.Looper;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.logging.Handler;

public class inviaOrdini {
    private static final String urlScript="https://script.google.com/macros/s/AKfycbzt3hWjH2RC-PrTUdbHm-P2KsOLtA7skENr5sK15vobxQ81NrqSt6A5JxotMgciq6h0/exec";
    public interface OnInviatoListener
    {
        void onSuccesso();
        void onErrore(String messaggio);
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
                conn.setRequestMethod("Content-Type","application/json; utf-8");
                conn.setDoInput(true);
                String json="{"
                        + "\"sala\":\"" + sala + "\","
                        + "\"tavolo\":\"" + tavolo + "\","
                        + "\"piatto\":\"" + item.getNome() + "\","
                        + "\"prezzo\":\"" + item.getPrezzo() + "\""
                        + "}";
                OutputStram os=conn.getOutputStream();
                os.write(json.getBytes(StandardCharsets.UTF_8));
                os.close;
                int codiceRisp= conn.getResponseCode();

                new Handler(Looper.getMainLooper().post())->
                {
                    if(codiceRisp==200)listener.onSuccesso();
                    else listener.onErrore("codice risposta"+codiceRisp);
                });
            }
            catch (Exception e)
            {
                new Handler(Looper.getMainLooper()).post(()->
                    listener.onErrore(e.getMessage());
                );
            }
        }).start();
    }
}
