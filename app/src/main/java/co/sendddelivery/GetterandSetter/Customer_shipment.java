package co.sendddelivery.GetterandSetter;

import java.io.Serializable;

/**
 * Created by harshkaranpuria on 7/25/15.
 */
public class Customer_shipment implements Serializable {

    private String category;
    private String cost_of_courier;
    private Drop_address drop_address;
    private String drop_name;
    private String real_tracking_no;

    public String getReal_tracking_no() {
        return real_tracking_no;
    }

    public void setReal_tracking_no(String real_tracking_no) {
        this.real_tracking_no = real_tracking_no;
    }

    private String drop_phone;
    private String img;
    private String item_name;
    private String price;
    private String weight;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCost_of_courier() {
        return cost_of_courier;
    }

    public void setCost_of_courier(String cost_of_courier) {
        this.cost_of_courier = cost_of_courier;
    }

    public Drop_address getDrop_address() {
        return drop_address;
    }

    public void setDrop_address(Drop_address drop_address) {
        this.drop_address = drop_address;
    }

    public String getDrop_name() {
        return drop_name;
    }

    public void setDrop_name(String drop_name) {
        this.drop_name = drop_name;
    }

    public String getDrop_phone() {
        return drop_phone;
    }

    public void setDrop_phone(String drop_phone) {
        this.drop_phone = drop_phone;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }
}
