package com.andrey.tasktesthandh.services;

import com.andrey.tasktesthandh.models.CurrentWeather;
import com.andrey.tasktesthandh.models.WeatherForecast;
import com.google.gson.annotations.SerializedName;

import org.apache.http.HttpException;

import java.util.ArrayList;
import java.util.List;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.http.GET;
import retrofit.http.Query;
import rx.Observable;
import rx.functions.Func1;



public class WeatherService {
    private static final String WEB_SERVICE_BASE_URL = "http://api.openweathermap.org/data/2.5";
    private static final String API_KEY = "8404f8da06a24838a7f24ea9ba7b3818";
    private final OpenWeatherMapWebService mWebService;

    public WeatherService() {
        RequestInterceptor requestInterceptor = new RequestInterceptor() {
            @Override
            public void intercept(RequestInterceptor.RequestFacade request) {
                request.addHeader("Accept", "application/json");
            }
        };

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(WEB_SERVICE_BASE_URL)
                .setRequestInterceptor(requestInterceptor)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();

        mWebService = restAdapter.create(OpenWeatherMapWebService.class);
    }

    private interface OpenWeatherMapWebService {
        @GET("/weather?units=metric&apikey=" + API_KEY)
        Observable<CurrentWeatherDataEnvelope> fetchCurrentWeather(@Query("lon") double longitude,
                                                                   @Query("lat") double latitude);

        @GET("/forecast/daily?units=metric&cnt=7&apikey=" + API_KEY)
        Observable<WeatherForecastListDataEnvelope> fetchWeatherForecasts(
                @Query("lon") double longitude, @Query("lat") double latitude);
    }

    public Observable<CurrentWeather> fetchCurrentWeather(final double longitude,
                                                          final double latitude) {
        return mWebService.fetchCurrentWeather(longitude, latitude)
                .flatMap(new Func1<CurrentWeatherDataEnvelope,
                        Observable<? extends CurrentWeatherDataEnvelope>>() {

                    // Error out if the request was not successful.
                    @Override
                    public Observable<? extends CurrentWeatherDataEnvelope> call(
                            final CurrentWeatherDataEnvelope data) {
                        return data.filterWebServiceErrors();
                    }

                }).map(new Func1<CurrentWeatherDataEnvelope, CurrentWeather>() {

                    // Parse the result and build a CurrentWeather object.
                    @Override
                    public CurrentWeather call(final CurrentWeatherDataEnvelope data) {
                        return new CurrentWeather(data.locationName, data.timestamp,
                                data.weather.get(0).description, data.main.temp,
                                data.main.temp_min, data.main.temp_max);
                    }
                });
    }

    public Observable<List<WeatherForecast>> fetchWeatherForecasts(final double longitude,
                                                                   final double latitude) {
        return mWebService.fetchWeatherForecasts(longitude, latitude)
                .flatMap(new Func1<WeatherForecastListDataEnvelope,
                        Observable<? extends WeatherForecastListDataEnvelope>>() {

                    // Error out if the request was not successful.
                    @Override
                    public Observable<? extends WeatherForecastListDataEnvelope> call(
                            final WeatherForecastListDataEnvelope listData) {
                        return listData.filterWebServiceErrors();
                    }

                }).map(new Func1<WeatherForecastListDataEnvelope, List<WeatherForecast>>() {

                    // Parse the result and build a list of WeatherForecast objects.
                    @Override
                    public List<WeatherForecast> call(final WeatherForecastListDataEnvelope listData) {
                        final ArrayList<WeatherForecast> weatherForecasts =
                                new ArrayList<>();

                        for (WeatherForecastListDataEnvelope.ForecastDataEnvelope data : listData.list) {
                            final WeatherForecast weatherForecast = new WeatherForecast(
                                    listData.city.name, data.timestamp, data.weather.get(0).description,
                                    data.temp.min, data.temp.max);
                            weatherForecasts.add(weatherForecast);
                        }

                        return weatherForecasts;
                    }
                });
    }

    private class WeatherDataEnvelope {
        @SerializedName("cod")
        private int httpCode;

        class Weather {
            public String description;
        }

        public Observable filterWebServiceErrors() {
            if (httpCode == 200) {
                return Observable.just(this);
            } else {
                return Observable.error(
                        new HttpException("There was a problem fetching the weather data."));
            }
        }
    }

    private class CurrentWeatherDataEnvelope extends WeatherDataEnvelope {
        @SerializedName("name")
        public String locationName;
        @SerializedName("dt")
        public long timestamp;
        public ArrayList<Weather> weather;
        public Main main;

        class Main {
            public float temp;
            public float temp_min;
            public float temp_max;
        }
    }

    private class WeatherForecastListDataEnvelope extends WeatherDataEnvelope {
        public Location city;
        public ArrayList<ForecastDataEnvelope> list;

        class Location {
            public String name;
        }

        class ForecastDataEnvelope {
            @SerializedName("dt")
            public long timestamp;
            public Temperature temp;
            public ArrayList<Weather> weather;
        }

        class Temperature {
            public float min;
            public float max;
        }
    }
}