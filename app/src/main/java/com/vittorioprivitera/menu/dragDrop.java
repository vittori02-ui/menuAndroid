package com.vittorioprivitera.menu;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
public class dragDrop extends ItemTouchHelper.SimpleCallback
{
    private cucinaAdapter adap;
    public dragDrop(cucinaAdapter adap)
    {
        super(ItemTouchHelper.UP|ItemTouchHelper.DOWN,ItemTouchHelper.RIGHT);
        this.adap=adap;
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView,@NonNull RecyclerView.ViewHolder viewHolder,@NonNull RecyclerView.ViewHolder target)
    {
        int daPos=viewHolder.getAdapterPosition();
        int aPos=target.getAdapterPosition();
        adap.spostaElemento(daPos,aPos);
        return true;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder,int dir)
    {
        int pos=viewHolder.getAdapterPosition();
        adap.elimanElemento(pos);
    }
}
