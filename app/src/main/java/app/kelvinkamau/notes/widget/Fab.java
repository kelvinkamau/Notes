package app.kelvinkamau.notes.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.appcompat.widget.AppCompatImageView;
import app.kelvinkamau.notes.R;
import app.kelvinkamau.notes.inner.Animator;

public class Fab extends AppCompatImageView {
    private boolean isHidden = false;

    public Fab(Context context) {
        super(context);
    }

    public Fab(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public Fab(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * Makes the fab visible if it is hidden
     */
    public void show() {
        if (isHidden) {
            isHidden = false;
            Animator.create(getContext().getApplicationContext())
                    .on(this)
                    .setStartVisibility(View.VISIBLE)
                    .animate(R.anim.fab_scroll_in);
        }
    }

    /**
     * Makes the fab gone if it is visible and smart fab preference is enabled
     */
    /*public void hide() {
        if (App.smartFab && !isHidden) {
            isHidden = true;
            Animator.create(getContext().getApplicationContext())
                    .on(this)
                    .setEndVisibility(View.GONE)
                    .animate(R.anim.fab_scroll_out);
        }
    }*/
}
