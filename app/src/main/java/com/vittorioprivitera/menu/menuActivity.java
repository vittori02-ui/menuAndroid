package com.vittorioprivitera.menu;

import static java.lang.Integer.parseInt;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class menuActivity extends AppCompatActivity {
    TextView lb1,lb2,lb3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_activity);
        lb1=findViewById(R.id.lb1);
        lb2=findViewById(R.id.lb2);
        lb3=findViewById(R.id.lb3);
        String sala=getIntent().getStringExtra("sala");
        String tav=getIntent().getStringExtra("tavolo");
        int cli=parseInt(getIntent().getStringExtra("clienti"));
        lb1.setText("La sala è "+sala);
        lb2.setText("Il tavolo è "+tav);
        lb3.setText("I clienti sono "+cli);
    }
}