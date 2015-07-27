package co.sendddelivery.GetterandSetter;

/**
 * Created by harshkaranpuria on 7/25/15.
 */
public class Pending_Orders {

    private Business_Order Business_Order;
    private Business_Shipment Business_Shipment;
    private Customer_shipment Customer_shipment;
    private Customer_Order Customer_Order;

    public co.sendddelivery.GetterandSetter.Business_Order getBusiness_Order() {
        return Business_Order;
    }

    public void setBusiness_Order(co.sendddelivery.GetterandSetter.Business_Order business_Order) {
        Business_Order = business_Order;
    }

    public co.sendddelivery.GetterandSetter.Business_Shipment getBusiness_Shipment() {
        return Business_Shipment;
    }

    public void setBusiness_Shipment(co.sendddelivery.GetterandSetter.Business_Shipment business_Shipment) {
        Business_Shipment = business_Shipment;
    }

    public co.sendddelivery.GetterandSetter.Customer_shipment getCustomer_shipment() {
        return Customer_shipment;
    }

    public void setCustomer_shipment(co.sendddelivery.GetterandSetter.Customer_shipment customer_shipment) {
        Customer_shipment = customer_shipment;
    }

    public co.sendddelivery.GetterandSetter.Customer_Order getCustomer_Order() {
        return Customer_Order;
    }

    public void setCustomer_Order(co.sendddelivery.GetterandSetter.Customer_Order customer_Order) {
        Customer_Order = customer_Order;
    }
}
