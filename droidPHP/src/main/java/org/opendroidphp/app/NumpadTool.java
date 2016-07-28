package org.opendroidphp.app;

import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import com.andexert.library.RippleView;

import org.opendroidphp.R;
import org.opendroidphp.app.util.SharedPreferencesUtils;

/**
 * Created by Harold Montenegro on 27/07/16.
 */
public class NumpadTool extends Activity {
    private TextView txtPassword;
    private String password = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.number_pad);
        Password();
        Numbers();
        SharedPreferencesUtils.setPassword(this, "11111");
    }

    @Override
    public void onBackPressed() {
//        android.os.Process.killProcess(android.os.Process.myPid());
        backSpaceAction();

    }

    private void backSpaceAction() {
        if (password.length() > 0) {
            password = password.substring(0, password.length() - 1);
            txtPassword.setTextColor(getResources().getColor(R.color.text));
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
        txtPassword.setText(getString(R.string.enter_your_password_for_exit));
        txtPassword.setTransformationMethod(null);
        txtPassword.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        txtPassword.setTextColor(getResources().getColor(R.color.text_disable_hint));
    }

    private void Password() {
        txtPassword = (TextView) findViewById(R.id.txtPassword);
    }

    private void Numbers() {
        RippleView btn1 = (RippleView) findViewById(R.id.btn1);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                markNumber(1);
            }
        });

        RippleView btn2 = (RippleView) findViewById(R.id.btn2);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                markNumber(2);
            }
        });

        RippleView btn3 = (RippleView) findViewById(R.id.btn3);
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                markNumber(3);
            }
        });

        RippleView btn4 = (RippleView) findViewById(R.id.btn4);
        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                markNumber(4);
            }
        });

        RippleView btn5 = (RippleView) findViewById(R.id.btn5);
        btn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                markNumber(5);
            }
        });

        RippleView btn6 = (RippleView) findViewById(R.id.btn6);
        btn6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                markNumber(6);
            }
        });

        RippleView btn7 = (RippleView) findViewById(R.id.btn7);
        btn7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                markNumber(7);
            }
        });

        RippleView btn8 = (RippleView) findViewById(R.id.btn8);
        btn8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                markNumber(8);
            }
        });

        RippleView btn9 = (RippleView) findViewById(R.id.btn9);
        btn9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                markNumber(9);
            }
        });

        RippleView btn0 = (RippleView) findViewById(R.id.btn0);
        btn0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                markNumber(0);
            }
        });

    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void markNumber(int number) {
        if (password.length() < 4) {
            password += String.valueOf(number);
            txtPassword.setText(password);
            txtPassword.setTextColor(getResources().getColor(R.color.text));
            txtPassword.setTransformationMethod(new PasswordTransformationMethod());
            txtPassword.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30);
        } else {

            if (password.length() < 5){
                password += String.valueOf(number);
                txtPassword.setText(password);
            }

            if (password.equals(SharedPreferencesUtils.getPassword(this))) {
                finish();
            } else {
                //Animacion de vibracion
                ObjectAnimator
                        .ofFloat(txtPassword, "translationX", 0, 25, -25, 25, -25, 15, -15, 6, -6, 0)
                        .setDuration(300)
                        .start();
                txtPassword.setTextColor(getResources().getColor(R.color.common_red));
            }
        }
    }
}
