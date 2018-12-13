package app.kelvinkamau.notes.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;

import app.kelvinkamau.notes.R;
import app.kelvinkamau.notes.adapter.template.ModelAdapter;
import app.kelvinkamau.notes.model.Note;
import app.kelvinkamau.notes.widget.NoteViewHolder;

public class NoteAdapter extends ModelAdapter<Note, NoteViewHolder> {
    public NoteAdapter(ArrayList<Note> items, ArrayList<Note> selected, ClickListener<Note> listener) {
        super(items, selected, listener);
    }

    @Override
    public NoteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new NoteViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_note, parent, false));
    }
}