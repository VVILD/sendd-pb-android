package co.sendddelivery.Fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import co.sendddelivery.GetterandSetter.Login;
import co.sendddelivery.R;
import co.sendddelivery.Utils.NetworkUtils;
import co.sendddelivery.Utils.Utils;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class Login_fragment extends Fragment {
    onLoginListner mCallback;

    private EditText PhoneNumber;
    private ProgressDialog mprogress;
    private NetworkUtils mnetworkutils;
    public static Activity LoginActivity;
    Utils mUtils;

    public Login_fragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return getActivity().getLayoutInflater().inflate(R.layout.fragment_login_page, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LoginActivity = getActivity();
        mUtils = new Utils(getActivity());
        Button login = (Button) view.findViewById(R.id.buttonLogin);
        PhoneNumber = (EditText) view.findViewById(R.id.LoginPhoneNumber);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(PhoneNumber.getText().toString())) {
                    mnetworkutils = new NetworkUtils(getActivity());
                    mprogress = new ProgressDialog(getActivity());
                    mprogress.setMessage("Please wait.");
                    mprogress.setCancelable(false);
                    mprogress.setIndeterminate(true);
                    if (mnetworkutils.isnetconnected()) {
                        mprogress.show();
                        mnetworkutils.getapi().login(PhoneNumber.getText().toString(), new Callback<Login>() {
                                    @Override
                                    public void success(Login login, Response response1) {
                                        if (mprogress.isShowing()) {
                                            mprogress.dismiss();
                                        }
                                        mCallback.onSuccessfulLogin(login.getPhone(),login.getOtp());
                                    }

                                    @Override
                                    public void failure(RetrofitError error) {
                                        if (mprogress.isShowing()) {
                                            mprogress.dismiss();
                                        }
                                        Toast.makeText(getActivity(),"Login Failed Please try again in some time.",Toast.LENGTH_LONG).show();
                                        Log.i("Error:->", error.toString());
                                    }
                                }
                        );
                    } else {
                        Toast.makeText(getActivity(), "Please Connect to a working Internet Connection", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "Please enter a phone number", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    public interface onLoginListner {
         void onSuccessfulLogin(String phone_no, String Otp);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mCallback = (onLoginListner) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }
    @Override
    public void onDetach() {
        super.onDetach();
        mCallback = null;
    }

}