package app.kelvinkamau.notes.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import app.kelvinkamau.notes.R;
import app.kelvinkamau.notes.db.OpenHelper;
import app.kelvinkamau.notes.fragment.CategoryFragment;
import app.kelvinkamau.notes.fragment.template.RecyclerFragment;
import app.kelvinkamau.notes.inner.Animator;
import app.kelvinkamau.notes.model.Category;

public class CategoryActivity extends AppCompatActivity implements RecyclerFragment.Callbacks {
    public static final int REQUEST_CODE = 1;
    public static final int RESULT_CHANGE = 101;
    private Toolbar toolbar;
    private CategoryFragment fragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setTheme(Category.getStyle(getIntent().getIntExtra(OpenHelper.COLUMN_THEME, Category.THEME_GREEN)));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        try {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        } catch (Exception ignored) {
        }

        toolbar.findViewById(R.id.back_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        if (savedInstanceState == null) {
            fragment = new CategoryFragment();

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, fragment)
                    .commit();
        }
    }

    @Override
    public void onChangeSelection(boolean state) {
        if (state) {
            Animator.create(getApplicationContext())
                    .on(toolbar)
                    .setEndVisibility(View.INVISIBLE)
                    .animate(R.anim.fade_out);
        } else {
            Animator.create(getApplicationContext())
                    .on(toolbar)
                    .setStartVisibility(View.VISIBLE)
                    .animate(R.anim.fade_in);
        }
    }

    @Override
    public void toggleOneSelection(boolean state) {
    }

    @Override
    public void onBackPressed() {
        if (fragment.isFabOpen) {
            fragment.toggleFab(true);
            return;
        }

        if (fragment.selectionState) {
            fragment.toggleSelection(false);
            return;
        }

        Intent data = new Intent();
        data.putExtra("position", fragment.categoryPosition);
        data.putExtra(OpenHelper.COLUMN_COUNTER, fragment.items.size());
        setResult(RESULT_CHANGE, data);
        finish();
    }
}
