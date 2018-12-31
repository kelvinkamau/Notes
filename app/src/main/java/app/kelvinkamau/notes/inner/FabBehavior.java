package app.kelvinkamau.notes.inner;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.ViewCompat;
import app.kelvinkamau.notes.widget.Fab;
import com.google.android.material.snackbar.Snackbar;

@SuppressWarnings("unused")
public class FabBehavior extends CoordinatorLayout.Behavior<Fab> {
    public FabBehavior(Context context, AttributeSet attrs) {
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, Fab child, View dependency) {
        return dependency instanceof Snackbar.SnackbarLayout;

    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, Fab child, View dependency) {
        if (dependency instanceof Snackbar.SnackbarLayout) {
            float translationY = Math.min(0, dependency.getTranslationY() - dependency.getHeight());
            child.setTranslationY(translationY);
            return true;
        } else {
            child.setTranslationY(0.0f);
            return false;
        }
    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, Fab child, View directTargetChild, View target, int nestedScrollAxes) {
        return nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL ||
                super.onStartNestedScroll(coordinatorLayout, child, directTargetChild, target,
                        nestedScrollAxes);
    }

    @Override
    public void onNestedScroll(CoordinatorLayout coordinatorLayout, Fab child, View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);

        if (dyConsumed > 0) {
            child.show();
        } else if (dyConsumed < 0) {
            child.show();
        }
    }
}
