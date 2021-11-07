package com.example.networks3.service;

import com.example.networks3.dao.GraphHopperDao;
import com.example.networks3.dao.OpenTripMapDao;
import com.example.networks3.dao.OpenWeatherDao;
import com.example.networks3.dto.CoordsDto;
import com.example.networks3.dto.FullInfoDto;
import com.example.networks3.dto.PlaceInfoDto;
import com.example.networks3.dto.PlaceXidDto;
import com.example.networks3.dto.WeatherDto;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AppService {

    private final GraphHopperDao graphHopperDao;

    private final OpenTripMapDao openTripMapDao;

    private final OpenWeatherDao openWeatherDao;

    public AppService() {
        this.graphHopperDao = new GraphHopperDao();
        this.openTripMapDao = new OpenTripMapDao();
        this.openWeatherDao = new OpenWeatherDao();
    }

    public CompletableFuture<List<FullInfoDto>> getByPlaceName(String placeName) {
        try {
            CompletableFuture<List<CoordsDto>> coordsDtoFutures = graphHopperDao.getCoordsOfPossibleVaries(placeName);

            CompletableFuture<List<PlaceXidDto>> placeXidDtoFutures = coordsDtoFutures.thenCompose(
                coordsDtos -> {
                    List<CompletableFuture<List<PlaceXidDto>>> futures = new ArrayList<>();

                    for (CoordsDto coordsDto : coordsDtos) {
                        try {
                            futures.add(openTripMapDao.getByRadius(coordsDto, placeName));
                        } catch (ExecutionException | InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }

                    return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
                        .thenApply(
                            __ -> futures.stream()
                                .map(CompletableFuture::join)
                                .collect(Collectors.toList())
                        ).thenApply(lists -> lists.stream().distinct().reduce(new ArrayList<>(), (list1, list2) ->
                            Stream.concat(list1.stream(), list2.stream()).collect(Collectors.toList()))
                        );
                }
            );

            CompletableFuture<List<PlaceInfoDto>> placeInfoDtoFutures = placeXidDtoFutures.thenCompose(
                xidDtoList -> {
                    List<CompletableFuture<PlaceInfoDto>> futures = new ArrayList<>();

                    for (PlaceXidDto placeXidDto : xidDtoList) {
                        try {
                            futures.add(openTripMapDao.getByXid(placeXidDto));
                        } catch (ExecutionException | InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }

                    return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
                        .thenApply(
                            __ -> futures.stream()
                                .map(CompletableFuture::join)
                                .collect(Collectors.toList())
                        );
                }
            );

            CompletableFuture<List<WeatherDto>> weatherDtoFutures = placeXidDtoFutures.thenCompose(
                xidDtoList -> {
                    List<CompletableFuture<WeatherDto>> futures = new ArrayList<>();

                    for (PlaceXidDto placeXidDto : xidDtoList) {
                        try {
                            futures.add(openWeatherDao.getWeather(placeXidDto));
                        } catch (ExecutionException | InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }

                    return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
                        .thenApply(
                            __ -> futures.stream()
                                .map(CompletableFuture::join)
                                .collect(Collectors.toList())
                        );
                }
            );

            return placeInfoDtoFutures.thenCombine(weatherDtoFutures, (listPlaceInfo, listWeather) -> {
                Iterator<PlaceInfoDto> placeInfoDtoIterator = listPlaceInfo.iterator();
                Iterator<WeatherDto> weatherDtoIterator = listWeather.iterator();

                List<FullInfoDto> fullInfoDtos = new ArrayList<>();
                while (placeInfoDtoIterator.hasNext() && weatherDtoIterator.hasNext()) {
                    fullInfoDtos.add(new FullInfoDto(placeInfoDtoIterator.next(), weatherDtoIterator.next()));
                }

                return fullInfoDtos;
            });
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e.getCause());
        }
    }

}
