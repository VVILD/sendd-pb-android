package co.sendddelivery.Activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import co.sendddelivery.GetterandSetter.Customer_Order;
import co.sendddelivery.GetterandSetter.Customer_shipment;
import co.sendddelivery.GetterandSetter.ShipmentsListItems;
import co.sendddelivery.R;

/**
 * Created by harshkaranpuria on 7/28/15.
 */
public class CustomerOrderSubDetails extends Activity {
    private RadioButton premium, express, standard;
    List<Customer_shipment> mCustomer_Shipment;
    private EditText etReceiverFlatno,etReceiverCountry, etReceiverLocality,etReceiverCity,etReceiverState,etReceiverPincode;
    Boolean x=true;
    int counter = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ordersubdetails);
        final TextView DestinationAddress = (TextView)findViewById(R.id.destinationaddress);
        final EditText etItemName = (EditText)findViewById(R.id.etItemName);
        final EditText etItemValue = (EditText)findViewById(R.id.etItemValue);
        final EditText etItemweight = (EditText)findViewById(R.id.etItemweight);
        final EditText etItemPrice = (EditText)findViewById(R.id.etItemPrice);

        Button QrScan = (Button)findViewById(R.id.QrCode);
        Button Cancel = (Button)findViewById(R.id.Cancel);
        Button Submit = (Button)findViewById(R.id.Submit);
        DestinationAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(CustomerOrderSubDetails.this);
                dialog.setTitle("Add Address");
                dialog.setContentView(R.layout.dialog_editaddress);
                dialog.getWindow().setBackgroundDrawable((new ColorDrawable(Color.WHITE)));
                dialog.show();
                Button bactivity_add_new_address_receiver_SaveAddress = (Button) dialog.findViewById(R.id.bactivity_add_new_address_receiver_SaveAddress);
                etReceiverFlatno = (EditText) dialog.findViewById(R.id.etactivity_add_new_address_receiver_flatno);
                etReceiverCountry = (EditText) dialog.findViewById(R.id.etactivity_add_new_address_receiver_Country);
                etReceiverLocality = (EditText) dialog.findViewById(R.id.etactivity_add_new_address_receiver_Locality);
                etReceiverCity = (EditText) dialog.findViewById(R.id.etactivity_add_new_address_receiver_City);
                etReceiverState = (EditText) dialog.findViewById(R.id.etactivity_add_new_address_receiver_State);
                etReceiverPincode = (EditText) dialog.findViewById(R.id.etactivity_add_new_address_receiver_Pincode);
                bactivity_add_new_address_receiver_SaveAddress.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        DestinationAddress.setText(etReceiverFlatno.getText().toString()+" "+etReceiverLocality.getText().toString()+" "+etReceiverCity.getText().toString()+" "+etReceiverState.getText().toString()+" "+etReceiverCountry.getText().toString()+" "+etReceiverPincode.getText().toString());
                    }
                });
            }
        });
        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                counter++;
                if(counter<mCustomer_Shipment.size()) {
                    DestinationAddress.setText(mCustomer_Shipment.get(counter).getDrop_address().getDrop_address_flat_no() + " " + mCustomer_Shipment.get(counter).getDrop_address().getDrop_address_locality() + " " + mCustomer_Shipment.get(counter).getDrop_address().getDrop_address_city() + " " + mCustomer_Shipment.get(counter).getDrop_address().getDrop_address_state() + " " + mCustomer_Shipment.get(counter).getDrop_address().getDrop_address_country() + " " + mCustomer_Shipment.get(counter).getDrop_address().getDrop_address_pincode());
                    if(!mCustomer_Shipment.get(counter).getItem_name().equals( "null")){
                        etItemName.setText(mCustomer_Shipment.get(counter).getItem_name());
                    }else{
                        etItemName.setText("");
                    }
                    if (!mCustomer_Shipment.get(counter).getPrice().equals( "null")){
                        etItemPrice.setText(mCustomer_Shipment.get(counter).getPrice());

                    }else{
                        etItemPrice.setText("");
                    }
                    if (!mCustomer_Shipment.get(counter).getWeight().equals( "null")){
                        etItemweight.setText(mCustomer_Shipment.get(counter).getWeight());

                    }else{
                        etItemweight.setText("");
                    }
                    if (!mCustomer_Shipment.get(counter).getCost_of_courier().equals( "null")){
                        etItemValue.setText(mCustomer_Shipment.get(counter).getCost_of_courier());

                    }else{
                        etItemValue.setText("");
                    }
                }else{
                    Intent i =new Intent(getApplicationContext(),Activity_Orders.class);
                    startActivity(i);
                }
            }
        });
        premium = (RadioButton) findViewById(R.id.radio_premium);
        standard = (RadioButton) findViewById(R.id.radio_regular);
        express = (RadioButton) findViewById(R.id.radio_express);
        final String Customer_shipment_object = getIntent().getStringExtra("Customer_shipment_object");
        Gson GS = new Gson();
        mCustomer_Shipment = Arrays.asList(GS.fromJson(Customer_shipment_object, Customer_shipment[].class));
        ShipmentsListItems sli = new ShipmentsListItems();
        DestinationAddress.setText(mCustomer_Shipment.get(counter).getDrop_address().getDrop_address_flat_no() + " " + mCustomer_Shipment.get(counter).getDrop_address().getDrop_address_locality() + " " + mCustomer_Shipment.get(counter).getDrop_address().getDrop_address_city() + " " + mCustomer_Shipment.get(counter).getDrop_address().getDrop_address_state() + " " + mCustomer_Shipment.get(counter).getDrop_address().getDrop_address_country() + " " + mCustomer_Shipment.get(counter).getDrop_address().getDrop_address_pincode());
        if(!mCustomer_Shipment.get(counter).getItem_name().equals( "null")){
            etItemName.setText(mCustomer_Shipment.get(counter).getItem_name());
        }else{
            etItemName.setText("");
        }
        if (!mCustomer_Shipment.get(counter).getPrice().equals( "null")){
            etItemPrice.setText(mCustomer_Shipment.get(counter).getPrice());

        }else{
            etItemPrice.setText("");
        }
        if (!mCustomer_Shipment.get(counter).getWeight().equals( "null")){
            etItemweight.setText(mCustomer_Shipment.get(counter).getWeight());

        }else{
            etItemweight.setText("");
        }
        if (!mCustomer_Shipment.get(counter).getCost_of_courier().equals( "null")){
            etItemValue.setText(mCustomer_Shipment.get(counter).getCost_of_courier());

        }else{
            etItemValue.setText("");
        }

//        for (int i = 0; i < mCustomer_Shipment.size(); i++) {
//            Log.i("asdf","asdf");
//
//            DestinationAddress.setText(mCustomer_Shipment.get(i).getDrop_address().getDrop_address_city());
//            while (x){
//                Log.i("asdf","asdf");
//            }
//        }
        Log.i("shipmentNUmber",String.valueOf(mCustomer_Shipment.size()));

    }
}
