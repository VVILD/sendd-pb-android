package co.sendddelivery.Activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
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

import co.sendddelivery.Databases.DB_PreviousOrders;
import co.sendddelivery.Databases.DB_PreviousOrders_details;
import co.sendddelivery.GetterandSetter.CustomerPatch;
import co.sendddelivery.GetterandSetter.Customer_shipment;
import co.sendddelivery.GetterandSetter.PickedupOrders;
import co.sendddelivery.GetterandSetter.Prev_Order_details;
import co.sendddelivery.R;
import co.sendddelivery.Utils.NetworkUtils;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class CustomerOrderSubDetails extends BaseActivity {
    private List<Customer_shipment> mCustomer_Shipment;
    private EditText etItemweight, etItemPrice, etItemValue, etItemName;
    private Button QrScan, EnterQrcode;
    public static String flat_no, locality, city, state, country, pincode, pk, d_name, d_phone, BarcodeValue;
    int counter = 0, shipmentnumber = 1;
    private NetworkUtils mnetworkutils;
    private ProgressDialog mprogress;
    private RadioButton premium, standard, express;
    private TextView currentshipment;
    private TextView DestinationAddress;
    public String promocode_type, promocode_amount, promocode_msg = "", promocode_code = "";
    public float TotalPrice = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ordersubdetails);
        setupUI(findViewById(R.id.parent));

        DestinationAddress = (TextView) findViewById(R.id.destinationaddress);
        etItemName = (EditText) findViewById(R.id.etItemName);
        etItemValue = (EditText) findViewById(R.id.etItemValue);
        etItemweight = (EditText) findViewById(R.id.etItemweight);
        final TextView totalshipment = (TextView) findViewById(R.id.TotalOrders);
        currentshipment = (TextView) findViewById(R.id.CurrentShipment);
        etItemPrice = (EditText) findViewById(R.id.etItemPrice);
        premium = (RadioButton) findViewById(R.id.radio_premium);
        standard = (RadioButton) findViewById(R.id.radio_regular);
        express = (RadioButton) findViewById(R.id.radio_express);
        EnterQrcode = (Button) findViewById(R.id.enterQRCode);
        QrScan = (Button) findViewById(R.id.QrCode);
        Button Cancel = (Button) findViewById(R.id.Cancel);
        final Button Submit = (Button) findViewById(R.id.Submit);
        Gson GS = new Gson();


        premium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calPrice();
            }
        });
        standard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calPrice();
            }
        });
        express.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (Double.parseDouble(etItemweight.getText().toString()) < 6) {
                        Toast.makeText(CustomerOrderSubDetails.this, "Weight Should be more than 6 Kgs for double", Toast.LENGTH_LONG).show();
                        standard.performClick();
                    } else {
                        calPrice();
                    }
                } catch (NumberFormatException ignored) {
                    standard.performClick();
                }
            }
        });

        if (getIntent().getExtras() != null) {
            final String Customer_shipment_object = getIntent().getStringExtra("Customer_shipment_object");
            mCustomer_Shipment = Arrays.asList(GS.fromJson(Customer_shipment_object, Customer_shipment[].class));
        }

        promocode_type = getIntent().getStringExtra("promocode_type");
        promocode_amount = getIntent().getStringExtra("promocode_amount");
        promocode_msg = getIntent().getStringExtra("promocode_msg");
        promocode_code = getIntent().getStringExtra("promocode_code");
        final int totalsize = mCustomer_Shipment.size();
        String totalShipment = "Total number of shipments = " + mCustomer_Shipment.size();
        totalshipment.setText(totalShipment);
        if (getIntent().getStringExtra("flat_no") != null) {
            flat_no = getIntent().getStringExtra("flat_no");
            locality = getIntent().getStringExtra("locality");
            city = getIntent().getStringExtra("city");
            state = getIntent().getStringExtra("state");
            country = getIntent().getStringExtra("country");
            pincode = getIntent().getStringExtra("pincode");
            d_name = getIntent().getStringExtra("d_name");
            d_phone = getIntent().getStringExtra("d_phone");
        } else {
            flat_no = mCustomer_Shipment.get(counter).getDrop_address().getDrop_address_flat_no();
            locality = mCustomer_Shipment.get(counter).getDrop_address().getDrop_address_locality();
            city = mCustomer_Shipment.get(counter).getDrop_address().getDrop_address_city();
            state = mCustomer_Shipment.get(counter).getDrop_address().getDrop_address_state();
            country = mCustomer_Shipment.get(counter).getDrop_address().getDrop_address_country();
            pincode = mCustomer_Shipment.get(counter).getDrop_address().getDrop_address_pincode();
            pk = mCustomer_Shipment.get(counter).getDrop_address().getDrop_address_pk();
            d_name = mCustomer_Shipment.get(counter).getDrop_name();
            d_phone = mCustomer_Shipment.get(counter).getDrop_phone();
        }
        String add1 = flat_no + " " + locality + " " + city + " " + state + " " + country + " " + pincode;
        DestinationAddress.setText(add1);
        if (!mCustomer_Shipment.get(counter).getItem_name().equals("null")) {
            etItemName.setText(mCustomer_Shipment.get(counter).getItem_name());
        } else {
            etItemName.setText("");
        }
        if (!mCustomer_Shipment.get(counter).getPrice().equals("null")) {
            etItemPrice.setText(mCustomer_Shipment.get(counter).getCost_of_courier());

        } else {
            etItemPrice.setText("");
        }
        if (!mCustomer_Shipment.get(counter).getWeight().equals("null")) {
            etItemweight.setText(mCustomer_Shipment.get(counter).getWeight());

        } else {
            etItemweight.setText("");
        }
        if (!mCustomer_Shipment.get(counter).getCost_of_courier().equals("null")) {
            etItemValue.setText(mCustomer_Shipment.get(counter).getPrice());

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

        etItemweight.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    calPrice();
                }
            }
        });
        QrScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new IntentIntegrator(CustomerOrderSubDetails.this).initiateScan();
            }
        });

        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(CustomerOrderSubDetails.this)
                        .setTitle("Confirm")
                        .setMessage("Are you sure you want to Cancel?")
                        .setCancelable(false)
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
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
                                                    PickedupOrders po = new PickedupOrders();
                                                    po.setName(getIntent().getStringExtra("custname"));
                                                    po.setScannedorders(0);
                                                    po.setBusinessusername(getIntent().getStringExtra("custname"));
                                                    po.setCancelledorders(1);
                                                    po.setPickeduporders(0);

                                                    Prev_Order_details prev = new Prev_Order_details();
                                                    prev.setName(d_name+"  "+mCustomer_Shipment.get(counter).getReal_tracking_no());
                                                    prev.setPickedup(false);
                                                    prev.setUsername(getIntent().getStringExtra("custname"));
                                                    DB_PreviousOrders_details dbPreviousOrdersDetails = new DB_PreviousOrders_details();
                                                    dbPreviousOrdersDetails.AddToDB(prev);
                                                    DB_PreviousOrders db_previousOrders = new DB_PreviousOrders();
                                                    db_previousOrders.AddToDB(po);

                                                    shipmentnumber++;
                                                    counter++;
                                                    if (counter < mCustomer_Shipment.size()) {
                                                        String QrText = "Enter QR Code", ScanText = "Scan QR Code";
                                                        EnterQrcode.setText(QrText);
                                                        QrScan.setText(ScanText);
                                                        flat_no = mCustomer_Shipment.get(counter).getDrop_address().getDrop_address_flat_no();
                                                        locality = mCustomer_Shipment.get(counter).getDrop_address().getDrop_address_locality();
                                                        city = mCustomer_Shipment.get(counter).getDrop_address().getDrop_address_city();
                                                        state = mCustomer_Shipment.get(counter).getDrop_address().getDrop_address_state();
                                                        country = mCustomer_Shipment.get(counter).getDrop_address().getDrop_address_country();
                                                        pincode = mCustomer_Shipment.get(counter).getDrop_address().getDrop_address_pincode();
                                                        pk = mCustomer_Shipment.get(counter).getDrop_address().getDrop_address_pk();
                                                        d_name = mCustomer_Shipment.get(counter).getDrop_name();
                                                        d_phone = mCustomer_Shipment.get(counter).getDrop_phone();
                                                        String ShipNumberVal = "Shipment number " + shipmentnumber;
                                                        currentshipment.setText(ShipNumberVal);
                                                        String Add = mCustomer_Shipment.get(counter).getDrop_address().getDrop_address_flat_no() + " " + mCustomer_Shipment.get(counter).getDrop_address().getDrop_address_locality() + " " + mCustomer_Shipment.get(counter).getDrop_address().getDrop_address_city() + " " + mCustomer_Shipment.get(counter).getDrop_address().getDrop_address_state() + " " + mCustomer_Shipment.get(counter).getDrop_address().getDrop_address_country() + " " + mCustomer_Shipment.get(counter).getDrop_address().getDrop_address_pincode();
                                                        DestinationAddress.setText(Add);
                                                        if (!mCustomer_Shipment.get(counter).getItem_name().equals("null")) {
                                                            etItemName.setText(mCustomer_Shipment.get(counter).getItem_name());
                                                        } else {
                                                            etItemName.setText("");
                                                        }
                                                        if (!mCustomer_Shipment.get(counter).getPrice().equals("null")) {
                                                            etItemPrice.setText(mCustomer_Shipment.get(counter).getCost_of_courier());

                                                        } else {
                                                            etItemPrice.setText("");
                                                        }
                                                        if (!mCustomer_Shipment.get(counter).getWeight().equals("null")) {
                                                            etItemweight.setText(mCustomer_Shipment.get(counter).getWeight());

                                                        } else {
                                                            etItemweight.setText("");
                                                        }
                                                        if (!mCustomer_Shipment.get(counter).getCost_of_courier().equals("null")) {
                                                            etItemValue.setText(mCustomer_Shipment.get(counter).getPrice());

                                                        } else {
                                                            etItemValue.setText("");
                                                        }
                                                        if(totalsize==shipmentnumber){
                                                            Submit.setText("Finish Order");
                                                        }else {
                                                            Submit.setText("Next Order");
                                                        }
                                                    } else {
                                                        Intent i = new Intent(getApplicationContext(), Customer_Bill_Summary.class);
                                                        i.putExtra("promocode_type", promocode_type);
                                                        i.putExtra("promocode_amount", promocode_amount);
                                                        i.putExtra("TotalPrice", TotalPrice);
                                                        i.putExtra("promocode_code", promocode_code);
                                                        i.putExtra("promocode_msg", promocode_msg);
                                                        startActivity(i);
                                                        finish();
                                                        Customer_OrderDetails.CustomerOrderDetailsActivity.finish();
                                                        CustomerOrderSubDetails.this.overridePendingTransition(R.animator.pull_in_right, R.animator.push_out_left);
                                                    }

                                                }

                                                @Override
                                                public void failure(RetrofitError error) {
                                                    if (mprogress.isShowing()) {
                                                        mprogress.dismiss();
                                                    }
                                                    Toast.makeText(CustomerOrderSubDetails.this, "Order did not process. Please check again", Toast.LENGTH_LONG).show();
                                                    Log.i("Error:->", error.toString());
                                                }
                                            }
                                    );
                                } else {
                                    Toast.makeText(CustomerOrderSubDetails.this, "Please Connect to a working Internet Connection", Toast.LENGTH_LONG).show();
                                }
                            }
                        })
                        .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .show();

            }
        });
        DestinationAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), CustomerOrderSubDetails_EditAddress.class);
                i.putExtra("d_phone", d_phone);
                i.putExtra("d_name", d_name);
                i.putExtra("flat_no", flat_no);
                i.putExtra("country", country);
                i.putExtra("locality", locality);
                i.putExtra("state", state);
                i.putExtra("city", city);
                i.putExtra("pincode", pincode);
                i.putExtra("promocode_amount", getIntent().getStringExtra("promocode_amount"));
                i.putExtra("promocode_type", getIntent().getStringExtra("promocode_type"));
                i.putExtra("Customer_shipment_object", getIntent().getStringExtra("Customer_shipment_object"));
                startActivity(i);
                CustomerOrderSubDetails.this.overridePendingTransition(R.animator.pull_in_right, R.animator.push_out_left);

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
                String setValText = "Set Value";
                getDetails.setText(setValText);
                getDetails.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final String barcodevalue1 = etBarcodeValue.getText().toString();
                        dialog.dismiss();
                        EnterQrcode.setText(barcodevalue1);
                        BarcodeValue = barcodevalue1;

                    }
                });
            }
        });
        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(etItemName.getText().toString())) {
                    if (!TextUtils.isEmpty(etItemPrice.getText().toString())) {
                        if (!TextUtils.isEmpty(etItemweight.getText().toString())) {
                            if (!TextUtils.isEmpty(etItemValue.getText().toString())) {
                                if (flat_no != null) {
                                    if (locality != null) {
                                        if (city != null) {
                                            if (state != null) {
                                                if (country != null) {
                                                    if (pincode != null) {
                                                        if (BarcodeValue != null) {
                                                            if (BarcodeValue.length() >= 10 && BarcodeValue.length() <= 12) {
                                                                CustomerPatch cp = new CustomerPatch();
                                                                cp.setWeight(etItemweight.getText().toString());
                                                                cp.setStatus("PU");
                                                                cp.setPrice(etItemValue.getText().toString());
                                                                cp.setName(etItemName.getText().toString());
                                                                cp.setCost_of_courier(etItemPrice.getText().toString());
                                                                cp.setBarcode(BarcodeValue);
                                                                cp.setDrop_address_pk(Integer.parseInt(pk));
                                                                cp.setCity(city);
                                                                cp.setCountry(country);
                                                                cp.setFlat_no(flat_no);
                                                                cp.setLocality(locality);
                                                                cp.setPincode(pincode);
                                                                cp.setState(state);
                                                                cp.setDrop_name(d_name);
                                                                cp.setDrop_phone(d_phone);
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
                                                                                    TotalPrice = Float.parseFloat(etItemValue.getText().toString()) + TotalPrice;
                                                                                    if (mprogress.isShowing()) {
                                                                                        mprogress.dismiss();
                                                                                    }
                                                                                    PickedupOrders po = new PickedupOrders();
                                                                                    po.setName(getIntent().getStringExtra("custname"));
                                                                                    po.setScannedorders(0);
                                                                                    po.setBusinessusername(getIntent().getStringExtra("custname"));
                                                                                    po.setCancelledorders(0);
                                                                                    po.setPickeduporders(1);
                                                                                    DB_PreviousOrders db_previousOrders = new DB_PreviousOrders();
                                                                                    db_previousOrders.AddToDB(po);

                                                                                    Prev_Order_details prev = new Prev_Order_details();
                                                                                    prev.setName(d_name+"  "+mCustomer_Shipment.get(counter).getReal_tracking_no());
                                                                                    prev.setPickedup(true);
                                                                                    prev.setUsername(getIntent().getStringExtra("custname"));
                                                                                    DB_PreviousOrders_details dbPreviousOrdersDetails = new DB_PreviousOrders_details();
                                                                                    dbPreviousOrdersDetails.AddToDB(prev);
                                                                                    String QrText = "Enter QR Code", ScanText = "Scan QR Code";
                                                                                    EnterQrcode.setText(QrText);
                                                                                    QrScan.setText(ScanText);
                                                                                    shipmentnumber++;
                                                                                    counter++;
                                                                                    if (counter < mCustomer_Shipment.size()) {
                                                                                        flat_no = mCustomer_Shipment.get(counter).getDrop_address().getDrop_address_flat_no();
                                                                                        locality = mCustomer_Shipment.get(counter).getDrop_address().getDrop_address_locality();
                                                                                        city = mCustomer_Shipment.get(counter).getDrop_address().getDrop_address_city();
                                                                                        state = mCustomer_Shipment.get(counter).getDrop_address().getDrop_address_state();
                                                                                        country = mCustomer_Shipment.get(counter).getDrop_address().getDrop_address_country();
                                                                                        pincode = mCustomer_Shipment.get(counter).getDrop_address().getDrop_address_pincode();
                                                                                        pk = mCustomer_Shipment.get(counter).getDrop_address().getDrop_address_pk();
                                                                                        d_name = mCustomer_Shipment.get(counter).getDrop_name();
                                                                                        d_phone = mCustomer_Shipment.get(counter).getDrop_phone();
                                                                                        String ShipNumberVal = "Shipment number " + shipmentnumber;
                                                                                        currentshipment.setText(ShipNumberVal);
                                                                                        String Add = mCustomer_Shipment.get(counter).getDrop_address().getDrop_address_flat_no() + " " + mCustomer_Shipment.get(counter).getDrop_address().getDrop_address_locality() + " " + mCustomer_Shipment.get(counter).getDrop_address().getDrop_address_city() + " " + mCustomer_Shipment.get(counter).getDrop_address().getDrop_address_state() + " " + mCustomer_Shipment.get(counter).getDrop_address().getDrop_address_country() + " " + mCustomer_Shipment.get(counter).getDrop_address().getDrop_address_pincode();
                                                                                        DestinationAddress.setText(Add);
                                                                                        if (!mCustomer_Shipment.get(counter).getItem_name().equals("null")) {
                                                                                            etItemName.setText(mCustomer_Shipment.get(counter).getItem_name());
                                                                                        } else {
                                                                                            etItemName.setText("");
                                                                                        }
                                                                                        if (!mCustomer_Shipment.get(counter).getPrice().equals("null")) {
                                                                                            etItemPrice.setText(mCustomer_Shipment.get(counter).getCost_of_courier());

                                                                                        } else {
                                                                                            etItemPrice.setText("");
                                                                                        }
                                                                                        if (!mCustomer_Shipment.get(counter).getWeight().equals("null")) {
                                                                                            etItemweight.setText(mCustomer_Shipment.get(counter).getWeight());

                                                                                        } else {
                                                                                            etItemweight.setText("");
                                                                                        }
                                                                                        if (!mCustomer_Shipment.get(counter).getCost_of_courier().equals("null")) {
                                                                                            etItemValue.setText(mCustomer_Shipment.get(counter).getPrice());

                                                                                        } else {
                                                                                            etItemValue.setText("");
                                                                                        }
                                                                                        if(totalsize == shipmentnumber){
                                                                                            Submit.setText("Finish Order");
                                                                                        }else {
                                                                                            Submit.setText("Next Order");
                                                                                        }
                                                                                    } else {

                                                                                        Intent i = new Intent(getApplicationContext(), Customer_Bill_Summary.class);
                                                                                        i.putExtra("promocode_type", promocode_type);
                                                                                        i.putExtra("promocode_amount", promocode_amount);
                                                                                        i.putExtra("TotalPrice", TotalPrice);
                                                                                        i.putExtra("promocode_code", promocode_code);
                                                                                        i.putExtra("promocode_msg", promocode_msg);
                                                                                        startActivity(i);
                                                                                        finish();
                                                                                        Customer_OrderDetails.CustomerOrderDetailsActivity.finish();
                                                                                        CustomerOrderSubDetails.this.overridePendingTransition(R.animator.pull_in_right, R.animator.push_out_left);
                                                                                    }

                                                                                }

                                                                                @Override
                                                                                public void failure(RetrofitError error) {
                                                                                    if (mprogress.isShowing()) {
                                                                                        mprogress.dismiss();
                                                                                    }
                                                                                    Toast.makeText(CustomerOrderSubDetails.this, "Order did not process. Please check Barcode", Toast.LENGTH_LONG).show();
                                                                                }
                                                                            }
                                                                    );
                                                                } else {
                                                                    Toast.makeText(CustomerOrderSubDetails.this, "Please Connect to a working Internet Connection", Toast.LENGTH_LONG).show();
                                                                }
                                                            } else {
                                                                Toast.makeText(CustomerOrderSubDetails.this, "Please enter correct Barcode Value", Toast.LENGTH_LONG).show();
                                                            }
                                                        } else {
                                                            Toast.makeText(CustomerOrderSubDetails.this, "Please enter correct Barcode Value", Toast.LENGTH_LONG).show();
                                                        }
                                                    } else {
                                                        Toast.makeText(CustomerOrderSubDetails.this, "Please enter correct Pincode", Toast.LENGTH_LONG).show();
                                                    }
                                                } else {
                                                    Toast.makeText(CustomerOrderSubDetails.this, "Please enter correct Country", Toast.LENGTH_LONG).show();
                                                }
                                            } else {
                                                Toast.makeText(CustomerOrderSubDetails.this, "Please enter correct State", Toast.LENGTH_LONG).show();
                                            }
                                        } else {
                                            Toast.makeText(CustomerOrderSubDetails.this, "Please enter correct City", Toast.LENGTH_LONG).show();
                                        }
                                    } else {
                                        Toast.makeText(CustomerOrderSubDetails.this, "Please enter correct Locality", Toast.LENGTH_LONG).show();
                                    }
                                } else {
                                    Toast.makeText(CustomerOrderSubDetails.this, "Please enter correct flat number", Toast.LENGTH_LONG).show();
                                }
                            } else {
                                Toast.makeText(CustomerOrderSubDetails.this, "Please enter correct value", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(CustomerOrderSubDetails.this, "Please enter correct weight", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(CustomerOrderSubDetails.this, "Please enter correct price", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(CustomerOrderSubDetails.this, "Please enter correct Name", Toast.LENGTH_LONG).show();
                }
            }
        });
        if(totalsize==1){
            Submit.setText("Finish Order");
        }else {
            Submit.setText("Next Order");
        }
    }

    public void onResume() {
        super.onResume();
        String add = flat_no + " " + locality + " " + city + " " + state + " " + country + " " + pincode;
        DestinationAddress.setText(add);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Log.d("MainActivity", "Cancelled scan");
            } else {
                QrScan.setText(result.getContents());
                BarcodeValue = result.getContents();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        CustomerOrderSubDetails.this.overridePendingTransition(R.animator.pull_in_left, R.animator.push_out_right);
    }

    public void calPrice() {

        try {
            float w = (float) Double.parseDouble(etItemweight.getText().toString());
            int[][] premium11 = new int[][]{{60, 30}, {90, 45}, {120, 50}, {140, 60}, {160, 65}};
            int[][] standard11 = new int[][]{{30, 28}, {60, 42}, {80, 47}, {100, 57}, {105, 62}};
            int[][] economy11 = new int[][]{{240, 15, 30}, {260, 15, 32}, {280, 15, 34}, {290, 15, 35}, {290, 15, 35}};
            String firsttwo = pincode.substring(0, 2);
            String firstthree = pincode.substring(0, 3);
            int t = Integer.parseInt(firsttwo);
            int z = Integer.parseInt(firstthree);
            int zone;
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
                priceCalculated = premium11[zone][0] + (Math.ceil(2 * w - 1)) * premium11[zone][1];

            } else if (standard.isChecked()) {
                priceCalculated = standard11[zone][0] + (Math.ceil(2 * w - 1)) * standard11[zone][1];

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

        } catch (NumberFormatException | StringIndexOutOfBoundsException ignored) {
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle savedInstanceState) {
        savedInstanceState.putString("Barcode", BarcodeValue);
        savedInstanceState.putString("name", etItemName.getText().toString());
        savedInstanceState.putString("price", etItemPrice.getText().toString());
        savedInstanceState.putString("value", etItemValue.getText().toString());
        savedInstanceState.putString("weight", etItemweight.getText().toString());
        savedInstanceState.putString("pk", pk);
        super.onSaveInstanceState(savedInstanceState);
    }

    public void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        BarcodeValue = savedInstanceState.getString("Barcode");
        etItemName.setText(savedInstanceState.getString("name"));
        etItemPrice.setText(savedInstanceState.getString("price"));
        etItemValue.setText(savedInstanceState.getString("value"));
        etItemweight.setText(savedInstanceState.getString("weight"));
        pk = savedInstanceState.getString("pk");
    }
}