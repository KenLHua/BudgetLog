package com.kentito.ken.budgetlog;


import androidx.appcompat.app.ActionBar;
import android.os.Bundle;


import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.AppCompatActivity;

public class NewExpense extends AppCompatActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_expense);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar =  getSupportActionBar();
        if (actionBar != null)
            actionBar.setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(view -> onBackPressed());

    }

}
