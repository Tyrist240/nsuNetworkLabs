package com.example.networks3.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CoordsDto {

    private Point point;


    @Data
    @AllArgsConstructor
    public static class Point {

        private double lng;

        private double lat;

    }

}
