package com.kentito.ken.budgetlog;

import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder>{
    private JSONArray mDataset;

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cardview, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        /*runEnterAnimation(holder.v, position);*/
        try {
            JSONObject js = (JSONObject) mDataset.get(position);
            holder.date.setText((CharSequence) js.get(Constant.JSON_DATE));
            holder.category.setText((CharSequence) js.get(Constant.JSON_CATEGORY));
            holder.cost.setText((CharSequence) js.get(Constant.JSON_COST));
        } catch (JSONException e) {
            Log.e("Adapter", e.toString(), e);
        }
    }

    @Override
    public int getItemCount() {
        return mDataset.length();
    }

    //Todo: Add function for add, remove instead of refresh all data
    void setDataset(JSONArray mDataset) {
        this.mDataset = mDataset;
        this.notifyDataSetChanged();

    }
    public JSONArray getDataset(){
        return mDataset;
    }

    static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView date, cost, category, note;
        View v;
        MyViewHolder(View v){
            super(v);
            this.v = v;
            date = v.findViewById(R.id.date);
            cost = v.findViewById(R.id.cost);
            category = v.findViewById(R.id.category);

        }
    }
    // Provide a suitable constructor (depends on the kind of dataset)
    MyAdapter(JSONArray myDataset) {
        mDataset = myDataset;
    }

    private void runEnterAnimation(View view, int position) {
        view.setTranslationY(Resources.getSystem().getDisplayMetrics().heightPixels);
        view.animate()
                .translationY(0)
                .setInterpolator(new DecelerateInterpolator(3.f))
                .setDuration(700)
                .start();
    }




}
