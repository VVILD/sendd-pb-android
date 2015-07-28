package co.sendddelivery.GetterandSetter;

/**
 * Created by harshkaranpuria on 7/28/15.
 */
public class ShipmentsListItems {
    private String Shipment_number;
    private String ImageUrl;
    private Customer_shipment CO;

    public Customer_shipment getCO() {
        return CO;
    }

    public void setCO(Customer_shipment CO) {
        this.CO = CO;
    }

    public String getShipment_number() {
        return Shipment_number;

    }

    public void setShipment_number(String shipment_number) {
        Shipment_number = shipment_number;
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
    }
}
