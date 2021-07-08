package com.homerianreyes.weatherapp;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class WeatherDataService {

    public static final String QUERY_FOR_CITY_ID = "https://www.metaweather.com/api/location/search/?query=";
    public static final String QUERY_FOR_CITY_WEATHER_BY_ID = "https://www.metaweather.com/api/location/";
    private Context context;
    private String cityID;

    public WeatherDataService(Context context) {
        this.context = context;
    }

    public interface VolleyResponseListener {
        void onError(String message);
        void onResponse(String cityID);
    }

    public void getCityID(String cityName, VolleyResponseListener volleyResponseListener) {

        // Instantiate the RequestQueue.
        String url = QUERY_FOR_CITY_ID + cityName;
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url,null,  new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    JSONObject cityInfo = response.getJSONObject(0);
                    cityID = cityInfo.getString("woeid");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                volleyResponseListener.onResponse(cityID);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                volleyResponseListener.onError("Error occured.");
            }
        });
        // Add the request to the RequestQueue.
        MySingleton.getInstance(context).addToRequestQueue(request);

    }

    public interface ForecastByIDResponse {
        void onError(String message);
        void onResponse(List<WeatherReportModel> weatherReportModels);
    }

    public void getCityForecastByID(String cityID, ForecastByIDResponse forecastByIDResponse) {
        List<WeatherReportModel> weatherReportModels = new ArrayList<>();

        String url = QUERY_FOR_CITY_WEATHER_BY_ID + cityID;
        //get json object
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //get the property
                try {
                    JSONArray consolidated_weather_list =  response.getJSONArray("consolidated_weather");

                    for (int i = 0; i < consolidated_weather_list.length(); i++) {

                    WeatherReportModel one_day_weather = new WeatherReportModel();
                    JSONObject first_day_from_api = (JSONObject) consolidated_weather_list.get(0);

                    one_day_weather.setId(first_day_from_api.getInt("id"));
                    one_day_weather.setWeather_state_name(first_day_from_api.getString("weather_state_name"));
                    one_day_weather.setWeather_state_abbr(first_day_from_api.getString("weather_state_abbr"));
                    one_day_weather.setWind_direction_compass(first_day_from_api.getString("wind_direction_compass"));
                    one_day_weather.setCreated(first_day_from_api.getString("created"));
                    one_day_weather.setApplicable_date(first_day_from_api.getString("applicable_date"));
                    one_day_weather.setMin_temp((float) first_day_from_api.getDouble("min_temp"));
                    one_day_weather.setMax_temp((float) first_day_from_api.getDouble("max_temp"));
                    one_day_weather.setThe_temp((float) first_day_from_api.getDouble("the_temp"));
                    one_day_weather.setWind_speed((float) first_day_from_api.getDouble("wind_speed"));
                    one_day_weather.setWind_direction((float) first_day_from_api.getDouble("wind_direction"));
                    one_day_weather.setAir_pressure((float) first_day_from_api.getDouble("air_pressure"));
                    one_day_weather.setHumidity(first_day_from_api.getInt("humidity"));
                    one_day_weather.setVisibility((float) first_day_from_api.getDouble("visibility"));
                    one_day_weather.setPredictability(first_day_from_api.getInt("predictability"));

                    weatherReportModels.add(one_day_weather);
                    }
                    forecastByIDResponse.onResponse(weatherReportModels);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                forecastByIDResponse.onError("Error occured.");
            }
        });

        MySingleton.getInstance(context).addToRequestQueue(request);
    }

    public interface getCityNameForecastByNameCallBack {
        void onError(String message);
        void onResponse(List<WeatherReportModel> weatherReportModels);
    }

    public void getCityForecastByName(String cityName, getCityNameForecastByNameCallBack getCityNameForecastByNameCallBack) {
        //fetch the city id given the city name
        getCityID(cityName, new VolleyResponseListener() {
            @Override
            public void onError(String message) {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(String cityID) {
                getCityForecastByID(cityID, new ForecastByIDResponse() {
                    @Override
                    public void onError(String message) {
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(List<WeatherReportModel> weatherReportModels) {
                        getCityNameForecastByNameCallBack.onResponse(weatherReportModels);
                    }
                });
            }
        });
    }
}
