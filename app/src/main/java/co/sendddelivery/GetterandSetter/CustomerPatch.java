package co.sendddelivery.GetterandSetter;

/**
 * Created by harshkaranpuria on 7/30/15.
 */
public class CustomerPatch {
    private String status;
    private String barcode;
    private String category;
    private String name;
    private String weight;
    private String price;
    private String city;
    private String country;
    private String flat_no;
    private String locality;
    private String pincode;
    private String state;
    private String drop_name;

    public String getDrop_phone() {
        return drop_phone;
    }

    public void setDrop_phone(String drop_phone) {
        this.drop_phone = drop_phone;
    }

    public String getDrop_name() {
        return drop_name;
    }

    public void setDrop_name(String drop_name) {
        this.drop_name = drop_name;
    }

    private String drop_phone;
    private int drop_address_pk;
    private String cost_of_courier;


    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getFlat_no() {
        return flat_no;
    }

    public void setFlat_no(String flat_no) {
        this.flat_no = flat_no;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public int getDrop_address_pk() {
        return drop_address_pk;
    }

    public void setDrop_address_pk(int drop_address_pk) {
        this.drop_address_pk = drop_address_pk;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCost_of_courier() {
        return cost_of_courier;
    }

    public void setCost_of_courier(String cost_of_courier) {
        this.cost_of_courier = cost_of_courier;
    }

}
