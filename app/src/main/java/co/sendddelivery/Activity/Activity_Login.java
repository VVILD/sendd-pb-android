package co.sendddelivery.Activity;

import android.os.Bundle;

import co.sendddelivery.Fragment.Login_fragment;
import co.sendddelivery.Fragment.Verify_fragment;
import co.sendddelivery.R;

public class Activity_Login extends BaseFragmentActivity implements Login_fragment.onLoginListner {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setupUI(findViewById(R.id.parent));
        if (findViewById(R.id.frame_container) != null) {
            Login_fragment LoginFrag = new Login_fragment();
            getSupportFragmentManager().beginTransaction().
                    add(R.id.frame_container, LoginFrag, "Login_Frag")
                    .commit();
        }
    }

    @Override
    public void onSuccessfulLogin(String phone_no, String Otp) {
        Verify_fragment newFragment = new Verify_fragment();
        Bundle args = new Bundle();
        args.putString("PhoneNumber", phone_no);
        args.putString("OTP", Otp);
        newFragment.setArguments(args);
        getSupportFragmentManager().beginTransaction()
                .addToBackStack(null)
                .setCustomAnimations(R.animator.pull_in_right, R.animator.push_out_left, R.animator.pull_in_left, R.animator.push_out_right)
                .replace(R.id.frame_container, newFragment, "newFragment")
                .commit();
    }
}