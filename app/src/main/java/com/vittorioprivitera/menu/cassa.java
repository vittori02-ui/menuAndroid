package com.vittorioprivitera.menu;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class cassa extends AppCompatActivity {
    RecyclerView listaCassa;
    TextView totale,lista2,prezzi;
    Button paga;
    String sala,tav;
    float tot=0f;
    List<MenuItem> tutti;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cassa);
        //listaCassa=findViewById(R.id.lista);
        totale=findViewById(R.id.totale);
        paga=findViewById(R.id.bPaga);
        lista2=findViewById(R.id.lista2);
        prezzi=findViewById(R.id.prezzi);
        sala=getIntent().getStringExtra("sala");
        tav=getIntent().getStringExtra("tavolo");
        tutti=new ArrayList<>();
        tutti.addAll(ordiniTutti.getOrdini());
        tutti.addAll(ordiniTutti.getInviati());
        for(MenuItem item:tutti)
        {
            lista2.append(item.getNome()+"\n");
            prezzi.append(item.getPrezzo()+"€"+"\n");
        }
        //listaCassa.setLayoutManager(new LinearLayoutManager(this));
        //listaCassa.setAdapter(new cassaAdapter(tutti));
        for(MenuItem item:tutti)
        {
            tot+=item.getPrezzo();
        }
        totale.setText("TOTALE "+tot+"€");
        paga.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}