//15:36
package com.vittorioprivitera.menu;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class cucinActivity extends AppCompatActivity {
    RecyclerView ordini;
    Button indietro,invia;
    Intent act;
    private Handler pollingHandler=new Handler(Looper.getMainLooper());
    private Runnable polling;
    private static final int inter=5000;

    private void avviaPolling(cucinaAdapter adap)
    {
        polling=new Runnable() {
            @Override
            public void run() {
                Set<Integer> daControllare=new HashSet<>();
                for(MenuItem item:ordiniTutti.getInviati())
                {
                    if(!item.getPronto())daControllare.add(item.getId());
                }
                if(daControllare.isEmpty())
                {
                    pollingHandler.postDelayed(this,inter);
                    return;
                }
                //List<Integer> lista=new ArrayList<>(daControllare);
                inviaOrdini.richiediStatiMult(ordiniTutti.getInviati(), new inviaOrdini.OnStatoMultiploListener() {
                    @Override
                    public void onRis(Map<String, Boolean> ris) {
                        for(MenuItem item:ordiniTutti.getInviati())
                        {
                            String chiave=item.getId()+"_"+item.getNome();
                            if(ris.containsKey(chiave))item.setPronto(ris.get(chiave));
                        }
                        adap.notifyDataSetChanged();
                        pollingHandler.postDelayed(polling,inter);
                    }

                    @Override
                    public void onErrore(String mess) {
                        System.out.println("errore nel polling");
                        pollingHandler.postDelayed(polling,inter);
                    }
                });
            }
        };
        pollingHandler.post(polling);
    }
    private void fermaPolling()
    {
        if(pollingHandler!=null)pollingHandler.removeCallbacks(polling);
    }


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
        List<MenuItem> ordiniTot=new ArrayList<>();
        ordiniTot.addAll(ordiniTutti.getOrdini());
        ordiniTot.addAll(ordiniTutti.getInviati());
        //List<Object> ordiniConv=new ArrayList<>(ordiniTot);
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
                inviaOrdini.richiediId(new inviaOrdini.OnIdRicevutoListener() {
                    @Override
                    public void onId(int id) {
                        for(MenuItem item: ordiniTutti.getOrdini())
                        {
                            item.setId(id);
                        }
                        inviaOrdini.invialista(sala,tav,ordiniTutti.getOrdini(),()->
                        {
                            Toast.makeText(cucinActivity.this,"ordine inviato",Toast.LENGTH_SHORT).show();
                            for(MenuItem item:ordiniTutti.getOrdini())
                            {
                                ordiniTutti.addElemInviato(item);
                            }
                            ordiniTutti.svuota();
                        });
                    }

                    @Override
                    public void onErrore(String mess) {
                        Toast.makeText(cucinActivity.this,"non inviato "+mess,Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
                /*
                inviaOrdini.richiediId(new inviaOrdini.OnIdRicevutoListener() {
                    @Override
                    public void onId(int id) {
                        List<MenuItem> copiaOrdini=new ArrayList<>(ordiniTutti.getOrdini());
                        for(MenuItem item:copiaOrdini)
                        {
                            item.setId(id);
                        }
                        inviaOrdini.invialista(sala,tav,copiaOrdini,()->
                        {
                            Toast.makeText(cucinActivity.this,"Ordine n "+id+" inviato",Toast.LENGTH_SHORT).show();
                        });
                    }
                    @Override
                    public void onErrore(String mess) {
                        Toast.makeText(cucinActivity.this,"Errore "+mess,Toast.LENGTH_LONG).show();
                        System.out.println(mess);
                    }
                });*/


                /*
                String sala=getIntent().getStringExtra("sala");
                String tav=getIntent().getStringExtra("tavolo");
                inviaOrdini.invialista(sala,tav,ordiniTutti.getOrdini(),()->
                {
                   Toast.makeText(cucinActivity.this,"tutti gli ordini inviati",Toast.LENGTH_SHORT).show();
                   ordiniTutti.svuota();
                });
                */
        avviaPolling(adap);
    }
    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        fermaPolling();
    }
}
