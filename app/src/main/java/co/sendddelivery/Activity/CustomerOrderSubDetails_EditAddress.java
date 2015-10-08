package co.sendddelivery.Activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import co.sendddelivery.R;

public class CustomerOrderSubDetails_EditAddress extends BaseActivity {
    private EditText etReceiverFlatno, etReceiverCountry, etReceiverLocality, etReceiverCity, etReceiverState, etReceiverPincode, etRecieverName, etRecieverNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_editaddress);
        setupUI(findViewById(R.id.parent));
        String flat_no = getIntent().getStringExtra("flat_no");
        String locality = getIntent().getStringExtra("locality");
        String city = getIntent().getStringExtra("city");
        String state = getIntent().getStringExtra("state");
        String country = getIntent().getStringExtra("country");
        String pincode = getIntent().getStringExtra("pincode");
        String d_name = getIntent().getStringExtra("d_name");
        String d_phone = getIntent().getStringExtra("d_phone");
        Button bactivity_add_new_address_receiver_SaveAddress = (Button) findViewById(R.id.bactivity_add_new_address_receiver_SaveAddress);
        etReceiverFlatno = (EditText) findViewById(R.id.etactivity_add_new_address_receiver_flatno);
        etReceiverCountry = (EditText) findViewById(R.id.etactivity_add_new_address_receiver_Country);
        etReceiverLocality = (EditText) findViewById(R.id.etactivity_add_new_address_receiver_Locality);
        etReceiverCity = (EditText) findViewById(R.id.etactivity_add_new_address_receiver_City);
        etReceiverState = (EditText) findViewById(R.id.etactivity_add_new_address_receiver_State);
        etReceiverPincode = (EditText) findViewById(R.id.etactivity_add_new_address_receiver_Pincode);
        etRecieverName = (EditText) findViewById(R.id.etactivity_add_new_address_receiver_name);
        etRecieverNumber = (EditText) findViewById(R.id.etactivity_add_new_address_receiver_phone);
        etRecieverNumber.setText(d_phone);
        etRecieverName.setText(d_name);
        etReceiverFlatno.setText(flat_no);
        etReceiverCountry.setText(country);
        etReceiverLocality.setText(locality);
        etReceiverState.setText(state);
        etReceiverCity.setText(city);
        etReceiverPincode.setText(pincode);
        bactivity_add_new_address_receiver_SaveAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(etReceiverFlatno.getText().toString())) {
                    if (!TextUtils.isEmpty(etReceiverLocality.getText().toString())) {
                        if (!TextUtils.isEmpty(etReceiverCity.getText().toString())) {
                            if (!TextUtils.isEmpty(etReceiverState.getText().toString())) {
                                if (!TextUtils.isEmpty(etReceiverCountry.getText().toString())) {
                                    if (!TextUtils.isEmpty(etReceiverPincode.getText().toString())) {
                                        CustomerOrderSubDetails.flat_no = etReceiverFlatno.getText().toString();
                                        CustomerOrderSubDetails.locality = etReceiverLocality.getText().toString();
                                        CustomerOrderSubDetails.city = etReceiverCity.getText().toString();
                                        CustomerOrderSubDetails.state = etReceiverState.getText().toString();
                                        CustomerOrderSubDetails.country = etReceiverCountry.getText().toString();
                                        CustomerOrderSubDetails.pincode = etReceiverPincode.getText().toString();
                                        CustomerOrderSubDetails.d_phone = etRecieverNumber.getText().toString();
                                        CustomerOrderSubDetails.d_name = etRecieverName.getText().toString();
                                        finish();
                                    } else {
                                        Toast.makeText(CustomerOrderSubDetails_EditAddress.this, "Please enter correct Pincode", Toast.LENGTH_LONG).show();
                                    }
                                } else {
                                    Toast.makeText(CustomerOrderSubDetails_EditAddress.this, "Please enter correct Country", Toast.LENGTH_LONG).show();
                                }
                            } else {
                                Toast.makeText(CustomerOrderSubDetails_EditAddress.this, "Please enter correct State", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(CustomerOrderSubDetails_EditAddress.this, "Please enter correct City", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(CustomerOrderSubDetails_EditAddress.this, "Please enter correct Locality", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(CustomerOrderSubDetails_EditAddress.this, "Please enter correct flat number", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        CustomerOrderSubDetails_EditAddress.this.overridePendingTransition(R.animator.pull_in_left, R.animator.push_out_right);
    }
}