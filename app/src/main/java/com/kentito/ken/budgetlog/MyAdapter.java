package com.kentito.ken.budgetlog;

import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder>{
    private JSONArray mDataSet;
    private RecyclerViewClickListener mListener;

    MyAdapter(JSONArray myDataSet, RecyclerViewClickListener listener) {
        mDataSet = myDataSet;
        mListener = listener; }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cardview, parent, false);
        return new MyViewHolder(v, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        try {
            JSONObject js = (JSONObject) mDataSet.get(position);
            holder.bind( (CharSequence) js.get(Constant.JSON_DATE),
                    (CharSequence) js.get(Constant.JSON_CATEGORY), (CharSequence) js.get(Constant.JSON_COST), (CharSequence) js.get(Constant.JSON_NOTE));
        } catch (JSONException e) {
            Log.e("Adapter", e.toString(), e);
        }
    }

    @Override
    public int getItemCount() {
        return mDataSet.length();
    }

    //Todo: Add function for add, remove instead of refresh all data
    void setDataset(JSONArray mDataSet) {
        this.mDataSet = mDataSet;
        this.notifyDataSetChanged();

    }
    public JSONArray getDataSet(){
        return mDataSet;
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener {
        TextView date, cost, category, note, note_expanded;
        CardView cv;
        View v;
        private RecyclerViewClickListener mViewListener;

        MyViewHolder(View v, RecyclerViewClickListener listener){
            super(v);
            this.v = v;
            mViewListener = listener;

            cv = v.findViewById(R.id.item_card);
            cv.setOnClickListener(this);
            cv.setOnCreateContextMenuListener(this);

            date = v.findViewById(R.id.date);
            cost = v.findViewById(R.id.cost);
            category = v.findViewById(R.id.category);
            note = v.findViewById(R.id.note);

        }

        void bind(CharSequence sDate, CharSequence sCategory, CharSequence sCost, CharSequence sNote){
            date.setText(sDate);
            category.setText(sCategory);
            cost.setText(sCost);
            note.setText(sNote);
        }

        @Override
        public void onClick(View v) {
            mViewListener.onClick(v, getAdapterPosition());
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            MenuItem Edit = menu.add(getAdapterPosition(), 1, 1, "Edit");
            MenuItem Delete = menu.add(getAdapterPosition(), 100, 2, "Delete");//groupId, itemId, order, title


        }

    }




}
