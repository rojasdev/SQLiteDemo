package com.demo.sqlitedemo;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;

import androidx.fragment.app.DialogFragment;

public class FragmentDialogBasic extends DialogFragment {
    private DialogListener dialogDismissListener;

    int ids;

    public FragmentDialogBasic(){
        // Required empty public constructor
    }

    public void setDialogDismissListener(DialogListener listener) {
        this.dialogDismissListener = listener;
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Bundle args = getArguments();
        if (args != null && args.containsKey("idReference")) {
            ids = args.getInt("idReference");
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Remove Item")
                .setMessage("Press okay to confirm.")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // You can add code here to handle the OK button click
                        removeItemInformation(ids);
                        dismiss();
                        if (dialogDismissListener != null) {
                            dialogDismissListener.onDialogDismissed();
                        }
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // You can add code here to handle the Cancel button click
                    }
                });

        return builder.create();
    }

    private void removeItemInformation(int id){
        DatabaseHelper dbHelper = new DatabaseHelper(getContext());
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Step 2: Define the table and row to delete
        String tableName = "TableItems";
        String whereClause = "id = ?";
        String[] whereArgs = new String[]{String.valueOf(id)}; // Replace 'idToDelete' with the actual ID you want to delete

        // Step 3: Delete the record
        int rowsDeleted = db.delete(tableName, whereClause, whereArgs);

        // Check if the record was successfully deleted
        if (rowsDeleted > 0) {
            Log.d("SQLite", "Record deleted successfully.");
        } else {
            Log.e("SQLite", "Failed to delete record." + ids);
        }

        // Step 4: Close the database
        db.close();
    }
}