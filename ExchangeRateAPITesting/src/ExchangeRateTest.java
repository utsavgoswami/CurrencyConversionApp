import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import jdk.nashorn.internal.parser.JSONParser;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.Scanner;
public class ExchangeRateTest {
    public static void main(String[] args) throws MalformedURLException, IOException {
        Scanner console = new Scanner(System.in);
        System.out.println("Enter base currency abbreviation");
        String baseCurrency = console.nextLine().toUpperCase();
        System.out.println("Enter the currency you want to convert to");
        String convertedCurrency = console.nextLine().toUpperCase();
        System.out.println("Enter your amount");
        Double amount = console.nextDouble();

        // Setting URL
        // Hardcoded USD for now
        String url_str = "https://api.exchangerate-api.com/v4/latest/" + baseCurrency;

        // Making Request
        URL url = new URL(url_str);
        HttpURLConnection request = (HttpURLConnection) url.openConnection();
        request.connect();

        // Convert to JSON
        JsonParser jp = new JsonParser();
        JsonElement root = jp.parse(new InputStreamReader((InputStream) request.getContent()));
        JsonObject jsonobj = root.getAsJsonObject();

        // Accessing object
        String req_result = jsonobj.toString();
        int cutOff = req_result.indexOf("\"rates\"");
        int startIndex = cutOff + 8;


        String currencyData = req_result.substring(startIndex);
        System.out.println(req_result);
        System.out.println(currencyData);

        int priceIndex = currencyData.indexOf(convertedCurrency) + 5;
        String conversionFactor = "";
        int i = priceIndex;
        while (currencyData.charAt(i) != ',') {
            String digit = currencyData.charAt(i) + "";
            conversionFactor += digit;
            i++;
        }

        double factor = Double.parseDouble(conversionFactor);

        double resultant = round((amount * factor), 2);

        System.out.println(amount + " " + baseCurrency + " is " + resultant + convertedCurrency);

        //Gson gson = new Gson();
        //Type empMapType = new TypeToken<Map<String, Double>>() {}.getType();
        //Map<String, Double> rates = gson.fromJson(currencyData, empMapType);

        //for (String currency : rates.keySet()) {
        //    System.out.println("Currency: " + currency);
        //}
    }

    // Taken from https://stackoverflow.com/questions/2808535/round-a-double-to-2-decimal-places
    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
