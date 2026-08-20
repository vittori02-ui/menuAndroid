package com.vittorioprivitera.menu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
public class cucinaAdapter extends RecyclerView.Adapter<cucinaAdapter.OrdineViewHolder>
{
    private List<MenuItem> lista;

    public cucinaAdapter(List<MenuItem> lista)
    {
        this.lista=lista;
    }
    public static class OrdineViewHolder extends RecyclerView.ViewHolder
    {
        TextView nome,prezzo;
        public OrdineViewHolder(@NonNull View itemView)
        {
            super(itemView);
            nome=itemView.findViewById(R.id.nome);
            prezzo=itemView.findViewById(R.id.prezzo);
        }
    }

    @NonNull
    @Override
    public OrdineViewHolder onCreateViewHolder(@NonNull ViewGroup parent,int viewType)
    {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item,parent,false);
        return new OrdineViewHolder(view);
    }

    @Override
    public void OnBindViewHolder(@NonNull OrdineViewHolder holder,int pos)
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

    public void spostaElemento(int daPos,int inPos)
    {
        Collections.swap(lista,daPos,inPos);
        notifyItemMoved(daPos,inPos);
    }
}
