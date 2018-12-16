package app.kelvinkamau.notes.widget;

import android.content.Context;
import android.util.AttributeSet;

import androidx.recyclerview.widget.RecyclerView;
import app.kelvinkamau.notes.App;

public class FixedHeightRecyclerView extends RecyclerView {
    public FixedHeightRecyclerView(Context context) {
        super(context);
    }

    public FixedHeightRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FixedHeightRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthSpec, int heightSpec) {
        heightSpec = MeasureSpec.makeMeasureSpec(App.DEVICE_HEIGHT / 2, MeasureSpec.EXACTLY);
        super.onMeasure(widthSpec, heightSpec);
    }
}
