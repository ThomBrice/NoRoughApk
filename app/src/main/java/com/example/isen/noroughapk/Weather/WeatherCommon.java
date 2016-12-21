package com.example.isen.noroughapk.Weather;

import android.support.annotation.NonNull;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Thaddée on 15/12/2016.
 */

public class WeatherCommon {
    public static String API_KEY ="ed5e3c3aa4bac08cd2de5807706398d6";
    public static String API_LINK="http://api.openweathermap.org/data/2.5/weather";

    @NonNull
    public static String apiRequest(String lat, String lng){
        StringBuilder sb =new StringBuilder(API_LINK);
        sb.append(String.format("?lat=%s&lon=%s&appid=%s",lat,lng,API_KEY));
        return sb.toString();
    }

    public static String unixTimeStampToDateTime(double uniwTimeStamp){
        DateFormat dateFormat = new SimpleDateFormat("HH:mm");
        Date date =new Date();
        date.setTime((long)uniwTimeStamp*1000);
        return dateFormat.format(date);
    }

    public static String getImage(String icon){
        return String.format("https://openweathermap.org/img/w/%s.png",icon);
    }

    public static String getDateNow(){
        DateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy HH:mm");
        Date date =new Date();
        return dateFormat.format(date);
    }

}
