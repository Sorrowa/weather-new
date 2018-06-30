package com.example.zhangzihao.weather;


import android.widget.ImageView;

/**
 * Created by zhangzihao on 2017/11/26.
 */

public class know_the_icon {
    private ImageView imageView;
    private String weather;
    public know_the_icon(ImageView imageView,String weather){
        this.imageView=imageView;
        this.weather=weather;
    }
    public void show_me_answer(){
        switch (weather){
            case "晴":
                imageView.setImageResource(R.drawable.sun);
                break;
            case "多云":
                imageView.setImageResource(R.drawable.cloudy);
                break;
            case "少云":
                imageView.setImageResource(R.drawable.few_clouds);
                break;
            case "晴间多云":
                imageView.setImageResource(R.drawable.partly_cloudy);
                break;
            case "阴":
                imageView.setImageResource(R.drawable.overcast);
                break;
            case "有风":
                imageView.setImageResource(R.drawable.windy);
                break;
            case "平静":
                imageView.setImageResource(R.drawable.calm);
                break;
            case "微风":
                imageView.setImageResource(R.drawable.light_breeze);
                break;
            case "和风":
                imageView.setImageResource(R.drawable.moderategentle_breeze);
                break;
            case "清风":
                imageView.setImageResource(R.drawable.fresh_breeze);
                break;
            case "强风":
                imageView.setImageResource(R.drawable.strong_breeze);
                break;
            case "劲风":
                imageView.setImageResource(R.drawable.strong_breeze);
                break;
            case "疾风":
                imageView.setImageResource(R.drawable.high_wind_near_gale);
                break;
            case "大风":
                imageView.setImageResource(R.drawable.gale);
                break;
            case "烈风":
                imageView.setImageResource(R.drawable.strong_gale);
                break;
            case "风暴":
                imageView.setImageResource(R.drawable.storm);
                break;
            case "狂爆风":
                imageView.setImageResource(R.drawable.violent_storm);
                break;
            case "飓风":
                imageView.setImageResource(R.drawable.hurricane);
                break;
            case "龙卷风":
                imageView.setImageResource(R.drawable.tornado);
                break;
            case "热带风暴":
                imageView.setImageResource(R.drawable.tropical_storm);
                break;
            case "阵雨":
                imageView.setImageResource(R.drawable.shower_rain);
                break;
            case "强阵雨":
                imageView.setImageResource(R.drawable.heavy_shower_rain);
                break;
            case "雷阵雨":
                imageView.setImageResource(R.drawable.thundershower);
                break;
            case "强雷阵雨":
                imageView.setImageResource(R.drawable.heavy_thunderstorm);
                break;
            case "雷阵雨伴有冰雹":
                imageView.setImageResource(R.drawable.hail);
                break;
            case "小雨":
                imageView.setImageResource(R.drawable.light_rain);
                break;
            case "中雨":
                imageView.setImageResource(R.drawable.moderate_rain);
                break;
            case "大雨":
                imageView.setImageResource(R.drawable.heavy_rain);
                break;
            case "极端阵雨":
                imageView.setImageResource(R.drawable.extreme_rain);
                break;
            case "毛毛雨":
                imageView.setImageResource(R.drawable.drizzle_rain);
                break;
            case "细雨":
                imageView.setImageResource(R.drawable.drizzle_rain);
                break;
            case "暴雨":
                imageView.setImageResource(R.drawable.storm);
                break;
            case "大暴雨":
                imageView.setImageResource(R.drawable.heavy_storm);
                break;
            case "特大暴雨":
                imageView.setImageResource(R.drawable.severe_storm);
                break;
            case "冻雨":
                imageView.setImageResource(R.drawable.freezing_rain);
                break;
            case "小雪":
                imageView.setImageResource(R.drawable.light_snow);
                break;
            case "中雪":
                imageView.setImageResource(R.drawable.moderate_snow);
                break;
            case "大雪":
                imageView.setImageResource(R.drawable.heavy_snow);
                break;
            case "暴雪":
                imageView.setImageResource(R.drawable.sleet);
                break;
            case "雨雪天气":
                imageView.setImageResource(R.drawable.rain_and_snow);
                break;
            case "阵雨夹雪":
                imageView.setImageResource(R.drawable.shower_snow);
                break;
            case "阵雪":
                imageView.setImageResource(R.drawable.snow_flurry);
                break;
            case "薄雾":
                imageView.setImageResource(R.drawable.mist);
                break;
            case "雾":
                imageView.setImageResource(R.drawable.foggy);
                break;
            case "霾":
                imageView.setImageResource(R.drawable.haze);
                break;
            case "扬沙":
                imageView.setImageResource(R.drawable.sand);
                break;
            case "浮尘":
                imageView.setImageResource(R.drawable.dust);
                break;
            case "沙尘暴":
                imageView.setImageResource(R.drawable.duststorm);
                break;
            case "强沙尘暴":
                imageView.setImageResource(R.drawable.sandstorm);
                break;
            case "热":
                imageView.setImageResource(R.drawable.hot);
                break;
            case "冷":
                imageView.setImageResource(R.drawable.cold);
                break;
            case "未知":
                imageView.setImageResource(R.drawable.unknown);
                break;
        }
    }
}
