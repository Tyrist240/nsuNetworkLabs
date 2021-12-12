package com.example.networks3.dao;

import com.example.networks3.domain.QueryConstants;
import com.example.networks3.dto.CoordsDto;
import com.example.networks3.dto.PlaceInfoDto;
import com.example.networks3.dto.PlaceXidDto;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.Dsl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class OpenTripMapDao {

    private final AsyncHttpClient asyncHttpClient;

    public OpenTripMapDao() {
        this.asyncHttpClient = Dsl.asyncHttpClient();
    }

    public CompletableFuture<List<PlaceXidDto>> getByRadius(CoordsDto coordsDto, String placeName) throws ExecutionException, InterruptedException {
        final String url = String.format(
            "http://api.opentripmap.com/0.1/ru/places/radius?lang=%s&radius=%s&lon=%s&lat=%s&name=%s&format=json&apikey=%s",
            QueryConstants.LOCALE,
            QueryConstants.RADIUS,
            coordsDto.getPoint().getLng(),
            coordsDto.getPoint().getLat(),
            placeName,
            QueryConstants.API_KEY_OPEN_TRIP_MAP
        );

        return asyncHttpClient.prepareGet(url)
            .execute()
            .toCompletableFuture()
            .thenApply(response -> new Gson().fromJson(response.getResponseBody(), new TypeToken<List<PlaceXidDto>>() {
            }.getType()));
    }

    public CompletableFuture<PlaceInfoDto> getByXid(PlaceXidDto placeXidDto) throws ExecutionException, InterruptedException {
        final String url = String.format(
            "http://api.opentripmap.com/0.1/ru/places/xid/%s?lang=%s&apikey=%s",
            placeXidDto.getXid(),
            QueryConstants.LOCALE,
            QueryConstants.API_KEY_OPEN_TRIP_MAP
        );

        return asyncHttpClient.prepareGet(url)
            .execute()
            .toCompletableFuture()
            .thenApply(response -> new Gson().fromJson(response.getResponseBody(), PlaceInfoDto.class));
    }

}