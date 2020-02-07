package com.kentito.ken.budgetlog;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import android.util.Log;
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


import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private File dir;
    private File dataFile;
    private ArrayList<String> expenseData;

    private Context context = MainActivity.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ViewGroup root = findViewById(R.id.scene_root);


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            startRevealActivity(view);
            fab.setEnabled(false);
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        recyclerView = findViewById(R.id.expense_list);
        layoutManager = new LinearLayoutManager(this);
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


        //Loading data into ArrayList expenseData

        dir = new File(context.getFilesDir(), "BudgetData");
        dataFile = new File(dir, Constant.FILE_NAME);
        TextView testBox = findViewById(R.id.mainTest);
        if (!dataFile.exists()) {
            Snackbar.make(findViewById(android.R.id.content), "no file exists", Snackbar.LENGTH_LONG).setAction("Action", null).show();
        } else {
            try {
                BufferedReader reader = new BufferedReader(new FileReader(dataFile));
                String fileData;
                String date, category, cost;
                StringBuilder sb = new StringBuilder();
                expenseData = new ArrayList<>();
                // Show all data in form of "#. Date=DATE Category=CATEGORY Cost=COST"
                while ((fileData = reader.readLine()) != null) {
                    date = fileData.substring(fileData.indexOf("DATE=") + 5, fileData.indexOf(",CATE"));
                    category = fileData.substring(fileData.indexOf("CATEGORY=") + 9, fileData.indexOf(",COST="));
                    cost = fileData.substring(fileData.indexOf("COST=") + 5);
                    sb.append(date + "," + category + "," + cost);
                    expenseData.add(sb.toString());
                    sb.setLength(0);

                }
                testBox.setText(""+expenseData.size());
            } catch (Exception e) {
                Log.e("DEV", "Error boy", e);
                Snackbar.make(findViewById(android.R.id.content), e.toString(), Snackbar.LENGTH_LONG).setAction("Action", null).show();
                Log.e("DEV", "Error boy", e);
            }
        }
        mAdapter = new MyAdapter(expenseData.toArray(new String[0]));
        recyclerView.setAdapter(mAdapter);
    }


        // MyAdapter needs a dataset to work
        //mAdapter = new MyAdapter()

    // Todo: Use recycler adapter
    private void inflateExpenseView(){

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
        super.onResume();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setEnabled(true);
    }


    @SuppressWarnings("StatementWithEmptyBody")
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
