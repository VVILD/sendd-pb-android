package co.sendddelivery.GetterandSetter;

import java.util.ArrayList;

public class AllOrders {
    private String Order_name;
    private String Locality;
    private String NumberofShipments;

    public String getNumberofShipments() {
        return NumberofShipments;
    }

    public void setNumberofShipments(String numberofShipments) {
        NumberofShipments = numberofShipments;
    }

    private String PickupTime;
    private Boolean isBusiness;
    private Customer_Order CO;
    private ArrayList<Customer_shipment> CS;
    private String BusinessUserName;

    public String getBusinessName() {
        return BusinessName;
    }

    public void setBusinessName(String businessName) {
        BusinessName = businessName;
    }

    private String BusinessName;

    public String getLocality() {
        return Locality;
    }

    public void setLocality(String locality) {
        Locality = locality;
    }


    public String getBusinessUserName() {
        return BusinessUserName;
    }

    public void setBusinessUserName(String businessUserName) {
        BusinessUserName = businessUserName;
    }


    public ArrayList<Customer_shipment> getCS() {
        return CS;
    }

    public void setCS(ArrayList<Customer_shipment> CS) {
        this.CS = CS;
    }

    public Customer_Order getCO() {

        return CO;
    }

    public void setCO(Customer_Order CO) {
        this.CO = CO;
    }

    public Boolean getIsBusiness() {
        return isBusiness;
    }

    public void setIsBusiness(Boolean isBusiness) {
        this.isBusiness = isBusiness;
    }

    public String getPickupTime() {
        return PickupTime;
    }

    public void setPickupTime(String pickupTime) {
        PickupTime = pickupTime;
    }

    public String getOrder_name() {

        return Order_name;
    }

    public void setOrder_name(String order_name) {
        Order_name = order_name;
    }
}
