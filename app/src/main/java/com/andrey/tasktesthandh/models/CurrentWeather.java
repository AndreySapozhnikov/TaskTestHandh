package com.andrey.tasktesthandh.models;


public class CurrentWeather extends WeatherForecast {
    private final float mTemperature;

    public CurrentWeather(final String locationName,
                          final long timestamp,
                          final String description,
                          final float temperature,
                          final float minimumTemperature,
                          final float maximumTemperature) {

        super(locationName, timestamp, description, minimumTemperature, maximumTemperature);
        mTemperature = temperature;
    }

    public float getTemperature() {
        return mTemperature;
    }
}
