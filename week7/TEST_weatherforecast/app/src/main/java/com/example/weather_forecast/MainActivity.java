package com.example.weather_forecast;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    TextView cityName;
    Button searchButton;
    TextView result;
    class Weather extends AsyncTask<String,Void,String>{

        @Override
        protected String doInBackground(String... address) {

            try {
                URL url = new URL(address[0]);
                HttpURLConnection connection = (HttpURLConnection)url.openConnection();
                connection.connect();

                InputStream is = connection.getInputStream();
                InputStreamReader isr = new InputStreamReader(is);

                int data = isr.read();
                String content = "";
                char ch;
                while(data != -1){
                    ch=(char)data;
                    content = content+ch;
                    data = isr.read();
                }
                return content;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            catch (IOException e){
                e.printStackTrace();
            }
            return null;
        }
    }

    public void search(View view){
        cityName = findViewById(R.id.cityName);
        searchButton = findViewById(R.id.searchButton);
        result = findViewById(R.id.resultText);
        String cName = cityName.getText().toString();

        String content;
        Weather weather = new  Weather();
        try {
            content = weather.execute("https://openweathermap.org/data/2.5/weather?q="
                    +cName+"&appid=439d4b804bc8187953eb36d2a8c26a02").get();
//            Log.i("content",content);

            //JSON
            JSONObject jsonObject = new JSONObject(content);
            String weatherData = jsonObject.getString("weather");

            String mainTemperature = jsonObject.getString("main");
            String Wind = jsonObject.getString("wind");

            double visibility;
            Log.i("weatherData",weatherData);

            JSONArray array = new JSONArray(weatherData);
            String main = "";
            String description = "";
            String temperature = "";
            String windspeed = "";
            String winddeg = "";

            for(int i=0;i< array.length();i++){
                JSONObject weatherPart =array.getJSONObject(i);
                main = weatherPart.getString("main");
                description = weatherPart.getString("description");
            }

            JSONObject mainPart = new JSONObject(mainTemperature);
            temperature  = mainPart.getString("temp");
            JSONObject Jwind = new JSONObject(Wind);
            visibility = Double.parseDouble(jsonObject.getString("visibility"));
            windspeed = Jwind.getString("speed");
            winddeg = Jwind.getString("deg");

            int visibilityUnKilometer = (int) visibility/1000;
            Log.i("Temperature",temperature);
            Log.i("main",main);
            Log.i("main",description);


            String resultText
                    ="Main :                    "+main
                    +"\nDescription :         "+description
                    +"\n\nTemperature :      "+temperature
                    +"\nVisibility :              "+visibilityUnKilometer+" K"
                    +"\n\nWind"
                    +"\n- Speed :                "+windspeed
                    +"\n- Degree :              "+winddeg;
            result.setText(resultText);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }
}
