package co.sendddelivery.Activity;

import android.app.Activity;
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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import co.sendddelivery.GetterandSetter.Customer_Order;
import co.sendddelivery.GetterandSetter.Customer_shipment;
import co.sendddelivery.GetterandSetter.ShipmentsListItems;
import co.sendddelivery.R;

/**
 * Created by harshkaranpuria on 7/27/15.
 */
public class Customer_OrderDetails extends Activity {
    List<Customer_shipment> mCustomer_Shipment;
    ArrayList<ShipmentsListItems> mCustomer_shipment_list;
    private Shipments_Adapter madapter;
    Button bProcess;
    ImageLoaderConfiguration config;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orderdetails);
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();
        config = new ImageLoaderConfiguration.Builder(this).defaultDisplayImageOptions(defaultOptions).build();

        ImageLoader.getInstance().init(config);
        bProcess = (Button) findViewById(R.id.bProcess);
        bProcess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), CustomerOrderSubDetails.class);
                Gson GS = new Gson();
                String CSList = GS.toJson(mCustomer_shipment_list);
                i.putExtra("Customer_shipment_object", getIntent().getStringExtra("Customer_shipment_object"));
                startActivity(i);
            }
        });
        TextView nameLabel = (TextView) findViewById(R.id.nameLabel);
        TextView addressLabel = (TextView) findViewById(R.id.addressLabel);
        TextView phoneLabel = (TextView) findViewById(R.id.phoneLabel);
        TextView pickuptimeLable = (TextView) findViewById(R.id.pickuptimeLable);
        String Customer_order_object = getIntent().getStringExtra("Customer_order_object");
        final String Customer_shipment_object = getIntent().getStringExtra("Customer_shipment_object");

        Gson GS = new Gson();
        Customer_Order customer_order = GS.fromJson(Customer_order_object, Customer_Order.class);
        mCustomer_Shipment = Arrays.asList(GS.fromJson(Customer_shipment_object, Customer_shipment[].class));
        Log.i("List<Customer_shipment>", String.valueOf(mCustomer_Shipment.size()));
        nameLabel.setText( customer_order.getName());
        addressLabel.setText(  customer_order.getFlat_no() + " " + customer_order.getAddress() + " " + customer_order.getPincode());
        phoneLabel.setText(   customer_order.getUser());
        pickuptimeLable.setText(  customer_order.getPickup_time());
        ListView lv_Saved_Address = (ListView) findViewById(R.id.shipmentList);
        madapter = new Shipments_Adapter(Customer_OrderDetails.this, R.layout.list_item_shipment_items_list, ShowAddressToList());
        lv_Saved_Address.setAdapter(madapter);


    }

    public class Shipments_holder {
        private TextView Shipment_number;
        private ImageView Shpiment_image;
    }

    //Set up Address Adapter
    public class Shipments_Adapter extends ArrayAdapter<ShipmentsListItems> {

        private Context c;
        private List<ShipmentsListItems> address_list;

        public Shipments_Adapter(Context context, int resource, List<ShipmentsListItems> objects) {
            super(context, resource, objects);
            this.c = context;
            this.address_list = objects;
        }


        @Override
        public void add(ShipmentsListItems object) {
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
        public ShipmentsListItems getItem(int position) {
            return super.getItem(position);
        }

        @Override
        public int getPosition(ShipmentsListItems item) {
            return super.getPosition(item);
        }

        @Override
        public long getItemId(int position) {
            return super.getItemId(position);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Shipments_holder shipment_holder;
            if (convertView == null) {
                //initialize holder
                shipment_holder = new Shipments_holder();
                LayoutInflater inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.list_item_shipment_items_list, parent, false);

                shipment_holder.Shpiment_image = (ImageView) convertView.findViewById(R.id.ivshipmentImage);
                shipment_holder.Shipment_number = (TextView) convertView.findViewById(R.id.tvshipmentnumber);
                convertView.setTag(shipment_holder);
            } else {
                shipment_holder = (Shipments_holder) convertView.getTag();
            }
            //setup list objects

            shipment_holder.Shipment_number.setText(address_list.get(position).getShipment_number());
            Log.i("mCustomer_Shipment.get(i).getReal_tracking_no()", address_list.get(position).getShipment_number());

            if (address_list.get(position).getImageUrl() ==  null) {
                ImageLoader.getInstance().displayImage("drawable://" + R.drawable.box_sample_icon, shipment_holder.Shpiment_image);
            } else {
                ImageLoader.getInstance().displayImage(address_list.get(position).getImageUrl(), shipment_holder.Shpiment_image);

            }
            return convertView;
        }
    }

    public ArrayList<ShipmentsListItems> ShowAddressToList() {

        mCustomer_shipment_list = new ArrayList<>();
        for (int i = 0; i < mCustomer_Shipment.size(); i++) {
            ShipmentsListItems sli = new ShipmentsListItems();
            sli.setShipment_number(mCustomer_Shipment.get(i).getReal_tracking_no());
            sli.setImageUrl(mCustomer_Shipment.get(i).getImg());

            mCustomer_shipment_list.add(sli);
        }

        return mCustomer_shipment_list;
    }
}
