package co.sendddelivery.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import co.sendddelivery.GetterandSetter.BusinessAllOrders;
import co.sendddelivery.GetterandSetter.BusinessPatch;
import co.sendddelivery.GetterandSetter.Business_Shipment;
import co.sendddelivery.GetterandSetter.Pending_Orders;
import co.sendddelivery.GetterandSetter.ShipmentsListItems;
import co.sendddelivery.R;
import co.sendddelivery.Utils.NetworkUtils;
import co.sendddelivery.Utils.Utils;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedByteArray;

public class Business_order_details_fromBarcode extends Activity {
    List<Business_Shipment> mBusiness_Shipment;
    Boolean x = true;
    Button QrScan;
    int counter = 0, shipmentnumber = 1;
    NetworkUtils mnetworkutils;
    Utils mUtils;
    ProgressDialog mprogress;
    ArrayList<Pending_Orders> mPending_Order_list;
    TextView totalshipment, currentshipment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_businesssubdeatails);
        mUtils = new Utils(this);
        final String BUsiness_shipment_object = getIntent().getStringExtra("Business_Shipment");
        final Gson GS = new Gson();
        mBusiness_Shipment = Arrays.asList(GS.fromJson(BUsiness_shipment_object, Business_Shipment[].class));
        totalshipment = (TextView) findViewById(R.id.TotalOrders);
        currentshipment = (TextView) findViewById(R.id.CurrentShipment);
        final EditText etItemName = (EditText) findViewById(R.id.etItemName);
        final EditText etItemValue = (EditText) findViewById(R.id.etItemValue);
        final EditText etItemweight = (EditText) findViewById(R.id.etItemweight);
        final EditText etItemPrice = (EditText) findViewById(R.id.etItemPrice);
        totalshipment.setText("Total number of shipments = " + mBusiness_Shipment.size());
        QrScan = (Button) findViewById(R.id.QrCode);
        Button Cancel = (Button) findViewById(R.id.Cancel);
        Button Submit = (Button) findViewById(R.id.Submit);
        QrScan.setText(getIntent().getStringExtra("barcodevalue"));
        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                BusinessPatch Bp = new BusinessPatch();
                Bp.setStatus("CA");
                mnetworkutils = new NetworkUtils(Business_order_details_fromBarcode.this);
                mprogress = new ProgressDialog(Business_order_details_fromBarcode.this);
                mprogress.setMessage("Please wait...");
                mprogress.setCancelable(false);
                mprogress.setIndeterminate(true);
                if (mnetworkutils.isnetconnected()) {
                    mprogress.show();
                    mnetworkutils.getapi().updateBusiness(mBusiness_Shipment.get(counter).getReal_tracking_no(), Bp, new Callback<BusinessPatch>() {
                                @Override
                                public void success(BusinessPatch response, Response response1) {
                                    if (mprogress.isShowing()) {
                                        mprogress.dismiss();
                                    }

                                    Intent i = new Intent(getApplicationContext(), Business_orders_sublist.class);
                                    String ForwardIntent_UserName = getIntent().getStringExtra("Business_username");
                                    String ForwardIntent_POL = getIntent().getStringExtra("PendingOrderList");
                                    i.putExtra("Business_username", ForwardIntent_UserName);
                                    i.putExtra("PendingOrderList", ForwardIntent_POL);
                                    startActivity(i);
                                    finish();


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
                    Toast.makeText(Business_order_details_fromBarcode.this, "Please Connect to a working Internet Connection", Toast.LENGTH_LONG).show();
                }
            }
        });
        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BusinessPatch Bp = new BusinessPatch();
                Bp.setStatus("PU");
                Bp.setBarcode(QrScan.getText().toString());
                mnetworkutils = new NetworkUtils(Business_order_details_fromBarcode.this);
                mprogress = new ProgressDialog(Business_order_details_fromBarcode.this);
                mprogress.setMessage("Please wait...");
                mprogress.setCancelable(false);
                mprogress.setIndeterminate(true);
                if (mnetworkutils.isnetconnected()) {
                    mprogress.show();
                    mnetworkutils.getapi().updateBusiness(mBusiness_Shipment.get(counter).getReal_tracking_no(), Bp, new Callback<BusinessPatch>() {
                                @Override
                                public void success(BusinessPatch response, Response response1) {
                                    if (mprogress.isShowing()) {
                                        mprogress.dismiss();
                                    }
                                    Intent i = new Intent(getApplicationContext(), Business_orders_sublist.class);
                                    String ForwardIntent_UserName = getIntent().getStringExtra("Business_username");
                                    String ForwardIntent_POL = getIntent().getStringExtra("PendingOrderList");
                                    i.putExtra("Business_username", ForwardIntent_UserName);
                                    i.putExtra("PendingOrderList", ForwardIntent_POL);
                                    startActivity(i);
                                    finish();

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
                    Toast.makeText(Business_order_details_fromBarcode.this, "Please Connect to a working Internet Connection", Toast.LENGTH_LONG).show();
                }
            }
        });
        final String Customer_shipment_object = getIntent().getStringExtra("Business_Shipment");
        mBusiness_Shipment = Arrays.asList(GS.fromJson(Customer_shipment_object, Business_Shipment[].class));
        ShipmentsListItems sli = new ShipmentsListItems();
        if (!mBusiness_Shipment.get(counter).getName().equals("null")) {
            etItemName.setText(mBusiness_Shipment.get(counter).getName());
        } else {
            etItemName.setText("");
        }
        if (!mBusiness_Shipment.get(counter).getPrice().equals("null")) {
            etItemPrice.setText(mBusiness_Shipment.get(counter).getPrice());

        } else {
            etItemPrice.setText("");
        }
        if (!mBusiness_Shipment.get(counter).getWeight().equals("null")) {
            etItemweight.setText(mBusiness_Shipment.get(counter).getWeight());

        } else {
            etItemweight.setText("");
        }
        if (!mBusiness_Shipment.get(counter).getSku().equals("null")) {
            etItemValue.setText(mBusiness_Shipment.get(counter).getSku());

        } else {
            etItemValue.setText("");
        }

    }
}
