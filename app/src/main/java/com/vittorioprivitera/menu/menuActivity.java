package com.vittorioprivitera.menu;
import static java.lang.Integer.parseInt;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuAdapter;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.os.Debug;
import android.view.View;
import android.widget.TextView;
import android.widget.Button;
import android.content.Intent;
import java.util.List;

public class menuActivity extends AppCompatActivity {
    TextView lb1,lb2,lb3,desc;
    Button bIndietro;
    Intent act;
    CardView menu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_activity);
        lb1=findViewById(R.id.lb1);
        lb2=findViewById(R.id.lb2);
        lb3=findViewById(R.id.lb3);
        bIndietro=findViewById(R.id.bIndietro);
        //menu=findViewById(R.id.menuGraf);
        //desc=findViewById(R.id.desTxt);
        String sala=getIntent().getStringExtra("sala");
        String tav=getIntent().getStringExtra("tavolo");
        int cli=parseInt(getIntent().getStringExtra("clienti"));
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
        List<MenuItem> menuList=menuLoader.caricaMenu(this);
        menuAdapter adap=new menuAdapter(menuList);
        menu.setAdapter(adap);
        System.out.println("ci sono");
    }
}