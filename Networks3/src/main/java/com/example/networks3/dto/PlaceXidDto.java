package com.example.networks3.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PlaceXidDto {

    private String xid;

    private Point point;

    @Data
    @AllArgsConstructor
    public static class Point {

        private double lon;

        private double lat;

    }

}
