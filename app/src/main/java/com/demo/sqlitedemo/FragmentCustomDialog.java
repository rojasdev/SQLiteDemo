package com.demo.sqlitedemo;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import java.util.Calendar;

public class FragmentCustomDialog extends DialogFragment {
    View dialogView;
    ImageView imageView;
    EditText etText, etDate, etTime;
    TextView tvQty;
    Button btnSave, btnAdd, btnMinus;
    int quantityCounter = 0;
    private DialogListener dialogDismissListener;

    public FragmentCustomDialog() {
        // Required empty public constructor
    }

    public void setDialogDismissListener(DialogListener listener) {
        this.dialogDismissListener = listener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        dialogView = inflater.inflate(R.layout.custom_dialog, null);

        // Set the background with rounded corners
        //dialogView.setBackgroundResource(R.drawable.rounded_corner_dialog_bg);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(dialogView);

        imageView = dialogView.findViewById(R.id.detailImageView);
        // Set the image resource
        imageView.setImageResource(R.drawable.baseline_data_exploration_24);

        etText = dialogView.findViewById(R.id.edit_text);
        etDate = dialogView.findViewById(R.id.editTextDate);
        etDate.setInputType(InputType.TYPE_NULL);
        etTime = dialogView.findViewById(R.id.editTextTime);
        etTime.setInputType(InputType.TYPE_NULL);
        tvQty = dialogView.findViewById(R.id.tv_qty);
        btnSave = dialogView.findViewById(R.id.btn_save);
        btnAdd = dialogView.findViewById(R.id.btn_plus);
        btnMinus = dialogView.findViewById(R.id.btn_minus);

        etText.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    // The EditText has gained focus
                    // Perform actions when the view gains focus

                } else {
                    // The EditText has lost focus
                    hideKeyboard();
                }
            }
        });
        etDate.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    // The EditText has gained focus
                    // Perform actions when the view gains focus
                    showDatePickerDialog();
                } else {
                    // The EditText has lost focus
                    // Perform actions when the view loses focus
                }
            }
        });
        etTime.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    // The EditText has gained focus
                    // Perform actions when the view gains focus
                    //showTimePickerDialog();
                    showCustomTimePickerDialog();
                } else {
                    // The EditText has lost focus
                    // Perform actions when the view loses focus
                }
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle button click, e.g., get text from editText
                String inputText = etText.getText().toString().trim();
                String inputDate = etDate.getText().toString().trim();
                String inputTime = etTime.getText().toString().trim();
                int intQty = Integer.valueOf(tvQty.getText().toString().trim());

                // check if fields are not empty
                if (inputText.isEmpty()) {
                    // The EditText is not empty, perform the action here
                    etText.setError("This field is required");
                } else if (inputDate.isEmpty()) {
                    // The EditText is not empty, perform the action here
                    etDate.setError("This field is required");
                } else if (inputTime.isEmpty()) {
                    // The EditText is not empty, perform the action here
                    etTime.setError("This field is required");
                }else{
                    // Save data
                    saveItemInformation(inputText,inputDate,inputTime,intQty);
                    // Dismiss the dialog and notify the parent fragment
                    dismiss();
                    if (dialogDismissListener != null) {
                        dialogDismissListener.onDialogDismissed();
                    }
                }
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quantityCounter += 1;
                tvQty.setText(Integer.toString(quantityCounter));
            }
        });
        btnMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(quantityCounter != 0){
                    quantityCounter -= 1;
                    tvQty.setText(Integer.toString(quantityCounter));
                }
            }
        });

        return builder.create();
    }
    private void showDatePickerDialog() {
        // Get the current date
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                requireContext(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        // Update the EditText with the selected date
                        String selectedDate = (month + 1) + "/" + dayOfMonth + "/" + year;
                        etDate.setText(selectedDate);
                    }
                },
                // Initial date (you can set it to a default date if needed)
                year, month, dayOfMonth // Year, Month (0-based), Day
        );
        datePickerDialog.show();
    }

    private void showCustomTimePickerDialog() {
        // Create a custom dialog
        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_custom_time_picker);
        dialog.setCancelable(true);

        // Find and customize UI elements in your custom layout
        final NumberPicker hourPicker = dialog.findViewById(R.id.hourPicker);
        final NumberPicker minutePicker = dialog.findViewById(R.id.minutePicker);
        Button okButton = dialog.findViewById(R.id.okButton);
        Button cancelButton = dialog.findViewById(R.id.cancelButton);

        // Initialize NumberPicker values and settings
        hourPicker.setMinValue(0);
        hourPicker.setMaxValue(23);
        minutePicker.setMinValue(0);
        minutePicker.setMaxValue(59);

        // Set custom formatter to add leading zeros
        hourPicker.setFormatter(new NumberPicker.Formatter() {
            @Override
            public String format(int value) {
                return String.format("%02d", value);
            }
        });

        minutePicker.setFormatter(new NumberPicker.Formatter() {
            @Override
            public String format(int value) {
                return String.format("%02d", value);
            }
        });

        // Handle OK button click
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedHour = hourPicker.getValue();
                int selectedMinute = minutePicker.getValue();

                // Format the selected time as needed
                String selectedTime = String.format("%02d:%02d", selectedHour, selectedMinute);

                // Set the selected time to the EditText
                etTime.setText(selectedTime);

                dialog.dismiss();
            }
        });

        // Handle Cancel button click
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void saveItemInformation(String item_name, String item_date, String item_time, int item_quantity){
        DatabaseHelper dbHelper = new DatabaseHelper(getContext());
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("item_name", item_name);
        values.put("item_date", item_date);
        values.put("item_time", item_time);
        values.put("item_quantity", item_quantity);
        //Log.v("DB Values",item_name + item_date + item_time + item_quantity);
        long newRowId = db.insert("TableItems", null, values);
        // Step 4: Close the database
        db.close();
    }
    // Function to hide the keyboard
    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(etText.getWindowToken(), 0);
        }
    }
}
