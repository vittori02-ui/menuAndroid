package com.vittorioprivitera.menu;
import static java.lang.Integer.parseInt;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuAdapter;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.os.Debug;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import android.widget.Button;
import android.content.Intent;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class menuActivity extends AppCompatActivity {
    TextView lb1,lb2,lb3,desc;
    Button bIndietro,bCucina;
    Intent act;
    CardView menu;
    /*  sbagliato approccio perche è ordini non menulist
    private List<Object> prendiLista()
    {
        try
        {
            List<Object>menuList=(List<Object>)getIntent().getSerializableExtra("ordini");
            if(menuList==null)
            {
                List<Object> menuList2 = menuLoader.caricaMenu(this);
                System.out.println("siamo nell'if");
                return menuList2;
            }
            System.out.println("abbiamo passato il try");
            return menuList;
        }
        catch(Exception e)
        {
            List<Object> menuList = menuLoader.caricaMenu(this);
            System.out.println("siamo nel catch");
            return menuList;
        }
    }*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_activity);
        lb1=findViewById(R.id.lb1);
        lb2=findViewById(R.id.lb2);
        lb3=findViewById(R.id.lb3);
        bIndietro=findViewById(R.id.bIndietro);
        bCucina=findViewById(R.id.cucina);
        //menu=findViewById(R.id.menuGraf);
        //desc=findViewById(R.id.desTxt);
        String sala=getIntent().getStringExtra("sala");
        String tav=getIntent().getStringExtra("tavolo");
        int cli=getIntent().getIntExtra("clienti",0);
        lb1.setText(sala);
        lb2.setText(tav);
        lb3.setText("Clienti: "+cli);
        bIndietro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                act=new Intent(menuActivity.this,MainActivity.class);
                startActivity(act);
            }
        });/*
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                desc.setText("ciao");
            }
        });*/
        RecyclerView menu=findViewById(R.id.menu);
        menu.setLayoutManager(new LinearLayoutManager(this));
        List<Object>menuList=menuLoader.caricaMenu(this);
        menuAdapter adap=new menuAdapter(menuList);
        System.out.println("ci sono");
        adap.setOnItemClickListener(item -> {
            //Toast.makeText(menuActivity.this,"Hai cliccato "+item.getNome(),Toast.LENGTH_SHORT).show();
            System.out.println("Hai cliccato "+item.getNome());
            ordiniTutti.addElem(item);
            System.out.println("Dimensione"+ordiniTutti.getOrdini().size());
            for(int i=0;i<ordiniTutti.getOrdini().size();i++)
            {
                System.out.println(ordiniTutti.getOrdini().get(i).completo());
            }
        });
        menu.setAdapter(adap);
        bCucina.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                act=new Intent(menuActivity.this,cucinActivity.class);
                act.putExtra("sala",sala);
                act.putExtra("tavolo",tav);
                act.putExtra("clienti",cli);
                startActivity(act);
            }
        });
        //ciao
    }
}