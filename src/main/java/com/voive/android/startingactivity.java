package com.voive.android;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.button.MaterialButton;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.thekhaeng.pushdownanim.PushDownAnim;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class startingactivity extends AppCompatActivity {


    public static Activity activity;

    @BindView(R.id.create_account)
    MaterialButton createAccount;
    @BindView(R.id.login)
    MaterialButton login;
    @BindView(R.id.back)
    ConstraintLayout back;
    @BindView(R.id.notice)
    TextView notice;

    static String getAlphaNumericString(int n) {

        // chose a Character random from this String
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                + "0123456789"
                + "abcdefghijklmnopqrstuvxyz";

        // create StringBuffer size of AlphaNumericString
        StringBuilder sb = new StringBuilder(n);

        for (int i = 0; i < n; i++) {

            // generate a random number between
            // 0 to AlphaNumericString variable length
            int index
                    = (int) (AlphaNumericString.length()
                    * Math.random());

            // add Character one by one in end of sb
            sb.append(AlphaNumericString
                    .charAt(index));
        }

        return sb.toString();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_startingactivity);


        activity = this;
        ButterKnife.bind(this);


      if(ParseUser.getCurrentUser()!=null){

            Intent intent = new Intent(startingactivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
           finish();

        }
      else {

          setTheme(R.style.AppTheme);
          String s = "This App Follow All Rules Of Indian Government Rules For Social Media . For More Detail Follow This Link";
          SpannableString spannableString = new SpannableString(s);
          ClickableSpan clickableSpan = new ClickableSpan() {
              @Override
              public void onClick(@NonNull View view) {

                  Intent browserIntent2 = new Intent(Intent.ACTION_VIEW, Uri.parse("https://mib.gov.in/sites/default/files/IT%20%28Intermediary%20Guidelines%20and%20Digital%20Media%20Ethics%20Code%29%20Rules%2C%202021%20Hindi.pdf"));
                  startActivity(browserIntent2);


              }
          };

          spannableString.setSpan(clickableSpan, 88, 104, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

          notice.setText(spannableString);
          notice.setMovementMethod(LinkMovementMethod.getInstance());


          ImageView gif = findViewById(R.id.gif);


          PushDownAnim.setPushDownAnimTo(createAccount).setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {

                  Intent intent = new Intent(startingactivity.this, starting_language_picker.class);
                  startActivity(intent);
                  overridePendingTransition(R.anim.slide_from_end, R.anim.stationary);


              }
          });
      }



    }



    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
