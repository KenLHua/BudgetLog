package com.kentito.ken.budgetlog;

import android.content.Context;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.File;

import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;

public class DataUtils {
    private static JSONArray entries;
    private static DataUtils instance;
    private static Context c;
    private File dir ;

    private DataUtils() {
        dir = new File(c.getFilesDir(), Constant.SUB_FOLDER_BUDGET_DATA);
        loadEntries(dir);
    }

    public JSONArray getEntries() {
        return entries;
    }

    public static DataUtils getInstance() {
        if (instance == null) {
            instance = new DataUtils();
        }
        Log.d("utils", instance.dir.toString());
        return instance;
    }

    public static void setAppContext(Context context){
        if (c == null){
            c = context;
        }
    }

    public JSONArray loadEntries(File dir) {
        try {
            File gpxfile = new File(dir, Constant.FILE_NAME);
            StringBuilder sb = new StringBuilder();
            // If data was stored in a file, load it
            if (gpxfile.exists()) {
                BufferedReader br = new BufferedReader(new FileReader(new File(dir, Constant.FILE_NAME)));
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
                entries = new JSONArray(sb.toString());
                Log.d("Array", "old array created");
            } else {
                // No old data
                entries = new JSONArray();
                Log.d("Array", "new array created");
            }
        } catch (Exception e) { Log.e("JSON Loading", e.toString(), e); }
        return getEntries();
    }

    public void saveEntries() {
        try {
            File gpxfile = new File(dir, Constant.FILE_NAME);
            FileWriter writer = new FileWriter(gpxfile);
            writer.append(entries.toString());
            writer.flush();
            writer.close();
        } catch (Exception e) {}
    }
}
