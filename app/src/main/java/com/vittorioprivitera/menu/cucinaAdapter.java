package com.vittorioprivitera.menu;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
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
        TextView nome,prezzo,desc,stato;
        public OrdineViewHolder(@NonNull View itemView)
        {
            super(itemView);
            nome=itemView.findViewById(R.id.nome);
            prezzo=itemView.findViewById(R.id.prezzo);
            desc=itemView.findViewById(R.id.descrizione);
            stato=itemView.findViewById(R.id.pronto);

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
    public void onBindViewHolder(@NonNull OrdineViewHolder holder,int pos)
    {
        MenuItem item=lista.get(pos);
        holder.nome.setText(item.getNome());
        holder.prezzo.setText(item.getPrezzo()+" €");
        holder.desc.setText(item.getDesc());
        if(item.getPronto())
        {
            holder.stato.setVisibility(View.VISIBLE);
            holder.itemView.setBackgroundColor(Color.parseColor("#C8E6C9"));
            holder.stato.setTextColor(Color.parseColor("#000000"));
            holder.stato.setText("Pronto");
        }
        else
        {
            holder.stato.setVisibility(View.VISIBLE);
            holder.itemView.setBackgroundColor(Color.parseColor("#FFECB3"));
            holder.stato.setTextColor(Color.parseColor("#000000"));
            holder.stato.setText("In attesa");
        }
    }

    @Override
    public int getItemCount()
    {
        return lista.size();
    }

    public void spostaElemento(int daPos,int inPos)
    {
        MenuItem daItem=lista.get(daPos);
        MenuItem inItem=lista.get(inPos);
        Collections.swap(lista,daPos,inPos);
        notifyItemMoved(daPos,inPos);
        List<MenuItem> carello=ordiniTutti.getOrdini();
        int x=carello.indexOf(daItem);
        int y=carello.indexOf(inItem);
        if(x!=-1&&y!=-1)Collections.swap(carello,x,y);
        System.out.println("spostato");
    }
}
