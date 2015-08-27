package co.sendddelivery.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import co.sendddelivery.R;

/**
 * Created by harshkaranpuria on 8/22/15.
 */

public class Customer_Bill_Summary extends Activity {
    float ff, totalPriceAfterDiscount, DiscountAmount;

    TextView FinalAmount, tvDiscountAmt, tvTotalPrice, tvCodeValue, tvCode;
    Button moneyCollected;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_summary);
        FinalAmount = (TextView) findViewById(R.id.FinalAmount);
        tvDiscountAmt = (TextView) findViewById(R.id.tvDiscountAmt);
        tvTotalPrice = (TextView) findViewById(R.id.tvTotalPrice);
        tvCodeValue = (TextView) findViewById(R.id.tvCodeValue);
        tvCode = (TextView) findViewById(R.id.tvCode);
        moneyCollected = (Button)findViewById(R.id.moneyCollected);
        if (getIntent().getStringExtra("promocode_type").equals("P")) {
            totalPriceAfterDiscount = getIntent().getFloatExtra("TotalPrice", 0) - (getIntent().getFloatExtra("TotalPrice", 0) * Float.parseFloat(getIntent().getStringExtra("promocode_amount"))) / 100;
            Log.i("totalPriceAfterDiscount", String.valueOf(totalPriceAfterDiscount));
            tvCodeValue.setText(getIntent().getStringExtra("promocode_amount") + "%");
            DiscountAmount = (getIntent().getFloatExtra("TotalPrice", 0) * Float.parseFloat(getIntent().getStringExtra("promocode_amount")))/100;
        } else if (getIntent().getStringExtra("promocode_type").equals("S")) {
            totalPriceAfterDiscount = getIntent().getFloatExtra("TotalPrice", 0) - Float.parseFloat(getIntent().getStringExtra("promocode_amount"));
            Log.i("totalPriceAfterDiscount", String.valueOf(totalPriceAfterDiscount));
            tvCodeValue.setText("Rs. " + getIntent().getStringExtra("promocode_amount") + " off");
            DiscountAmount = Float.parseFloat(getIntent().getStringExtra("promocode_amount"));
        } else {
            totalPriceAfterDiscount = getIntent().getFloatExtra("TotalPrice", 0);
            tvDiscountAmt.setText("0");
            tvCode.setText("No Code");
            tvCodeValue.setText("0");

        }
        if(!getIntent().getStringExtra("promocode_msg").equals("")){
            Toast.makeText(Customer_Bill_Summary.this,getIntent().getStringExtra("promocode_msg"),Toast.LENGTH_LONG).show();
        }
        if(getIntent().getStringExtra("promocode_code") != null) {
            tvCode.setText(getIntent().getStringExtra("promocode_code"));
        }else{
            tvCode.setText(getIntent().getStringExtra("No Code"));

        }
        tvDiscountAmt.setText(String.valueOf(DiscountAmount));
        tvTotalPrice.setText(String.valueOf(getIntent().getFloatExtra("TotalPrice", 0)));
        FinalAmount.setText(String.valueOf(totalPriceAfterDiscount));
        Log.i("ptype", getIntent().getStringExtra("promocode_type"));
        Log.i("pamount", getIntent().getStringExtra("promocode_amount"));
        Log.i("total", String.valueOf(getIntent().getFloatExtra("TotalPrice", 0)));
        moneyCollected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), Activity_Orders.class);
                startActivity(i);
                finish();
                Customer_Bill_Summary.this.overridePendingTransition(R.animator.pull_in_right, R.animator.push_out_left);
            }
        });
    }
}
