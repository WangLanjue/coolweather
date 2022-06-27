package com.wlj.coolweather.service;

import static com.wlj.coolweather.WeatherActivity.BING_PIC_URL;
import static com.wlj.coolweather.WeatherActivity.URL_KEY;
import static com.wlj.coolweather.WeatherActivity.WEATHER_URL;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.SystemClock;
import android.preference.PreferenceManager;

import com.wlj.coolweather.gson.Weather;
import com.wlj.coolweather.util.HttpUtil;
import com.wlj.coolweather.util.Utility;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/*
* 后台自动更新天气信息，8小时自动更新1次
* */
public class AutoUpdateService extends Service {

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        updateWeather();
        updateBingPic();
        AlarmManager manager=(AlarmManager) getSystemService(ALARM_SERVICE);
        int anHour=8*60*60*1000;  //8小时毫秒数
        long triggerAtTime= SystemClock.elapsedRealtime()+anHour;
        Intent i=new Intent(this,AutoUpdateService.class);
        PendingIntent pi=PendingIntent.getService(this,0,i,0);
        manager.cancel(pi);
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,triggerAtTime,pi);
        return super.onStartCommand(intent,flags,startId);

    }

    /*
    * 更新必应每日一图
    * */
    private void updateBingPic() {
        String requestBingPic=BING_PIC_URL;
        HttpUtil.sendOkHttpRequest(requestBingPic, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String bingPic=response.body().string();
                SharedPreferences.Editor editor=PreferenceManager.getDefaultSharedPreferences(AutoUpdateService.this).edit();
                editor.putString("bing_pic",bingPic);
                editor.apply();
            }
        });

    }


    /*
    * 更新天气信息
    * */
    private void updateWeather() {
        SharedPreferences prefs= PreferenceManager.getDefaultSharedPreferences(this);
        String weatherString=prefs.getString("weather",null);
        if(weatherString!=null){
            //有缓存天气数据
            Weather weather= Utility.handleWeatherResponse(weatherString);
            String weatherId=weather.basic.weatherId;
            String weatherUrl=WEATHER_URL+weatherId+URL_KEY;
            HttpUtil.sendOkHttpRequest(weatherUrl, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String responseText=response.body().string();
                    Weather weather=Utility.handleWeatherResponse(responseText);
                    if(weather!=null&&"ok".equals(weather.status)){
                        SharedPreferences.Editor editor=PreferenceManager.getDefaultSharedPreferences(AutoUpdateService.this).edit();
                        editor.putString("weather",responseText);
                        editor.apply();
                    }
                }
            });
        }

    }




}