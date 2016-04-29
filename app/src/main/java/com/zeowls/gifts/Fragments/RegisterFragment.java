package com.zeowls.gifts.Fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.zeowls.gifts.BackEndOwl.Core;
import com.zeowls.gifts.R;

import org.json.JSONException;

/**
 * Created by nezar on 4/20/16.
 */
public class RegisterFragment extends DialogFragment {


    EditText Email_Field, Password_Field, Mobile_Field;
    String Email_Value, Password_Value, Mobile_Value;
    Button CreateAccount;
    TextView Sign_In_Again;
    View focusView;

    private UserSignUpTask mAuthTask = null;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.register_fragment_layout, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Email_Field = (EditText) view.findViewById(R.id.SignUp_Fr_email);
        Mobile_Field = (EditText) view.findViewById(R.id.SignUp_Fr_Mobile);
        Password_Field = (EditText) view.findViewById(R.id.SignUp_Fr_Password);
        CreateAccount = (Button) view.findViewById(R.id.SignUp_Fr_CreateAccount);
        Sign_In_Again = (TextView) view.findViewById(R.id.SignUp_Fr_SignInAgain);


        Sign_In_Again.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                DialogFragment newFragment = new LoginFragment();
                newFragment.show(getFragmentManager(), "missiles");

            }
        });


        CreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Email_Value = Email_Field.getText().toString();
                Password_Value = Password_Field.getText().toString();
                Mobile_Value = Mobile_Field.getText().toString();
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

                if (TextUtils.isEmpty(Mobile_Value)) {
                    Mobile_Field.setError(getString(R.string.error_field_required));
                    focusView = Mobile_Field;
                    cancel = true;
                }

                if (cancel) {
                    // There was an error; don't attempt login and focus the first
                    // form field with an error.
                    focusView.requestFocus();
                } else {
                    // Show a progress spinner, and kick off a background task to
                    mAuthTask = new UserSignUpTask(Email_Value, Password_Value, Mobile_Value);
                    mAuthTask.execute((Void) null);
                }


            }
        });


    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;


    }


    public class UserSignUpTask extends AsyncTask<Void, Void, Integer> {

        private final String mEmail;
        private final String mPassword;
        private final String mMobile;

        UserSignUpTask(String email, String password, String Mobile) {
            mEmail = email;
            mPassword = password;
            mMobile = Mobile;
        }

        @Override
        protected Integer doInBackground(Void... params) {
            int state = 0;
            Core core = new Core(getActivity());
            try {
                int user_id = core.signUpUser(mEmail, mPassword,mMobile);
                if (user_id == -2) {
                    state = 1;
                    return state;
                } else if (user_id == -3) {
                    state = 2;
                    return state;
                } else if (user_id == -1) {
                    state = 3;
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
                    Email_Field.setError(getString(R.string.Email_alrady_Exist));
                    Email_Field.requestFocus();
                    break;
                case 2:
                    Password_Field.setError(getString(R.string.Mobile_Aready_Exist));
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
