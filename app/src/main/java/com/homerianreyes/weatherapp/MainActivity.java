package com.homerianreyes.weatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Button btn_cityID;
    private Button btn_getWeatherCityID;
    private Button btn_getWeatherbyName;
    private EditText mEditText_input;
    private ListView mListViewWeatherReport;
    final WeatherDataService weatherDataService = new WeatherDataService(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_cityID = (Button) findViewById(R.id.button_getCityID);
        btn_getWeatherCityID = (Button) findViewById(R.id.button_getWeatherByCityID);
        btn_getWeatherbyName = (Button) findViewById(R.id.button_getWeatherByCityName);

        mEditText_input = (EditText) findViewById(R.id.edittext_input);
        mListViewWeatherReport = (ListView) findViewById(R.id.listview);

        btn_cityID.setOnClickListener(view -> {
            weatherDataService.getCityID(mEditText_input.getText().toString(), new WeatherDataService.VolleyResponseListener() {
                @Override
                public void onError(String message) {
                    Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onResponse(String cityID) {
                    Toast.makeText(MainActivity.this, "RETURN City ID = " + cityID, Toast.LENGTH_SHORT).show();
                }
            });
        });
        btn_getWeatherCityID.setOnClickListener(view -> {
            weatherDataService.getCityForecastByID(mEditText_input.getText().toString(), new WeatherDataService.ForecastByIDResponse() {
                @Override
                public void onError(String message) {
                    Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onResponse(List<WeatherReportModel> weatherReportModels) {
                    //put the entire list into listview

                    ArrayAdapter mAdapter = new ArrayAdapter(MainActivity.this, android.R.layout.simple_list_item_1, weatherReportModels);
                    mListViewWeatherReport.setAdapter(mAdapter);
                }
            });
        });
        btn_getWeatherbyName.setOnClickListener(view -> {
            weatherDataService.getCityForecastByName(mEditText_input.getText().toString(), new WeatherDataService.getCityNameForecastByNameCallBack() {
                @Override
                public void onError(String message) {
                    Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onResponse(List<WeatherReportModel> weatherReportModels) {
                    //put the entire list into listview

                    ArrayAdapter mAdapter = new ArrayAdapter(MainActivity.this, android.R.layout.simple_list_item_1, weatherReportModels);
                    mListViewWeatherReport.setAdapter(mAdapter);
                }
            });
        });
    }
}