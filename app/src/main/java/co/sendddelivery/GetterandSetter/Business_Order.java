package co.sendddelivery.GetterandSetter;

import java.io.Serializable;

/**
 * Created by harshkaranpuria on 7/25/15.
 */
public class Business_Order implements Serializable {
    private String address1;
    private String address2;
    private String b_address;
    private String b_business_name;
    private String b_city;
    private String b_contact_mob;
    private String b_contact_office;
    private String b_name;
    private String b_pincode;
    private String b_state;
    private String b_username;
    private String name;
    private String order_id;
    private String phone;
    private String pickup_time;
    private String pincode;
    private Boolean isComplete;

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public Boolean getIsComplete() {
        return isComplete;
    }

    public void setIsComplete(Boolean isComplete) {
        this.isComplete = isComplete;
    }

    public String getB_username() {
        return b_username;
    }

    public void setB_username(String b_username) {
        this.b_username = b_username;
    }


    public String getPickup_time() {
        return pickup_time;
    }

    public void setPickup_time(String pickup_time) {
        this.pickup_time = pickup_time;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getB_address() {
        return b_address;
    }

    public void setB_address(String b_address) {
        this.b_address = b_address;
    }

    public String getB_business_name() {
        return b_business_name;
    }

    public void setB_business_name(String b_business_name) {
        this.b_business_name = b_business_name;
    }

    public String getB_city() {
        return b_city;
    }

    public void setB_city(String b_city) {
        this.b_city = b_city;
    }

    public String getB_contact_mob() {
        return b_contact_mob;
    }

    public void setB_contact_mob(String b_contact_mob) {
        this.b_contact_mob = b_contact_mob;
    }

    public String getB_contact_office() {
        return b_contact_office;
    }

    public void setB_contact_office(String b_contact_office) {
        this.b_contact_office = b_contact_office;
    }

    public String getB_name() {
        return b_name;
    }

    public void setB_name(String b_name) {
        this.b_name = b_name;
    }

    public String getB_pincode() {
        return b_pincode;
    }

    public void setB_pincode(String b_pincode) {
        this.b_pincode = b_pincode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getB_state() {
        return b_state;
    }

    public void setB_state(String b_state) {
        this.b_state = b_state;
    }
}
