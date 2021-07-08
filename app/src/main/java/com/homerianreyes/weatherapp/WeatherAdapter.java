package com.homerianreyes.weatherapp;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.ViewHolder> {

    private Context context;


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(WeatherAdapter.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView weatherTitle;
        private WeatherAdapter mAdapter;

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
