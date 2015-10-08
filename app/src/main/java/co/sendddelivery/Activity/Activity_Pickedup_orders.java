package co.sendddelivery.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import co.sendddelivery.Databases.DB_PreviousOrders;
import co.sendddelivery.GetterandSetter.PickedupOrders;
import co.sendddelivery.R;

public class Activity_Pickedup_orders extends AppCompatActivity {
    ListView prevOrdersList;
    OrderList_Adapter mAdapter;
    private ArrayList<PickedupOrders> pickedupOrdersArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_previous_order);
        prevOrdersList = (ListView) findViewById(R.id.prevOrdersList);
        mAdapter = new OrderList_Adapter(this, R.layout.list_item_previousorder_list, ShowItemList());
        prevOrdersList.setAdapter(mAdapter);

        prevOrdersList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent ii = new Intent(getApplicationContext(),Activity_Prev_order_details.class);
                ii.putExtra("Name",pickedupOrdersArrayList.get(i).getName());
                ii.putExtra("ScannedOrders",pickedupOrdersArrayList.get(i).getScannedorders());
                ii.putExtra("UsernameName",pickedupOrdersArrayList.get(i).getBusinessusername());
                startActivity(ii);
            }
        });
    }

    public class previousAddress_holder {
        private TextView name;
        private TextView pickeduporders;
        private TextView scannedorders;
        private TextView cancelledorders;
    }

    public class OrderList_Adapter extends ArrayAdapter<PickedupOrders> {

        private Context c;
        private List<PickedupOrders> Item_list;

        public OrderList_Adapter(Context context, int resource, List<PickedupOrders> objects) {
            super(context, resource, objects);
            this.c = context;
            this.Item_list = objects;
        }


        @Override
        public void add(PickedupOrders object) {
            super.add(object);
        }

        @Override
        public void notifyDataSetChanged() {
            super.notifyDataSetChanged();
            prevOrdersList.invalidateViews();
        }

        @Override
        public int getCount() {
            return Item_list.size();
        }

        @Override
        public PickedupOrders getItem(int position) {
            return super.getItem(position);
        }

        @Override
        public int getPosition(PickedupOrders item) {
            return super.getPosition(item);
        }

        @Override
        public long getItemId(int position) {
            return super.getItemId(position);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final previousAddress_holder prev_holder;
            if (convertView == null) {
                prev_holder = new previousAddress_holder();
                LayoutInflater inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.list_item_previousorder_list, parent, false);
                prev_holder.name = (TextView) convertView.findViewById(R.id.name);
                prev_holder.cancelledorders = (TextView) convertView.findViewById(R.id.cancelledorders);
                prev_holder.pickeduporders = (TextView) convertView.findViewById(R.id.pickeduporders);
                prev_holder.scannedorders = (TextView) convertView.findViewById(R.id.scannedorders);

                convertView.setTag(prev_holder);
            } else {
                prev_holder = (previousAddress_holder) convertView.getTag();
            }
            prev_holder.name.setText(Item_list.get(position).getName());
            prev_holder.cancelledorders.setText(String.valueOf(Item_list.get(position).getCancelledorders()));
            prev_holder.pickeduporders.setText(String.valueOf(Item_list.get(position).getPickeduporders()));
            prev_holder.scannedorders.setText(String.valueOf(Item_list.get(position).getScannedorders()));
            return convertView;
        }

    }

    public ArrayList<PickedupOrders> ShowItemList() {
        pickedupOrdersArrayList = new ArrayList<>();
        List<DB_PreviousOrders> list = DB_PreviousOrders.getAllItems();
        for (int i = 0; i < DB_PreviousOrders.getAllItems().size(); i++) {
            DB_PreviousOrders addDBReceiver = list.get(i);
            PickedupOrders po = new PickedupOrders();
            po.setName(addDBReceiver.name);
            po.setScannedorders(addDBReceiver.scannedorders);
            po.setBusinessusername(addDBReceiver.businessusername);
            po.setCancelledorders(addDBReceiver.cancelledorders);
            po.setPickeduporders(addDBReceiver.pickeduporders);
            pickedupOrdersArrayList.add(po);
        }

        return pickedupOrdersArrayList;
    }
    @Override
    public void onBackPressed() {
        Intent i = new Intent(this,Activity_Orders.class);
        startActivity(i);
        finish();
        overridePendingTransition(R.animator.fade_in, R.animator.fade_out);
    }
}
