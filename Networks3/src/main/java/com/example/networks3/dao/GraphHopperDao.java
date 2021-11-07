package com.example.networks3.dao;

import com.example.networks3.domain.QueryConstants;
import com.example.networks3.dto.CoordsDto;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.Dsl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class GraphHopperDao {

    private final AsyncHttpClient asyncHttpClient;

    public GraphHopperDao() {
        this.asyncHttpClient = Dsl.asyncHttpClient();
    }

    public CompletableFuture<List<CoordsDto>> getCoordsOfPossibleVaries(String placeName) throws ExecutionException, InterruptedException {
        final String url = String.format(
            "https://graphhopper.com/api/1/geocode?q=%s&locale=%s&key=%s",
            placeName,
            QueryConstants.LOCALE,
            QueryConstants.API_KEY_GRAPHHOPPER
        );

        return asyncHttpClient.prepareGet(url)
            .execute()
            .toCompletableFuture()
            .thenApply(response -> {
                JsonElement hitsArray = new Gson().fromJson(response.getResponseBody(), JsonObject.class).get("hits");

                return new Gson().fromJson(hitsArray, new TypeToken<List<CoordsDto>>() {
                }.getType());
            });
    }

}
