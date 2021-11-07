package com.example.networks3.controller;

import com.example.networks3.dto.FullInfoDto;
import com.example.networks3.service.AppService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping(value = "/application")
public class AppController {

    @RequestMapping(value = "/getPlacesInfo/{placeName}", method = RequestMethod.GET)
    public CompletableFuture<List<FullInfoDto>> getFullPlaceInfo(@PathVariable String placeName) {
        return new AppService().getByPlaceName(placeName);
    }

}
