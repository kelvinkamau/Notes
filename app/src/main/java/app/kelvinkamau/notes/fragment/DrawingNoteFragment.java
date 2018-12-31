package app.kelvinkamau.notes.fragment;

import android.util.Base64;
import android.view.View;
import app.kelvinkamau.notes.R;
import app.kelvinkamau.notes.fragment.template.NoteFragment;
import app.kelvinkamau.notes.model.DatabaseModel;
import com.android.graphics.CanvasView;

public class DrawingNoteFragment extends NoteFragment {
    private CanvasView canvas;

    public DrawingNoteFragment() {
    }

    @Override
    public int getLayout() {
        return R.layout.fragment_drawing_note;
    }

    @Override
    public void saveNote(final SaveListener listener) {
        super.saveNote(listener);

        new Thread() {
            @Override
            public void run() {
                note.body = Base64.encodeToString(canvas.getBitmapAsByteArray(), Base64.NO_WRAP);

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
        canvas = view.findViewById(R.id.canvas);
        view.findViewById(R.id.pen_tool).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                canvas.setMode(CanvasView.Mode.DRAW);
                canvas.setPaintStrokeWidth(5F);
            }
        });

        view.findViewById(R.id.eraser_tool).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                canvas.setMode(CanvasView.Mode.ERASER);
                canvas.setPaintStrokeWidth(40F);
            }
        });

        view.findViewById(R.id.undo_tool).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //todo add undo canvas method
            }
        });

        view.findViewById(R.id.redo_tool).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //todo add redo canvas method

            }
        });

        if (!note.body.isEmpty()) {
            canvas.drawBitmap(Base64.decode(note.body, Base64.NO_WRAP));
        }
    }
}
