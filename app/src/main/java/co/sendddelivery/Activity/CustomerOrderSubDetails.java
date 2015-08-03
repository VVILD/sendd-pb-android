package co.sendddelivery.Activity;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.Arrays;
import java.util.List;

import co.sendddelivery.GetterandSetter.CustomerPatch;
import co.sendddelivery.GetterandSetter.Customer_shipment;
import co.sendddelivery.GetterandSetter.Login;
import co.sendddelivery.GetterandSetter.ShipmentsListItems;
import co.sendddelivery.R;
import co.sendddelivery.Utils.NetworkUtils;
import co.sendddelivery.Utils.Utils;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedByteArray;

/**
 * Created by harshkaranpuria on 7/28/15.
 */
public class CustomerOrderSubDetails extends Activity {
    List<Customer_shipment> mCustomer_Shipment;
    private EditText etReceiverFlatno, etReceiverCountry, etReceiverLocality, etReceiverCity, etReceiverState, etReceiverPincode;
    Boolean x = true;
    Button QrScan;
    String flat_no, locality, city, state, country, pincode, pk;
    int counter = 0;
    NetworkUtils mnetworkutils;
    Utils mUtils;
    ProgressDialog mprogress;
    RadioButton premium, standard, express;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ordersubdetails);
        mUtils = new Utils(this);

        final TextView DestinationAddress = (TextView) findViewById(R.id.destinationaddress);
        final EditText etItemName = (EditText) findViewById(R.id.etItemName);
        final EditText etItemValue = (EditText) findViewById(R.id.etItemValue);
        final EditText etItemweight = (EditText) findViewById(R.id.etItemweight);
        final EditText etItemPrice = (EditText) findViewById(R.id.etItemPrice);
        premium = (RadioButton) findViewById(R.id.radio_premium);
        standard = (RadioButton) findViewById(R.id.radio_regular);
        express = (RadioButton) findViewById(R.id.radio_express);

        QrScan = (Button) findViewById(R.id.QrCode);
        Button Cancel = (Button) findViewById(R.id.Cancel);
        Button Submit = (Button) findViewById(R.id.Submit);
        QrScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new IntentIntegrator(CustomerOrderSubDetails.this).initiateScan();
            }
        });
        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CustomerPatch cp = new CustomerPatch();
                cp.setStatus("CA");
                mnetworkutils = new NetworkUtils(CustomerOrderSubDetails.this);
                mprogress = new ProgressDialog(CustomerOrderSubDetails.this);
                mprogress.setMessage("Please wait...");
                mprogress.setCancelable(false);
                mprogress.setIndeterminate(true);
                if (mnetworkutils.isnetconnected()) {
                    mprogress.show();
                    mnetworkutils.getapi().updateCustomer(mCustomer_Shipment.get(counter).getReal_tracking_no(), cp, new Callback<CustomerPatch>() {
                                @Override
                                public void success(CustomerPatch response, Response response1) {
                                    if (mprogress.isShowing()) {
                                        mprogress.dismiss();
                                    }
                                    counter++;
                                    if (counter < mCustomer_Shipment.size()) {
                                        DestinationAddress.setText(mCustomer_Shipment.get(counter).getDrop_address().getDrop_address_flat_no() + " " + mCustomer_Shipment.get(counter).getDrop_address().getDrop_address_locality() + " " + mCustomer_Shipment.get(counter).getDrop_address().getDrop_address_city() + " " + mCustomer_Shipment.get(counter).getDrop_address().getDrop_address_state() + " " + mCustomer_Shipment.get(counter).getDrop_address().getDrop_address_country() + " " + mCustomer_Shipment.get(counter).getDrop_address().getDrop_address_pincode());
                                        if (!mCustomer_Shipment.get(counter).getItem_name().equals("null")) {
                                            etItemName.setText(mCustomer_Shipment.get(counter).getItem_name());
                                        } else {
                                            etItemName.setText("");
                                        }
                                        if (!mCustomer_Shipment.get(counter).getPrice().equals("null")) {
                                            etItemPrice.setText(mCustomer_Shipment.get(counter).getPrice());

                                        } else {
                                            etItemPrice.setText("");
                                        }
                                        if (!mCustomer_Shipment.get(counter).getWeight().equals("null")) {
                                            etItemweight.setText(mCustomer_Shipment.get(counter).getWeight());

                                        } else {
                                            etItemweight.setText("");
                                        }
                                        if (!mCustomer_Shipment.get(counter).getCost_of_courier().equals("null")) {
                                            etItemValue.setText(mCustomer_Shipment.get(counter).getCost_of_courier());

                                        } else {
                                            etItemValue.setText("");
                                        }
                                    } else {
                                        Intent i = new Intent(getApplicationContext(), Activity_Orders.class);
                                        startActivity(i);
                                        finish();
                                    }

                                }

                                @Override
                                public void failure(RetrofitError error) {
                                    if (mprogress.isShowing()) {
                                        mprogress.dismiss();
                                    }
                                    Log.i("Error:->", error.toString());
                                }
                            }

                    );
                } else {
                    Toast.makeText(CustomerOrderSubDetails.this, "Please Connect to a working Internet Connection", Toast.LENGTH_LONG).show();
                }
            }
        });
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

                        flat_no = etReceiverFlatno.getText().toString();
                        locality = etReceiverLocality.getText().toString();
                        city = etReceiverCity.getText().toString();
                        state = etReceiverState.getText().toString();
                        country = etReceiverCountry.getText().toString();
                        pincode = etReceiverPincode.getText().toString();
                        DestinationAddress.setText(etReceiverFlatno.getText().toString() + " " + etReceiverLocality.getText().toString() + " " + etReceiverCity.getText().toString() + " " + etReceiverState.getText().toString() + " " + etReceiverCountry.getText().toString() + " " + etReceiverPincode.getText().toString());
                    }
                });
            }
        });
        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CustomerPatch cp = new CustomerPatch();
                cp.setWeight(etItemweight.getText().toString());
                cp.setStatus("PU");
                cp.setPrice(etItemPrice.getText().toString());
                cp.setName(etItemName.getText().toString());
                cp.setCost_of_courier(etItemValue.getText().toString());
                cp.setBarcode(QrScan.getText().toString());
                cp.setDrop_address_pk(Integer.parseInt(pk));
                cp.setCity(city);
                cp.setCountry(country);
                cp.setFlat_no(flat_no);
                cp.setLocality(locality);
                cp.setPincode(pincode);
                cp.setState(state);

                if (premium.isChecked()) {
                    cp.setCategory("P");
                } else if (standard.isChecked()) {
                    cp.setCategory("S");
                } else if (express.isChecked()) {
                    cp.setCategory("E");
                }

                mnetworkutils = new NetworkUtils(CustomerOrderSubDetails.this);
                mprogress = new ProgressDialog(CustomerOrderSubDetails.this);
                mprogress.setMessage("Please wait...");
                mprogress.setCancelable(false);
                mprogress.setIndeterminate(true);
                if (mnetworkutils.isnetconnected()) {
                    mprogress.show();
                    mnetworkutils.getapi().updateCustomer(mCustomer_Shipment.get(counter).getReal_tracking_no(), cp, new Callback<CustomerPatch>() {
                                @Override
                                public void success(CustomerPatch response, Response response1) {
                                    if (mprogress.isShowing()) {
                                        mprogress.dismiss();
                                    }
                                    counter++;
                                    if (counter < mCustomer_Shipment.size()) {
                                        DestinationAddress.setText(mCustomer_Shipment.get(counter).getDrop_address().getDrop_address_flat_no() + " " + mCustomer_Shipment.get(counter).getDrop_address().getDrop_address_locality() + " " + mCustomer_Shipment.get(counter).getDrop_address().getDrop_address_city() + " " + mCustomer_Shipment.get(counter).getDrop_address().getDrop_address_state() + " " + mCustomer_Shipment.get(counter).getDrop_address().getDrop_address_country() + " " + mCustomer_Shipment.get(counter).getDrop_address().getDrop_address_pincode());
                                        if (!mCustomer_Shipment.get(counter).getItem_name().equals("null")) {
                                            etItemName.setText(mCustomer_Shipment.get(counter).getItem_name());
                                        } else {
                                            etItemName.setText("");
                                        }
                                        if (!mCustomer_Shipment.get(counter).getPrice().equals("null")) {
                                            etItemPrice.setText(mCustomer_Shipment.get(counter).getPrice());

                                        } else {
                                            etItemPrice.setText("");
                                        }
                                        if (!mCustomer_Shipment.get(counter).getWeight().equals("null")) {
                                            etItemweight.setText(mCustomer_Shipment.get(counter).getWeight());

                                        } else {
                                            etItemweight.setText("");
                                        }
                                        if (!mCustomer_Shipment.get(counter).getCost_of_courier().equals("null")) {
                                            etItemValue.setText(mCustomer_Shipment.get(counter).getCost_of_courier());

                                        } else {
                                            etItemValue.setText("");
                                        }
                                    } else {
                                        Intent i = new Intent(getApplicationContext(), Activity_Orders.class);
                                        startActivity(i);
                                        finish();
                                    }

                                }

                                @Override
                                public void failure(RetrofitError error) {
                                    if (mprogress.isShowing()) {
                                        mprogress.dismiss();
                                    }
                                    String json = new String(((TypedByteArray) error.getResponse().getBody()).getBytes());
                                    Log.v("failure", json.toString());
                                }
                            }

                    );
                } else {
                    Toast.makeText(CustomerOrderSubDetails.this, "Please Connect to a working Internet Connection", Toast.LENGTH_LONG).show();
                }
            }
        });

        final String Customer_shipment_object = getIntent().getStringExtra("Customer_shipment_object");
        Gson GS = new Gson();
        mCustomer_Shipment = Arrays.asList(GS.fromJson(Customer_shipment_object, Customer_shipment[].class));
        ShipmentsListItems sli = new ShipmentsListItems();

        flat_no = mCustomer_Shipment.get(counter).getDrop_address().getDrop_address_flat_no();
        locality = mCustomer_Shipment.get(counter).getDrop_address().getDrop_address_locality();
        city = mCustomer_Shipment.get(counter).getDrop_address().getDrop_address_city();
        state = mCustomer_Shipment.get(counter).getDrop_address().getDrop_address_state();
        country = mCustomer_Shipment.get(counter).getDrop_address().getDrop_address_country();
        pincode = mCustomer_Shipment.get(counter).getDrop_address().getDrop_address_pincode();
        pk = mCustomer_Shipment.get(counter).getDrop_address().getDrop_address_pk();

        DestinationAddress.setText(mCustomer_Shipment.get(counter).getDrop_address().getDrop_address_flat_no() + " " + mCustomer_Shipment.get(counter).getDrop_address().getDrop_address_locality() + " " + mCustomer_Shipment.get(counter).getDrop_address().getDrop_address_city() + " " + mCustomer_Shipment.get(counter).getDrop_address().getDrop_address_state() + " " + mCustomer_Shipment.get(counter).getDrop_address().getDrop_address_country() + " " + mCustomer_Shipment.get(counter).getDrop_address().getDrop_address_pincode());
        if (!mCustomer_Shipment.get(counter).getItem_name().equals("null")) {
            etItemName.setText(mCustomer_Shipment.get(counter).getItem_name());
        } else {
            etItemName.setText("");
        }
        if (!mCustomer_Shipment.get(counter).getPrice().equals("null")) {
            etItemPrice.setText(mCustomer_Shipment.get(counter).getPrice());

        } else {
            etItemPrice.setText("");
        }
        if (!mCustomer_Shipment.get(counter).getWeight().equals("null")) {
            etItemweight.setText(mCustomer_Shipment.get(counter).getWeight());

        } else {
            etItemweight.setText("");
        }
        if (!mCustomer_Shipment.get(counter).getCost_of_courier().equals("null")) {
            etItemValue.setText(mCustomer_Shipment.get(counter).getCost_of_courier());

        } else {
            etItemValue.setText("");
        }
        if (mCustomer_Shipment.get(counter).getCategory().equals("P")) {
            premium.setChecked(true);
        } else if (mCustomer_Shipment.get(counter).getCategory().equals("S")) {
            standard.setChecked(true);
        } else if (mCustomer_Shipment.get(counter).getCategory().equals("E")) {
            express.setChecked(true);

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Log.d("MainActivity", "Cancelled scan");
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                Log.d("MainActivity", "Scanned");
                QrScan.setText(result.getContents());
//                Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
            }
        } else {
            Log.d("MainActivity", "Weird");
            // This is important, otherwise the result will not be passed to the fragment
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

}
