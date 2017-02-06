package DataResource;

import android.os.Environment;

/**
 * Created by u on 2016/10/13.
 */
public class Data_weather {

    public static String city_face="https://free-api.heweather.com/v5/weather";
    public static String weather_img="http://files.heweather.com/cond_icon/";
    public static String city_list="http://files.heweather.com/china-city-list.json";
    public static String key="420b013813d9411f80dc60bb7017d033";

    public static String FileImage=Environment.getExternalStorageDirectory().getPath() + "//MyWeather"+"//Cache"+"//Image";
}
