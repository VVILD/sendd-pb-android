package co.sendddelivery.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import co.sendddelivery.R;
import co.sendddelivery.Utils.Utils;

/**
 * Created by harshkaranpuria on 7/24/15.
 */
public class Activity_Login extends Activity {

    private EditText PhoneNumber;
    private String phoneNumber;
    Button Login;
    Utils mUtils;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mUtils = new Utils(this);
        Login = (Button)findViewById(R.id.buttonLogin);
        PhoneNumber = (EditText)findViewById(R.id.LoginPhoneNumber);
        phoneNumber = PhoneNumber.getText().toString();
        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mUtils.setvalue("PhoneNumber", phoneNumber);
                Intent i = new Intent(getApplicationContext(), Activity_Orders.class);
                startActivity(i);
                Activity_Login.this.overridePendingTransition(R.animator.pull_in_right, R.animator.push_out_left);

            }
        });

    }
}
