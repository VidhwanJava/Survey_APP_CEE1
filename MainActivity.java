package com.threefoolz.survey_revision2;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import butterknife.BindView;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.AuthUI.IdpConfig;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;

import com.google.firebase.auth.FirebaseAuth;
import static xdroid.toaster.Toaster.toast;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final int RC_SIGN_IN = 100;
    private static final String TAG = "Mainactivity";
    @BindView(R.id.root) View mRootView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }
    @NonNull
    public static Intent createIntent(@NonNull Context context) {
        return new Intent(context, MainActivity.class);
    }

    public void signIn(View view) {
        AuthUI.SignInIntentBuilder builder =  AuthUI.getInstance().createSignInIntentBuilder()
                .setAvailableProviders(getSelectedProviders());



        startActivityForResult(builder.build(), RC_SIGN_IN);
    }
    private List<IdpConfig> getSelectedProviders() {
        List<IdpConfig> selectedProviders = new ArrayList<>();



        selectedProviders.add(new IdpConfig.PhoneBuilder().build());




        return selectedProviders;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            handleSignInResponse(resultCode, data);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            startSignedInActivity(null);
            finish();
        }
    }

    private void handleSignInResponse(int resultCode, @Nullable Intent data) {
        IdpResponse response = IdpResponse.fromResultIntent(data);

        // Successfully signed in
        if (resultCode == RESULT_OK) {
            startSignedInActivity(response);
            finish();
        } else {
            // Sign in failed
            if (response == null) {
                // User pressed back button
                toast(R.string.sign_in_cancelled);
                return;
            }

            if (response.getError().getErrorCode() == ErrorCodes.NO_NETWORK) {
                toast(R.string.no_internet_connection);
                return;
            }

            toast(R.string.unknown_error);
            Log.e(TAG, "Sign-in error: ", response.getError());
        }
    }

    private void startSignedInActivity(@Nullable IdpResponse response) {
        startActivity(SignedInActivity.createIntent(this, response));
    }

}
