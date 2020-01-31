package com.kentito.ken.budgetlog;


import androidx.appcompat.app.ActionBar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.AppCompatActivity;

public class NewExpense extends AppCompatActivity{
    RevealAnimation mRevealAnimation;
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

        Button mSubmitButton = findViewById(R.id.submit);
        mSubmitButton.setOnClickListener(v -> {
            Snackbar.make(findViewById(android.R.id.content), "Date =" + mDate.getText() + " Category = " + mCategory.getText() + " Cost = " + mCost.getText(), Snackbar.LENGTH_LONG).setAction("Action", null).show();
            //TODO: Add sending intent

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

}
