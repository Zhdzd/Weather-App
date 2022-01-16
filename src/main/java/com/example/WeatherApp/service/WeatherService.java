package com.example.WeatherApp.service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.stream.Collectors;

import com.example.WeatherApp.Model.Weather;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;



@Service
public class WeatherService {
    public static final Logger logger = LoggerFactory.getLogger(WeatherService.class);
    public static final String ENV_VAR_API = "API_KEY";
    private String urlWeather = "http://api.openweathermap.org/data/2.5/weather";
    private final String appId;



    
    public WeatherService(){
        String enVar = System.getenv(ENV_VAR_API);
        if((null != enVar) && (enVar.trim().length() > 0)){
            appId = enVar;
            logger.info("Open weather map key set");
          
         } else{
                appId = "abc123";
                logger.info("API key: " + appId);
         }
    }
    
    public List<Weather> getWeather(String city){
     

        String url = UriComponentsBuilder
            .fromUriString(urlWeather)
            .queryParam("q", city.trim().replace(" ", "+"))
            .queryParam("appid", appId)
            .queryParam("units", "metric")
            .toUriString();
        
        logger.info("url: " +url);

        RequestEntity<Void> req = RequestEntity.get(url).build();
        RestTemplate template = new RestTemplate();
        ResponseEntity<String> resp = template.exchange(req, String.class);

        if(resp.getStatusCode() != HttpStatus.OK)
            throw new IllegalArgumentException(
                "Error: status code %s".formatted(resp.getStatusCode().toString())
            );
        String body = resp.getBody();
        //logger.log(Level.INFO, "payload: %s".formatted(body));

        try(InputStream is = new ByteArrayInputStream(body.getBytes())){
            JsonReader reader = Json.createReader(is);
            JsonObject result = reader.readObject();
            JsonArray readings = result.getJsonArray("weather");
            String cityName = result.getString("name");
            float temperature = (float)result.getJsonObject("main").getJsonNumber("temp").doubleValue();
            
            return readings.stream()  
                .map(v -> (JsonObject)v)
                .map(Weather::create)
                .map(w ->{
                    w.setCityName(cityName);
                    w.setTemperature(temperature);
                    return w;
                })
                .collect(Collectors.toList());
            } catch (Exception ex){
                ex.printStackTrace();
            }
            return Collections.EMPTY_LIST;
            
            
        }
        
}
