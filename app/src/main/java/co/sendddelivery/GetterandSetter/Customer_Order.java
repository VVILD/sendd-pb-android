package co.sendddelivery.GetterandSetter;

import java.io.Serializable;

/**
 * Created by harshkaranpuria on 7/25/15.
 */
public class Customer_Order implements Serializable {
    private String address;
    private String flat_no;
    private String name;
    private String pincode;
    private String pickup_time;
    private String user;
    private String promocode_amount;
    private String promocode_type;

    public String getPromocode_amount() {
        return promocode_amount;
    }

    public void setPromocode_amount(String promocode_amount) {
        this.promocode_amount = promocode_amount;
    }

    public String getPromocode_type() {
        return promocode_type;
    }

    public void setPromocode_type(String promocode_type) {
        this.promocode_type = promocode_type;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getFlat_no() {
        return flat_no;
    }

    public void setFlat_no(String flat_no) {
        this.flat_no = flat_no;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }
    public String getPickup_time() {
        return pickup_time;
    }

    public void setPickup_time(String pickup_time) {
        this.pickup_time = pickup_time;
    }

        public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
