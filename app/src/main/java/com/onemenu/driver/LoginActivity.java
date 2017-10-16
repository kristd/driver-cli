package com.onemenu.driver;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.app.Activity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.firebase.iid.FirebaseInstanceId;
import okhttp3.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends Activity implements LoaderCallbacks<Cursor> {
    private static String TAG = "LoginActivity";
    SharedPreferences loginPref;
    SharedPreferences.Editor loginEditor;
    //SharedPreferences loginPref = getSharedPreferences("LOGIN", Activity.MODE_PRIVATE);
    //SharedPreferences.Editor loginEditor = loginPref.edit();

    String notifyToken = "";

    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;
    private UserReLoginTask mReAuthTask = null;

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Init HttpClient
        HttpApi.init();

        //get notify token
        notifyToken = getNotifyToken();

        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        //populateAutoComplete();

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);

        //Try auto-login
        loginPref = getSharedPreferences("LOGIN", MODE_PRIVATE);
        loginEditor = loginPref.edit();
        if (loginPref != null) {
            Log.i(TAG, "AUTO LOGIN");
            autoLogin();
        }
    }

    private void autoLogin() {
        if (mReAuthTask != null) {
            return;
        }

        String token = loginPref.getString("token", "");
        if (token.equals("")) {
            mEmailView.requestFocus();
        } else {
            showProgress(true);
            mReAuthTask = new UserReLoginTask(token);
            mReAuthTask.execute((Void) null);
        }
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isPhoneNumberValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            mAuthTask = new UserLoginTask(email, password);
            mAuthTask.execute((Void) null);
        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 6;
    }

    private boolean isPhoneNumberValid(String phone) {
        int i = 0, j = 0;
        String numbers = "0123456789";

        for (i = 0; i < phone.length(); i++) {
            j = 0;

            for (j = 0; j < numbers.length(); j++) {
                if (phone.charAt(i) == numbers.charAt(j)) {
                    break;
                }
            }

            if (j == numbers.length()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
                int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

                mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                        show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                    }
                });

                mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                mProgressView.animate().setDuration(shortAnimTime).alpha(
                        show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                    }
                });
            } else {
                // The ViewPropertyAnimator APIs are not available, so simply show
                // and hide the relevant UI components.
                mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(LoginActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mEmailView.setAdapter(adapter);
    }


    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }

    private void initUser(JSONObject params) {
        JSONObject driver = params.getJSONObject("driver");
        String token = params.getString("login_token");

        Avatar avatar;
        try {
            avatar = new Avatar(driver.getJSONObject("avatar").getString("url"), driver.getJSONObject("avatar").getIntValue("width"), driver.getJSONObject("avatar").getIntValue("height"));
        } catch (Exception ex) {
            Log.e(TAG, "no avatar available");
            avatar = new Avatar("", 0, 0);
        }

        User.init(driver.getString("phone_num"),
                "",
                token,
                driver.getIntValue("work_status"),
                driver.getString("name"),
                driver.getString("id"),
                avatar,
                driver.getIntValue("driver_type"));

        loginEditor.putString("url", avatar.getUrl());
        loginEditor.putInt("width", avatar.getWidth());
        loginEditor.putInt("height", avatar.getHeight());

        loginEditor.putString("token", token);
        loginEditor.putString("name", User.getInstant().getName());
        loginEditor.putString("phone_num", User.getInstant().getPhone());
        loginEditor.putString("id", User.getInstant().getId());
        loginEditor.putInt("driver_type", User.getInstant().getType());
        // 提交数据修改
        loginEditor.commit();
    }

    public class UserReLoginTask extends AsyncTask<Void, Void, Boolean> {
        private final String mToken;

        UserReLoginTask(String token) {
            mToken = token;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                Response response = HttpApi.getInstant().newCall(HttpApi.ReLogin(mToken, notifyToken)).execute();
                String data = response.body().string();
                Log.i(TAG, data);

                JSONObject object = (JSONObject) JSON.parse(data);
                if (object != null && object.getString("status").equals(ErrorCode.Success)) {
                    initUser(object.getJSONObject("data"));
                    return true;
                } else {
                    return false;
                }
            } catch (Exception ex) {
                //Log.e(TAG, ex.getMessage());'
                ex.printStackTrace();
                return false;
            }
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mReAuthTask = null;
            showProgress(false);

            if (success) {
                Log.i(TAG, "LOGIN SUCCESS");
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);

                finish();
            } else {
                mEmailView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mReAuthTask = null;
            showProgress(false);
        }
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mPhone;
        private final String mPassword;

        UserLoginTask(String phone, String password) {
            mPhone = phone;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                Response response = HttpApi.getInstant().newCall(HttpApi.Login(mPhone, mPassword, notifyToken)).execute();
                String data = response.body().string();
                Log.i(TAG, data);

                JSONObject object = (JSONObject) JSON.parse(data);
                if (object != null && object.getString("status").equals(ErrorCode.Success)) {
                    initUser(object.getJSONObject("data"));
                    return true;
                } else {
                    return false;
                }
            } catch (IOException e) {
                //Log.e(TAG, e.getMessage());
                e.printStackTrace();
                return false;
            }
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);

            if (success) {
                Log.i(TAG, "LOGIN SUCCESS");

                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            } else {
                mPasswordView.setError("Login Failed");
                mPasswordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }

    private String getNotifyToken() {
        String notifyToken = FirebaseInstanceId.getInstance().getToken();
        if (notifyToken == null) {
            notifyToken = "";
        }

        Log.d(TAG, "Notify token: " + notifyToken);
        return notifyToken;
    }
}

