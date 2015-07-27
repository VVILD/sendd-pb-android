package co.sendddelivery.GetterandSetter;

/**
 * Created by harshkaranpuria on 7/25/15.
 */
public class Customer_Order {
    private String address;
    private String flat_no;
    private String name;
    private String pincode;
    private String time;
    private String user;

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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
