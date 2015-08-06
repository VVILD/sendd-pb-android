package co.sendddelivery.GetterandSetter;

import java.util.ArrayList;

public class BusinessAllOrders {

    private String Order_name;
    private Business_Order BO;
    private Business_Order BO1;
    private ArrayList<Business_Shipment>BS;
    private String BusinessUserName;
    private Boolean isComplete;
    private int PendingOrderId;

    public int getPendingOrderId() {
        return PendingOrderId;
    }

    public void setPendingOrderId(int pendingOrderId) {
        PendingOrderId = pendingOrderId;
    }

    public Business_Order getBO() {
        return BO;
    }

    public void setBO(Business_Order BO) {
        this.BO = BO;
    }

    public ArrayList<Business_Shipment> getBS() {
        return BS;
    }

    public void setBS(ArrayList<Business_Shipment> BS) {
        this.BS = BS;
    }

    public String getBusinessUserName() {
        return BusinessUserName;
    }

    public void setBusinessUserName(String businessUserName) {
        BusinessUserName = businessUserName;
    }


    public String getOrder_name() {

        return Order_name;
    }

    public void setOrder_name(String order_name) {
        Order_name = order_name;
    }

    public Boolean getIsComplete() {
        return isComplete;
    }

    public void setIsComplete(Boolean isComplete) {
        this.isComplete = isComplete;
    }
}
