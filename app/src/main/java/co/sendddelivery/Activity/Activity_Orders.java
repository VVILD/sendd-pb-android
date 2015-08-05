package co.sendddelivery.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import co.sendddelivery.GetterandSetter.AllOrders;
import co.sendddelivery.GetterandSetter.Business_Order;
import co.sendddelivery.GetterandSetter.Business_Shipment;
import co.sendddelivery.GetterandSetter.Customer_Order;
import co.sendddelivery.GetterandSetter.Customer_shipment;
import co.sendddelivery.GetterandSetter.Drop_address;
import co.sendddelivery.GetterandSetter.Pending_Orders;
import co.sendddelivery.R;
import co.sendddelivery.Utils.NetworkUtils;
import co.sendddelivery.Utils.Utils;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedByteArray;

public class Activity_Orders extends Activity {
    private NetworkUtils mnetworkutils = new NetworkUtils(this);
    private ProgressDialog mprogress;
    private PendingOrders_Adapter madapter;
    private ArrayList<AllOrders> allOrders;
    private ArrayList<Pending_Orders> Pending_Orders_List = new ArrayList<>();
    private Utils mUtils;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_allorders);
        mUtils = new Utils(this);
        Intent intent = new Intent(this, LocationService.class);
        startService(intent);
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
                mUtils.setvalue("PhoneNumber", "");
                mUtils.setvalue("LoggedIn", "No");
                Intent i = new Intent(getApplicationContext(), Activity_Login.class);
                startActivity(i);
                Activity_Orders.this.overridePendingTransition(R.animator.pull_in_right, R.animator.push_out_left);
                finish();
            }
        });

    }

    public class PendingOrders_holder {
        private TextView Order_Name;
        private TextView PickupTime;
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
        public View getView(int position, View convertView, ViewGroup parent) {
            PendingOrders_holder pendingorders_holder;
            if (convertView == null) {
                //initialize holder
                pendingorders_holder = new PendingOrders_holder();
                LayoutInflater inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.list_item_orders_list, parent, false);

                pendingorders_holder.Order_Name = (TextView) convertView.findViewById(R.id.Order_Name);
                pendingorders_holder.PickupTime = (TextView) convertView.findViewById(R.id.PickupTime);
                convertView.setTag(pendingorders_holder);
            } else {
                pendingorders_holder = (PendingOrders_holder) convertView.getTag();
            }
            //setup list objects
            pendingorders_holder.Order_Name.setText(address_list.get(position).getOrder_name());
            pendingorders_holder.PickupTime.setText(address_list.get(position).getPickupTime());

            return convertView;
        }
        //Get all Saved Addresses in Address_Receiver DB.


    }

    public void onResume() {
        super.onResume();
        Log.i("onResume","onResume");
        updateOrders();
    }

    public ArrayList<AllOrders> ShowAddressToList() {

        allOrders = new ArrayList<>();
        for (int i = 0; i < Pending_Orders_List.size(); i++) {
            AllOrders allorders = new AllOrders();
            Boolean flag = true;

            if (Pending_Orders_List.get(i).getIsBusiness()) {
                for (int l = 0; l < allOrders.size(); l++) {
                    if (allOrders.get(l).getOrder_name().equals(Pending_Orders_List.get(i).getBusiness_Order().getB_business_name())) {
                        flag = false;
                    } else {
                        flag = true;
                    }
                }
                if (flag) {
                    allorders.setBusinessUserName(Pending_Orders_List.get(i).getBusiness_Order().getB_username());
                    allorders.setOrder_name(Pending_Orders_List.get(i).getBusiness_Order().getB_business_name());
                    allorders.setPickupTime(Pending_Orders_List.get(i).getBusiness_Order().getPickup_time());
                    allorders.setIsBusiness(true);
                    allOrders.add(allorders);
                }
            } else {
                allorders.setCO(Pending_Orders_List.get(i).getCustomer_Order());
                allorders.setCS(Pending_Orders_List.get(i).getCustomer_shipment());
                allorders.setOrder_name(Pending_Orders_List.get(i).getCustomer_Order().getName());
                allorders.setPickupTime(Pending_Orders_List.get(i).getCustomer_Order().getPickup_time());
                allorders.setIsBusiness(false);
                allOrders.add(allorders);
            }
        }

        return allOrders;
    }

    public void updateOrders() {
        mprogress = new ProgressDialog(this);
        mprogress.setMessage("Please wait.");
        mprogress.setCancelable(false);
        mprogress.setIndeterminate(true);
        if (!mprogress.isShowing()) {
            Pending_Orders_List.clear();
            mUtils = new Utils(this);
            Utils utils = new Utils(this);
            if (mnetworkutils.isnetconnected()) {
                mprogress.show();
                mnetworkutils.getapi().getOrders("9920899602", new Callback<Response>() {
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
                                Log.i("Pending_Orders_size", String.valueOf(Pending_Orders_List.size()));
                                ListView lv_Saved_Address = (ListView) findViewById(R.id.allordersListView);
                                madapter = new PendingOrders_Adapter(Activity_Orders.this, R.layout.list_item_orders_list, ShowAddressToList());
                                lv_Saved_Address.setAdapter(madapter);

                                lv_Saved_Address.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                                        if (allOrders.get(position).getIsBusiness()) {
                                            Log.i("", allOrders.get(position).getOrder_name());
                                            Intent i = new Intent(getApplicationContext(), Business_orders_sublist.class);
                                            Gson GS = new Gson();
                                            String PO = GS.toJson(Pending_Orders_List);
                                            i.putExtra("Business_username", allOrders.get(position).getBusinessUserName());
                                            i.putExtra("PendingOrderList", PO);
                                            startActivity(i);
                                        } else {
                                            Log.i("", allOrders.get(position).getOrder_name());
                                            Intent i = new Intent(getApplicationContext(), Customer_OrderDetails.class);
                                            Gson GS = new Gson();
                                            String CO = GS.toJson(allOrders.get(position).getCO());
                                            String CS = GS.toJson(allOrders.get(position).getCS());
                                            i.putExtra("Customer_order_object", CO);
                                            i.putExtra("Customer_shipment_object", CS);
                                            startActivity(i);
                                        }
                                    }
                                });
                            }


                            @Override
                            public void failure(RetrofitError error) {
                                if (mprogress.isShowing()) {
                                    mprogress.dismiss();
                                }
                                String json = new String(((TypedByteArray) error.getResponse().getBody()).getBytes());
                                Log.v("failure", json.toString());
                                Log.i("qwertyuiopajklzxcvbnm,", error.toString());

                            }
                        }

                );
            } else {
                if (mprogress.isShowing()) {
                    mprogress.dismiss();
                }
                Toast.makeText(this, "Please Connect to a working Internet Connection", Toast.LENGTH_LONG).show();
            }
        }
    }
}