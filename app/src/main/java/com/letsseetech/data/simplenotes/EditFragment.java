package com.letsseetech.data.simplenotes;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditFragment extends Fragment {
    EditText title, description;
    Button updateNotes;
    Toolbar toolbar;
    ImageView Profile;
    String id;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit, container, false);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        DrawerLayout drawerLayout = view.findViewById(R.id.drawer_layout);
        toolbar = view.findViewById(R.id.toolbar);
        title = view.findViewById(R.id.title);
        description = view.findViewById(R.id.description);


        title.setText(getArguments().getString("title"));
        description.setText(getArguments().getString("description"));
        id = getArguments().getString("id");
        String userId = getArguments().getString("userId");
        //replacing the nevigation icon to back icon
        toolbar.setNavigationIcon(R.drawable.back);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //if no input thn it will delete the data
                if (TextUtils.isEmpty(title.getText().toString()) && TextUtils.isEmpty(description.getText().toString())) {
                    Toast.makeText(getActivity(), "Deleted ", Toast.LENGTH_LONG).show();
                    DatabaseClass db = new DatabaseClass(getActivity());
                    db.deleteSingleItem(id, userId);

                    // open Homefragment
                    FragmentManager fragmentManager = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    HomeFragment sampleFragment = new HomeFragment();
                    fragmentTransaction.replace(R.id.fragmentContainer, sampleFragment);
                    fragmentTransaction.commit();
                }

                //if ipun is updated thn it will update in database
                if (!TextUtils.isEmpty(title.getText().toString()) || !TextUtils.isEmpty(description.getText().toString())) {

                    DatabaseClass db = new DatabaseClass(getContext());
                    db.updateNotes(title.getText().toString(), description.getText().toString(), id);

                    FragmentManager fragmentManager = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    HomeFragment sampleFragment = new HomeFragment();
                    fragmentTransaction.replace(R.id.fragmentContainer, sampleFragment);
                    fragmentTransaction.commit();


                }

            }
        });


        return view;
    }
}