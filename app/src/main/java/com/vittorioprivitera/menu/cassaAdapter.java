package com.vittorioprivitera.menu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.view.accessibility.AccessibilityViewCommand;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.List;

public class cassaAdapter extends RecyclerView.Adapter<cassaAdapter.CassaViewHolder>
{
    private List<MenuItem> lista;
    public cassaAdapter(List<MenuItem>lista)
    {
        this.lista=lista;
    }

    public static class CassaViewHolder extends RecyclerView.ViewHolder
    {
        TextView nome,prezzo;
        public CassaViewHolder(@NonNull View itemView)
        {
            super(itemView);
            nome=itemView.findViewById(R.id.nome);
            prezzo=itemView.findViewById(R.id.prezzo);
        }
    }
    @NonNull
    @Override
    public CassaViewHolder onCreateViewHolder(@NonNull ViewGroup parent,int viewType)
    {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item,parent,false);
        return new CassaViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull CassaViewHolder holder,int pos)
    {
        MenuItem item=lista.get(pos);
        holder.nome.setText(item.getNome());
        holder.prezzo.setText(item.getPrezzo()+" €");
    }
    @Override
    public int getItemCount()
    {
        return lista.size();
    }
}
