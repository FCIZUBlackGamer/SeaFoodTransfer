package com.ccis.seafoodtrans.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.ccis.seafoodtrans.R;
import com.ccis.seafoodtrans.utils.CheckForNetwork;
import com.ccis.seafoodtrans.utils.InputValidation;
import com.ccis.seafoodtrans.viewmodel.UserViewModel;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.hbb20.CountryCodePicker;

import static android.view.View.GONE;

public class RegisterActivity extends AppCompatActivity {

    private Button btn_registerButton;
    private TextInputEditText email, password, rePassword, fNameEditText, phoneEditText;
    Spinner typeSp;
    CountryCodePicker countryCodePicker;
    UserViewModel model;
    private boolean observerCalled;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_register);
        initViews();
        model= ViewModelProviders.of(this).get(UserViewModel.class);


        btn_registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CheckForNetwork.isConnectionOn(RegisterActivity.this)) {
                    findViewById(R.id.loadingPanel).setVisibility(View.VISIBLE);
                    if (valid()) {
                        register((fNameEditText.getText().toString()),
                                password.getText().toString(),
                                countryCodePicker.getSelectedCountryCode() + phoneEditText.getText().toString(),
                                typeSp.getSelectedItem().toString(),
                                email.getText().toString());
                    } else {
                        Toast.makeText(RegisterActivity.this, "Incomplete Data!", Toast.LENGTH_SHORT).show();
                        findViewById(R.id.loadingPanel).setVisibility(GONE);
                    }
                } else {
                    Snackbar snack = Snackbar.make(findViewById(R.id.rootView).getRootView(), R.string.network_connection, Snackbar.LENGTH_INDEFINITE);
                    snack.setAction("Dismiss", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            snack.dismiss();
                        }
                    });
                    View view1 = snack.getView();
                    FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) view1.getLayoutParams();
                    params.gravity = Gravity.TOP;
                    view1.setLayoutParams(params);
                    snack.show();
                }
            }
        });

    }

    private boolean valid() {
        if (InputValidation.isValidName(fNameEditText.getText().toString()) || InputValidation.isArabic(fNameEditText.getText().toString())) {
            if (InputValidation.isValidEmail(email.getText().toString())) {
                if (InputValidation.isValidPassword(password.getText().toString())) {
                    if (rePassword.getText().toString().equals(password.getText().toString())) {
                        if (InputValidation.isValidPhoneNumber(phoneEditText.getText().toString())) {
                                return true;
                        } else {
                            phoneEditText.setError("Number must be 11 digit!");
                            return false;
                        }
                    }else {
                        rePassword.setError("Not Identical Password");
                        password.setError("Not Identical Password");
                        return false;
                    }
                } else {
                    password.setError("Password must contain at least one letter, at least one number, and be longer than six characters!");
                    return false;
                }
            } else {
                email.setError("Not a valid Email!");
                return false;
            }
        } else {
            fNameEditText.setError("Not a valid name!");
            return false;
        }

    }

    private void initViews() {
//        loadingProgressBar = findViewById(R.id.loadingProgressBar);
        btn_registerButton = findViewById(R.id.registerBtn);
        email = findViewById(R.id.emailEditText);
        fNameEditText = findViewById(R.id.fNameEditText);
        phoneEditText = findViewById(R.id.phoneEditText);
        password = findViewById(R.id.passwordEditText);
        typeSp = findViewById(R.id.spinner);
        countryCodePicker = findViewById(R.id.ccp);
        rePassword = findViewById(R.id.rePasswordEditText);
        findViewById(R.id.loadingPanel).setVisibility(GONE);

        fNameEditText.setOnKeyListener((view, i, keyEvent) -> {
            if ((keyEvent.getAction() == KeyEvent.ACTION_DOWN) &&
                    (i == KeyEvent.KEYCODE_ENTER)) {
                // Perform action on Enter key press
                fNameEditText.clearFocus();
                email.requestFocus();
                return true;
            }
            return false;
        });

        email.setOnKeyListener((view, i, keyEvent) -> {
            if ((keyEvent.getAction() == KeyEvent.ACTION_DOWN) &&
                    (i == KeyEvent.KEYCODE_ENTER)) {
                // Perform action on Enter key press
                email.clearFocus();
                password.requestFocus();
                return true;
            }
            return false;
        });

        password.setOnKeyListener((view, i, keyEvent) -> {
            if ((keyEvent.getAction() == KeyEvent.ACTION_DOWN) &&
                    (i == KeyEvent.KEYCODE_ENTER)) {
                // Perform action on Enter key press
                password.clearFocus();
                rePassword.requestFocus();
                return true;
            }
            return false;
        });

        rePassword.setOnKeyListener((view, i, keyEvent) -> {
            if ((keyEvent.getAction() == KeyEvent.ACTION_DOWN) &&
                    (i == KeyEvent.KEYCODE_ENTER)) {
                // Perform action on Enter key press
                rePassword.clearFocus();
                phoneEditText.requestFocus();
                return true;
            }
            return false;
        });

        phoneEditText.setOnKeyListener((view, i, keyEvent) -> {
            if ((keyEvent.getAction() == KeyEvent.ACTION_DOWN) &&
                    (i == KeyEvent.KEYCODE_ENTER)) {
                // Perform action on Enter key press
                phoneEditText.clearFocus();
                typeSp.requestFocus();
                return true;
            }
            return false;
        });

    }

    private void register(String name, String password, String phone, String type, String email) {
        // Map is used to multipart the file using okhttp3.RequestBody
        if (!observerCalled) {
            observerCalled = true;
            model.register(email, password, name, phone, type).observe(this, new Observer<String>() {
                @Override
                public void onChanged(String s) {
                    if (s != null) {
                        if (s.contains("User Add Successfuly")) {
                            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                        } else {
                            Log.e("Err", "Empty");
                        }

                        Toast.makeText(RegisterActivity.this, s, Toast.LENGTH_SHORT).show();
                    }
                    findViewById(R.id.loadingPanel).setVisibility(GONE);
                }
            });
        }else {
            model.register(email, password, name, phone, type);
        }
    }

}
