package com.example.isen.noroughapk.Weather;

import java.util.List;

/**
 * Created by Thaddée on 19/12/2016.
 */

public class OpenWheatherMap {
    private Coordinate coordinate;
    private List<Weather> weather;
    private String base;
    private Main main;
    private Wind wind;
    private Rain rain;
    private Clouds clouds;
    private int dt;
    private Sys sys;
    private int id;
    private String name;
    private int cod;

    public OpenWheatherMap() {
    }

    public OpenWheatherMap(Coordinate coordinate, List<Weather> weatherList, String base, Main main, Wind wind, Rain rain, Clouds clouds, int dt, Sys sys, int id, String name, int cod) {
        this.coordinate = coordinate;
        this.weather = weatherList;
        this.base = base;
        this.main = main;
        this.wind = wind;
        this.rain = rain;
        this.clouds = clouds;
        this.dt = dt;
        this.sys = sys;
        this.id = id;
        this.name = name;
        this.cod = cod;
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(Coordinate coordinate) {
        this.coordinate = coordinate;
    }

    public List<Weather> getWeather() {
        return weather;
    }

    public void setWeather(List<Weather> weather) {
        this.weather = weather;
    }

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public Main getMainWeather() {
        return main;
    }

    public void setMainWeather(Main mainWeather) {
        this.main = mainWeather;
    }

    public Wind getWind() {
        return wind;
    }

    public void setWind(Wind wind) {
        this.wind = wind;
    }

    public Rain getRain() {
        return rain;
    }

    public void setRain(Rain rain) {
        this.rain = rain;
    }

    public Clouds getClouds() {
        return clouds;
    }

    public void setClouds(Clouds clouds) {
        this.clouds = clouds;
    }

    public int getDt() {
        return dt;
    }

    public void setDt(int dt) {
        this.dt = dt;
    }

    public Sys getSys() {
        return sys;
    }

    public void setSys(Sys sys) {
        this.sys = sys;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCod() {
        return cod;
    }

    public void setCod(int cod) {
        this.cod = cod;
    }
}
