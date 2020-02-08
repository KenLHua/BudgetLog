package com.kentito.ken.budgetlog;


import android.content.Context;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONObject;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

public class NewExpense extends AppCompatActivity{
    RevealAnimation mRevealAnimation;
    File dir;
    File dataFile;
    Context context = NewExpense.this;
    Button test;
    String FILENAME = "expenses.txt";
    JSONObject entry = new JSONObject();
    JSONArray entries;


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

        // Create subfolder if it didn't exist
        dir = new File(context.getFilesDir(), Constant.SUB_FOLDER_BUDGET_DATA);
        if(!dir.exists()) {
            if(!dir.mkdir()) Snackbar.make(findViewById(android.R.id.content),"Creating directory BudgetData failed",Snackbar.LENGTH_LONG ).show();
        }

        // Attempt to load previous data
        entries = DataUtils.getInstance().getEntries();


        mSubmitButton.setOnClickListener(v -> {
            try {
                entry.put("date", mDate.getText());
                entry.put("category", mCategory.getText());
                entry.put("cost", mCost.getText());
                entries.put(entry);
            }catch (Exception e){Log.e("JSON", e.toString(), e);}
            DataUtils.getInstance().saveEntries();


        });
        test.setOnClickListener(view -> {
            try {
                JSONObject iter;
                StringBuilder sb = new StringBuilder();
                TextView testBox = findViewById(R.id.testBox);

                for(int i = 0; i < entries.length(); i++) {
                    iter = entries.getJSONObject(i);
                    sb.append("Date "+ iter.get(Constant.JSON_DATE) + " Category " + iter.get(Constant.JSON_CATEGORY) + " Cost " + iter.get(Constant.JSON_COST) +'\n');
                }
                testBox.setText(sb.toString());
            }catch (Exception e){
                Log.e("DEV","Error boy", e);
                Snackbar.make(findViewById(android.R.id.content), e.toString(), Snackbar.LENGTH_LONG).setAction("Action", null).show(); }

        });
    }

    @Override
    public void onBackPressed(){
        mRevealAnimation.unRevealActivity();

    }

}
