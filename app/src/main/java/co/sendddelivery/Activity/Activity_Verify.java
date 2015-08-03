package co.sendddelivery.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import co.sendddelivery.R;
import co.sendddelivery.Utils.Utils;

/**
 * Created by harshkaranpuria on 7/30/15.
 */
public class Activity_Verify extends Activity {
    String phone,otp;
    Button Verify;
    Utils mUtils;
    EditText OTP;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_otp);
        OTP= (EditText)findViewById(R.id.etOTP);
        Verify= (Button)findViewById(R.id.bVerify);
        mUtils = new Utils(this);

        phone = getIntent().getStringExtra("PhoneNumber");
        otp = getIntent().getStringExtra("OTP");
        Verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(OTP.getText().toString().equals(otp)){
                    mUtils.setvalue("PhoneNumber", phone);
                    mUtils.setvalue("LoggedIn", "Yes");
                    Intent i = new Intent(getApplicationContext(), Activity_Orders.class);
                    startActivity(i);
                    Activity_Login.LoginActivity.finish();
                    Activity_Verify.this.overridePendingTransition(R.animator.pull_in_right, R.animator.push_out_left);
                    finish();
                }else {
                    Toast.makeText(Activity_Verify.this,"Invalid OTP",Toast.LENGTH_LONG).show();
                }
            }
        });

    }
}
