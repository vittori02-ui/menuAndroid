//11:56
package com.vittorioprivitera.menu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import java.util.List;
import androidx.recyclerview.widget.RecyclerView;
public class menuAdapter extends RecyclerView.Adapter<menuAdapter.MenuViewHolder>
{
    private List<MenuItem> lista;
    private OnItemClickListener listener;

    public interface OnItemClickListener
    {
        void onItemClick(MenuItem item);
    }
    public void setOnItemClickListener(OnItemClickListener listener)
    {
            this.listener=listener;
    }
    public menuAdapter(List<MenuItem> lista)
    {
        this.lista=lista;
    }
    public static class MenuViewHolder extends RecyclerView.ViewHolder
    {
        ImageView img;
        TextView nome,desc,prezzo;
        public MenuViewHolder(@NonNull View itemView)
        {
            super(itemView);
            img=itemView.findViewById(R.id.img);
            nome=itemView.findViewById(R.id.nome);
            desc=itemView.findViewById(R.id.descrizione);
            prezzo=itemView.findViewById(R.id.prezzo);
        }
    }
    @NonNull
    @Override
    public MenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent,int position)
    {
        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.item,parent,false);
        return new MenuViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuViewHolder holder,int position)
    {
        MenuItem item=lista.get(position);
        holder.nome.setText(item.getNome());
        holder.desc.setText(item.getDesc());
        holder.prezzo.setText(item.getPrezzo()+" €");
        holder.img.setImageResource(item.getImg());
        holder.itemView.setOnClickListener(v ->
        {
            if(listener!=null)listener.onItemClick(item);
        });
    }
    @Override
    public int getItemCount()
    {
        return lista.size();
    }
}
