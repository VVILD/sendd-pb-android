package co.sendddelivery.Activity;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import co.sendddelivery.GetterandSetter.BusinessAllOrders;
import co.sendddelivery.GetterandSetter.Business_Order;
import co.sendddelivery.GetterandSetter.Business_Shipment;
import co.sendddelivery.GetterandSetter.Pending_Orders;
import co.sendddelivery.R;
import co.sendddelivery.Utils.NetworkUtils;
import co.sendddelivery.Utils.Utils;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedByteArray;

public class Business_orders_sublist extends Activity {
    List<Pending_Orders> mPending_Order_list;
    ArrayList<BusinessAllOrders> BusinessallOrders;
    BusinessOrders_Adapter madapter;
    String businessUserName;
    ArrayList<BusinessAllOrders> BAO;
    Button scanQRCODE,EnterQRCode,detailsButton;
    ArrayList<Pending_Orders> Pending_Orders_List = new ArrayList<>();
    NetworkUtils mnetworkutils = new NetworkUtils(this);
    ProgressDialog mprogress;
    Utils utils, mUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_orders_sublist);

        scanQRCODE = (Button) findViewById(R.id.bScan_order);
        EnterQRCode = (Button)findViewById(R.id.bEnter_Code);
        detailsButton = (Button)findViewById(R.id.detailsButton);

        final String ForwardIntent_UserName = getIntent().getStringExtra("Business_username");
        final String ForwardIntent_POL = getIntent().getStringExtra("PendingOrderList");

        detailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Dialog dialog = new Dialog(Business_orders_sublist.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_details_business);
                dialog.show();
                TextView nameLabel = (TextView) dialog.findViewById(R.id.nameLabel);
                TextView addressLabel = (TextView) dialog.findViewById(R.id.addressLabel);
                TextView Pickuptime = (TextView) dialog.findViewById(R.id.pickuptimeLable);
                TextView phoneLabel = (TextView) dialog.findViewById(R.id.phoneLabel);
                Button Dismiss = (Button) dialog.findViewById(R.id.Dismiss_dialog);
                Dismiss.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                phoneLabel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent callIntent = new Intent(Intent.ACTION_CALL);
                        if(BAO.get(0).getBO().getB_contact_mob() != null) {
                            callIntent.setData(Uri.parse("tel:" + BAO.get(0).getBO().getB_contact_mob()));
                        }
                        startActivity(callIntent);
                    }
                });
                Pickuptime.setText(BAO.get(0).getBO().getPickup_time());
                nameLabel.setText(BAO.get(0).getBO().getB_business_name());
                addressLabel.setText(BAO.get(0).getBO().getB_address());
                phoneLabel.setText(BAO.get(0).getBO().getB_contact_mob() + " , " + BAO.get(0).getBO().getB_contact_office());
            }
        });
        final String pendingOrders = getIntent().getStringExtra("PendingOrderList");
        businessUserName = getIntent().getStringExtra("Business_username");
        Gson GS = new Gson();
        mPending_Order_list = Arrays.asList(GS.fromJson(pendingOrders, Pending_Orders[].class));
        BAO = ShowAddressToList();
        if (BAO.size() == 0) {
            Intent i = new Intent(getApplicationContext(), Activity_Orders.class);
            startActivity(i);
            finish();
        }
        ListView lv_Saved_Address = (ListView) findViewById(R.id.businesssubordersListView);
        madapter = new BusinessOrders_Adapter(Business_orders_sublist.this, R.layout.businessorders_list_item_list, ShowAddressToList());
        lv_Saved_Address.setAdapter(madapter);
        lv_Saved_Address.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                Intent i = new Intent(getApplicationContext(), Business_order_details.class);
                Gson GS = new Gson();
                String BS = GS.toJson(BAO.get(position).getBS());
                String BO = GS.toJson(BAO.get(position).getBO());
                i.putExtra("Business_Shipment", BS);
                i.putExtra("Business_Order", BO);
                i.putExtra("position", position);
                i.putExtra("pendingOrderId", BAO.get(position).getPendingOrderId());
                i.putExtra("OrderName", BAO.get(position).getOrder_name());
                i.putExtra("Business_username", ForwardIntent_UserName);
                i.putExtra("PendingOrderList", ForwardIntent_POL);
                startActivity(i);
                finish();
                Business_orders_sublist.this.overridePendingTransition(R.animator.pull_in_right, R.animator.push_out_left);


            }
        });
        scanQRCODE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new IntentIntegrator(Business_orders_sublist.this).initiateScan();

            }
        });
        EnterQRCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(Business_orders_sublist.this);
                dialog.setTitle("Enter QR Code");
                dialog.setContentView(R.layout.dialog_barcode);
                dialog.getWindow().setBackgroundDrawable((new ColorDrawable(Color.WHITE)));
                dialog.show();
                final EditText etBarcodeValue = (EditText) dialog.findViewById(R.id.etBarcodeEntry);
                Button getDetails = (Button) dialog.findViewById(R.id.bGetDetails);
                getDetails.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final String barcodevalue1 = etBarcodeValue.getText().toString();
                        mUtils = new Utils(Business_orders_sublist.this);
                        utils = new Utils(Business_orders_sublist.this);
                        mprogress = new ProgressDialog(Business_orders_sublist.this);
                        mprogress.setMessage("Please wait.");
                        mprogress.setCancelable(false);
                        mprogress.setIndeterminate(true);
                        if (mnetworkutils.isnetconnected()) {
                            mprogress.show();
                            Log.i("barcodevalue1=", barcodevalue1);
                            mnetworkutils.getapi().getorder(barcodevalue1, new Callback<Response>() {
                                        @Override
                                        public void success(Response response, Response response2) {
                                            if (mprogress.isShowing()) {
                                                mprogress.dismiss();
                                            }

                                            BufferedReader reader;
                                            StringBuilder sb = new StringBuilder();
                                            try {
                                                reader = new BufferedReader(new InputStreamReader(response.getBody().in()));
                                                String line;
                                                try {
                                                    while ((line = reader.readLine()) != null) {
                                                        sb.append(line);
                                                    }
                                                } catch (IOException e) {
                                                    e.printStackTrace();
                                                }
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }
                                            String result = sb.toString();
                                            Log.i("ultresult", result);
                                            try {
                                                JSONObject jObj = new JSONObject(result);
                                                JSONArray pending_orders = jObj.getJSONArray("pending_orders");
                                                for (int j = 0; j < pending_orders.length(); j++) {
                                                    Pending_Orders PO = new Pending_Orders();
                                                    JSONObject booking = pending_orders.getJSONObject(j);
                                                    PO.setIsBusiness(true);
                                                    JSONObject Business_order = booking.getJSONObject("order");
                                                    Business_Order BO = new Business_Order();
                                                    BO.setAddress1(Business_order.getString("address1"));
                                                    BO.setAddress2(Business_order.getString("address2"));
                                                    BO.setB_address(Business_order.getString("b_address"));
                                                    BO.setB_business_name(Business_order.getString("b_business_name"));
                                                    BO.setB_city(Business_order.getString("b_city"));
                                                    BO.setB_contact_mob(Business_order.getString("b_contact_mob"));
                                                    BO.setB_contact_office(Business_order.getString("b_contact_office"));
                                                    BO.setB_name(Business_order.getString("b_name"));
                                                    BO.setOrder_id(Business_order.getString("order_id"));
                                                    BO.setPickup_time(Business_order.getString("pickup_time"));
                                                    BO.setB_pincode(Business_order.getString("b_pincode"));
                                                    BO.setB_state(Business_order.getString("b_state"));
                                                    BO.setIsComplete(false);
                                                    BO.setName(Business_order.getString("name"));
                                                    BO.setPhone(Business_order.getString("phone"));
                                                    BO.setB_username(Business_order.getString("b_username"));
                                                    BO.setB_pincode(Business_order.getString("pincode"));
                                                    ArrayList<Business_Shipment> Business_Shpiment_List = new ArrayList<>();
                                                    JSONArray Business_shipment = booking.getJSONArray("shipments");
                                                    for (int k = 0; k < Business_shipment.length(); k++) {
                                                        Business_Shipment BS = new Business_Shipment();
                                                        BS.setName(Business_shipment.getJSONObject(k).getString("name"));
                                                        BS.setPrice(Business_shipment.getJSONObject(k).getString("price"));
                                                        BS.setQuantity(Business_shipment.getJSONObject(k).getString("quantity"));
                                                        BS.setReal_tracking_no(Business_shipment.getJSONObject(k).getString("real_tracking_no"));
                                                        BS.setShipping_cost(Business_shipment.getJSONObject(k).getString("shipping_cost"));
                                                        BS.setSku(Business_shipment.getJSONObject(k).getString("sku"));
                                                        BS.setWeight(Business_shipment.getJSONObject(k).getString("weight"));
                                                        Business_Shpiment_List.add(BS);
                                                    }
                                                    PO.setBusiness_Order(BO);
                                                    PO.setBusiness_Shipment(Business_Shpiment_List);
                                                    Pending_Orders_List.add(PO);
                                                }
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                            Intent i = new Intent(getApplicationContext(), Business_order_details_fromBarcode.class);
                                            i.putExtra("barcodevalue", barcodevalue1);
                                            Gson GS = new Gson();
                                            String BS = GS.toJson(Pending_Orders_List.get(0).getBusiness_Shipment());
                                            String pol = GS.toJson(Pending_Orders_List);
                                            i.putExtra("Business_Shipment", BS);
                                            i.putExtra("Business_username", Pending_Orders_List.get(0).getBusiness_Order().getB_username());
                                            i.putExtra("PendingOrderList", pol);
                                            startActivity(i);
                                            finish();
                                            Business_orders_sublist.this.overridePendingTransition(R.animator.pull_in_right, R.animator.push_out_left);

                                            dialog.dismiss();

                                        }


                                        @Override
                                        public void failure(RetrofitError error) {
                                            if (mprogress.isShowing()) {
                                                mprogress.dismiss();
                                            }
                                            String json = new String(((TypedByteArray) error.getResponse().getBody()).getBytes());
                                            Log.v("failure", json);
                                            Log.i("qwertyuiopajklzxcvbnm,", error.toString());

                                        }
                                    }

                            );
                        } else {
                            if (mprogress.isShowing()) {
                                mprogress.dismiss();
                            }
                            Toast.makeText(Business_orders_sublist.this, "Please Connect to a working Internet Connection", Toast.LENGTH_LONG).show();
                        }

                    }
                });
            }
        });
    }

    public class BusinessOrder_holder {
        private TextView Order_Name;
    }

    //Set up Address Adapter
    public class BusinessOrders_Adapter extends ArrayAdapter<BusinessAllOrders> {

        private Context c;
        private List<BusinessAllOrders> address_list;

        public BusinessOrders_Adapter(Context context, int resource, List<BusinessAllOrders> objects) {
            super(context, resource, objects);
            this.c = context;
            this.address_list = objects;
        }


        @Override
        public void add(BusinessAllOrders object) {
            super.add(object);
        }

        @Override
        public void notifyDataSetChanged() {
            super.notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return address_list.size();
        }

        @Override
        public BusinessAllOrders getItem(int position) {
            return super.getItem(position);
        }

        @Override
        public int getPosition(BusinessAllOrders item) {
            return super.getPosition(item);
        }

        @Override
        public long getItemId(int position) {
            return super.getItemId(position);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            BusinessOrder_holder Business_holder;
            if (convertView == null) {
                //initialize holder
                Business_holder = new BusinessOrder_holder();
                LayoutInflater inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.businessorders_list_item_list, parent, false);
                Business_holder.Order_Name = (TextView) convertView.findViewById(R.id.Order_Name);
                convertView.setTag(Business_holder);
            } else {
                Business_holder = (BusinessOrder_holder) convertView.getTag();
            }
            Business_holder.Order_Name.setText(address_list.get(position).getOrder_name());

            return convertView;
        }
    }

    public ArrayList<BusinessAllOrders> ShowAddressToList() {


        int pendingorderId = 0;
        BusinessallOrders = new ArrayList<>();
        for (int i = 0; i < mPending_Order_list.size(); i++) {
            BusinessAllOrders allorders = new BusinessAllOrders();

            if (mPending_Order_list.get(i).getIsBusiness()) {
                if (mPending_Order_list.get(i).getBusiness_Order().getB_username().equals(businessUserName)) {
                    if (!mPending_Order_list.get(i).getBusiness_Order().getIsComplete()) {
                        allorders.setOrder_name("Order-" + mPending_Order_list.get(i).getBusiness_Order().getOrder_id() + "   " + mPending_Order_list.get(i).getBusiness_Order().getName());
                        allorders.setBO(mPending_Order_list.get(i).getBusiness_Order());
                        allorders.setBS(mPending_Order_list.get(i).getBusiness_Shipment());
                        allorders.setPendingOrderId(pendingorderId);
                        BusinessallOrders.add(allorders);
                    }
                }
            }
            pendingorderId++;
        }

        return BusinessallOrders;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        final String barcodevalue;
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Log.d("MainActivity", "Cancelled scan");
            } else {
                Toast.makeText(Business_orders_sublist.this,result.getContents(),Toast.LENGTH_LONG).show();
                Log.d("MainActivity", "Scanned");
                mUtils = new Utils(Business_orders_sublist.this);
                utils = new Utils(Business_orders_sublist.this);
                mprogress = new ProgressDialog(Business_orders_sublist.this);
                mprogress.setMessage("Please wait.");
                mprogress.setCancelable(false);
                mprogress.setIndeterminate(true);
                if (mnetworkutils.isnetconnected()) {
                    barcodevalue = result.getContents();
                    mprogress.show();
                    mnetworkutils.getapi().getorder(barcodevalue, new Callback<Response>() {
                                @Override
                                public void success(Response response, Response response2) {
                                    if (mprogress.isShowing()) {
                                        mprogress.dismiss();
                                    }

                                    BufferedReader reader;
                                    StringBuilder sb = new StringBuilder();
                                    try {
                                        reader = new BufferedReader(new InputStreamReader(response.getBody().in()));
                                        String line;
                                        try {
                                            while ((line = reader.readLine()) != null) {
                                                sb.append(line);
                                            }
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    String result = sb.toString();
                                    Log.i("ultresult", result);
                                    try {
                                        JSONObject jObj = new JSONObject(result);
                                        JSONArray pending_orders = jObj.getJSONArray("pending_orders");
                                        for (int j = 0; j < pending_orders.length(); j++) {
                                            Pending_Orders PO = new Pending_Orders();
                                            JSONObject booking = pending_orders.getJSONObject(j);
                                            PO.setIsBusiness(true);
                                            JSONObject Business_order = booking.getJSONObject("order");
                                            Business_Order BO = new Business_Order();
                                            BO.setAddress1(Business_order.getString("address1"));
                                            BO.setAddress2(Business_order.getString("address2"));
                                            BO.setB_address(Business_order.getString("b_address"));
                                            BO.setB_business_name(Business_order.getString("b_business_name"));
                                            BO.setB_city(Business_order.getString("b_city"));
                                            BO.setB_contact_mob(Business_order.getString("b_contact_mob"));
                                            BO.setB_contact_office(Business_order.getString("b_contact_office"));
                                            BO.setB_name(Business_order.getString("b_name"));
                                            BO.setOrder_id(Business_order.getString("order_id"));
                                            BO.setPickup_time(Business_order.getString("pickup_time"));
                                            BO.setB_pincode(Business_order.getString("b_pincode"));
                                            BO.setB_state(Business_order.getString("b_state"));
                                            BO.setIsComplete(false);
                                            BO.setName(Business_order.getString("name"));
                                            BO.setPhone(Business_order.getString("phone"));
                                            BO.setB_username(Business_order.getString("b_username"));
                                            BO.setB_pincode(Business_order.getString("pincode"));
                                            ArrayList<Business_Shipment> Business_Shpiment_List = new ArrayList<>();
                                            JSONArray Business_shipment = booking.getJSONArray("shipments");
                                            for (int k = 0; k < Business_shipment.length(); k++) {
                                                Business_Shipment BS = new Business_Shipment();
                                                BS.setName(Business_shipment.getJSONObject(k).getString("name"));
                                                BS.setPrice(Business_shipment.getJSONObject(k).getString("price"));
                                                BS.setQuantity(Business_shipment.getJSONObject(k).getString("quantity"));
                                                BS.setReal_tracking_no(Business_shipment.getJSONObject(k).getString("real_tracking_no"));
                                                BS.setShipping_cost(Business_shipment.getJSONObject(k).getString("shipping_cost"));
                                                BS.setSku(Business_shipment.getJSONObject(k).getString("sku"));
                                                BS.setWeight(Business_shipment.getJSONObject(k).getString("weight"));
                                                Business_Shpiment_List.add(BS);
                                            }
                                            PO.setBusiness_Order(BO);
                                            PO.setBusiness_Shipment(Business_Shpiment_List);
                                            Pending_Orders_List.add(PO);
                                         }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    Intent i = new Intent(getApplicationContext(), Business_order_details_fromBarcode.class);

                                    i.putExtra("barcodevalue", barcodevalue);
                                    Gson GS = new Gson();
                                    String BS = GS.toJson(Pending_Orders_List.get(0).getBusiness_Shipment());
                                    String pol = GS.toJson(Pending_Orders_List);
                                    i.putExtra("Business_Shipment", BS);
                                    i.putExtra("Business_username", Pending_Orders_List.get(0).getBusiness_Order().getB_username());
                                    i.putExtra("PendingOrderList", pol);
                                    startActivity(i);
                                    finish();
                                    Business_orders_sublist.this.overridePendingTransition(R.animator.pull_in_right, R.animator.push_out_left);
                                }


                                @Override
                                public void failure(RetrofitError error) {
                                    if (mprogress.isShowing()) {
                                        mprogress.dismiss();
                                    }
                                    Toast.makeText(Business_orders_sublist.this,"Barcode Already Exist",Toast.LENGTH_LONG).show();
                                    String json = new String(((TypedByteArray) error.getResponse().getBody()).getBytes());
                                    Log.v("failure", json);
                                    Log.i("qwertyuiopajklzxcvbnm,", error.toString());

                                }
                            }

                    );
                } else {
                    if (mprogress.isShowing()) {
                        mprogress.dismiss();
                    }
                    Toast.makeText(Business_orders_sublist.this, "Please Connect to a working Internet Connection", Toast.LENGTH_LONG).show();
                }

            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        Business_orders_sublist.this.overridePendingTransition(R.animator.pull_in_left, R.animator.push_out_right);
    }
}

