package com.example.WeatherApp.Model;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

public class Weather {
    private String cityName;
    private String main;
    private String description;
    private String icon;
    private float temperature;
    private float latitude;
    private float longitude;

    public String getcityName(){
        return this.cityName;
    }
    public void setCityName(String cityName){
        this.cityName = cityName;
    }
    public String getMain(){
        return this.main;
    }
    public void setMain(String main){
        this.main  = main;
    }
    public String getDescription(){
        return this.description;
    }
    public void setDescription(String description){
        this.description = description;
    }
    public String getIcon(){
        return this.icon;
    }
    public void setIcon(String icon){
        this.icon = "http://openweathermap.org/img/wn/%s@2x.png".formatted(icon);
    }
    public float getTemperature(){
        return this.temperature;
    }
    public void setTemperature(float temperature){
        this.temperature =temperature;
    }
    public float latitude(){
        return this.latitude;
    }
    public void setLatitude(float latitude){
        this.latitude = latitude;
    }
    public float geLatitude(){
        return this.latitude;
    }
    public void setLongitude(float longitude){
        this.longitude = longitude;
    }
    public static Weather create(JsonObject jObj){
            Weather wx = new Weather();
            wx.setMain(jObj.getString("main"));
            wx.setDescription(jObj.getString("description"));
            wx.setIcon(jObj.getString("icon"));
            return wx;
    }
    //creating Json String
    public static Weather create(String jsonString){
        try(InputStream is = new ByteArrayInputStream(jsonString.getBytes())){
            JsonReader reader = Json.createReader(is);
            return create(reader.readObject());
        }catch (Exception ex){ }

        return new Weather();
    }
    //returnin fields into Json object
    public JsonObject toJson(){
        return Json.createObjectBuilder()
        .add("cityName",cityName)
        .add("main", main)
        .add("description", description)
        .add("icon", icon)
        .add("temperature", temperature)
        .add("latitude", latitude)
        .add("longitude", longitude)
        .build();
    }
    public String toString(){
        return this.toJson().toString();
    }

}
