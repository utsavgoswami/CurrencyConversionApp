package com.myappcompany.utsavgoswami.simplecurrencyconverter;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    public void convert(View view) {
        EditText amount = (EditText) findViewById(R.id.userAmount);
        double eurAmount = Double.parseDouble(amount.getText().toString());

        // Hardcoded for now, will use API to reflect current FOREX values
        double usdAmount = eurAmount * 1.3;

        // Hardcoded in for now
        Toast.makeText(this, "£" + eurAmount + " is $" + usdAmount, Toast.LENGTH_SHORT)
                .show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
