package com.vittorioprivitera.menu;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class cassa extends AppCompatActivity {
    RecyclerView listaCassa;
    TextView totale,lista2,prezzi;
    Button paga,dietro;
    String sala,tav;
    int cli;
    float tot=0f;
    List<MenuItem> tutti;
    private File generaPdf() {
        try
        {
            android.graphics.pdf.PdfDocument doc=new android.graphics.pdf.PdfDocument();
            PdfDocument.PageInfo info= new PdfDocument.PageInfo.Builder(300,500,1).create();
            PdfDocument.Page pagina=doc.startPage(info);
            android.graphics.Canvas canvas=pagina.getCanvas();
            android.graphics.Paint paint=new Paint();
            paint.setTextSize(20);
            int y=30;
            canvas.drawText("SCONTRINO",100,y,paint);
            y+=20;
            canvas.drawText("Sala: "+sala+" Tavolo: "+tav+" Clienti: "+cli,20,y,paint);
            y+=30;
            for(MenuItem item:tutti)
            {
                canvas.drawText(item.getNome() +" - "+item.getPrezzo()+" €",20,y,paint);
                y+=20;
            }
            y+=20;
            canvas.drawText("Coperto "+2.50*cli+"€",20,y,paint);
            y+=20;
            paint.setTextSize(16);
            canvas.drawText("Totale: "+tot+"€",20,y,paint);
            doc.finishPage(pagina);
            File dir=new File(getExternalFilesDir(null),"scontrini");
            if(!dir.exists())dir.mkdirs();
            File file=new File(dir,"scontrino_"+System.currentTimeMillis()+".pdf");
            doc.writeTo(new java.io.FileOutputStream(file));
            doc.close();
            return file;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            Toast.makeText(this,"errore nel pdf",Toast.LENGTH_SHORT).show();
            return null;
        }
    }
    private void condPdf(File file)
    {
        Uri uri= FileProvider.getUriForFile(this,getPackageName()+".fileprovider",file);
        Intent act=new Intent(Intent.ACTION_SEND);
        act.setType("application/pdf");
        act.putExtra(Intent.EXTRA_STREAM,uri);
        act.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(Intent.createChooser(act,"Condividi/Salva scontrino"));

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cassa);
        //listaCassa=findViewById(R.id.lista);
        totale=findViewById(R.id.totale);
        paga=findViewById(R.id.bPaga);
        lista2=findViewById(R.id.lista2);
        prezzi=findViewById(R.id.prezzi);
        dietro=findViewById(R.id.bDietro);
        sala=getIntent().getStringExtra("sala");
        tav=getIntent().getStringExtra("tavolo");
        cli=getIntent().getIntExtra("clienti",0);
        tutti=new ArrayList<>();
        tutti.addAll(ordiniTutti.getOrdini());
        tutti.addAll(ordiniTutti.getInviati());
        for(MenuItem item:tutti)
        {
            lista2.append(item.getNome()+"\n");
            prezzi.append(item.getPrezzo()+"€"+"\n");
        }
        lista2.append("Coperto:"+"\n");
        prezzi.append(2.50*cli+"€");
        //listaCassa.setLayoutManager(new LinearLayoutManager(this));
        //listaCassa.setAdapter(new cassaAdapter(tutti));
        for(MenuItem item:tutti)
        {
            tot+=item.getPrezzo();
        }
        totale.setText("TOTALE "+tot+2.50*cli+"€");
        paga.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}