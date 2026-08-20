package com.vittorioprivitera.menu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class MainActivity extends AppCompatActivity {

    Spinner elenTavoli,elenSala;
    EditText clienti;
    Button avanti;
    Intent act;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        elenTavoli=findViewById(R.id.elencoTavoli);
        elenSala=findViewById(R.id.elencoSale);
        clienti=findViewById(R.id.numClienti_txt);
        avanti=findViewById(R.id.buttonAvanti);
        avanti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                act=new Intent(MainActivity.this,menuActivity.class);
                act.putExtra("sala",elenSala.getSelectedItem().toString());
                act.putExtra("tavolo",elenTavoli.getSelectedItem().toString());
                if(clienti.getText().toString().equals(""))return;
                act.putExtra("clienti",Integer.parseInt(clienti.getText().toString()));
                startActivity(act);
            }
        });
    }
}