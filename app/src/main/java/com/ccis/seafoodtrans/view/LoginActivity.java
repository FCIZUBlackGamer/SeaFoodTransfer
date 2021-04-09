package com.ccis.seafoodtrans.view;

import android.animation.Animator;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.ccis.seafoodtrans.BaseActivity;
import com.ccis.seafoodtrans.R;
import com.ccis.seafoodtrans.model.ListUser;
import com.ccis.seafoodtrans.utils.CheckForNetwork;
import com.ccis.seafoodtrans.utils.InputValidation;
import com.ccis.seafoodtrans.utils.PreferenceController;
import com.ccis.seafoodtrans.viewmodel.UserViewModel;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class LoginActivity extends BaseActivity {

    private ImageView bookIconImageView;
    private TextView bookITextView, signupTextView;
    private ProgressBar loadingProgressBar;
    private RelativeLayout rootView, afterAnimationView;
    private Button btn_loginButton;
    private TextInputEditText email, password;
    LayoutInflater inflate;
    UserViewModel model;
    private boolean observerCalled;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);
        model = ViewModelProviders.of(this).get(UserViewModel.class);
        initViews();
        inflate = getLayoutInflater();

        if (PreferenceController.getInstance(this).get(PreferenceController.PREF_USER_ID) == null || PreferenceController.getInstance(this).get(PreferenceController.PREF_USER_ID).isEmpty()) {
            PreferenceController.getInstance(this).persist(PreferenceController.PREF_USER_ID, "0");
        }

        if (!PreferenceController.getInstance(this).get(PreferenceController.PREF_USER_ID).equals("0")) {
            if (PreferenceController.getInstance(this).get(PreferenceController.PREF_USER_TYPE).equals("Fisherman")) {
                startActivity(new Intent(LoginActivity.this, FishermanActivity.class));
            } else if (PreferenceController.getInstance(this).get(PreferenceController.PREF_USER_TYPE).equals("Client")) {
                startActivity(new Intent(LoginActivity.this, ClientActivity.class));
            } else if (PreferenceController.getInstance(this).get(PreferenceController.PREF_USER_TYPE).equals("Admin")) {
                startActivity(new Intent(LoginActivity.this, AdminActivity.class));
            }
        }

        signupTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });

        btn_loginButton.setOnClickListener(view -> {
            if (CheckForNetwork.isConnectionOn(this)) {
                findViewById(R.id.loadingPanel).setVisibility(VISIBLE);
                if (valid()) {
                    if (!observerCalled) {
                        observerCalled = true;
                        model.login(email.getText().toString(), password.getText().toString()).observe(this, new Observer<ListUser>() {
                            @Override
                            public void onChanged(ListUser s) {
                                if (s !=null) {
                                    try {
                                        if (Integer.parseInt(s.getData().get(0).getId()) != 0) {
                                            PreferenceController.getInstance(LoginActivity.this).persist(PreferenceController.PREF_USER_ID, s.getData().get(0).getId());
                                            PreferenceController.getInstance(LoginActivity.this).persist(PreferenceController.PREF_USER_TYPE, s.getData().get(0).getType());

                                            Log.e("Response", s.getData().get(0).getId());
                                            Toast.makeText(LoginActivity.this, "Welcome Home!", Toast.LENGTH_SHORT).show();
                                            if (s.getData().get(0).getType().equals("Fisherman")) {
                                                startActivity(new Intent(LoginActivity.this, FishermanActivity.class));
                                            } else if (s.getData().get(0).getType().equals("Client")) {
                                                startActivity(new Intent(LoginActivity.this, ClientActivity.class));
                                            } else if (s.getData().get(0).getType().equals("Admin")) {
                                                startActivity(new Intent(LoginActivity.this, AdminActivity.class));
                                            }
                                        } else {
                                            //textView.setText(serverResponse.toString());
                                            Log.e("Err", "Empty");

                                            new AlertDialog.Builder(LoginActivity.this)
                                                    .setMessage("اسم مستخدم او كلمة مرور خاطئة")
                                                    .setPositiveButton("OK", (dialogInterface, i) -> dialogInterface.dismiss())
                                                    .setNegativeButton("Cancel", (dialogInterface, i) -> dialogInterface.dismiss())
                                                    .create()
                                                    .show();
                                            findViewById(R.id.loadingPanel).setVisibility(GONE);
                                        }
                                    } catch (NumberFormatException e) {
                                        e.printStackTrace();
                                        Log.e("Err", "Empty");

                                        new AlertDialog.Builder(LoginActivity.this)
                                                .setMessage("اسم مستخدم او كلمة مرور خاطئة")
                                                .setPositiveButton("OK", (dialogInterface, i) -> dialogInterface.dismiss())
                                                .setNegativeButton("Cancel", (dialogInterface, i) -> dialogInterface.dismiss())
                                                .create()
                                                .show();
                                        findViewById(R.id.loadingPanel).setVisibility(GONE);
                                    }
                                }else {
                                    Toast.makeText(LoginActivity.this, "Timeout Connection", Toast.LENGTH_SHORT).show();
                                }
                                findViewById(R.id.loadingPanel).setVisibility(GONE);
                            }
                        });
                    } else {
                        model.login(email.getText().toString(), password.getText().toString());
                    }
                } else {
                    findViewById(R.id.loadingPanel).setVisibility(GONE);
                }
            } else {
                Snackbar snack = Snackbar.make(findViewById(R.id.rootView).getRootView(), R.string.network_connection, Snackbar.LENGTH_INDEFINITE);
                View view1 = snack.getView();
                FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) view1.getLayoutParams();
                params.gravity = Gravity.TOP;
                view1.setLayoutParams(params);
                snack.show();
            }

        });

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                bookITextView.setVisibility(GONE);
                loadingProgressBar.setVisibility(GONE);
                rootView.setBackgroundColor(ContextCompat.getColor(LoginActivity.this, R.color.white));
                //bookIconImageView.setImageResource(R.drawable.zlogo);
                startAnimation();
            }
        }, 5000);

    }

    private boolean valid() {
        if (InputValidation.isValidEmail(email.getText().toString().replace(" ", ""))) {
//            if (InputValidation.isValidPassword(password.getText().toString())) {
            return true;
//            } else {
//                password.setError("Invalid Password");
//                return false;
//            }
        } else {
            email.setError("Invalid Email");
            return false;
        }
    }


    private void initViews() {
        bookIconImageView = findViewById(R.id.bookIconImageView);
        bookITextView = findViewById(R.id.bookITextView);
        loadingProgressBar = findViewById(R.id.loadingProgressBar);
        rootView = findViewById(R.id.rootView);
        btn_loginButton = findViewById(R.id.loginButton);
        email = findViewById(R.id.emailEditText);
        password = findViewById(R.id.passwordEditText);
        afterAnimationView = findViewById(R.id.afterAnimationView);
        signupTextView = findViewById(R.id.signupTextView);
        findViewById(R.id.loadingPanel).setVisibility(GONE);
    }

    private void startAnimation() {
        ViewPropertyAnimator viewPropertyAnimator = bookIconImageView.animate();
//        viewPropertyAnimator.x(25f);
        viewPropertyAnimator.y(50f);
        viewPropertyAnimator.setDuration(1000);
        viewPropertyAnimator.setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                afterAnimationView.setVisibility(VISIBLE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }
}
