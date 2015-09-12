package co.sendddelivery.Databases;

import android.util.Log;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;

import java.util.List;

import co.sendddelivery.GetterandSetter.PickedupOrders;

@Table(name = "user")
public class DB_PreviousOrders extends Model {

    @Column(name = "businessusername")
    public String businessusername;
    @Column(name = "name")
    public String name;
    @Column(name = "pickeduporders")
    public int pickeduporders;
    @Column(name = "cancelledorders")
    public int cancelledorders;
    @Column(name = "scannedorders")
    public int scannedorders;

    public void AddToDB(PickedupOrders pickedupOrders) {
        DB_PreviousOrders prevOrders = new Select()
                .from(DB_PreviousOrders.class)
                .where("name = ?", pickedupOrders.name)
                .executeSingle();
        if (prevOrders != null) {
            prevOrders.pickeduporders = pickedupOrders.getPickeduporders()+prevOrders.pickeduporders;
            prevOrders.cancelledorders = pickedupOrders.getCancelledorders()+prevOrders.cancelledorders;
            prevOrders.scannedorders = pickedupOrders.getScannedorders()+prevOrders.scannedorders;
            prevOrders.save();
            Log.i("Inside AddToDB", "Update ");
        } else {
            this.businessusername = pickedupOrders.getBusinessusername();
            this.name = pickedupOrders.getName();
            this.pickeduporders = pickedupOrders.getPickeduporders();
            this.cancelledorders = pickedupOrders.getCancelledorders();
            this.scannedorders = pickedupOrders.getScannedorders();
            this.save();
            Log.i("Inside AddToDB", "User Added");
        }
    }
    public void deleteAllItems(){
        new Delete().from(DB_PreviousOrders.class).execute();
    }

    public static List<DB_PreviousOrders> getAllItems() {
        return new Select()
                .from(DB_PreviousOrders.class)
                .execute();
    }

}