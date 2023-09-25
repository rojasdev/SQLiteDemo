package com.demo.sqlitedemo;

import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ListViewCustomAdapter implements ListAdapter {
    ArrayList<ItemData> arrayList;
    Context context;
    ItemData itemData;
    public interface OnItemClickListener {
        void onItemClick(Object item);
    }
    public interface OnItemLongClickListener {
        void onItemLongClick(Object item);
    }
    private final OnItemClickListener itemClickListener;
    private final OnItemLongClickListener itemLongClickListener;
    public ListViewCustomAdapter(Context context, ArrayList<ItemData> arrayList, OnItemClickListener listener, OnItemLongClickListener listenerLong) {
        this.arrayList=arrayList;
        this.context=context;
        this.itemClickListener = listener;
        this.itemLongClickListener = listenerLong;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEnabled(int position) {
        return true;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        itemData = arrayList.get(position);
        if(convertView==null){
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            convertView=layoutInflater.inflate(R.layout.list_view_custom, null);

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (itemClickListener != null) {
                        itemClickListener.onItemClick(arrayList.get(position));
                    }
                    Log.v("Adapter Touichy","Ouch ouch!" + position);
                }
            });
            convertView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    if (itemLongClickListener != null) {
                        itemLongClickListener.onItemLongClick(arrayList.get(position));

                        return true; // Consume the long press even
                    }
                    return false;
                }
            });

            TextView listName = convertView.findViewById(R.id.list_name);
            TextView listDescription = convertView.findViewById(R.id.list_description);
            ImageView listImage = convertView.findViewById(R.id.list_image);
            listName.setText(itemData.ItemName);
            listDescription.setText(itemData.ItemDescription);
            Picasso.with(context)
                    .load(itemData.ItemImage)
                    .into(listImage);

        }
        return convertView;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        return arrayList.size();
    }


    @Override
    public boolean isEmpty() {
        return false;
    }
}
