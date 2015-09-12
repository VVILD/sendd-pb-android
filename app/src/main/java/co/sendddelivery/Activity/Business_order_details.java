package co.sendddelivery.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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

import co.sendddelivery.Databases.DB_PreviousOrders;
import co.sendddelivery.GetterandSetter.BusinessPatch;
import co.sendddelivery.GetterandSetter.Business_Shipment;
import co.sendddelivery.GetterandSetter.Pending_Orders;
import co.sendddelivery.GetterandSetter.PickedupOrders;
import co.sendddelivery.R;
import co.sendddelivery.Utils.NetworkUtils;
import co.sendddelivery.Utils.Utils;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedByteArray;

public class Business_order_details extends Activity {
    List<Business_Shipment> mBusiness_Shipment;
    Button QrScan, EnterQrcode;
    int counter = 0, shipmentnumber = 1;
    NetworkUtils mnetworkutils;
    Utils mUtils;
    ProgressDialog mprogress;
    ArrayList<Pending_Orders> mPending_Order_list;
    TextView totalshipment, currentshipment;
    String BarcodeValue;

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
        EnterQrcode = (Button) findViewById(R.id.enterQRCode);
        Button Cancel = (Button) findViewById(R.id.Cancel);
        Button Submit = (Button) findViewById(R.id.Submit);
        QrScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new IntentIntegrator(Business_order_details.this).initiateScan();
            }
        });
        EnterQrcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(Business_order_details.this);
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
        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new AlertDialog.Builder(Business_order_details.this)
                        .setTitle("Confirm")
                        .setMessage("Are you sure you want to Cancel?")
                        .setCancelable(false)
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                BusinessPatch Bp = new BusinessPatch();
                                Bp.setStatus("CA");
                                mnetworkutils = new NetworkUtils(Business_order_details.this);
                                mprogress = new ProgressDialog(Business_order_details.this);
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
                                                    counter++;
                                                    shipmentnumber++;


                                                    if (counter < mBusiness_Shipment.size()) {
                                                        currentshipment.setText("Shipment number " + shipmentnumber);
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
                                                        QrScan.setText("Scan Qr Code");
                                                    } else {
                                                        counter = 0;
                                                        Intent i = new Intent(getApplicationContext(), Business_orders_sublist.class);
                                                        String ForwardIntent_UserName = getIntent().getStringExtra("Business_username");
                                                        String ForwardIntent_POL = getIntent().getStringExtra("PendingOrderList");
                                                        mPending_Order_list = new ArrayList<>(Arrays.asList(GS.fromJson(ForwardIntent_POL, Pending_Orders[].class)));
                                                        mPending_Order_list.remove(getIntent().getIntExtra("pendingOrderId", 0));
                                                        String pendingorderupdated = GS.toJson(mPending_Order_list);
                                                        i.putExtra("Business_username", ForwardIntent_UserName);
                                                        i.putExtra("PendingOrderList", pendingorderupdated);
                                                        i.putExtra("OrderName", getIntent().getStringExtra("OrderName"));
                                                        startActivity(i);
                                                        finish();
                                                        Business_order_details.this.overridePendingTransition(R.animator.pull_in_right, R.animator.push_out_left);

                                                    }

                                                }

                                                @Override
                                                public void failure(RetrofitError error) {
                                                    if (mprogress.isShowing()) {
                                                        mprogress.dismiss();
                                                    }
                                                    Toast.makeText(Business_order_details.this, "CANCEL did not process", Toast.LENGTH_LONG).show();
                                                }
                                            }

                                    );
                                } else {
                                    Toast.makeText(Business_order_details.this, "Please Connect to a working Internet Connection", Toast.LENGTH_LONG).show();
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
        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (BarcodeValue != null) {
                    if (BarcodeValue.length() >= 10 && BarcodeValue.length() <= 12) {
                        BusinessPatch Bp = new BusinessPatch();
                        Bp.setStatus("PU");
                        Bp.setBarcode(BarcodeValue);
                        mnetworkutils = new NetworkUtils(Business_order_details.this);
                        mprogress = new ProgressDialog(Business_order_details.this);
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
                                            Log.i("response1", String.valueOf(response1.getStatus()));
                                            counter++;
                                            shipmentnumber++;
                                            PickedupOrders po = new PickedupOrders();
                                            po.setName(getIntent().getStringExtra("Business_name"));
                                            po.setScannedorders(0);
                                            po.setBusinessusername(getIntent().getStringExtra("Business_username"));
                                            po.setCancelledorders(0);
                                            po.setPickeduporders(1);


                                            DB_PreviousOrders db_previousOrders = new DB_PreviousOrders();
                                            db_previousOrders.AddToDB(po);
                                            if (counter < mBusiness_Shipment.size()) {
                                                currentshipment.setText("Shipment number " + shipmentnumber);
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
                                                QrScan.setText("Scan Qr Code");
                                            } else {

                                                counter = 0;
                                                Intent i = new Intent(getApplicationContext(), Business_orders_sublist.class);
                                                String ForwardIntent_UserName = getIntent().getStringExtra("Business_username");
                                                String ForwardIntent_POL = getIntent().getStringExtra("PendingOrderList");
                                                mPending_Order_list = new ArrayList<>(Arrays.asList(GS.fromJson(ForwardIntent_POL, Pending_Orders[].class)));
                                                mPending_Order_list.remove(getIntent().getIntExtra("pendingOrderId", 0));
                                                Log.i("asdfafs", String.valueOf(mPending_Order_list.size()));
                                                String pendingorderupdated = GS.toJson(mPending_Order_list);
                                                i.putExtra("Business_username", ForwardIntent_UserName);
                                                i.putExtra("PendingOrderList", pendingorderupdated);
                                                i.putExtra("OrderName", getIntent().getStringExtra("OrderName"));
                                                startActivity(i);
                                                finish();
                                                Business_order_details.this.overridePendingTransition(R.animator.pull_in_right, R.animator.push_out_left);

                                            }

                                        }

                                        @Override
                                        public void failure(RetrofitError error) {
                                            if (mprogress.isShowing()) {
                                                mprogress.dismiss();
                                            }
                                            Toast.makeText(Business_order_details.this, "Order did not process. Please check Barcode", Toast.LENGTH_LONG).show();
                                        }
                                    }

                            );
                        } else {
                            Toast.makeText(Business_order_details.this, "Please Connect to a working Internet Connection", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(Business_order_details.this, "Please Choose a correct barcode value", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(Business_order_details.this, "Please Choose a correct barcode value", Toast.LENGTH_LONG).show();
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Log.d("MainActivity", "Cancelled scan");
            } else {
                Log.d("MainActivity", "Scanned");
                QrScan.setText(result.getContents());
                BarcodeValue = result.getContents();

            }
        } else {
            Log.d("MainActivity", "Weird");
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        Business_order_details.this.overridePendingTransition(R.animator.pull_in_left, R.animator.push_out_right);
    }
}
