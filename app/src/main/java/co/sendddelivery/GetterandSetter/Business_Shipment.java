package co.sendddelivery.GetterandSetter;

import java.io.Serializable;

/**
 * Created by harshkaranpuria on 7/25/15.
 */
public class Business_Shipment implements Serializable {
    private String name;
    private String price;
    private String quantity;
    private String real_tracking_no;
    private String sku;
    private String weight;
    private String shipping_cost;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getReal_tracking_no() {
        return real_tracking_no;
    }

    public void setReal_tracking_no(String real_tracking_no) {
        this.real_tracking_no = real_tracking_no;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getShipping_cost() {
        return shipping_cost;
    }

    public void setShipping_cost(String shipping_cost) {
        this.shipping_cost = shipping_cost;
    }
}
