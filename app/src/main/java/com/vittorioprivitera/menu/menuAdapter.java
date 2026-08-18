//10:48
//13:22
package com.vittorioprivitera.menu;
import android.preference.PreferenceActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import java.util.List;
import java.util.Objects;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
public class menuAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    private static final int tit=0;
    private static final int piatto=1;
    private List<Object> lista;
    private OnItemClickListener listener;

    public interface OnItemClickListener
    {
        void onItemClick(MenuItem item);
    }
    public void setOnItemClickListener(OnItemClickListener listener)
    {
            this.listener=listener;
    }
    public menuAdapter(List<Object> lista)
    {
        this.lista=lista;
    }

    @Override
    public int getItemViewType(int pos)
    {
        if(lista.get(pos) instanceof titolo) return tit;
        else return piatto;
    }

    public static class titVisibile extends RecyclerView.ViewHolder
    {
        TextView titolo;
        public titVisibile(@NonNull View itemView)
        {
            super(itemView);
            titolo=itemView.findViewById(R.id.titolo);
        }
    }

    public  static class vediMenu extends RecyclerView.ViewHolder
    {
        ImageView img;
        TextView nome,desc,prezzo;
        public vediMenu(@NonNull View itemView)
        {
            super(itemView);
            img=itemView.findViewById(R.id.img);
            nome=itemView.findViewById(R.id.nome);
            desc=itemView.findViewById(R.id.descrizione);
            prezzo=itemView.findViewById(R.id.prezzo);
        }
    }
    @Override
    @NonNull
    public RecyclerView.ViewHolder onCreateViewHolder (@NonNull ViewGroup parent,int viewType)
    {
        if(viewType==tit)
        {
            View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.titolo_graf,parent,false);
            return new titVisibile(view);
        }
        else
        {
            View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.item,parent,false);
            return new vediMenu(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder,int pos)
    {
        Object ogg=lista.get(pos);
        if(holder instanceof titVisibile)
        {
            titolo tit=(titolo)ogg;
            ((titVisibile)holder).titolo.setText(tit.getTitolo());
        }
        else if(holder instanceof vediMenu)
        {
            MenuItem item=(MenuItem)ogg;
            vediMenu h=(vediMenu)holder;
            h.nome.setText(item.getNome());
            h.desc.setText(item.getDesc());
            h.prezzo.setText(item.getPrezzo()+" €");
            h.img.setImageResource(item.getImg());
            h.itemView.setOnClickListener(v ->
            {
                if(listener!=null)listener.onItemClick(item);
            });
        }
    }
    @Override
    public int getItemCount()
    {
        return lista.size();
    }
}
