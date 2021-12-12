package com.example.networks3.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FullInfoDto {

    private PlaceInfoDto placeInfoDto;

    private WeatherDto weatherDto;

}
