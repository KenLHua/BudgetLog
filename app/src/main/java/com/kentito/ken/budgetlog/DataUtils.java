package com.kentito.ken.budgetlog;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;

import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;

class DataUtils {
    private JSONArray entries;
    private static DataUtils instance;
    private boolean refreshRequired;
    private File dir ;
    private static String[] categories;



    private DataUtils() {

        dir = new File(MyApplication.getContext().getFilesDir(), Constant.SUB_FOLDER_BUDGET_DATA);
        loadEntries(dir);
        refreshRequired = false;
    }

    JSONArray getEntries() {
        return entries;
    }

    static DataUtils getInstance() {
        if (instance == null) {
            instance = new DataUtils();
        }
        Log.d("utils", instance.dir.toString());
        return instance;
    }


    private JSONArray loadEntries(File dir) {
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
    String[] loadCategories(){
        Map<String, Integer> categoryMap = new HashMap<String,Integer>();
        for(int i =0; i < entries.length(); i++){
            try {
                JSONObject entry = (JSONObject) entries.get(i);
                String category = (String) entry.get("category");
                if(!categoryMap.containsKey(category)){
                    categoryMap.put(category,1);
                }
            }
            catch(Exception e){}
        }
        categories = categoryMap.keySet().toArray(new String[0]);
        return categories;
    }
    String[] getCategories(){
        return categories;
    }

    void saveEntries() {
        try {
            File gpxfile = new File(dir, Constant.FILE_NAME);
            FileWriter writer = new FileWriter(gpxfile);
            writer.append(entries.toString());
            writer.flush();
            writer.close();
            refreshRequired = true;
        } catch (Exception e) {
            Log.e("DataUtils Saving", e.toString(), e);
        }
    }

    void setEntries(JSONArray entries) {
        this.entries = entries;
    }

    void removeEntry (int position){
        entries.remove(position);
        refreshRequired = true;

    }

    boolean isRefreshRequired() {
        if (refreshRequired){
            refreshRequired = false;
            return true;
        }
        return false;
    }
    void setRefreshRequired(boolean b){
        refreshRequired = b;
    }
}
