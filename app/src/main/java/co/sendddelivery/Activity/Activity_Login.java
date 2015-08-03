package co.sendddelivery.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import co.sendddelivery.GetterandSetter.Business_Order;
import co.sendddelivery.GetterandSetter.Business_Shipment;
import co.sendddelivery.GetterandSetter.Customer_Order;
import co.sendddelivery.GetterandSetter.Customer_shipment;
import co.sendddelivery.GetterandSetter.Drop_address;
import co.sendddelivery.GetterandSetter.Login;
import co.sendddelivery.GetterandSetter.Pending_Orders;
import co.sendddelivery.R;
import co.sendddelivery.Utils.NetworkUtils;
import co.sendddelivery.Utils.Utils;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by harshkaranpuria on 7/24/15.
 */
public class Activity_Login extends Activity {

    private EditText PhoneNumber;
    Button Login;
    Utils mUtils;
    ProgressDialog mprogress;
    NetworkUtils mnetworkutils;

    public static Activity LoginActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        LoginActivity = this;
        mUtils = new Utils(this);
        Login = (Button)findViewById(R.id.buttonLogin);
        PhoneNumber = (EditText)findViewById(R.id.LoginPhoneNumber);

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("phoneNumber",PhoneNumber.getText().toString());
                if (!TextUtils.isEmpty(PhoneNumber.getText().toString())){
                mnetworkutils = new NetworkUtils(Activity_Login.this);
                mprogress = new ProgressDialog(Activity_Login.this);
                mprogress.setMessage("Please wait.");
                mprogress.setCancelable(false);
                mprogress.setIndeterminate(true);
                if (mnetworkutils.isnetconnected()) {
                    mprogress.show();
                    mnetworkutils.getapi().login(PhoneNumber.getText().toString(), new Callback<co.sendddelivery.GetterandSetter.Login>() {
                                @Override
                                public void success(Login login, Response response1) {
                                    if (mprogress.isShowing()) {
                                        mprogress.dismiss();
                                    }
                                    Intent i = new Intent(getApplicationContext(), Activity_Verify.class);
                                    i.putExtra("PhoneNumber",login.getPhone());
                                    i.putExtra("OTP",login.getOtp());
                                    startActivity(i);
                                    Activity_Login.this.overridePendingTransition(R.animator.pull_in_right, R.animator.push_out_left);
                                }

                                @Override
                                public void failure(RetrofitError error) {
                                    if (mprogress.isShowing()) {
                                        mprogress.dismiss();
                                    }
                                    Log.i("Error:->" ,error.toString());
                                }
                            }

                    );
                } else{
                    Toast.makeText(Activity_Login.this, "Please Connect to a working Internet Connection", Toast.LENGTH_LONG).show();
                }

            }else{
                    Toast.makeText(Activity_Login.this, "Please enter a phone number", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
