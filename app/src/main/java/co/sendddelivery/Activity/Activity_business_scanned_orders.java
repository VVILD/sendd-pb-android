package co.sendddelivery.Activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;

import co.sendddelivery.Databases.DB_PreviousOrders;
import co.sendddelivery.GetterandSetter.BarcodeAllotment;
import co.sendddelivery.GetterandSetter.PickedupOrders;
import co.sendddelivery.GetterandSetter.allotment_list;
import co.sendddelivery.GetterandSetter.business_is_completePatch;
import co.sendddelivery.R;
import co.sendddelivery.Utils.NetworkUtils;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedByteArray;

public class Activity_business_scanned_orders extends AppCompatActivity {
    TextView businessname;
    ListView prevOrdersList;
    Button FinishScan, Scan,Enter;
    ProgressDialog mprogress;
    NetworkUtils mnetworkutils;
    ArrayList<allotment_list> arrBarcode;
    ArrayList<String> barcodes;
    ArrayAdapter adapter;

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        Intent i = new Intent(this,Activity_Orders.class);
        startActivity(i);
        finish();
        overridePendingTransition(R.animator.fade_in, R.animator.fade_out);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_scanned_orders);
        arrBarcode = new ArrayList<>();
        barcodes = new ArrayList<>();
        businessname = (TextView) findViewById(R.id.businessname);
        prevOrdersList = (ListView) findViewById(R.id.prevOrdersList);
        adapter = new ArrayAdapter(this,
                android.R.layout.simple_list_item_1, barcodes);
        prevOrdersList.setAdapter(adapter);
        FinishScan = (Button) findViewById(R.id.FinishScan);
        Scan = (Button) findViewById(R.id.Scan);
        Enter = (Button) findViewById(R.id.Enter);
        Enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(Activity_business_scanned_orders.this);
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
                        allotment_list allotment_list1 = new allotment_list();
                        allotment_list1.setUsername(getIntent().getStringExtra("businessusername"));
                        allotment_list1.setValue(barcodevalue1);
                        if(!barcodes.contains(barcodevalue1)) {
                            arrBarcode.add(allotment_list1);
                            barcodes.add(barcodevalue1);
                        }
                        Log.d("MainActivity", "Scanned");
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        });
        Scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new IntentIntegrator(Activity_business_scanned_orders.this).initiateScan();

            }
        });

        businessname.setText(getIntent().getStringExtra("businessusername"));
        FinishScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                business_is_completePatch bis = new business_is_completePatch();
                bis.setIs_complete(true);
                mnetworkutils = new NetworkUtils(Activity_business_scanned_orders.this);
                mprogress = new ProgressDialog(Activity_business_scanned_orders.this);
                mprogress.setMessage("Please wait...");
                mprogress.setCancelable(false);
                mprogress.setIndeterminate(true);
                BarcodeAllotment barcodeAllotment = new BarcodeAllotment();
                barcodeAllotment.setObjects(arrBarcode);
                if (mnetworkutils.isnetconnected()) {
                    mprogress.show();
                    mnetworkutils.getapi().addbarcodes(barcodeAllotment, new Callback<Response>() {
                                @Override
                                public void success(Response response, Response response1) {
                                    if (mprogress.isShowing()) {
                                        mprogress.dismiss();
                                    }
                                    PickedupOrders po = new PickedupOrders();
                                    po.setName(getIntent().getStringExtra("Business_name"));
                                    po.setScannedorders(arrBarcode.size());
                                    po.setBusinessusername(getIntent().getStringExtra("businessusername"));
                                    po.setCancelledorders(0);
                                    po.setPickeduporders(0);

                                    DB_PreviousOrders db_previousOrders = new DB_PreviousOrders();
                                    db_previousOrders.AddToDB(po);
                                    Intent i = new Intent(Activity_business_scanned_orders.this,Activity_Orders.class);
                                    startActivity(i);
                                    finish();
                                }

                                @Override
                                public void failure(RetrofitError error) {
                                    if (mprogress.isShowing()) {
                                        mprogress.dismiss();
                                    }
                                    String json = new String(((TypedByteArray) error.getResponse().getBody()).getBytes());
                                }
                            }
                    );
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
             } else {
                allotment_list allotment_list1 = new allotment_list();
                allotment_list1.setUsername(getIntent().getStringExtra("businessusername"));
                allotment_list1.setValue(result.getContents());
                if(!barcodes.contains(result.getContents())) {
                    arrBarcode.add(allotment_list1);
                    barcodes.add(result.getContents());
                }
                Log.d("MainActivity", "Scanned");
                adapter.notifyDataSetChanged();

            }
        } else {
            Log.d("MainActivity", "Weird");
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

}
