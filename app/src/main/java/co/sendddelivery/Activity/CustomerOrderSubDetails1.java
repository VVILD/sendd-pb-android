package co.sendddelivery.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;

import java.util.Arrays;
import java.util.List;

import co.sendddelivery.GetterandSetter.Customer_shipment;
import co.sendddelivery.R;
import co.sendddelivery.Utils.Utils;

public class CustomerOrderSubDetails1 extends BaseActivity {
    List<Customer_shipment> mCustomer_Shipment;
    private EditText etReceiverFlatno, etReceiverCountry, etReceiverLocality, etReceiverCity, etReceiverState, etReceiverPincode, etRecieverName, etRecieverNumber;
    String flat_no="", locality="", city="", state="", country="", pincode="", pk, d_name="", d_phone="";
    int counter = 0;
    Utils mUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_editaddress);

        mUtils = new Utils(this);
        setupUI(findViewById(R.id.parent));
        final String Customer_shipment_object = getIntent().getStringExtra("Customer_shipment_object");
        Gson GS = new Gson();
        flat_no = getIntent().getStringExtra("flat_no");
        locality = getIntent().getStringExtra("locality");
        city = getIntent().getStringExtra("city");
        state = getIntent().getStringExtra("state");
        country = getIntent().getStringExtra("country");
        pincode = getIntent().getStringExtra("pincode");
         d_name = getIntent().getStringExtra("d_name");
        d_phone = getIntent().getStringExtra("d_phone");


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

                CustomerOrderSubDetails.flat_no = etReceiverFlatno.getText().toString();
                CustomerOrderSubDetails.locality = etReceiverLocality.getText().toString();
                CustomerOrderSubDetails.city = etReceiverCity.getText().toString();
                CustomerOrderSubDetails.state = etReceiverState.getText().toString();
                CustomerOrderSubDetails.country = etReceiverCountry.getText().toString();
                CustomerOrderSubDetails.pincode = etReceiverPincode.getText().toString();
                CustomerOrderSubDetails.d_phone = etRecieverNumber.getText().toString();
                CustomerOrderSubDetails.d_name = etRecieverName.getText().toString();
                finish();
//                Intent i = new Intent(getApplicationContext(), CustomerOrderSubDetails.class);
//                i.putExtra("Customer_shipment_object", getIntent().getStringExtra("Customer_shipment_object"));
//                i.putExtra("flat_no",flat_no);
//                i.putExtra("locality",locality);
//                i.putExtra("city",city);
//                i.putExtra("state",state);
//                i.putExtra("country",country);
//                i.putExtra("pincode",pincode);
//                i.putExtra("d_phone",d_phone);
//                i.putExtra("d_name",d_name);
//                startActivity(i);
//                finish();
//                CustomerOrderSubDetails1.this.overridePendingTransition(R.animator.pull_in_right, R.animator.push_out_left);
            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        CustomerOrderSubDetails1.this.overridePendingTransition(R.animator.pull_in_left, R.animator.push_out_right);
    }
}
