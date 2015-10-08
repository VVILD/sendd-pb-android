package co.sendddelivery.Activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import co.sendddelivery.Databases.DB_PreviousOrders_details;
import co.sendddelivery.GetterandSetter.Prev_Order_details;
import co.sendddelivery.R;

public class Activity_Prev_order_details extends Activity {
    TextView ScannedOrders, Name;
    ListView lvPickedUpOrders, lvCancelledOrders;
    ArrayList<Prev_Order_details> pickedupOrdersArrayList;
    ArrayList<Prev_Order_details> cancelledOrdersArrayList;
    PickedupOrders_Adapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prev_order_details);
        Name = (TextView) findViewById(R.id.Name);
        ScannedOrders = (TextView) findViewById(R.id.ScannedOrders);
        lvPickedUpOrders = (ListView) findViewById(R.id.lvPickedUpOrders);
        mAdapter = new PickedupOrders_Adapter(this, R.layout.list_item_ordername, ShowItemList());
        lvPickedUpOrders.setAdapter(mAdapter);
        lvCancelledOrders = (ListView) findViewById(R.id.lvCancelledOrders);
        mAdapter = new PickedupOrders_Adapter(this, R.layout.list_item_ordername, ShowItemList2());
        lvCancelledOrders.setAdapter(mAdapter);
        Name.setText(getIntent().getStringExtra("Name"));
        ScannedOrders.setText(String.valueOf(getIntent().getIntExtra("ScannedOrders", 0)));
    }

    public class PickedupOrders_holder {
        private TextView name;
    }

    public class PickedupOrders_Adapter extends ArrayAdapter<Prev_Order_details> {

        private Context c;
        private List<Prev_Order_details> Item_list;

        public PickedupOrders_Adapter(Context context, int resource, List<Prev_Order_details> objects) {
            super(context, resource, objects);
            this.c = context;
            this.Item_list = objects;
        }


        @Override
        public void add(Prev_Order_details object) {
            super.add(object);
        }

        @Override
        public void notifyDataSetChanged() {
            super.notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return Item_list.size();
        }

        @Override
        public Prev_Order_details getItem(int position) {
            return super.getItem(position);
        }

        @Override
        public int getPosition(Prev_Order_details item) {
            return super.getPosition(item);
        }

        @Override
        public long getItemId(int position) {
            return super.getItemId(position);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final PickedupOrders_holder prev_holder;
            if (convertView == null) {
                prev_holder = new PickedupOrders_holder();
                LayoutInflater inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.list_item_ordername, parent, false);
                prev_holder.name = (TextView) convertView.findViewById(R.id.Order_Name);
                convertView.setTag(prev_holder);
            } else {
                prev_holder = (PickedupOrders_holder) convertView.getTag();
            }
            prev_holder.name.setText(Item_list.get(position).getUsername());
            return convertView;
        }
    }

    public ArrayList<Prev_Order_details> ShowItemList() {
        pickedupOrdersArrayList = new ArrayList<>();
        List<DB_PreviousOrders_details> list = DB_PreviousOrders_details.getAllItems();
        for (int i = 0; i < DB_PreviousOrders_details.getAllItems().size(); i++) {
            DB_PreviousOrders_details addDBReceiver = list.get(i);
            Prev_Order_details po = new Prev_Order_details();
            if (addDBReceiver.pickedup) {
                if (addDBReceiver.name.equals(getIntent().getStringExtra("UsernameName"))) {
                    po.setName(addDBReceiver.name);
                    po.setUsername(addDBReceiver.username);
                    pickedupOrdersArrayList.add(po);
                }
            }
        }
        return pickedupOrdersArrayList;
    }

    public ArrayList<Prev_Order_details> ShowItemList2() {
        pickedupOrdersArrayList = new ArrayList<>();
        List<DB_PreviousOrders_details> list = DB_PreviousOrders_details.getAllItems();
        for (int i = 0; i < DB_PreviousOrders_details.getAllItems().size(); i++) {
            DB_PreviousOrders_details addDBReceiver = list.get(i);
            Prev_Order_details po = new Prev_Order_details();
            if (!addDBReceiver.pickedup) {
                Log.i("NAME",getIntent().getStringExtra("Name"));
                Log.i("name",addDBReceiver.name);
                if (addDBReceiver.name.equals(getIntent().getStringExtra("UsernameName"))) {
                    po.setName(addDBReceiver.name);
                    po.setUsername(addDBReceiver.username);
                    pickedupOrdersArrayList.add(po);
                }
            }
        }
        return pickedupOrdersArrayList;
    }
}
