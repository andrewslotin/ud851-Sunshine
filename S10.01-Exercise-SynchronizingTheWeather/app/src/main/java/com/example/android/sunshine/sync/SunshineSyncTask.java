package com.example.android.sunshine.sync;

import android.content.ContentValues;
import android.content.Context;
import android.util.Log;

import com.example.android.sunshine.data.WeatherContract;
import com.example.android.sunshine.utilities.NetworkUtils;
import com.example.android.sunshine.utilities.OpenWeatherJsonUtils;

import org.json.JSONException;

import java.io.IOException;

public class SunshineSyncTask {

    synchronized public static void syncWeather(Context context) {
        try {
            String jsonData = NetworkUtils.getResponseFromHttpUrl(NetworkUtils.getUrl(context));
            ContentValues[] results = OpenWeatherJsonUtils.getWeatherContentValuesFromJson(context, jsonData);

            if (null == results || results.length == 0) return;

            context.getContentResolver().delete(
                    WeatherContract.WeatherEntry.CONTENT_URI,
                    "",
                    null
            );

            context.getContentResolver().bulkInsert(
                    WeatherContract.WeatherEntry.CONTENT_URI,
                    results
            );
        } catch (IOException e) {
            Log.d("SunshineSyncTask", "failed to fetch weather data", e);
        } catch (JSONException e) {
            Log.d("SunshineSyncTask", "failed to parse weather data", e);
        }
    }
}