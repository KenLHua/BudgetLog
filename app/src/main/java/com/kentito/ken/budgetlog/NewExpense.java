package com.kentito.ken.budgetlog;


import android.content.Context;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.WindowManager;
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
    Button mSubmitButton;


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
        mCategory.setText("");
        mCost.setText("");

        mSubmitButton = findViewById(R.id.submit);
        test = findViewById(R.id.test);

        // Create subfolder if it didn't exist
        dir = new File(context.getFilesDir(), Constant.SUB_FOLDER_BUDGET_DATA);
        if(!dir.exists()) {
            if(!dir.mkdir()) Snackbar.make(findViewById(android.R.id.content),"Creating directory BudgetData failed",Snackbar.LENGTH_LONG ).show();
        }

        // Attempt to load previous data
        entries = DataUtils.getInstance().getEntries();


        mSubmitButton.setOnClickListener(v -> {
            // Disable user touches as the activity disappears
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            try {
                entry.put("date", mDate.getText());
                entry.put("category", mCategory.getText());
                entry.put("cost", mCost.getText());
                entries.put(entry);
                DataUtils.getInstance().setEntries(entries);
                DataUtils.getInstance().setRefreshRequired(true);
                mRevealAnimation.unRevealActivity();

            }catch (Exception e){Log.e("JSON", e.toString(), e);}


        });
        test.setOnClickListener(view -> {
            try {
                JSONObject iter;
                StringBuilder sb = new StringBuilder();
                TextView testBox = findViewById(R.id.testBox);

                for(int i = 0; i < entries.length(); i++) {
                    iter = entries.getJSONObject(i);
                    sb.append("Date ").append(iter.get(Constant.JSON_DATE)).append(" Category ").append(iter.get(Constant.JSON_CATEGORY)).append(" Cost ").append(iter.get(Constant.JSON_COST)).append('\n');
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
    @Override
    public void onStop() {
        DataUtils.getInstance().saveEntries();
        super.onStop();
    }

}
