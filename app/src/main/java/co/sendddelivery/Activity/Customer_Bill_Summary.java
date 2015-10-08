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

public class Customer_Bill_Summary extends Activity {
    float  totalPriceAfterDiscount, DiscountAmount;

    TextView FinalAmount, tvDiscountAmt, tvTotalPrice, tvCodeValue, tvCode;
    Button moneyCollected;
    String x,y, noCode ="NO CODE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_summary);
        tvCode = (TextView) findViewById(R.id.tvCode);
        tvCodeValue = (TextView) findViewById(R.id.tvCodeValue);
        tvTotalPrice = (TextView) findViewById(R.id.tvTotalPrice);
        tvDiscountAmt = (TextView) findViewById(R.id.tvDiscountAmt);
        FinalAmount = (TextView) findViewById(R.id.FinalAmount);
        moneyCollected = (Button)findViewById(R.id.moneyCollected);

        if (getIntent().getStringExtra("promocode_type").equals("P")) {
            totalPriceAfterDiscount = getIntent().getFloatExtra("TotalPrice", 0) - (getIntent().getFloatExtra("TotalPrice", 0) * Float.parseFloat(getIntent().getStringExtra("promocode_amount"))) / 100;
            String CodeAmt =getIntent().getStringExtra("promocode_amount") + "% OFF";
            tvCodeValue.setText(CodeAmt);
            DiscountAmount = (getIntent().getFloatExtra("TotalPrice", 0) * Float.parseFloat(getIntent().getStringExtra("promocode_amount")))/100;
        } else if (getIntent().getStringExtra("promocode_type").equals("S")) {
            try{
                totalPriceAfterDiscount = getIntent().getFloatExtra("TotalPrice", 0) - Float.parseFloat(getIntent().getStringExtra("promocode_amount"));
            }catch (NumberFormatException e){
                totalPriceAfterDiscount = getIntent().getFloatExtra("TotalPrice", 0);
            }
            String codeVal ="Rs. " + getIntent().getStringExtra("promocode_amount") + " off";
            tvCodeValue.setText(codeVal);
            DiscountAmount = Float.parseFloat(getIntent().getStringExtra("promocode_amount"));
        } else {
            totalPriceAfterDiscount = getIntent().getFloatExtra("TotalPrice", 0);
            tvDiscountAmt.setText("0");
            tvCodeValue.setText("0");
        }
        x =getIntent().getStringExtra("promocode_msg");
        y =getIntent().getStringExtra("promocode_code");


        if(!getIntent().getStringExtra("promocode_code").equals("") || getIntent().getStringExtra("promocode_code") != null) {
            tvCode.setText(getIntent().getStringExtra("promocode_code"));
        }else{
            tvCode.setText(noCode);
        }
        if(tvCode.getText().toString().equals("null")){
            tvCode.setText(noCode);
        }
        if(totalPriceAfterDiscount<0){
            FinalAmount.setText("0");

        }else{
            FinalAmount.setText(String.valueOf(totalPriceAfterDiscount));
        }
        tvDiscountAmt.setText(String.valueOf(DiscountAmount));
        tvTotalPrice.setText(String.valueOf(getIntent().getFloatExtra("TotalPrice", 0)));
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
