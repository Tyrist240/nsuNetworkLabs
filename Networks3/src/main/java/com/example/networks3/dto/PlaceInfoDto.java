package com.example.networks3.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PlaceInfoDto {

    private String otm;

    private String xid;

    private String name;

    private String wikipedia;

    private String image;

    private String wikidata;

    private String rate;

    private Bbox bbox;

    private Point point;

    private Info info;

    private WikipediaExtracts wikipedia_extracts;

    private Address address;


    @Data
    @AllArgsConstructor
    public static class Bbox {

        private double lat_max;

        private double lat_min;

        private double lon_max;

        private double lon_min;

    }

    @Data
    @AllArgsConstructor
    public static class Point {

        private double lon;

        private double lat;

    }

    @Data
    @AllArgsConstructor
    public static class Info {

        private String descr;

    }

    @Data
    @AllArgsConstructor
    public static class WikipediaExtracts {

        private String text;

    }

    @Data
    @AllArgsConstructor
    public static class Address {

        private String city;

        private String house;

        private String state;

        private String county;

        private String country;

        private String postcode;

        private String pedestrian;

        private String country_code;

        private String house_number;

        private String neighbourhood;

    }

}
