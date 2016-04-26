package com.zeowls.gifts.Fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v4.app.DialogFragment;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.zeowls.gifts.BackEndOwl.Core;
import com.zeowls.gifts.R;
import com.zeowls.gifts.Fragments.RegisterFragment;
import com.facebook.FacebookSdk;


import org.json.JSONException;


public class LoginFragment extends DialogFragment {


    EditText Email_Field, Password_Field;
    String Email_Value, Password_Value;
    ImageView Sign_In_Im;
    TextView Forget_Pass_Txt;
    Button Sign_Up_Btn;
    View focusView;
    private UserLoginTask mAuthTask = null;
    LoginButton loginButton;
    CallbackManager callbackManager;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login_2, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Email_Field = (EditText) view.findViewById(R.id.Login_Fr_email);
        Password_Field = (EditText) view.findViewById(R.id.Login_Fr_Password);
        Sign_In_Im = (ImageView) view.findViewById(R.id.Login_Fr_SignBtn);
        Forget_Pass_Txt = (TextView) view.findViewById(R.id.Login_Fr_ForgetPassTxt);
        Sign_Up_Btn = (Button) view.findViewById(R.id.Login_Fr_SignUpBtn);
        loginButton = (LoginButton) view.findViewById(R.id._Facebook_login_button);
        loginButton.setReadPermissions("email");
        loginButton.setFragment(this);

        FacebookSdk.sdkInitialize(getContext());
        callbackManager = CallbackManager.Factory.create();

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
            }
        });


        Password_Field.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (actionId == R.id.login) {
                    Login_Task();
                    return true;

                }
                return false;
            }
        });

        Sign_Up_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dismiss();
                DialogFragment newFragment = new RegisterFragment();
                newFragment.show(getFragmentManager(), "missiles");

            }
        });


        Sign_In_Im.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Login_Task();


            }
        });


    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    public void Login_Task() {
        Email_Value = Email_Field.getText().toString();
        Password_Value = Password_Field.getText().toString();
        boolean cancel = false;
        focusView = null;

        if (TextUtils.isEmpty(Email_Value)) {
            Email_Field.setError(getString(R.string.error_field_required));
            focusView = Email_Field;
            cancel = true;
        }

        if (TextUtils.isEmpty(Password_Value)) {
            Password_Field.setError(getString(R.string.error_field_required));
            focusView = Password_Field;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            mAuthTask = new UserLoginTask(Email_Value, Password_Value);
            mAuthTask.execute((Void) null);
        }

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;


    }


    public class UserLoginTask extends AsyncTask<Void, Void, Integer> {

        private final String mEmail;
        private final String mPassword;

        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected Integer doInBackground(Void... params) {
            int state = 0;
            Core core = new Core(getActivity());
            try {
                int user_id = core.getCredentials(mEmail, mPassword);
                if (user_id == -2) {
                    state = 1;
                    return state;
                } else if (user_id == -1) {
                    state = 2;
                    return state;
                } else {
                    SharedPreferences.Editor editor = getActivity().getSharedPreferences("Credentials", Context.MODE_PRIVATE).edit();
                    editor.putInt("id", user_id);
                    Log.d("user Id", String.valueOf(user_id));
                    editor.apply();
                    return state;
                }
            } catch (JSONException e) {
                state = 3;
                Log.d("Error", e.getMessage());
                e.printStackTrace();
                return state;
            }

        }

        @Override
        protected void onPostExecute(final Integer state) {
            mAuthTask = null;
            switch (state) {
                case 1:
                    Email_Field.setError(getString(R.string.email_does_not_exist));
                    Email_Field.requestFocus();
                    break;
                case 2:
                    Password_Field.setError(getString(R.string.password_is_wrong));
                    Password_Field.requestFocus();
                    break;
                case 3:
                    Toast.makeText(getActivity(), getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                    break;
                default:
                    getActivity().finish();
                    startActivity(getActivity().getIntent());
            }


        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
        }
    }

}
