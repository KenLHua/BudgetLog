package com.kentito.ken.budgetlog;


import androidx.appcompat.app.ActionBar;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

public class NewExpense extends AppCompatActivity{
    RevealAnimation mRevealAnimation;
    File f;
    File dataFile;
    Context context = NewExpense.this;
    Button test;
    String FILENAME = "expenses.txt";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_expense);

        RelativeLayout rootLayout = findViewById(R.id.root_layout);
        mRevealAnimation = new RevealAnimation(rootLayout,this.getIntent(),this);

        TextView mDate = findViewById(R.id.date);
        TextView mCategory = findViewById(R.id.category);
        TextView mCost = findViewById(R.id.cost);

        // Grabbing current date
        mDate.setText(DateUtils.getInstance().getTime());
        mCategory.setText("CategoryTest1");
        mCost.setText("CostTest1");

        Button mSubmitButton = findViewById(R.id.submit);
        test = findViewById(R.id.test);

        // Data saved as __________\n
        mSubmitButton.setOnClickListener(v -> {
            f = new File(context.getFilesDir(), "BudgetData");
            if(!f.exists()) {
                if(!f.mkdir()) Snackbar.make(findViewById(android.R.id.content),"Creating directory BudgetData failed",Snackbar.LENGTH_LONG ).show();
            }
            String data = "Date =" + mDate.getText() + " Category = " + mCategory.getText() + " Cost = " + mCost.getText() +"\n";
            try{
                File gpxfile = new File(f, FILENAME);
                FileWriter writer = new FileWriter(gpxfile,true);
                writer.append(data);
                writer.flush();
                writer.close();
            }
            catch (Exception e){
                Snackbar.make(findViewById(android.R.id.content), "weewer1", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
            //System.out.println("Files : " + files.length);
            //TODO: Add sending intent

        });

        test.setOnClickListener(view -> {
            f = new File(context.getFilesDir(), "BudgetData");
            dataFile = new File(f,Constant.FILE_NAME);
            if(!dataFile.exists()){
                Snackbar.make(findViewById(android.R.id.content), "no file exists", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
            else{
                try {
                    printFile(f, FILENAME);
                    //Snackbar.make(findViewById(android.R.id.content), fileData, Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }catch (Exception e){
                    Snackbar.make(findViewById(android.R.id.content), e.toString(), Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    Log.e("DEV","Error boy", e);
                }

            }
        });

    }
    @Override
    // Creates the checkmark
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.check_done, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Snackbar.make(findViewById(android.R.id.content)
                , "Pressed", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
        return true;
    }
    @Override
    public void onBackPressed(){
        mRevealAnimation.unRevealActivity();

    }
    private void printFile(File dir, String fileName){
        try {
            File file = new File(dir, fileName);
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String fileData;
            while ((fileData = reader.readLine()) != null) {
                Log.i("Ken123", fileData);
            }
        }catch (Exception e){
            Log.e("IO Exception", e.toString());
        }

    }

}
