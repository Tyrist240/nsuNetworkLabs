package com.example.networks3.dao;

import com.example.networks3.domain.QueryConstants;
import com.example.networks3.dto.PlaceXidDto;
import com.example.networks3.dto.WeatherDto;
import com.google.gson.Gson;
import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.Dsl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class OpenWeatherDao {

    private final AsyncHttpClient asyncHttpClient;

    public OpenWeatherDao() {
        this.asyncHttpClient = Dsl.asyncHttpClient();
    }

    public CompletableFuture<WeatherDto> getWeather(PlaceXidDto placeXidDto) throws ExecutionException, InterruptedException {
        final String url = String.format(
            "http://api.openweathermap.org/data/2.5/weather?lat=%s&lon=%s&appid=%s",
            placeXidDto.getPoint().getLat(),
            placeXidDto.getPoint().getLon(),
            QueryConstants.API_KEY_OPEN_WEATHER
        );

        return asyncHttpClient.prepareGet(url)
            .execute()
            .toCompletableFuture()
            .thenApply(response -> new Gson().fromJson(response.getResponseBody(), WeatherDto.class));
    }

}