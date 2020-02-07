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
        // Writes data to file expenses.txt when submit button is pressed
        mSubmitButton.setOnClickListener(v -> {
            f = new File(context.getFilesDir(), "BudgetData");
            if(!f.exists()) {
                if(!f.mkdir()) Snackbar.make(findViewById(android.R.id.content),"Creating directory BudgetData failed",Snackbar.LENGTH_LONG ).show();
            }
            String data = "DATE=" + mDate.getText() + ",CATEGORY=" + mCategory.getText() + ",COST=" + mCost.getText() +"\n";
            try{
                File gpxfile = new File(f, FILENAME);
                FileWriter writer = new FileWriter(gpxfile,true);
                writer.append(data);
                writer.flush();
                writer.close();
                //TODO: Notify adapter to update
            }
            catch (Exception e){
                Snackbar.make(findViewById(android.R.id.content), "weewer1", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
            finally {
                //TODO: Send back to main activity
            }

        });

        test.setOnClickListener(view -> {
            f = new File(context.getFilesDir(), "BudgetData");
            dataFile = new File(f,Constant.FILE_NAME);
            TextView testBox = findViewById(R.id.testBox);
            if(!dataFile.exists()){
                Snackbar.make(findViewById(android.R.id.content), "no file exists", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
            else{
                try {
                    BufferedReader reader = new BufferedReader(new FileReader(dataFile));
                    String fileData;
                    String date, category, cost;
                    StringBuilder sb = new StringBuilder();
                    int i = 1;
                    // Show all data in form of "#. Date=DATE Category=CATEGORY Cost=COST"
                    while ((fileData = reader.readLine()) != null) {
                        date = fileData.substring(fileData.indexOf("DATE=")+5,fileData.indexOf(",CATE"));
                        category = fileData.substring(fileData.indexOf("CATEGORY=")+9,fileData.indexOf(",COST="));
                        cost = fileData.substring(fileData.indexOf("COST=")+5);
                        sb.append(i+". Date="+date+" Category="+category+" Cost="+cost+"\n");
                        i++;
                    }
                    testBox.setText(sb.toString());
                }catch (Exception e){
                    Log.e("DEV","Error boy", e);
                    Snackbar.make(findViewById(android.R.id.content), e.toString(), Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    Log.e("DEV","Error boy", e);
                }

            }
        });

    }

    @Override
    public void onBackPressed(){
        mRevealAnimation.unRevealActivity();

    }

}
