package co.sendddelivery.GetterandSetter;

public class PickedupOrders {
    public String businessusername;
    public String name;
    public int pickeduporders;
    public int cancelledorders;
    public int scannedorders;

    public String getBusinessusername() {
        return businessusername;
    }

    public void setBusinessusername(String businessusername) {
        this.businessusername = businessusername;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPickeduporders() {
        return pickeduporders;
    }

    public void setPickeduporders(int pickeduporders) {
        this.pickeduporders = pickeduporders;
    }

    public int getCancelledorders() {
        return cancelledorders;
    }

    public void setCancelledorders(int cancelledorders) {
        this.cancelledorders = cancelledorders;
    }

    public int getScannedorders() {
        return scannedorders;
    }

    public void setScannedorders(int scannedorders) {
        this.scannedorders = scannedorders;
    }
}
