package com.kentito.ken.budgetlog;


import android.content.Context;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
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
        TextView mNote = findViewById(R.id.note);

        // Grabbing current date
        mDate.setText(DateUtils.getInstance().getTime());


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

                CharSequence cost = mCost.getText();
                if (cost.length() <= 1){
                    cost = "$0";
                }
                entry.put("cost", cost.subSequence(1,cost.length()));
                entry.put("note", mNote.getText());
                entries.put(entry);

                DataUtils.getInstance().setEntries(entries);
                DataUtils.getInstance().setRefreshRequired(true);
                mRevealAnimation.unRevealActivity();

            }catch (Exception e){Log.e("JSON", e.toString(), e);}


        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        AutoCompleteTextView mCategory = findViewById(R.id.category);
        Log.d("Debug", "Ken " + DataUtils.getInstance().getCategories().toString());
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_activated_1, DataUtils.getInstance().getCategories());
        mCategory.setAdapter(adapter);
        mCategory.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                mCategory.showDropDown();
            }
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
