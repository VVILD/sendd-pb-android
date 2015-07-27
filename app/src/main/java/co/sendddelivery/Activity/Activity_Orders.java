package co.sendddelivery.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import co.sendddelivery.GetterandSetter.Business;
import co.sendddelivery.GetterandSetter.Business_Order;
import co.sendddelivery.GetterandSetter.Business_Shipment;
import co.sendddelivery.GetterandSetter.Customer_Order;
import co.sendddelivery.GetterandSetter.Customer_shipment;
import co.sendddelivery.GetterandSetter.Pending_Orders;
import co.sendddelivery.R;
import co.sendddelivery.Utils.NetworkUtils;
import co.sendddelivery.Utils.Utils;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by harshkaranpuria on 7/24/15.
 */
public class Activity_Orders extends Activity {
    NetworkUtils mnetworkutils = new NetworkUtils(this);
    ProgressDialog mprogress;
    Utils utils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_allorders);
        utils = new Utils(this);
        mprogress = new ProgressDialog(this);
        mprogress.setMessage("Please wait.");
        mprogress.setCancelable(false);
        mprogress.setIndeterminate(true);
        if (mnetworkutils.isnetconnected()) {
            mnetworkutils.getapi().register("9920899602", new Callback<Response>() {
                @Override
                public void success(Response response, Response response2) {
                    BufferedReader reader;
                    StringBuilder sb = new StringBuilder();
                    try {
                        reader = new BufferedReader(new InputStreamReader(response.getBody().in()));
                        String line;
                        try {
                            while ((line = reader.readLine()) != null) {
                                sb.append(line);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    String result = sb.toString();
                    Log.i("ultresult", result);
                    Pending_Orders[] pd =new Pending_Orders[]{};
                    try {
                        JSONObject jObj = new JSONObject(result);
                        JSONArray pending_orders = jObj.getJSONArray("pending_orders");
                        for (int j = 0; j < pending_orders.length(); j++) {
                            JSONObject booking = pending_orders.getJSONObject(j);
                            JSONArray business = booking.getJSONArray("business");
                            for (int k = 0; k < business.length(); k++) {
                                JSONObject booking2 = business.getJSONObject(k);

                                Business B = new Business();
                                Business_Order BO = new Business_Order();
                                Business_Shipment BS = new Business_Shipment();
                                Customer_Order CO = new Customer_Order();
                                Customer_shipment CS = new Customer_shipment();

//                            B.setAddress(booking.getJSONObject("fields").getString("address"));
//                            pd[j].setBusiness(B);
//                            pd[j].setBusiness_Order(BO);
//                            pd[j].setBusiness_Shipment(BS);
//                            pd[j].setCustomer_Order(CO);
//                            pd[j].setCustomer_shipment(CS);
//                            Log.i("order",booking.getJSONObject("fields").getString("address"));
//                            Log.i("asdasd",booking.getJSONArray("business").getJSONObject(j).getString("address"));
                            Log.i("order", booking2.getJSONObject("fields").getString("address"));
                            Log.i("Shipment", booking.getString("shipments"));
                            Log.i("business", booking.getString("business"));
                            }

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                    @Override
                    public void failure (RetrofitError error){
                        if (mprogress.isShowing()) {
                            mprogress.dismiss();
                        }
                        Log.i("qwertyuiopajklzxcvbnm,", error.toString());

                    }
                }

                );
            }else {
            Toast.makeText(this, "Please Connect to a working Internet Connection", Toast.LENGTH_LONG).show();
        }
    }
}