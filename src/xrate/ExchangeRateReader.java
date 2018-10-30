package xrate;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Provide access to basic currency exchange rate services.
 * 
 * @author team-pizza: Ethan Hamer and Laly Xiong
 */
public class ExchangeRateReader {
    String baseUrl;
    String accessKey;


    public ExchangeRateReader(String newURL) throws IOException {
            this.baseUrl = newURL;
        try {
            this.accessKey = new BufferedReader(new FileReader("src/accessKey")).readLine();
        } catch (FileNotFoundException e) {
            System.out.println("Make sure the file exists");
            System.out.println(e);
        } catch (IOException e) {
            System.out.println("Something went wrong");
            System.out.println(e);
        }

    }

    public float getExchangeRate(String currencyCode, int year, int month, int day) throws IOException {

        URL completeURL = generateURL(year, month, day);

        InputStream inputStream = completeURL.openStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        JsonParser parser = new JsonParser();
        JsonObject rawData = parser.parse(reader).getAsJsonObject();

        JsonElement exchangeRate = rawData.getAsJsonObject("rates").get(currencyCode);
        Float parsedExchangeRate = exchangeRate.getAsFloat();

        return parsedExchangeRate;
    }

    private URL generateURL(int year, int month, int day) throws MalformedURLException {
        String yearString = String.valueOf(year);
        String monthString;
        String dayString;
        if(month < 10){
            monthString = "0" + String.valueOf(month);
        } else {
            monthString = String.valueOf(month);
        }
        if(day < 10){
            dayString = "0" + String.valueOf(day);
        } else {
            dayString = String.valueOf(day);
        }

        String completeURLString = baseUrl + yearString + "-" + monthString + "-" + dayString + "?access_key=" + this.accessKey;
        URL completeURL = null;
        try {
            completeURL =  new URL(completeURLString);
        } catch (MalformedURLException e) {
            System.out.println("Make sure you have a proper URL");
            System.out.println(e);
        }
        return completeURL;
    }

    public float getExchangeRate(
            String fromCurrency, String toCurrency,
            int year, int month, int day) throws IOException {

        URL completeURL = generateURL(year, month, day);

        InputStream inputStream = completeURL.openStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        JsonParser parser = new JsonParser();
        JsonObject rawData = parser.parse(reader).getAsJsonObject();

        Float fromCurrencyValue = rawData.getAsJsonObject("rates").get(fromCurrency).getAsFloat();
        Float toCurrencyValue = rawData.getAsJsonObject("rates").get(toCurrency).getAsFloat();

        return fromCurrencyValue/toCurrencyValue;
    }
}