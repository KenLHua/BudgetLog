package com.kentito.ken.budgetlog;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import android.view.View;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;


import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;

import org.json.JSONArray;

import java.io.File;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private LinearLayoutManager layoutManager;
    private File dir;
    private File dataFile;
    private ArrayList<String> expenseData;

    Context context;
    TextView testBox;
    TextView noDataText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            fab.hide();
            startRevealActivity(view);
        });



        testBox = findViewById(R.id.mainTest);
        noDataText = findViewById(R.id.no_data_text);
        context = MainActivity.this;





        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        JSONArray arr = DataUtils.getInstance().getEntries();
        if(arr.length() == 0){
            noDataText.setVisibility(View.VISIBLE);
        }

        recyclerView = findViewById(R.id.expense_list);


        //Loading data into ArrayList expenseData
        layoutManager = new LinearLayoutManager(this);
        mAdapter = new MyAdapter(arr);
        recyclerView.addOnItemTouchListener( new RecyclerView.SimpleOnItemTouchListener() {

                                             });

        SlideInUpAnimator anim = new SlideInUpAnimator();
        anim.setInterpolator( new DecelerateInterpolator());
        anim.setAddDuration(100);
        recyclerView.setItemAnimator(anim);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                if (dy >0){
                    fab.hide();
                }
                else{
                    fab.show();
                }
                super.onScrolled(recyclerView, dx, dy);
            }
        });


    }

    private void startRevealActivity(View v) {
        //calculates the center of the View v you are passing
        int revealX = (int) (v.getX() + v.getWidth() / 2);
        int revealY = (int) (v.getY() + v.getHeight() / 2);

        //create an intent, that launches the second activity and pass the x and y coordinates
        Intent intent = new Intent(this, NewExpense.class);
        intent.putExtra(RevealAnimation.EXTRA_CIRCULAR_REVEAL_X, revealX);
        intent.putExtra(RevealAnimation.EXTRA_CIRCULAR_REVEAL_Y, revealY);

        //just start the activity as an shared transition, but set the options bundle to null
        ActivityCompat.startActivity(this, intent, null);

        //to prevent strange behaviours override the pending transitions
        overridePendingTransition(0, 0);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        else if (id == R.id.clear_all) {
            // Prompt user if they really want to delete data
            new AlertDialog.Builder(context).setTitle("Confirm Data Deletion").setMessage("Are you sure?")
                    // Should be (Confirm+ | Cancel-) but it is (-, +), so just switch them
                    .setPositiveButton("Cancel", (dialog, which) -> dialog.cancel())
                    .setNegativeButton("Confirm", ((dialog, which) -> {

                        JSONArray jsonArray = new JSONArray();
                        ((MyAdapter) mAdapter).setDataset(jsonArray);
                        DataUtils.getInstance().setEntries(jsonArray);
                        synchronized(recyclerView) {
                            recyclerView.notify();
                        }
                        TextView noDataText = findViewById(R.id.no_data_text);
                        noDataText.setVisibility(View.VISIBLE);

                        File f = new File(context.getFilesDir(), Constant.SUB_FOLDER_BUDGET_DATA);
                        File dataFile = new File(f, Constant.FILE_NAME);
                        if(dataFile.exists())
                            if(dataFile.delete()) Snackbar.make(findViewById(android.R.id.content), "Success : Saved data has been deleted!", Snackbar.LENGTH_LONG).show();
                            else Snackbar.make(findViewById(android.R.id.content), "Error : Saved data has not been deleted!", Snackbar.LENGTH_LONG).show();
                    }
                    )).show();



        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onResume() {
        if(DataUtils.getInstance().isRefreshRequired()){
            ((MyAdapter) mAdapter).setDataset(DataUtils.getInstance().getEntries());
            if(DataUtils.getInstance().getEntries().length() != 0){
                noDataText.setVisibility(View.INVISIBLE);
            }

            synchronized(recyclerView) {
                recyclerView.notify();
            }
        }
        super.onResume();


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.show();
        fab.setEnabled(true);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        }

        DrawerLayout drawer =  findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
