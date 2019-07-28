package com.myappcompany.utsavgoswami.simplecurrencyconverter;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    String selectedBaseCurrency;
    String selectedConvertCurrency;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*Set Contents of Spinners*/
        Spinner baseSpinner = (Spinner) findViewById(R.id.baseSpinner);
        Spinner convertSpinner = (Spinner) findViewById(R.id.convertSpinner);

        // Create an ArrayAdapter using the string array and a default spinner layout
        // createFromResource() method allows you to create an ArrayAdapter from the string array
        // defined in currencies.xml
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.available_currencies, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        // The setAdapter() method applies the adapter to BaseSpinner
        baseSpinner.setAdapter(adapter);
        convertSpinner.setAdapter(adapter);

        // Spinner click listeners
        baseSpinner.setOnItemSelectedListener(this);
        convertSpinner.setOnItemSelectedListener(this);

    }

    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        // An item was selected. You can retrieve the selected item using
        // parent.getItemAtPosition(pos)
        String item = parent.getItemAtPosition(pos).toString();

        // Reference: https://stackoverflow.com/questions/9262871/android-two-spinner-onitemselected
        if (parent.getId() == R.id.baseSpinner) {
            selectedBaseCurrency = item.substring(0, 3);
            Toast.makeText(this, "User selected " +
                    selectedBaseCurrency + " as base currency",
                    Toast.LENGTH_SHORT).show();
        } else { // Has to be convertedSpinner since there are only two spinners
            selectedConvertCurrency = item.substring(0, 3);
            Toast.makeText(this, "User selected " + selectedConvertCurrency
            + " as converted currency", Toast.LENGTH_SHORT).show();
        }

    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }
}
