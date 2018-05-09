package com.example.sun.coolweather.android.util;

import android.text.TextUtils;

import com.example.sun.coolweather.android.db.City;
import com.example.sun.coolweather.android.db.County;
import com.example.sun.coolweather.android.db.Province;
import com.example.sun.coolweather.android.gson.Weather;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Utility {
    /**
     *  解析和处理服务器返回的json数据
     */
    public static boolean handleProvinceResponse(String response){
        if(!TextUtils.isEmpty(response)){
            try{
                JSONArray allProvinces = new JSONArray(response);
                for(int i = 0;i < allProvinces.length(); ++ i){
                    JSONObject provinceObject = allProvinces.getJSONObject(i);
                    Province province = new Province();
                    province.setProvinceName(provinceObject.getString("name"));
                    province.setProvinceCode(provinceObject.getInt("id"));
                    province.save();
                }
                return true;
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
        return false;
    }

    public static boolean handleCityResponse(String response,int provinceId){
        if(!TextUtils.isEmpty(response)){
            try{
                JSONArray allCities = new JSONArray(response);
                for(int i = 0;i < allCities.length(); ++ i) {
                    JSONObject cityObject = allCities.getJSONObject(i);
                    City city = new City();
                    city.setCityName(cityObject.getString("name"));
                    city.setCityCode(cityObject.getInt("id"));
                    city.setProvinceId(provinceId);
                    city.save();
                }
                return true;
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
        return false;
    }

    public static boolean handleCountyResponse(String response,int cityId){
        if(!TextUtils.isEmpty(response)){
            try{
                JSONArray allCounties = new JSONArray(response);
                for(int i = 0;i < allCounties.length(); ++ i){
                    JSONObject countyObject = allCounties.getJSONObject(i);
                    County county = new County();
                    county.setCountyName(countyObject.getString("name"));
                    county.setWeatherId(countyObject.getString("weather_id"));
                    county.setCityId(cityId);
                    county.save();
                }
                return true;
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
        return false;
    }
    /**
     * 将返回的JSON数据解析成Weather实体类
     */
    public static Weather handleWeatherResponse(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArray = jsonObject.getJSONArray("HeWeather");
            String wertherContent = jsonArray.getJSONObject(0).toString();
            return new Gson().fromJson(wertherContent, Weather.class);

        }catch (Exception e) {
            e.printStackTrace();

        }
        return null;
    }

    public static Map<String,String> handleLocationResponse(String str) {
        Map<String,String> map = null;
        if(str!=null){
            int lngStart = str.indexOf("lng\":");
            int lngEnd = str.indexOf(",\"lat");
            int latEnd = str.indexOf("},\"precise");
            if(lngStart > 0 && lngEnd > 0 && latEnd > 0){
                String lng = str.substring(lngStart+5, lngEnd);
                String lat = str.substring(lngEnd+7, latEnd);
                map = new HashMap<String,String>();
                map.put("lng", lng);
                map.put("lat", lat);
                return map;
            }
        }
        return null;
    }
}
