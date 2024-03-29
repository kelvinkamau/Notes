package app.kelvinkamau.notes.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.material.snackbar.Snackbar;


import org.json.JSONArray;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import app.kelvinkamau.notes.App;
import app.kelvinkamau.notes.R;
import app.kelvinkamau.notes.adapter.DrawerAdapter;
import app.kelvinkamau.notes.db.Controller;
import app.kelvinkamau.notes.dialog.ImportDialog;
import app.kelvinkamau.notes.dialog.SaveDialog;
import app.kelvinkamau.notes.fragment.MainFragment;
import app.kelvinkamau.notes.fragment.template.RecyclerFragment;
import app.kelvinkamau.notes.inner.Animator;
import app.kelvinkamau.notes.inner.Formatter;
import app.kelvinkamau.notes.model.Drawer;

public class MainActivity extends AppCompatActivity implements RecyclerFragment.Callbacks {
    public static final int PERMISSION_REQUEST = 3;
    public View drawerHolder;
    public Handler handler = new Handler();
    private DrawerLayout drawerLayout;
    private ImageView account;
    private boolean exitStatus = false;
    public Runnable runnable = new Runnable() {
        @Override
        public void run() {
            exitStatus = false;
        }
    };
    private MainFragment fragment;
    private Toolbar toolbar;
    private View selectionEdit;
    private boolean permissionNotGranted = false;
    private boolean checkForPermission = true;


