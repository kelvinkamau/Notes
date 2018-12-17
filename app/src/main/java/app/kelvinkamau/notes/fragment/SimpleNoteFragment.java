package app.kelvinkamau.notes.fragment;

import android.preference.PreferenceManager;
import android.view.View;

import com.google.android.material.snackbar.Snackbar;

import androidx.core.content.ContextCompat;
import app.kelvinkamau.notes.R;
import app.kelvinkamau.notes.fragment.template.NoteFragment;
import app.kelvinkamau.notes.model.DatabaseModel;
import jp.wasabeef.richeditor.RichEditor;

public class SimpleNoteFragment extends NoteFragment {
    private RichEditor body;
    private PreferenceManager PrefManager;

    public SimpleNoteFragment() {
    }

    @Override
    public int getLayout() {
        return R.layout.fragment_simple_note;

    }

    @Override
    public void saveNote(final SaveListener listener) {
        super.saveNote(listener);
        note.body = body.getHtml();

        new Thread() {
            @Override
            public void run() {
                long id = note.save();
                if (note.id == DatabaseModel.NEW_MODEL_ID) {
                    note.id = id;
                }
                listener.onSave();
                interrupt();
            }
        }.start();
    }

    @Override
    public void init(View view) {


        body = view.findViewById(R.id.editor);
        body.setPlaceholder("Type notes here");
        body.setEditorBackgroundColor(ContextCompat.getColor(getContext(), R.color.ripple_dark));

        Snackbar.make(view, R.string.notify_save, Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.ok, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                })
                .show();

        view.findViewById(R.id.action_bold).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                body.setBold();
            }
        });

        view.findViewById(R.id.action_italic).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                body.setItalic();
            }
        });

        view.findViewById(R.id.action_underline).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                body.setUnderline();
            }
        });

        view.findViewById(R.id.action_strike).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                body.setStrikeThrough();
            }
        });
        view.findViewById(R.id.action_bullets).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                body.setBullets();
            }
        });

        view.findViewById(R.id.action_superscript).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                body.setSuperscript();
            }
        });
        view.findViewById(R.id.action_subscript).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                body.setSubscript();
            }
        });
        view.findViewById(R.id.action_undo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                body.undo();
            }
        });
        view.findViewById(R.id.action_redo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                body.redo();
            }
        });
        body.setHtml(note.body);
    }
}
