package com.example.WeatherApp.controller;

import java.util.List;

import com.example.WeatherApp.Model.Weather;
import com.example.WeatherApp.service.WeatherService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping(path = "/weather", produces=MediaType.TEXT_HTML_VALUE)
public class WeatherController {
    public static final Logger logger =LoggerFactory.getLogger(WeatherController.class);

    @Autowired
    private WeatherService weatherService;

    @GetMapping
    public String getWeather(@RequestParam(required=true) String city, Model model){
        String cityForQuery = city.replace(" ", "+");
        List<Weather> wxList = weatherService.getWeather(cityForQuery);
        model.addAttribute("city", city.toUpperCase());
       model.addAttribute("weather", wxList);
        return "weather";

    }

    

    
}
