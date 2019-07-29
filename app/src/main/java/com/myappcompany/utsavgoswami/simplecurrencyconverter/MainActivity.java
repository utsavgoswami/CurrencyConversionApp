package com.myappcompany.utsavgoswami.simplecurrencyconverter;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.TextView;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    String selectedBaseCurrency;
    String selectedConvertCurrency;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

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

    public void convert(View view) throws MalformedURLException, IOException {
        EditText userAmount = (EditText) findViewById(R.id.userAmount);
        Double amount = Double.parseDouble(userAmount.getText().toString());
        //Connection c = new Connection();

        // String conversionMessage = c.doInBackground(userAmount.getText().toString());

        // Setting URL
        String url_str = "https://api.exchangerate-api.com/v4/latest/" + selectedBaseCurrency;

        // Making request
        URL url = new URL(url_str);
        HttpURLConnection request = (HttpURLConnection) url.openConnection();
        request.connect();


        // Convert to JSON
        JsonParser jp = new JsonParser();
        JsonElement root = jp.parse(new InputStreamReader((InputStream) request.getContent()));
        JsonObject jsonobj = root.getAsJsonObject();

        // Accessing object
        String req_result = jsonobj.toString();
        int cutoff = req_result.indexOf("\"rates\"");
        int startIndex = cutoff + 8;

        String currencyData = req_result.substring(startIndex);

        int priceIndex = currencyData.indexOf(selectedConvertCurrency) + 5;
        String conversionFactor = "";
        int i = priceIndex;
        while (currencyData.charAt(i) != ',') {
            String digit = currencyData.charAt(i) + "";
            conversionFactor += digit;
            i++;
        }

        double factor = Double.parseDouble(conversionFactor);
        double resultant = round((amount * factor), 2);

        TextView displayConversion = findViewById(R.id.conversionResults);
        String conversionMessage = amount + " " + selectedBaseCurrency + " = " + resultant + " " +
        selectedConvertCurrency;
        displayConversion.setText(conversionMessage);

    }

    public class Connection extends AsyncTask<String, String, String>  {
        @Override
        protected  String doInBackground(String... args) {
            Double amount = Double.parseDouble(args[0]);

            // Setting URL
            String url_str = "https://api.exchangerate-api.com/v4/latest/" + selectedBaseCurrency;

            String conversionMessage = "";

            try {
                // Making request
                URL url = new URL(url_str);
                HttpURLConnection request = (HttpURLConnection) url.openConnection();
                //request.setReadTimeout(10000);
                //request.setConnectTimeout(15000);
                //request.setRequestMethod("GET");
                //request.setDoInput(true);
                // request.getResponseCode();
                // request.connect();

                // Convert to JSON
                JsonParser jp = new JsonParser();
                JsonElement root = jp.parse(new InputStreamReader((InputStream) request.getContent()));
                JsonObject jsonobj = root.getAsJsonObject();

                // Accessing object
                String req_result = jsonobj.toString();
                int cutoff = req_result.indexOf("\"rates\"");
                int startIndex = cutoff + 8;

                String currencyData = req_result.substring(startIndex);

                int priceIndex = currencyData.indexOf(selectedConvertCurrency) + 5;
                String conversionFactor = "";
                int i = priceIndex;
                while (currencyData.charAt(i) != ',') {
                    String digit = currencyData.charAt(i) + "";
                    conversionFactor += digit;
                    i++;
                }

                double factor = Double.parseDouble(conversionFactor);
                double resultant = round((amount * factor), 2);

                // TextView displayConversion = findViewById(R.id.conversionResults);

                conversionMessage += amount + " " + selectedBaseCurrency + " = " + " " + resultant
                        + " " + selectedConvertCurrency;

            } catch (MalformedURLException e) {
                Log.e("Error: ", "MalformedURLException");
            } catch (IOException e) {
                 Log.e("Error: ", "MalformedURLException");
            }

            return conversionMessage;
        }
    }

    // Taken from https://stackoverflow.com/questions/2808535/round-a-double-to-2-decimal-places
    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
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
