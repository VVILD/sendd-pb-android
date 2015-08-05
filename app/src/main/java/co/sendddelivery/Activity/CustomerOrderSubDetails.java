package co.sendddelivery.Activity;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.renderscript.Double2;
import android.text.Editable;
import android.text.TextWatcher;
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

import java.lang.reflect.Array;
import java.util.ArrayList;
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
public class CustomerOrderSubDetails extends Activity implements TextWatcher {
    List<Customer_shipment> mCustomer_Shipment;
    private EditText etReceiverFlatno, etReceiverCountry, etReceiverLocality, etReceiverCity, etReceiverState, etReceiverPincode, etItemweight, etItemPrice, etItemValue, etItemName;
    Boolean x = true;
    Button QrScan, EnterQrcode, CalPrice;
    String flat_no, locality, city, state, country, pincode, pk;
    int counter = 0;
    NetworkUtils mnetworkutils;
    Utils mUtils;
    ProgressDialog mprogress;
    RadioButton premium, standard, express;
    String BarcodeValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ordersubdetails);

        mUtils = new Utils(this);

        final TextView DestinationAddress = (TextView) findViewById(R.id.destinationaddress);
        etItemName = (EditText) findViewById(R.id.etItemName);
        etItemValue = (EditText) findViewById(R.id.etItemValue);
        etItemweight = (EditText) findViewById(R.id.etItemweight);
        etItemPrice = (EditText) findViewById(R.id.etItemPrice);
        etItemweight.addTextChangedListener(this);
        premium = (RadioButton) findViewById(R.id.radio_premium);
        standard = (RadioButton) findViewById(R.id.radio_regular);
        express = (RadioButton) findViewById(R.id.radio_express);
        EnterQrcode = (Button) findViewById(R.id.enterQRCode);
        QrScan = (Button) findViewById(R.id.QrCode);
        CalPrice = (Button) findViewById(R.id.calculatePrice);
        Button Cancel = (Button) findViewById(R.id.Cancel);
        Button Submit = (Button) findViewById(R.id.Submit);
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


        QrScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new IntentIntegrator(CustomerOrderSubDetails.this).initiateScan();
            }
        });
        CalPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    int[][] premium11 = new int[][]{{60, 30}, {90, 45}, {120, 50}, {140, 60}, {160, 65}};
                    int[][] standard11 = new int[][]{{30, 28}, {60, 42}, {80, 47}, {100, 57}, {105, 62}};
                    int[][] economy11 = new int[][]{{240, 15, 30}, {260, 15, 32}, {280, 15, 34}, {290, 15, 35}, {290, 15, 35}};
                    Log.i("pincode", pincode);
                    String firsttwo = pincode.substring(0, 2);
                    String firstthree = pincode.substring(0, 3);
                    int t = Integer.parseInt(firsttwo);
                    int z = Integer.parseInt(firstthree);
                    int zone = 0;
                    int w = Integer.parseInt(etItemweight.getText().toString());
                    double priceCalculated = 0;
                    if (t == 40 && z != 403) {
                        zone = 0;
                    } else if (t == 41 || t == 42 || t == 43 || t == 44) {
                        zone = 1;
                    } else if (t == 56 || t == 11 || t == 60 || t == 70) {
                        zone = 2;
                    } else if (t == 78 || t == 79 || t == 18 || t == 19) {
                        zone = 4;
                    } else {
                        zone = 3;
                    }

                    if (premium.isChecked()) {
                        priceCalculated = 1 * premium11[zone][0] + (Math.ceil(2 * w - 1)) * premium11[zone][1];

                    } else if (standard.isChecked()) {
                        priceCalculated = 1 * standard11[zone][0] + (Math.ceil(2 * w - 1)) * standard11[zone][1];

                    } else if (express.isChecked()) {
                        if (w >= 10) {
                            priceCalculated = economy11[zone][0] + 4 * economy11[zone][1] + Math.ceil(w - 10) * economy11[zone][2];
                        } else if (w >= 6) {
                            priceCalculated = economy11[zone][0] + Math.ceil(w - 6) * economy11[zone][1];
                        } else {
                            etItemValue.setText("-");
                        }

                    }
                    etItemValue.setText(String.valueOf(priceCalculated));

                } catch (NumberFormatException | StringIndexOutOfBoundsException e) {
                    Toast.makeText(CustomerOrderSubDetails.this, "Enter Correct Values", Toast.LENGTH_LONG).show();
                }
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
                etReceiverFlatno.setText(mCustomer_Shipment.get(counter).getDrop_address().getDrop_address_flat_no());
                etReceiverCountry.setText(mCustomer_Shipment.get(counter).getDrop_address().getDrop_address_country());
                etReceiverLocality.setText(mCustomer_Shipment.get(counter).getDrop_address().getDrop_address_locality());
                etReceiverState.setText(mCustomer_Shipment.get(counter).getDrop_address().getDrop_address_state());
                etReceiverCity.setText(mCustomer_Shipment.get(counter).getDrop_address().getDrop_address_city());
                etReceiverPincode.setText(mCustomer_Shipment.get(counter).getDrop_address().getDrop_address_pincode());
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
        EnterQrcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(CustomerOrderSubDetails.this);
                dialog.setTitle("Enter QR Code");
                dialog.setContentView(R.layout.dialog_barcode);
                dialog.getWindow().setBackgroundDrawable((new ColorDrawable(Color.WHITE)));
                dialog.show();
                final EditText etBarcodeValue = (EditText) dialog.findViewById(R.id.etBarcodeEntry);
                Button getDetails = (Button) dialog.findViewById(R.id.bGetDetails);
                getDetails.setText("Set Value");

                getDetails.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final String barcodevalue1 = etBarcodeValue.getText().toString();
                        dialog.dismiss();
                        Log.i("barcodevalue1", barcodevalue1);
                        EnterQrcode.setText(barcodevalue1);
                        BarcodeValue = barcodevalue1;

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
                cp.setBarcode(BarcodeValue);
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
                BarcodeValue = result.getContents();

//                Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
            }
        } else {
            Log.d("MainActivity", "Weird");
            // This is important, otherwise the result will not be passed to the fragment
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
}
