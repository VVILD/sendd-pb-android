package co.sendddelivery.Utils;

import java.util.ArrayList;

import co.sendddelivery.GetterandSetter.BarcodeAllotment;
import co.sendddelivery.GetterandSetter.BusinessPatch;
import co.sendddelivery.GetterandSetter.CustomerPatch;
import co.sendddelivery.GetterandSetter.LocationParameters;
import co.sendddelivery.GetterandSetter.Login;
import co.sendddelivery.GetterandSetter.allotment_list;
import co.sendddelivery.GetterandSetter.business_is_completePatch;
import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.PATCH;
import retrofit.http.POST;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by Kuku on 17/02/15.
 */
public interface NetworkCalls {
    @GET("/pb_api/v1/pending_orders/")
    void getOrders(@Query("pb_ph") String pb_ph, Callback<Response> responseCallback);

    @GET("/pb_api/v1/barcode_scan/")
    void getorder(@Query("bid") String bid, Callback<Response> responseCallback);

    @GET("/pb_api/v1/pb_users/{phone}/")
    void login(@Path("phone") String phone,Callback<Login> loginCallback);


    @Headers({
            "X-HTTP-Method-Override: PATCH",
            "Authorization:A"
    })
    @POST("/bapi/v1/product/{trackingId}/")
    void updateBusiness(@Path("trackingId") String trackingId, @Body BusinessPatch registeruser, Callback<BusinessPatch> customerPatchCallback);

    @Headers("X-HTTP-Method-Override: PATCH")
    @POST("/api/v2/shipment/{trackingId}/")
    void updateCustomer(@Path("trackingId") String trackingId, @Body CustomerPatch registeruser, Callback<CustomerPatch> customerPatchCallback);

    @POST("/pb_api/v1/pb_locations/")
    void sendlocation( @Body LocationParameters locationParameters, Callback<Response> customerPatchCallback);

    @PATCH("/bapi/v3/business_patch/{username}/")
    void closeBusiness(@Path("username") String username,@Body business_is_completePatch is_complete, Callback<Response> responseCallback);

    @PATCH("/bapi/v2/barcode_allotment/")
    void addbarcodes(@Body BarcodeAllotment objects, Callback<Response> responseCallback);

}
