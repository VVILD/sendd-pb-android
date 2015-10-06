package co.sendddelivery.Activity;

 import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.joda.time.DateTime;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import co.sendddelivery.GetterandSetter.AllOrders;
import co.sendddelivery.GetterandSetter.Business_Order;
import co.sendddelivery.GetterandSetter.Business_Shipment;
import co.sendddelivery.GetterandSetter.Customer_Order;
import co.sendddelivery.GetterandSetter.Customer_shipment;
import co.sendddelivery.GetterandSetter.Drop_address;
import co.sendddelivery.GetterandSetter.Pending_Orders;
import co.sendddelivery.GetterandSetter.business_is_completePatch;
import co.sendddelivery.R;
import co.sendddelivery.Utils.NetworkUtils;
import co.sendddelivery.Utils.Utils;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedByteArray;

public class Activity_Orders extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    private NetworkUtils mnetworkutils = new NetworkUtils(this);
    private PendingOrders_Adapter madapter;
    private ArrayList<AllOrders> allOrders;
    private ArrayList<Pending_Orders> Pending_Orders_List = new ArrayList<>();
    private Utils mUtils;
    public static Toolbar toolbar;
    private ProgressDialog mprogress;
    private Handler handler;
    private SwipeRefreshLayout swipeRefreshLayout;
    DateFormat format = new SimpleDateFormat("H:m:s", Locale.ENGLISH);
    DateFormat format2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH);
    int counter = 0;
    boolean shouldRestart= true;

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_allorders);
        mUtils = new Utils(this);
        updateOrders();
        Log.i("OnCreate","Called");
        final Utils utils = new Utils(Activity_Orders.this);
        DateTime dt = new DateTime();
        if (utils.getInt("date") != 0) {
            if (utils.getInt("date") < dt.getDayOfMonth()) {
                Utils.ClearPickedUpOrders();
                utils.setInt("date", dt.getDayOfMonth());
            }
        } else {
           utils.setInt("date", dt.getDayOfMonth());
        }

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);
        if (!isMyServiceRunning(LocationService.class)) {
            Intent intent = new Intent(this, LocationService.class);
            startService(intent);
        }
        toolbar = (Toolbar) findViewById(R.id.thankyou_activity);
        setSupportActionBar(toolbar);

        Button closeButton = (Button) findViewById(R.id.closeButton);
        Button refreshbutton = (Button) findViewById(R.id.refreshButton);

        refreshbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateOrders();
            }
        });
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(Activity_Orders.this)
                        .setTitle("Confirm")
                        .setMessage("Are you sure you want to logout?")
                        .setCancelable(false)
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // continue with delete
                                mUtils.setvalue("PhoneNumber", "");
                                mUtils.setvalue("LoggedIn", "No");
                                Intent i = new Intent(getApplicationContext(), Activity_Login.class);
                                startActivity(i);
                                shouldRestart = false;
                                Activity_Orders.this.overridePendingTransition(R.animator.pull_in_right, R.animator.push_out_left);
                                finish();
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

    }

    public class PendingOrders_holder {
        private TextView Order_Name;
        private TextView Locality;
        private TextView numberofShipments;
        private ImageView ScanBarcodes;
        private TextView PickupTime;
        private CheckBox isComplete;
    }

    //Set up Address Adapter
    public class PendingOrders_Adapter extends ArrayAdapter<AllOrders> {

        private Context c;
        private List<AllOrders> address_list;

        public PendingOrders_Adapter(Context context, int resource, List<AllOrders> objects) {
            super(context, resource, objects);
            this.c = context;
            this.address_list = objects;
        }


        @Override
        public void add(AllOrders object) {
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
        public int getViewTypeCount() {
            return 2;
        }

        @Override
        public int getItemViewType(int position) {
            if (address_list.get(position).getIsBusiness()) {
                return 1;
            } else {
                return 0;
            }
        }

        @Override
        public AllOrders getItem(int position) {
            return super.getItem(position);
        }

        @Override
        public int getPosition(AllOrders item) {
            return super.getPosition(item);
        }

        @Override
        public long getItemId(int position) {
            return super.getItemId(position);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final PendingOrders_holder pendingorders_holder;
            int type = getItemViewType(position);

            if (convertView == null) {
                //initialize holder
                if (type == 0) {
                    LayoutInflater inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    convertView = inflater.inflate(R.layout.list_item_orders_list, parent, false);
                } else {
                    LayoutInflater inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    convertView = inflater.inflate(R.layout.list_item_orders_list_businessc, parent, false);
                }
                pendingorders_holder = new PendingOrders_holder();
                pendingorders_holder.numberofShipments = (TextView) convertView.findViewById(R.id.number_of_shipments);
                pendingorders_holder.Order_Name = (TextView) convertView.findViewById(R.id.Order_Name);
                pendingorders_holder.PickupTime = (TextView) convertView.findViewById(R.id.PickupTime);
                pendingorders_holder.Locality = (TextView) convertView.findViewById(R.id.Locality);
                convertView.setTag(pendingorders_holder);
            } else {
                pendingorders_holder = (PendingOrders_holder) convertView.getTag();
            }
            if (type == 1) {
                pendingorders_holder.isComplete = (CheckBox) convertView.findViewById(R.id.isBusinessComplete);
                pendingorders_holder.ScanBarcodes = (ImageView) convertView.findViewById(R.id.ScanBarcode);
                pendingorders_holder.ScanBarcodes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(getApplicationContext(), Activity_business_scanned_orders.class);
                        i.putExtra("businessusername",address_list.get(position).getBusinessUserName());
                        i.putExtra("Business_name",address_list.get(position).getBusinessName());
                        startActivity(i);
                        shouldRestart= false;
                        finish();
                    }
                });
                pendingorders_holder.isComplete.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        if (b) {
                            new AlertDialog.Builder(Activity_Orders.this)
                                    .setTitle("Confirm")
                                    .setMessage("Are you sure you want to Finish" + address_list.get(position).getBusinessUserName() + "business")
                                    .setCancelable(false)
                                    .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {

                                            business_is_completePatch bis = new business_is_completePatch();
                                        bis.setIs_complete(true);
                                        mnetworkutils = new NetworkUtils(Activity_Orders.this);
                                        mprogress = new ProgressDialog(Activity_Orders.this);
                                        mprogress.setMessage("Please wait...");
                                        mprogress.setCancelable(false);
                                        mprogress.setIndeterminate(true);

                                        if (mnetworkutils.isnetconnected()) {
                                            mprogress.show();
                                            mnetworkutils.getapi().closeBusiness(address_list.get(position).getBusinessUserName(), bis, new Callback<Response>() {
                                                        @Override
                                                        public void success(Response response, Response response1) {
                                                            if (mprogress.isShowing()) {
                                                                mprogress.dismiss();
                                                            }

                                                            updateOrders();
                                                        }

                                                        @Override
                                                        public void failure(RetrofitError error) {
                                                            if (mprogress.isShowing()) {
                                                                mprogress.dismiss();
                                                            }
                                                        }
                                                    }
                                            );
                                        }

                                    }
                                    })
                                    .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                            pendingorders_holder.isComplete.setChecked(false);
                                        }
                                    })
                                    .show();
                        }
                    }
                });

            }

            //setup list objects
            if (address_list.get(position).getIsBusiness()) {
                pendingorders_holder.PickupTime.setBackgroundColor(getResources().getColor(R.color.lightgreen));
            } else {
                pendingorders_holder.PickupTime.setBackgroundColor(getResources().getColor(R.color.yellow));

            }
            //Log.i("address_list.get(position).getLocality()",address_list.get(position).getLocality());
            pendingorders_holder.numberofShipments.setText(address_list.get(position).getNumberofShipments());
            pendingorders_holder.Locality.setText(address_list.get(position).getLocality());
            pendingorders_holder.Order_Name.setText(address_list.get(position).getOrder_name());
            pendingorders_holder.PickupTime.setText(address_list.get(position).getPickupTime());

            return convertView;
        }
    }

    public void onResume() {
        super.onResume();
        shouldRestart =true;
        mUtils = new Utils(this);

        final Utils utils = new Utils(Activity_Orders.this);
        DateTime dt = new DateTime();
        if (utils.getInt("date") != 0) {
            if (utils.getInt("date") < dt.getDayOfMonth()) {
                Utils.ClearPickedUpOrders();
                utils.setInt("date", dt.getDayOfMonth());
            }
        } else {
            utils.setInt("date", dt.getDayOfMonth());
        }
        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
//                updateOrders();
                return true;
            }
        });
        Thread timer = new Thread() {
            public void run() {
                try {
                    sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    Message msg = handler.obtainMessage();
                    handler.sendMessage(msg);
                }
            }
        };
        timer.start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.previousShipment:
                Intent i = new Intent(getApplicationContext(), Activity_Pickedup_orders.class);
                startActivity(i);
                shouldRestart=false;
                finish();
                overridePendingTransition(R.animator.pull_in_right, R.animator.push_out_left);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public ArrayList<AllOrders> ShowAddressToList() throws ParseException {
        allOrders = new ArrayList<>();
        for (int i = 0; i < Pending_Orders_List.size(); i++) {

            AllOrders allorders = new AllOrders();
            Boolean flag = true;
            int j = 0;
            if (Pending_Orders_List.get(i).getIsBusiness()) {
                for (int l = 0; l < allOrders.size(); l++) {
                    // Log.i("Count" + Pending_Orders_List.get(i).getBusiness_Order().getB_business_name(), String.valueOf(counter++));
                    flag = !allOrders.get(l).getOrder_name().equals(Pending_Orders_List.get(i).getBusiness_Order().getB_business_name());

                }
                if (flag) {
                    for (int k = 0; k < Pending_Orders_List.size(); k++) {
                        if (Pending_Orders_List.get(k).getIsBusiness()) {
                            if (Pending_Orders_List.get(i).getBusiness_Order().getB_username().equals(Pending_Orders_List.get(k).getBusiness_Order().getB_username())) {
                                if(!Pending_Orders_List.get(k).getBusiness_Order().getOrder_id().equals("null")) {
                                    counter++;
                                }
                                Log.i("counter=", String.valueOf(counter));
                            }
                        }
                    }
                    allorders.setNumberofShipments(String.valueOf(counter));
                    allorders.setLocality(Pending_Orders_List.get(i).getBusiness_Order().getB_address());
                    allorders.setBusinessUserName(Pending_Orders_List.get(i).getBusiness_Order().getB_username());
                    allorders.setOrder_name(Pending_Orders_List.get(i).getBusiness_Order().getB_business_name());
                    Date date = format.parse(Pending_Orders_List.get(i).getBusiness_Order().getPickup_time());
                    String timeStamp = new SimpleDateFormat("hh:mm a", Locale.getDefault()).format(date);
                    allorders.setPickupTime(timeStamp);
                    allorders.setIsBusiness(true);
                    allorders.setBusinessName(Pending_Orders_List.get(i).getBusiness_Order().getB_business_name());
                    allOrders.add(allorders);
                    counter = 0;

                }
            } else {
                try {
                    allorders.setLocality(Pending_Orders_List.get(i).getCustomer_Order().getFlat_no() + " " + Pending_Orders_List.get(i).getCustomer_Order().getAddress());
                } catch (IndexOutOfBoundsException | NullPointerException e) {
                    allorders.setLocality("");
                }
                allorders.setNumberofShipments(String.valueOf(Pending_Orders_List.get(i).getCustomer_shipment().size()));
                allorders.setCO(Pending_Orders_List.get(i).getCustomer_Order());
                allorders.setCS(Pending_Orders_List.get(i).getCustomer_shipment());
                allorders.setOrder_name(Pending_Orders_List.get(i).getCustomer_Order().getName());
                Date date = format.parse(Pending_Orders_List.get(i).getCustomer_Order().getPickup_time());
                String timeStamp = new SimpleDateFormat("hh:mm a", Locale.getDefault()).format(date);
                allorders.setPickupTime(timeStamp);
                allorders.setIsBusiness(false);
                allOrders.add(allorders);
            }
        }

        return allOrders;
    }

    public void onPause() {
        super.onPause();
     }

    public void updateOrders() {
        mprogress = new ProgressDialog(this);
        mprogress.setMessage("Please wait..");
        mprogress.setCancelable(false);
        mprogress.setIndeterminate(true);
        if (!mprogress.isShowing()) {
            Pending_Orders_List.clear();
            mUtils = new Utils(this);
            Utils utils = new Utils(this);
            if (mnetworkutils.isnetconnected()) {
                mprogress.show();
                mnetworkutils.getapi().getOrders(utils.getvalue("PhoneNumber"), new Callback<Response>() {
                            @Override
                            public void success(Response response, Response response2) {
                                mprogress.dismiss();
                                swipeRefreshLayout.setRefreshing(false);

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
                                        Log.i("B2b or b2c", booking.getString("type"));
                                        if (booking.getString("type").equals("b2b")) {
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
                                            BO.setB_pincode(Business_order.getString("b_pincode"));
                                            BO.setB_state(Business_order.getString("b_state"));
                                            BO.setB_username(Business_order.getString("b_username"));
                                            BO.setName(Business_order.getString("name"));
                                            BO.setOrder_id(Business_order.getString("order_id"));
                                            BO.setPhone(Business_order.getString("phone"));
                                            BO.setPickup_time(Business_order.getString("pickup_time"));
                                            BO.setPincode(Business_order.getString("pincode"));

                                            try {
                                                Date date = format2.parse(Business_order.getString("book_time"));
                                                String timeStamp = new SimpleDateFormat("dd-MMM", Locale.getDefault()).format(date);
                                                BO.setOrderdate(timeStamp);
                                            } catch (ParseException e) {

                                            }
                                            BO.setIsComplete(false);
                                            if(booking.getJSONArray("shipments") != null) {
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

                                                PO.setBusiness_Shipment(Business_Shpiment_List);
                                            }
                                            PO.setBusiness_Order(BO);
                                            Pending_Orders_List.add(PO);
                                        } else {
                                            PO.setIsBusiness(false);

                                            JSONObject Customer_order = booking.getJSONObject("order");
                                            Customer_Order CO = new Customer_Order();
                                            CO.setAddress(Customer_order.getString("address"));
                                            CO.setFlat_no(Customer_order.getString("flat_no"));
                                            CO.setName(Customer_order.getString("name"));
                                            CO.setPincode(Customer_order.getString("pincode"));
                                            CO.setPickup_time(Customer_order.getString("pickup_time"));
                                            CO.setUser(Customer_order.getString("user"));
                                            if (Customer_order.getString("promocode_type") != null)
                                                CO.setPromocode_type(Customer_order.getString("promocode_type"));
                                            else
                                                CO.setPromocode_type("NO CODE");

                                            if (Customer_order.getString("promocode_amount") != null )
                                                CO.setPromocode_amount(Customer_order.getString("promocode_amount"));
                                            else
                                                CO.setPromocode_amount("0");

                                            if (Customer_order.getString("promocode_msg") != null)
                                                CO.setPromocode_msg(Customer_order.getString("promocode_msg"));
                                            else
                                                CO.setPromocode_msg("");

                                            if (Customer_order.getString("promocode_code") != null)
                                                CO.setPromocode_code(Customer_order.getString("promocode_code"));
                                            else
                                                CO.setPromocode_code("");

                                            ArrayList<Customer_shipment> Customer_Shpiment_List = new ArrayList<>();
                                            JSONArray Customer_shipment = booking.getJSONArray("shipments");
                                            for (int k = 0; k < Customer_shipment.length(); k++) {
                                                Customer_shipment CS = new Customer_shipment();
                                                Drop_address DA = new Drop_address();
                                                CS.setPrice(Customer_shipment.getJSONObject(k).getString("price"));
                                                CS.setCategory(Customer_shipment.getJSONObject(k).getString("category"));
                                                CS.setCost_of_courier(Customer_shipment.getJSONObject(k).getString("cost_of_courier"));

                                                DA.setDrop_address_pk(Customer_shipment.getJSONObject(k).getJSONObject("drop_address").getString("drop_address_pk"));
                                                DA.setDrop_address_city(Customer_shipment.getJSONObject(k).getJSONObject("drop_address").getString("drop_address_city"));
                                                DA.setDrop_address_country(Customer_shipment.getJSONObject(k).getJSONObject("drop_address").getString("drop_address_country"));
                                                DA.setDrop_address_flat_no(Customer_shipment.getJSONObject(k).getJSONObject("drop_address").getString("drop_address_flat_no"));
                                                DA.setDrop_address_locality(Customer_shipment.getJSONObject(k).getJSONObject("drop_address").getString("drop_address_locality"));
                                                DA.setDrop_address_pincode(Customer_shipment.getJSONObject(k).getJSONObject("drop_address").getString("drop_address_pincode"));
                                                DA.setDrop_address_state(Customer_shipment.getJSONObject(k).getJSONObject("drop_address").getString("drop_address_state"));

                                                CS.setReal_tracking_no(Customer_shipment.getJSONObject(k).getString("real_tracking_no"));
                                                CS.setDrop_address(DA);
                                                CS.setDrop_name(Customer_shipment.getJSONObject(k).getString("drop_name"));
                                                CS.setDrop_phone(Customer_shipment.getJSONObject(k).getString("drop_phone"));
                                                CS.setImg(Customer_shipment.getJSONObject(k).getString("img"));
                                                CS.setItem_name(Customer_shipment.getJSONObject(k).getString("item_name"));
                                                CS.setWeight(Customer_shipment.getJSONObject(k).getString("weight"));

                                                Customer_Shpiment_List.add(CS);
                                            }
                                            PO.setCustomer_Order(CO);
                                            PO.setCustomer_shipment(Customer_Shpiment_List);
                                            Pending_Orders_List.add(PO);
                                        }
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                ListView lv_Saved_Address = (ListView) findViewById(R.id.allordersListView);
                                try {
                                    madapter = new PendingOrders_Adapter(Activity_Orders.this, R.layout.list_item_orders_list, ShowAddressToList());
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                lv_Saved_Address.setAdapter(madapter);
                                lv_Saved_Address.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                                        if (allOrders.get(position).getIsBusiness()) {
                                            if(Integer.parseInt(allOrders.get(position).getNumberofShipments()) != 0) {
                                                Intent i = new Intent(getApplicationContext(), Business_orders_sublist.class);
                                                Gson GS = new Gson();
                                                String PO = GS.toJson(Pending_Orders_List);
                                                i.putExtra("Business_username", allOrders.get(position).getBusinessUserName());
                                                i.putExtra("PendingOrderList", PO);
                                                startActivity(i);
                                                shouldRestart=false;
                                                finish();
                                                Activity_Orders.this.overridePendingTransition(R.animator.pull_in_right, R.animator.push_out_left);
                                            }
                                        } else {
                                            Intent i = new Intent(getApplicationContext(), Customer_OrderDetails.class);
                                            Gson GS = new Gson();
                                            String CO = GS.toJson(allOrders.get(position).getCO());
                                            String CS = GS.toJson(allOrders.get(position).getCS());
                                            i.putExtra("Customer_order_object", CO);
                                            i.putExtra("Customer_shipment_object", CS);
                                            startActivity(i);
                                            shouldRestart =false;
                                            finish();
                                            Activity_Orders.this.overridePendingTransition(R.animator.pull_in_right, R.animator.push_out_left);

                                        }
                                    }
                                });
                            }

                            @Override
                            public void failure(RetrofitError error) {
                                mprogress.dismiss();
                                swipeRefreshLayout.setRefreshing(false);
                            }
                        }
                );
            } else {
                Toast.makeText(this, "Please Connect to a working Internet Connection", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void onDestroy() {
        super.onDestroy();
        stopService(new Intent(this, LocationService.class));
    }

    public void onBackPressed() {
        super.onBackPressed();
        Log.i("onBackPressed", "onBackPressed");
        if (mprogress.isShowing()) {
            mprogress.dismiss();
        }
    }

    @Override
    public void onRefresh() {
        updateOrders();
    }
}