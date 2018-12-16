package app.kelvinkamau.notes.widget.template;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;
import app.kelvinkamau.notes.R;
import app.kelvinkamau.notes.model.DatabaseModel;

abstract public class ModelViewHolder<T extends DatabaseModel> extends RecyclerView.ViewHolder {
    public View holder;
    public View selected;

    public ModelViewHolder(View itemView) {
        super(itemView);
        holder = itemView.findViewById(R.id.holder);
        selected = itemView.findViewById(R.id.selected);
    }

    public void setSelected(boolean status) {
        selected.setVisibility(status ? View.VISIBLE : View.GONE);
    }

    abstract public void populate(T item);
}
