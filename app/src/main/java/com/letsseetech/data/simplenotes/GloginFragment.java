package com.letsseetech.data.simplenotes;




import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import static android.content.ContentValues.TAG;
import static android.content.Context.CONNECTIVITY_SERVICE;
import static com.letsseetech.data.simplenotes.sharedpreference.getuserId;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GloginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GloginFragment extends Fragment {
    Toolbar toolbar;
    View view;
    Button GButton;
    int RC_SIGN_IN = 0;
    GoogleSignInClient mGoogleSignInClient;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_glogin, container, false);

        toolbar = view.findViewById(R.id.toolbar);
        //setting action bar
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        GButton = (Button) view.findViewById(R.id.btn);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("547045791264-dj9rqp6c4t1cksrt5bsaffha9f1lid14.apps.googleusercontent.com")
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);
        //Checking the user Id in sharedpreference if data available thn directly open Homefragment otherwise it will open Gloginfragment
        sharedpreference sh = new sharedpreference();
        String po = getuserId(getActivity());
        if (!po.equals("")) {
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            HomeFragment sampleFragment = new HomeFragment();
            fragmentTransaction.replace(R.id.fragmentContainer, sampleFragment);
            fragmentTransaction.commit();
        }
        //on pressng google signin button it will open homefragment once the user authenticated.
        GButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.btn:
                        signIn();
                        break;
                }

            }
        });

        return view;
    }

    //signin method
    private void signIn() {
        //here we are checking, whether a device is connected to internet or not
        if (haveNetwork()) {
            Intent signInIntent = mGoogleSignInClient.getSignInIntent();
            startActivityForResult(signInIntent, RC_SIGN_IN);
        } else
            Toast.makeText(getContext(), "Internet is not Available", Toast.LENGTH_LONG).show();

    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.

            GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getActivity());
            String personId = acct.getId();
            sharedpreference sh = new sharedpreference();
            sh.saveUserId(personId, getContext());
            //once the user is authenticated, it will open homefragment
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            HomeFragment sampleFragment = new HomeFragment();
            fragmentTransaction.replace(R.id.fragmentContainer, sampleFragment);
            fragmentTransaction.commit();
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
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
