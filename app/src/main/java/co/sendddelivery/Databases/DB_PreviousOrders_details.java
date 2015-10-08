package co.sendddelivery.Databases;

import android.util.Log;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;

import java.util.List;

import co.sendddelivery.GetterandSetter.PickedupOrders;
import co.sendddelivery.GetterandSetter.Prev_Order_details;

@Table(name = "details")
public class DB_PreviousOrders_details extends Model {
    @Column(name = "name")
    public String name;
    @Column(name = "username")
    public String username;
    @Column(name = "pickedup")
    public boolean pickedup;
    public void AddToDB(Prev_Order_details pickeduporders) {
            this.username = pickeduporders.getName();
            this.name = pickeduporders.getUsername();
            this.pickedup = pickeduporders.isPickedup();
            this.save();
            Log.i("Inside AddToDB", "User Added");
    }
    public static List<DB_PreviousOrders_details> getAllItems() {
        return new Select()
                .from(DB_PreviousOrders_details.class)
                .execute();
    }
    public void deleteAllItems(){
        new Delete().from(DB_PreviousOrders_details.class).execute();
    }
}