    /*todo sign out
    firebaseAuth.signOut();
    Auth.GoogleSignInApi.signOut(apiClient);
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        int x = 1;

        try {
            //noinspection ConstantConditions
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        } catch (Exception ignored) {
        }

        setupDrawer();

        selectionEdit = findViewById(R.id.selection_edit);
        selectionEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragment.onEditSelected();
            }
        });

        if (savedInstanceState == null) {
            fragment = new MainFragment();

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, fragment)
                    .commit();
        }

        if (checkForPermission) {
            checkForPermission = false;
            if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermission();
            }
        }

    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(drawerHolder)) {
            drawerLayout.closeDrawers();
            return;
        }

        if (fragment.selectionState) {
            fragment.toggleSelection(false);
            return;
        }

        if (exitStatus) {
            finish();
        } else {
            exitStatus = true;
            Snackbar.make(fragment.fab != null ? fragment.fab : toolbar, R.string.exit_message, Snackbar.LENGTH_LONG).show();
            handler.postDelayed(runnable, 3500);
        }
    }


    private void setupDrawer() {
        // Set date in drawer
        ((TextView) findViewById(R.id.drawer_date)).setText(Formatter.formatDate());

        drawerLayout = findViewById(R.id.drawer_layout);
        drawerHolder = findViewById(R.id.drawer_holder);
        ListView drawerList = findViewById(R.id.drawer_list);

        // Navigation menu button
        findViewById(R.id.nav_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        // Settings button
        findViewById(R.id.settings_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickDrawer(Drawer.TYPE_SETTINGS);
            }
        });

        // Set adapter of drawer
        drawerList.setAdapter(new DrawerAdapter(
                getApplicationContext(),
                new DrawerAdapter.ClickListener() {
                    @Override
                    public void onClick(int type) {
                        onClickDrawer(type);
                    }
                }
        ));
    }

    private void onClickDrawer(final int type) {
        drawerLayout.closeDrawers();

        try {
            handler.removeCallbacks(runnable);
        } catch (Exception ignored) {
        }

        new Thread() {
            @Override
            public void run() {
                try {
                    // wait for completion of drawer animation
                    sleep(500);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            switch (type) {
                                case Drawer.TYPE_ABOUT:
                                    new MaterialDialog.Builder(MainActivity.this)
                                            .title(R.string.app_name)
                                            .content(R.string.about_desc)
                                            .positiveText(R.string.ok)
                                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                                @Override
                                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                                    dialog.dismiss();
                                                }
                                            })
                                            .show();
                                    break;
                                case Drawer.TYPE_BACKUP:
                                    backupData();
                                    break;
                                case Drawer.TYPE_RESTORE:
                                    restoreData();
                                    break;
                                case Drawer.TYPE_SETTINGS:
                                    // TODO implement settings
                                    new MaterialDialog.Builder(MainActivity.this)
                                            .title(R.string.app_name)
                                            .content(R.string.about_desc)
                                            .positiveText(R.string.ok)
                                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                                @Override
                                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                                    dialog.dismiss();
                                                }
                                            })
                                            .show();
                                    break;
                            }
                        }
                    });

                    interrupt();
                } catch (Exception ignored) {
                }
            }
        }.start();
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
        selectionEdit.setVisibility(state ? View.VISIBLE : View.GONE);
    }

    private void restoreData() {
        ImportDialog.newInstance(
                R.string.restore,
                new String[]{App.BACKUP_EXTENSION},
                new ImportDialog.ImportListener() {
                    @Override
                    public void onSelect(final String path) {
                        new Thread() {
                            @Override
                            public void run() {
                                try {
                                    readBackupFile(path);

                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            fragment.loadItems();

                                            Snackbar.make(fragment.fab != null ? fragment.fab : toolbar, R.string.data_restored, Snackbar.LENGTH_LONG).show();
                                        }
                                    });
                                } catch (final Exception e) {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            new MaterialDialog.Builder(MainActivity.this)
                                                    .title(R.string.restore_error)
                                                    .positiveText(R.string.ok)
                                                    .content(e.getMessage())
                                                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                                                        @Override
                                                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                                            dialog.dismiss();
                                                        }
                                                    })
                                                    .show();
                                        }
                                    });
                                } finally {
                                    interrupt();
                                }
                            }
                        }.start();
                    }

                    @Override
                    public void onError(String msg) {
                        new MaterialDialog.Builder(MainActivity.this)
                                .title(R.string.restore_error)
                                .positiveText(R.string.ok)
                                .content(msg)
                                .onPositive(new MaterialDialog.SingleButtonCallback() {
                                    @Override
                                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                        dialog.dismiss();
                                    }
                                })
                                .show();
                    }
                }
        ).show(getSupportFragmentManager(), "");
    }

    private void backupData() {
        SaveDialog.newInstance(
                R.string.backup,
                "mynotes",
                App.BACKUP_EXTENSION,
                new SaveDialog.SaveListener() {
                    @Override
                    public void onSelect(final String path) {
                        new Thread() {
                            @Override
                            public void run() {
                                try {
                                    saveBackupFile(path);

                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            new MaterialDialog.Builder(MainActivity.this)
                                                    .title(R.string.backup)
                                                    .positiveText(R.string.ok)
                                                    .content(getString(R.string.backup_saved, path))
                                                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                                                        @Override
                                                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                                            dialog.dismiss();
                                                        }
                                                    })
                                                    .show();
                                        }
                                    });
                                } catch (final Exception e) {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            new MaterialDialog.Builder(MainActivity.this)
                                                    .title(R.string.backup_error)
                                                    .positiveText(R.string.ok)
                                                    .content(e.getMessage())
                                                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                                                        @Override
                                                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                                            dialog.dismiss();
                                                        }
                                                    })
                                                    .show();
                                        }
                                    });
                                } finally {
                                    interrupt();
                                }
                            }
                        }.start();
                    }

                    @Override
                    public void onError() {

                    }

                    @Override
                    public void onCancel() {

                    }
                }
        ).show(getSupportFragmentManager(), "");
    }

    private void readBackupFile(String path) throws Exception {
        DataInputStream dis = new DataInputStream(new FileInputStream(path));
        byte[] backup_data = new byte[dis.available()];
        dis.readFully(backup_data);
        JSONArray json = new JSONArray(new String(backup_data));
        dis.close();

        Controller.instance.readBackup(json);
    }

    private void saveBackupFile(String path) throws Exception {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(path);
            fos.write("[".getBytes("UTF-8"));
            Controller.instance.writeBackup(fos);
            fos.write("]".getBytes("UTF-8"));
            fos.flush();
        } finally {
            if (fos != null) fos.close();
        }
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST);
    }

    private void displayPermissionError() {
        new MaterialDialog.Builder(this)
                .title(R.string.permission_error)
                .content(R.string.permission_error_desc)
                .negativeText(R.string.request)
                .positiveText(R.string.continue_anyway)
                .negativeColor(ContextCompat.getColor(this, R.color.secondary_text))
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                        permissionNotGranted = false;
                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                        requestPermission();
                    }
                })
                .show();
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        if (permissionNotGranted) {
            permissionNotGranted = false;
            displayPermissionError();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (Manifest.permission.WRITE_EXTERNAL_STORAGE.equals(permissions[0])) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getApplicationContext(), R.string.permission_granted, Toast.LENGTH_SHORT).show();
            } else {
                permissionNotGranted = true;
            }
        }
    }
}
