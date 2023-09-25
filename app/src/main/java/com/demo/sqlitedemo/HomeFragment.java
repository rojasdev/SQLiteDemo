package com.demo.sqlitedemo;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment implements DialogListener, ListViewCustomAdapter.OnItemClickListener, ListViewCustomAdapter.OnItemLongClickListener{
    private FloatingActionButton fabMenu;
    private ListView listViewCustom;
    View view;
    ListViewCustomAdapter customAdapter;
    ArrayList<ItemData> arrayList;
    private SwipeRefreshLayout swipeRefreshLayout;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);

        listViewCustom = view.findViewById(R.id.content_list);

        //ArrayList<ItemData> arrayList;
        arrayList = getDataList(); // read from database

        customAdapter = new ListViewCustomAdapter(getContext(), arrayList, this, this);
        listViewCustom.setAdapter(customAdapter);

        fabMenu = view.findViewById(R.id.fab_menu);

        fabMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });

        // Set up the SwipeRefreshLayout
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // This code will be executed when the user pulls down to refresh
                // You can perform your data fetching or refreshing logic here
                refreshFragment();

            }
        });

        return view;
    }

    @Override
    public void onItemClick(Object item) {
        int id=0;
        // Check if the object is of the correct type (e.g., MyItem)
        if (item instanceof ItemData) {
            ItemData myItem = (ItemData) item; // Cast the Object to your data class
            // Now you can access the values of myItem
            id = myItem.getItemId();
        }

        // Create a ContentFragment and pass the selected content
        ItemFragment contentFragment = new ItemFragment();
        Bundle args = new Bundle();
        args.putString("content", String.valueOf(id));
        //contentFragment.setArguments(args);

        // Replace the current child fragment with the ContentFragment
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_right,R.anim.slide_in_right, R.anim.slide_out_right);
        transaction.replace(R.id.fragment_container, contentFragment);
        transaction.addToBackStack(null); // Optional: Add to back stack for navigation
        transaction.commit();
    }

    public interface ListViewItemClickListener {
        void onListViewItemClick(int position, String item);
    }

    @Override
    public void onItemLongClick(Object item) {
        int id=0;
        // Check if the object is of the correct type (e.g., MyItem)
        if (item instanceof ItemData) {
            ItemData myItem = (ItemData) item; // Cast the Object to your data class

            // Now you can access the values of myItem
            String itemName = myItem.getItemName(); // Replace with your data access method
            String itemDescription = myItem.getItemDescription();
            id = myItem.getItemId();

            // Use itemName and itemValue as needed
            Log.v("LongClick", "Long clicked item at position " + id);
        }
        FragmentDialogBasic dialogFragment = new FragmentDialogBasic();
        Bundle args = new Bundle();
        args.putInt("idReference", id);
        dialogFragment.setArguments(args);
        dialogFragment.setDialogDismissListener(this);

        //dialogFragment.setDialogDismissListener(this); // Set this fragment as the listener
        dialogFragment.show(getParentFragmentManager(), "MyDialogFragment");
    }


    private void showDialog() {
        // Create and show the AlertDialog
        FragmentCustomDialog dialogFragment = new FragmentCustomDialog();
        dialogFragment.setDialogDismissListener(this); // Set this fragment as the listener
        dialogFragment.show(getParentFragmentManager(), "MyDialogFragment");
    }

    public ArrayList<ItemData> getDataList() {
        ArrayList<ItemData> arrayList = new ArrayList<>();
        //db starts here

        DatabaseHelper dbHelper = new DatabaseHelper(getContext());
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] tblFields = {"id", "item_name", "item_date", "item_time", "item_quantity"};
        Cursor cursor = db.query("TableItems", tblFields, null, null, null, null, null);

        // initialize array and store items
        JSONArray jsonArray = new JSONArray();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("id", cursor.getLong(cursor.getColumnIndexOrThrow("id")));
                    jsonObject.put("item_name", cursor.getString(cursor.getColumnIndexOrThrow("item_name")));
                    jsonObject.put("item_date", cursor.getString(cursor.getColumnIndexOrThrow("item_date")));
                    jsonObject.put("item_time", cursor.getString(cursor.getColumnIndexOrThrow("item_time")));
                    jsonObject.put("item_quantity", cursor.getInt(cursor.getColumnIndexOrThrow("item_quantity")));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                jsonArray.put(jsonObject);
            }
            cursor.close();
        }
        // place items to array for listview
        try {
            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject jsonChildNode = jsonArray.getJSONObject(i);
                int id = jsonChildNode.optInt("id");
                String item = jsonChildNode.optString("item_name");
                String name = id + " - " + item + " - ";
                String date = jsonChildNode.optString("item_date");
                String time = jsonChildNode.optString("item_time");
                String quantity = jsonChildNode.optString("item_quantity");
                String description = date + " " + time + " " + "[" + quantity + "]";
                String image = "https://www.usls.edu.ph/uploads/icons/Affiliates/3.png";
                // concatinated values for demo purposes, can be broken down individually
                // image is dummy no database storage of value, can be customized if there is an upload
                arrayList.add(new ItemData(id, name, description, image));
            }
        }catch (Exception e) {
            //Toast.makeText(getApplicationContext(), "Error"+e.toString(), Toast.LENGTH_LONG).show();
        }
        return arrayList;
    }
    @Override
    public void onDialogDismissed() {
        refreshFragment();
    }
    private void refreshFragment() {
        // Implement your refresh logic here, such as reloading data in the fragment
        //Log.e("REFRESH","Reffff");
        ArrayList<ItemData> arrayList = new ArrayList<>();
        arrayList = getDataList();

        customAdapter = new ListViewCustomAdapter(requireContext(), arrayList, this, this);
        listViewCustom.setAdapter(customAdapter);

        // Stop the refresh animation
        swipeRefreshLayout.setRefreshing(false);
    }
    @Override
    public void onResume() {
        super.onResume();
        //
    }
}