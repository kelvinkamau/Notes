package app.kelvinkamau.notes.widget;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import app.kelvinkamau.notes.R;
import app.kelvinkamau.notes.inner.Formatter;
import app.kelvinkamau.notes.model.DatabaseModel;
import app.kelvinkamau.notes.model.Note;
import app.kelvinkamau.notes.widget.template.ModelViewHolder;

public class NoteViewHolder extends ModelViewHolder<Note> {
    public ImageView badge;
    public TextView title;
    public TextView date;

    public NoteViewHolder(View itemView) {
        super(itemView);
        badge = itemView.findViewById(R.id.badge_icon);
        title = itemView.findViewById(R.id.title_txt);
        date = itemView.findViewById(R.id.date_txt);
    }

    @Override
    public void populate(Note item) {
        if (item.type == DatabaseModel.TYPE_NOTE_DRAWING) {
            badge.setImageResource(R.drawable.fab_drawing);
        } else {
            badge.setImageResource(R.drawable.fab_type);
        }
        title.setText(item.title);
        date.setText(Formatter.formatShortDate(item.createdAt));
    }
}
