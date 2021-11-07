package com.example.networks3.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WeatherDto {

    private List<Weather> weather;

    private Main main;

    private Wind wind;

    private Clouds clouds;


    @Data
    @AllArgsConstructor
    public static class Weather {

        private int id;

        private String main;

        private String description;

    }

    @Data
    @AllArgsConstructor
    public static class Main {

        private double temp;

        private double feels_like;

        private double temp_min;

        private double temp_max;

        private int pressure;

        private int humidity;

    }

    @Data
    @AllArgsConstructor
    public static class Wind {

        private double speed;

        private double deg;

    }

    @Data
    @AllArgsConstructor
    public static class Clouds {

        private int all;

    }

}
