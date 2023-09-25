package com.demo.sqlitedemo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class ItemFragment extends Fragment {
    private ImageView imageView;
    private boolean isImage1 = true; // Used to track which image is currently displayed

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.item_child_layout, container, false);

        // Handle the back button click
        ImageButton backButton = view.findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Use popBackStack to navigate back to the previous fragment
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                if (fragmentManager != null) {
                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    fragmentManager.popBackStack();
                    transaction.commit();
                }
            }
        });
        /// logical side of the like / smile image add logic to count
        imageView = view.findViewById(R.id.imageLikeButton);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check which image is currently displayed and set the other image
                if (isImage1) {
                    imageView.setImageResource(R.drawable.ic_heart_smile); // Change to the new image
                } else {
                    imageView.setImageResource(R.drawable.ic_heart_outline); // Change back to the initial image
                }

                // Toggle the flag to track the current image
                isImage1 = !isImage1;
            }
        });
        return view;
    }

}
