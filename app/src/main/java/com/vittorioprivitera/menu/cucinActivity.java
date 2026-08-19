//10:25
package com.vittorioprivitera.menu;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import java.util.ArrayList;
import java.util.List;

public class cucinActivity extends AppCompatActivity {
    RecyclerView ordini;
    Button indietro;
    Intent act;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cucina);
        ordini=findViewById(R.id.ordini);
        indietro=findViewById(R.id.Bindietro);
        ordini.setLayoutManager(new LinearLayoutManager(this));
        //ArrayList<Object> ordiniTot=(ArrayList<Object>) getIntent().getSerializableExtra("ordini");
        List<MenuItem> ordiniTot=ordiniTutti.getOrdini();
        List<Object> ordiniConv=new ArrayList<>(ordiniTot);
        menuAdapter menu= new menuAdapter(ordiniConv);
        ordini.setAdapter(menu);
        indietro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                act=new Intent(cucinActivity.this,menuActivity.class);
                startActivity(act);
            }
        });
    }
}