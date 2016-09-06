package org.opendroidphp.app;

import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.TypedValue;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.andexert.library.RippleView;

import org.opendroidphp.R;
import org.opendroidphp.app.util.DroidPhpActivity;
import org.opendroidphp.app.util.SharedPreferencesUtils;
import org.opendroidphp.app.util.Utilities;

/**
 * Created by Harold Montenegro on 27/07/16.
 */
public class NumpadTool extends DroidPhpActivity {
    public static final String SCREEN_FOR_EXPIRED = "ScreenForExpired";
    private TextView txtPassword;
    private String password = "";
    private RelativeLayout contentMain;
    private TextView txt1, txt2, txt3, txt4, txt5, txt6, txt7, txt8, txt9, txt0;
    private TextView btnCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.number_pad);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        Init();
        Password();
        Numbers();
        Cancel();
        ChangedTheme();
        SharedPreferencesUtils.setPassword(this, "91735");
    }

    private void Init() {
        contentMain = (RelativeLayout) findViewById(R.id.contentMain);

        //si la contraseña de liberacion no esta en el shared esta se crea
        if (SharedPreferencesUtils.getPasswordUnlock(this).isEmpty()) {
            String pass = Utilities.extractNumberString(Utilities.getFingerPrint(this));
            if (pass.length() > 5) {
                pass = pass.substring(0, 5) + "53719";
            } else {
                pass = pass + "53719";
            }
            SharedPreferencesUtils.setPasswordUnlock(this, pass);
        }
    }

    private void ChangedTheme() {
        //Cambia el tema de la pantalla completo si la app expiro
        if (getIntent().getBooleanExtra(SCREEN_FOR_EXPIRED, false)) {
            contentMain.setBackgroundColor(getResources().getColor(R.color.black));
            txtPassword.setTextColor(
                    getResources().getColor(R.color.text_disable_hint_black_theme));
            txtPassword.setText(R.string.enter_your_password_for_use_app);

            txt0.setTextColor(getResources().getColor(R.color.white));
            txt1.setTextColor(getResources().getColor(R.color.white));
            txt2.setTextColor(getResources().getColor(R.color.white));
            txt3.setTextColor(getResources().getColor(R.color.white));
            txt4.setTextColor(getResources().getColor(R.color.white));
            txt5.setTextColor(getResources().getColor(R.color.white));
            txt6.setTextColor(getResources().getColor(R.color.white));
            txt7.setTextColor(getResources().getColor(R.color.white));
            txt8.setTextColor(getResources().getColor(R.color.white));
            txt9.setTextColor(getResources().getColor(R.color.white));

            btnCancel.setVisibility(View.GONE);
        }
    }

    @Override
    public void onBackPressed() {
        backSpaceAction();
    }


    private void Cancel() {
        btnCancel = (TextView) findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void backSpaceAction() {
        if (password.length() > 0) {
            password = password.substring(0, password.length() - 1);
            if (getIntent().getBooleanExtra(SCREEN_FOR_EXPIRED, false)) {
                txtPassword.setTextColor(getResources().getColor(R.color.white));
            } else {
                txtPassword.setTextColor(getResources().getColor(R.color.text));
            }
            if (password.isEmpty()) {
                initTxtPassword();
            } else {
                txtPassword.setText(password);
            }
        } else {
            initTxtPassword();
        }
    }

    private void initTxtPassword() {
        txtPassword.setTransformationMethod(null);
        txtPassword.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        if (getIntent().getBooleanExtra(SCREEN_FOR_EXPIRED, false)) {
            txtPassword.setText(getString(R.string.enter_your_password_for_use_app));
            txtPassword.setTextColor(getResources().getColor(R.color.text_disable_hint_black_theme));
        } else {
            txtPassword.setText(getString(R.string.enter_your_password_for_exit));
            txtPassword.setTextColor(getResources().getColor(R.color.text_disable_hint));
        }
    }

    private void Password() {
        txtPassword = (TextView) findViewById(R.id.txtPassword);
    }

    private void Numbers() {
        txt1 = (TextView) findViewById(R.id.txt1);
        RippleView btn1 = (RippleView) findViewById(R.id.btn1);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                markNumber(1);
            }
        });

        txt2 = (TextView) findViewById(R.id.txt2);
        RippleView btn2 = (RippleView) findViewById(R.id.btn2);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                markNumber(2);
            }
        });

        txt3 = (TextView) findViewById(R.id.txt3);
        RippleView btn3 = (RippleView) findViewById(R.id.btn3);
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                markNumber(3);
            }
        });

        txt4 = (TextView) findViewById(R.id.txt4);
        RippleView btn4 = (RippleView) findViewById(R.id.btn4);
        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                markNumber(4);
            }
        });

        txt5 = (TextView) findViewById(R.id.txt5);
        RippleView btn5 = (RippleView) findViewById(R.id.btn5);
        btn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                markNumber(5);
            }
        });

        txt6 = (TextView) findViewById(R.id.txt6);
        RippleView btn6 = (RippleView) findViewById(R.id.btn6);
        btn6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                markNumber(6);
            }
        });

        txt7 = (TextView) findViewById(R.id.txt7);
        RippleView btn7 = (RippleView) findViewById(R.id.btn7);
        btn7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                markNumber(7);
            }
        });

        txt8 = (TextView) findViewById(R.id.txt8);
        RippleView btn8 = (RippleView) findViewById(R.id.btn8);
        btn8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                markNumber(8);
            }
        });

        txt9 = (TextView) findViewById(R.id.txt9);
        RippleView btn9 = (RippleView) findViewById(R.id.btn9);
        btn9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                markNumber(9);
            }
        });

        txt0 = (TextView) findViewById(R.id.txt0);
        RippleView btn0 = (RippleView) findViewById(R.id.btn0);
        btn0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                markNumber(0);
            }
        });

    }

    private void markNumber(int number) {
        if (getIntent().getBooleanExtra(SCREEN_FOR_EXPIRED, false)) {
            if(password.length() < 10){
                putDigit(number);
                if (password.equals(SharedPreferencesUtils.getPasswordUnlock(this))) {
                    SharedPreferencesUtils.setDeviceUnlock(this, true);
                    finish();
                }else if(password.equals(SharedPreferencesUtils.getPassword(this))){
                    finishApp();
                }
            }else {
                errorPassword();
            }
        } else {
            if (password.length() < 4) {
                putDigit(number);
            } else {
                if (password.length() < 5) {
                    password += String.valueOf(number);
                    txtPassword.setText(password);
                }
                if (password.equals(SharedPreferencesUtils.getPassword(this))) {
                    finishApp();
                } else {
                    errorPassword();
                }
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void errorPassword() {
        //Animacion de vibracion
        ObjectAnimator
                .ofFloat(txtPassword, "translationX", 0, 25, -25, 25, -25, 15, -15, 6, -6, 0)
                .setDuration(300)
                .start();
        txtPassword.setTextColor(getResources().getColor(R.color.common_red));
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void finishApp() {
        PackageManager pm = getPackageManager();
        pm.clearPackagePreferredActivities(getPackageName());
        finishAffinity();
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    private void putDigit(int number){
        password += String.valueOf(number);
        txtPassword.setText(password);
        if (getIntent().getBooleanExtra(SCREEN_FOR_EXPIRED, false)) {
            txtPassword.setTextColor(getResources().getColor(R.color.white));
        } else {
            txtPassword.setTextColor(getResources().getColor(R.color.text));
        }
        txtPassword.setTransformationMethod(new PasswordTransformationMethod());
        txtPassword.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30);
    }
}
