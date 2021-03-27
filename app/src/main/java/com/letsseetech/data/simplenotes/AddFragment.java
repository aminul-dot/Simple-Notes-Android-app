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

import com.google.android.gms.auth.api.signin.GoogleSignInClient;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddFragment extends Fragment {
    EditText title, description;
    Toolbar toolbar;
    GoogleSignInClient mGoogleSignInClient;
    Button addNote;
    ImageView Profile;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add, container, false);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        DrawerLayout drawerLayout = view.findViewById(R.id.drawer_layout);
        toolbar = view.findViewById(R.id.toolbar);

        title = view.findViewById(R.id.title);
        description = view.findViewById(R.id.description);

        String userId = getArguments().getString("userId");

        //replacing the nevigation icon to back icon
        toolbar.setNavigationIcon(R.drawable.back);
        //On pressing back button
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // No input it will  back to homefragment
                if (TextUtils.isEmpty(title.getText().toString()) && TextUtils.isEmpty(description.getText().toString())) {
                    Toast.makeText(getActivity(), "No Input, Discarded", Toast.LENGTH_LONG).show();
                    FragmentManager fragmentManager = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    HomeFragment sampleFragment = new HomeFragment();
                    fragmentTransaction.replace(R.id.fragmentContainer, sampleFragment);
                    fragmentTransaction.commit();

                }
                // input provided thn it will save data to database and open homefragment
                if (!TextUtils.isEmpty(title.getText().toString()) || !TextUtils.isEmpty(description.getText().toString())) {
                    DatabaseClass db = new DatabaseClass(getContext());
                    db.addNotes(title.getText().toString(), description.getText().toString(), userId);
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