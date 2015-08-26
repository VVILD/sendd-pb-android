package co.sendddelivery.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import co.sendddelivery.Activity.Activity_Orders;
import co.sendddelivery.R;
import co.sendddelivery.Utils.Utils;

public class Verify_fragment extends Fragment {
    String phone,otp;
    Button Verify;
    Utils mUtils;
    EditText OTP;

    public Verify_fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return getActivity().getLayoutInflater().inflate(R.layout.fragment_verify_page, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        OTP= (EditText)view.findViewById(R.id.etOTP);
        Verify= (Button)view.findViewById(R.id.bVerify);
        mUtils = new Utils(getActivity());
        Bundle b = getArguments();
        Log.i("asd",b.getString("OTP"));
        phone = b.getString("PhoneNumber");
        otp = b.getString("OTP");
         Verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (OTP.getText().toString().equals(otp)) {
                    mUtils.setvalue("PhoneNumber", phone);
                    mUtils.setvalue("LoggedIn", "Yes");
                    Intent i = new Intent(getActivity().getApplicationContext(), Activity_Orders.class);
                    startActivity(i);
                    getActivity().finish();
                    getActivity().overridePendingTransition(R.animator.pull_in_right, R.animator.push_out_left);
                } else {
                    Toast.makeText(getActivity(), "Invalid OTP", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}