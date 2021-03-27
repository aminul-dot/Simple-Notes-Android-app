package com.letsseetech.data.simplenotes;

import android.database.Cursor;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.CONNECTIVITY_SERVICE;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {
    Toolbar toolbar;
    Menu menu;
    NavigationView navigationView;
    DrawerLayout drawerLayout;
    TextView pro_name;
    RecyclerView recyclerView;
    data_adapter adapter;
    List<Data> notesList;
    DatabaseClass databaseClass;
    RelativeLayout reLayout;
    ImageView Profile;
    Uri personPhoto;
    ListView list_data;
    String personEmail;
    String personId;
    String personName;
    FloatingActionButton Add;

    GoogleSignInClient mGoogleSignInClient;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        reLayout = view.findViewById(R.id.layout_main);
        drawerLayout = view.findViewById(R.id.drawer_layout);
        navigationView = view.findViewById(R.id.nav_view);
        toolbar = view.findViewById(R.id.toolbar);

        recyclerView = view.findViewById(R.id.recycler_view);
        View header = navigationView.getHeaderView(0);
        Profile = header.findViewById(R.id.profile);
        pro_name = header.findViewById(R.id.profilename);
        Add = view.findViewById(R.id.add);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        // For navigationView
        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new
                ActionBarDrawerToggle(getActivity(), drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_signout:
                        signOut();
                        break;
                }
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });
        navigationView.setCheckedItem(R.id.nav_home);

        notesList = new ArrayList<>();
        //google sign in
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("547045791264-dj9rqp6c4t1cksrt5bsaffha9f1lid14.apps.googleusercontent.com")
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getActivity());
        if (acct != null) {
            personName = acct.getDisplayName();
            String personGivenName = acct.getGivenName();
            String personFamilyName = acct.getFamilyName();
            personEmail = acct.getEmail();
            personId = acct.getId();
            personPhoto = acct.getPhotoUrl();
            //setting the profile photo and name
            Picasso.get().load(personPhoto).placeholder(R.drawable.profile).into(Profile);
            pro_name.setText(personName);
        }


        adapter = new data_adapter(getContext(), getActivity(), notesList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        // fetching all the data available in database with respective userId
        databaseClass = new DatabaseClass(getActivity());
        fetchAllNotesFromDatabase(personId);


        ItemTouchHelper helper = new ItemTouchHelper(callback);
        helper.attachToRecyclerView(recyclerView);

        //when we click the add button it will open another fragment called Addfragment
        Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("userId", personId);
                FragmentManager fragmentManager = getFragmentManager();
                AddFragment sampleFragment = new AddFragment();
                sampleFragment.setArguments(bundle);
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragmentContainer, sampleFragment);
                fragmentTransaction.commit();
            }
        });


        return view;
    }

    // for swiping to delete feature
    ItemTouchHelper.SimpleCallback callback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

            int position = viewHolder.getAdapterPosition();
            Data item = adapter.getList().get(position);

            adapter.removeItem(viewHolder.getAdapterPosition());

            Snackbar snackbar = Snackbar.make(reLayout, "Item Deleted", Snackbar.LENGTH_LONG)
                    .setAction("UNDO", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            adapter.restoreItem(item, position);
                            recyclerView.scrollToPosition(position);
                        }
                    }).addCallback(new BaseTransientBottomBar.BaseCallback<Snackbar>() {
                        @Override
                        public void onDismissed(Snackbar transientBottomBar, int event) {
                            super.onDismissed(transientBottomBar, event);

                            if (!(event == DISMISS_EVENT_ACTION)) {
                                DatabaseClass db = new DatabaseClass(getActivity());
                                db.deleteSingleItem(item.getId(), personId);
                            }


                        }
                    });

            snackbar.setActionTextColor(Color.YELLOW);
            snackbar.show();

        }
    };

    // fetching method
    void fetchAllNotesFromDatabase(String personId) {
        Cursor cursor = databaseClass.readAllData(personId);

        if (cursor.getCount() == 0) {
            Toast.makeText(getActivity(), "No Data to show", Toast.LENGTH_SHORT).show();
        } else {

            while (cursor.moveToNext()) {

                notesList.add(new Data(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3)));
            }
        }

    }

    //signout method
    private void signOut() {
        if (haveNetwork()) {
            mGoogleSignInClient.signOut()
                    .addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            sharedpreference sh = new sharedpreference();
                            sh.resetshred(getActivity());
                            Toast.makeText(getActivity(), "Sign Out successfully", Toast.LENGTH_LONG).show();
                            FragmentManager fragmentManager = getFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            GloginFragment sampleFragment = new GloginFragment();
                            fragmentTransaction.replace(R.id.fragmentContainer, sampleFragment);
                            fragmentTransaction.commit();
                        }
                    });
        } else {
            Toast.makeText(getContext(), "Internet is not Available", Toast.LENGTH_LONG).show();
        }

    }


    //checking a device is connected to internet or not.
    private boolean haveNetwork() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }
}
