//15:36
package com.vittorioprivitera.menu;
import static java.lang.Integer.parseInt;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class cucinActivity extends AppCompatActivity {
    RecyclerView ordini;
    Button indietro,invia;
    Intent act;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cucina);
        ordini=findViewById(R.id.ordini);
        indietro=findViewById(R.id.Bindietro);
        invia=findViewById(R.id.bInvia);
        ordini.setLayoutManager(new LinearLayoutManager(this));
        //ArrayList<Object> ordiniTot=(ArrayList<Object>) getIntent().getSerializableExtra("ordini");
        //List<MenuItem> ordiniTot=new ArrayList<>(ordiniTutti.getOrdini()); //copia
        List<MenuItem> ordiniTot=ordiniTutti.getOrdini();
        List<Object> ordiniConv=new ArrayList<>(ordiniTot);
        //menuAdapter menu= new menuAdapter(ordiniConv);
        cucinaAdapter adap=new cucinaAdapter(ordiniTot);
        ordini.setAdapter(adap);
        ItemTouchHelper.Callback drag=new dragDrop(adap);
        ItemTouchHelper touchHelper=new ItemTouchHelper(drag);
        touchHelper.attachToRecyclerView(ordini);
        String sala=getIntent().getStringExtra("sala");
        String tav=getIntent().getStringExtra("tavolo");
        int cli=getIntent().getIntExtra("clienti",0);
        indietro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                act=new Intent(cucinActivity.this,menuActivity.class);
                act.putExtra("clienti",cli);
                act.putExtra("tavolo",tav);
                act.putExtra("sala",sala);
                startActivity(act);
            }
        });
        invia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String sala=getIntent().getStringExtra("sala");
                String tav=getIntent().getStringExtra("tavolo");
                inviaOrdini.invialista(sala,tav,ordiniTutti.getOrdini(),()->
                {
                   Toast.makeText(cucinActivity.this,"tutti gli ordini inviati",Toast.LENGTH_SHORT).show();
                   ordiniTutti.svuota();
                });

                /*for(MenuItem item:ordiniTutti.getOrdini())
                {
                    inviaOrdini.invia(sala,tav,item,new inviaOrdini.OnInviatoListener()
                    {
                        @Override
                        public void onSuccesso()
                        {
                            Toast.makeText(cucinActivity.this,"ordine inviato: "+item.getNome(),Toast.LENGTH_SHORT).show();
                        }
                        @Override
                        public void onErrore(String mes)
                        {
                            Toast.makeText(cucinActivity.this,"errore: "+mes,Toast.LENGTH_LONG).show();
                        }
                    });
                }*/
            }
        });
    }
}