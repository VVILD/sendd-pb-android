package co.sendddelivery.GetterandSetter;

/**
 * Created by harshkaranpuria on 10/7/15.
 */
public class Prev_Order_details {
    private String name;
    private boolean pickedup;
    private String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isPickedup() {
        return pickedup;
    }

    public void setPickedup(boolean pickedup) {
        this.pickedup = pickedup;
    }
}
