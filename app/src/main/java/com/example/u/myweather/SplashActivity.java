package com.example.u.myweather;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.example.City;
import com.example.CityDao;
import com.example.DaoMaster;
import com.example.DaoSession;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.List;
import java.util.Map;

import DataResource.DBManager;
import DataResource.Data_weather;
import HTTP.Http;


/**
 * Created by u on 2016/10/27.
 */
public class SplashActivity extends Activity {


    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private DaoMaster master;
    private DaoSession session;
    private SQLiteDatabase db;
    private CityDao cityDao;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            Intent intent=new Intent(SplashActivity.this,MainActivity.class);
            SplashActivity.this.startActivity(intent);
            SplashActivity.this.finish();
            SplashActivity.this.overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
        }
    };

    private void openDb(){
        db=new DaoMaster.DevOpenHelper(SplashActivity.this, DBManager.DB_PATH + "/"+ DBManager.DB_NAME,null).getWritableDatabase();

        master=new DaoMaster(db);

        session=master.newSession();

        cityDao=session.getCityDao();
    }

    private void ShowAll(){
        Log.i("info",cityDao.queryBuilder().list().toString());
        //Log.i("info",list.toString());

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        File fileimage=new File(Data_weather.FileImage);
        if(!fileimage.exists()){
            fileimage.mkdirs();
        }

        sharedPreferences=getSharedPreferences("citylist",MODE_PRIVATE);
        editor=sharedPreferences.edit();

        if (sharedPreferences.getAll().size()==0) {
            editor.putString("0", "南昌");
        }
        editor.commit();
       // openDb();
//        File dbFile = this.getDatabasePath("cityid.db");

//        if (dbFile.exists()){
//
//        }else {
//
//            openDb();
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//
//                    try {
//                        String data = Http.request(Data_weather.city_list);
//                        JSONArray array = new JSONArray(data);
//
//                        for (int i = 0; i < array.length(); i++) {
//                            JSONObject jsonObject = array.getJSONObject(i);
//                            String cityZh = jsonObject.get("cityZh").toString();
//                            String provinceZh = jsonObject.get("provinceZh").toString();
//                            String leaderZh = jsonObject.get("leaderZn").toString();
//
//                            City city=new City();
//
//                            city.setCityZh(cityZh);
//
//                            city.setProvinceZh(provinceZh);
//
//                            city.setLeaderZh(leaderZh);
//
//                            cityDao.insert(city);
//
//                        }
//                        Log.i("info", "SplashActivity  数据库构建成功");
//
//                        handler.sendEmptyMessage(000);
//
//                    } catch (JSONException e) {
//
//                        e.printStackTrace();
//                    }
//
//                }
//            }).start();
//        }
//

        DBManager dbManager=new DBManager(SplashActivity.this);
        dbManager.openDatabase();
        dbManager.closeDatabase();
        openDb();
        ShowAll();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                    handler.sendEmptyMessage(00);
            }
        },3000);

    }
}
