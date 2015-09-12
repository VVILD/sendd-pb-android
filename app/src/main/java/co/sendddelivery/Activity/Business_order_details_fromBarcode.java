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

import java.util.Arrays;
import java.util.List;

import co.sendddelivery.Databases.DB_PreviousOrders;
import co.sendddelivery.GetterandSetter.BusinessPatch;
import co.sendddelivery.GetterandSetter.Business_Shipment;
import co.sendddelivery.GetterandSetter.PickedupOrders;
import co.sendddelivery.R;
import co.sendddelivery.Utils.NetworkUtils;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedByteArray;

public class Business_order_details_fromBarcode extends Activity {
    List<Business_Shipment> mBusiness_Shipment;
    Button QrScan;
    int counter = 0;
    NetworkUtils mnetworkutils;
     ProgressDialog mprogress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_businesssubdeatails);
        final EditText etItemName = (EditText) findViewById(R.id.etItemName);
        final EditText etItemValue = (EditText) findViewById(R.id.etItemValue);
        final EditText etItemweight = (EditText) findViewById(R.id.etItemweight);
        final EditText etItemPrice = (EditText) findViewById(R.id.etItemPrice);
        final String BUsiness_shipment_object = getIntent().getStringExtra("Business_Shipment");
        final Gson GS = new Gson();
        TextView totalshipment = (TextView) findViewById(R.id.TotalOrders);
        mBusiness_Shipment = Arrays.asList(GS.fromJson(BUsiness_shipment_object, Business_Shipment[].class));
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
                                    PickedupOrders po = new PickedupOrders();
                                    po.setName(getIntent().getStringExtra("Business_name"));
                                    po.setScannedorders(0);
                                    po.setBusinessusername(getIntent().getStringExtra("Business_username"));
                                    po.setCancelledorders(1);
                                    po.setPickeduporders(0);
                                    DB_PreviousOrders db_previousOrders = new DB_PreviousOrders();
                                    db_previousOrders.AddToDB(po);

                                    Intent i = new Intent(getApplicationContext(), Business_orders_sublist.class);
                                    String ForwardIntent_UserName = getIntent().getStringExtra("Business_username");
                                    String ForwardIntent_POL = getIntent().getStringExtra("PendingOrderList");
                                    i.putExtra("Business_username", ForwardIntent_UserName);
                                    i.putExtra("PendingOrderList", ForwardIntent_POL);
                                    startActivity(i);
                                    finish();
                                    Business_order_details_fromBarcode.this.overridePendingTransition(R.animator.pull_in_left, R.animator.push_out_right);
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
                                    PickedupOrders po = new PickedupOrders();
                                    po.setName(getIntent().getStringExtra("Business_name"));
                                    po.setScannedorders(0);
                                    po.setBusinessusername(getIntent().getStringExtra("Business_username"));
                                    po.setCancelledorders(0);
                                    po.setPickeduporders(1);


                                    DB_PreviousOrders db_previousOrders = new DB_PreviousOrders();
                                    db_previousOrders.AddToDB(po);

                                    Intent i = new Intent(getApplicationContext(), Business_orders_sublist.class);
                                    String ForwardIntent_UserName = getIntent().getStringExtra("Business_username");
                                    String ForwardIntent_POL = getIntent().getStringExtra("PendingOrderList");
                                    i.putExtra("Business_username", ForwardIntent_UserName);
                                    i.putExtra("PendingOrderList", ForwardIntent_POL);
                                    startActivity(i);
                                    finish();
                                    Business_order_details_fromBarcode.this.overridePendingTransition(R.animator.pull_in_right, R.animator.push_out_left);

                                }

                                @Override
                                public void failure(RetrofitError error) {
                                    if (mprogress.isShowing()) {
                                        mprogress.dismiss();
                                    }
                                    String json = new String(((TypedByteArray) error.getResponse().getBody()).getBytes());
                                    Log.v("failure", json);
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        Business_order_details_fromBarcode.this.overridePendingTransition(R.animator.pull_in_left, R.animator.push_out_right);
    }
}
