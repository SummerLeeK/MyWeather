package com.example.u.myweather;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import DataResource.Data_weather;
import HTTP.Http;
import ui.MyPaperAdapter;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;


public class MainActivity extends Activity {

    private Map<String,String>  city_list;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private TextView toolbar_title;
    private ImageView image_city;
    private PtrClassicFrameLayout mPtrFrame;
    private ViewPager citypaper;
    private MyPaperAdapter adapter;
    private List<Map<String,Object>> now;
    private List<Map<String,Object>> suggestion;
    private List<Map<String,Map>> daily_list;
    private List<View> papers;
    private ArrayList<String> citys;
    private Bundle bundle;
    private Handler handler=new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what==0x123)
            {
                Toast.makeText(getApplicationContext(), "连接服务器失败...", Toast.LENGTH_LONG).show();
            }

            else if(msg.what==0x110)
            {
                Toast.makeText(getApplicationContext(),"数据解析失败",Toast.LENGTH_LONG).show();
            }

            else if (msg.what==0x111)
            {
                adapter.setData(now, daily_list, suggestion);
                adapter.notifyDataSetChanged();
                mPtrFrame.refreshComplete();
                Toast.makeText(getApplicationContext(),"数据更新成功",Toast.LENGTH_LONG).show();

            }
        }
    };


    public void initFrame()
    {

        image_city= (ImageView) findViewById(R.id.image_city);
        toolbar_title= (TextView) findViewById(R.id.title);
        List<Map<String,Object>> listitems=new ArrayList<Map<String,Object>>();
        List<Map<String,Object>> listitems_about=new ArrayList<Map<String,Object>>();

        //获取城市列表
        citys=new ArrayList<>();
        city_list=new HashMap<>();
        papers=new ArrayList<>();
        now= new ArrayList<Map<String, Object>>();
        daily_list=new ArrayList<Map<String,Map>>() ;
        suggestion=new ArrayList<Map<String,Object>>();
        sharedPreferences=getSharedPreferences("citylist",MODE_PRIVATE);

        city_list= (Map<String, String>) sharedPreferences.getAll();

        for (int i=0;i<city_list.size();i++)
        {
            Log.i("info", city_list.get(String.valueOf(i)));
            citys.add(city_list.get(String.valueOf(i)));
        }

        bundle=new Bundle();
        bundle.putStringArrayList("city", citys);




        image_city.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CityManager.class);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                startActivity(intent);


            }

        });





    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initFrame();
        for (int i=0;i<citys.size();i++){
            LayoutInflater layoutInflater=LayoutInflater.from(this);
            View page=layoutInflater.inflate(R.layout.weather_list,null,false);
            papers.add(page);
        }



        mPtrFrame = (PtrClassicFrameLayout)findViewById(R.id.material_style_ptr_frame);
        citypaper= (ViewPager) findViewById(R.id.viewpaper);


        adapter=new MyPaperAdapter(MainActivity.this,papers);
        mPtrFrame.setLastUpdateTimeRelateObject(this);
        mPtrFrame.setPtrHandler(new PtrHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                updateData();
            }

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
            }
        });
        // the following are default settings
        mPtrFrame.setResistance(1.7f);
        mPtrFrame.setRatioOfHeaderHeightToRefresh(1.2f);
        mPtrFrame.setDurationToClose(200);
        mPtrFrame.setDurationToCloseHeader(1000);
        // default is false
        mPtrFrame.setPullToRefresh(false);
        // default is true
        mPtrFrame.setKeepHeaderWhenRefresh(true);
        mPtrFrame.postDelayed(new Runnable() {
            @Override
            public void run() {
                mPtrFrame.autoRefresh();
            }
        }, 100);

        adapter.setData(now, daily_list, suggestion);

        citypaper.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (now.size()!=0) {
                    toolbar_title.setText(now.get(position).get("city").toString());
                } else {
                    toolbar_title.setText("天气");
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        citypaper.setAdapter(adapter);




    }


    protected void updateData() {

        mPtrFrame.postDelayed(new Runnable() {
            @Override
            public void run() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 0; i < citys.size(); i++) {
                            Map<String, Object> name = new HashMap<String, Object>();
                            name.put("city", citys.get(i));
                            name.put("key", Data_weather.key);

                            String data = Http.doPostSubmit(Data_weather.city_face, name);
                            if (data == null) {
                                handler.sendEmptyMessage(0x123);
                            } else {
                                try {
                                    JSONObject jsonObject = new JSONObject(data);
                                    Log.i("info", "data +++" + jsonObject);
                                    JSONArray array = jsonObject.getJSONArray("HeWeather5");
//                        Log.i("info","array  +++++"+array);


                                    JSONObject json_basic = array.getJSONObject(0);

                                    JSONArray daily = json_basic.getJSONArray("daily_forecast");
//                        Log.i("info","daily +++ "+daily);

                                    JSONObject json_aqi = json_basic.getJSONObject("aqi");

//                        Log.i("info","json_aqi +++ "+json_aqi);
                                    JSONObject json_sunggestion = json_basic.getJSONObject("suggestion");

//                        Log.i("info","json_suggestion ++ " +json_sunggestion);


                                    JSONObject json_now = json_basic.getJSONObject("now");

//                        Log.i("info", "now   +++ " + json_now);

                                    JSONObject json_cond = json_now.getJSONObject("cond");
//                        Log.i("info","json_cond    ++"+json_cond);

//                       Log.i("info", "json_basic>>>" + json_basic);
                                    JSONObject json_city = json_basic.getJSONObject("basic");
//                        Log.i("info","json_city"+json_city);
//                        Log.i("info", " json_city" + json_city.getString("city"));

                                    JSONObject json_wind = json_now.getJSONObject("wind");
                                    JSONObject json_qlty = json_aqi.getJSONObject("city");

                                    //now 存储数据。

                                    Map<String, Object> now_map = new HashMap<String, Object>();
                                    now_map.put("city", json_city.getString("city"));
                                    now_map.put("weatherTxt", json_cond.getString("txt"));
                                    now_map.put("weatherImg", json_cond.getString("code"));


                                    now_map.put("tmp", json_now.getString("tmp"));
                                    now_map.put("sc", json_wind.getString("sc"));
                                    now_map.put("qlty", json_qlty.getString("qlty"));
                                    now_map.put("pm25", json_qlty.getString("pm25"));

                                    now.add(now_map);
//                        Log.i("info","daily_length"+daily.length());
                                    //七天数据
                                    Map daily_cache = new HashMap();
                                    for (int j = 0; j < daily.length(); j++) {
                                        Map<String, Object> daily_map = new HashMap<String, Object>();
                                        JSONObject wind = daily.getJSONObject(j);
                                        JSONObject cond = wind.getJSONObject("cond");

                                        Bitmap image = Http.getGossipImage(Data_weather.weather_img, cond.getString("code_d"));
                                        String filename = Data_weather.FileImage + "/" + wind.getString("date") + cond.getString("code_d") + ".png";
                                        Http.saveBitmap(filename, image);

                                        daily_map.put("date", wind.getString("date"));
                                        daily_map.put("txt_d", cond.getString("txt_d"));
                                        daily_map.put("code_d", cond.getString("code_d"));

                                        daily_cache.put(String.valueOf(j), daily_map);
                                    }
                                    daily_list.add(daily_cache);

                                    JSONObject json_flu = json_sunggestion.getJSONObject("flu");
                                    JSONObject json_drsg = json_sunggestion.getJSONObject("drsg");
                                    JSONObject json_comf = json_sunggestion.getJSONObject("comf");
                                    JSONObject json_sport = json_sunggestion.getJSONObject("sport");
                                    JSONObject json_trav = json_sunggestion.getJSONObject("trav");
                                    JSONObject json_cw = json_sunggestion.getJSONObject("cw");
                                    JSONObject json_uv = json_sunggestion.getJSONObject("uv");


                                    Map<String, Object> suggestion_map = new HashMap<String, Object>();
                                    suggestion_map.put("flu_txt", json_flu.getString("brf"));
                                    suggestion_map.put("drsg_txt", json_drsg.getString("brf"));
                                    suggestion_map.put("comf_txt", json_comf.getString("brf"));
                                    suggestion_map.put("sport_txt", json_sport.getString("brf"));
                                    suggestion_map.put("trav_txt", json_trav.getString("brf"));
                                    suggestion_map.put("cw_txt", json_cw.getString("brf"));
                                    suggestion_map.put("uv_txt", json_uv.getString("brf"));
                                    suggestion_map.put("hum_txt", json_now.getString("hum"));

                                    suggestion.add(suggestion_map);

//
//                                    Log.i("info", "Map now" + now);
//                                    Log.i("info", "Map daily" + daily_list);
//                                    Log.i("info", "Map suggestion" + suggestion);

                                } catch (JSONException e) {
                                    handler.sendEmptyMessage(0x110);
                                    e.printStackTrace();
                                }

                            }


                        }
                        handler.sendEmptyMessage(0x111);
                    }
                }).start();

            }
        }, 0);


    }

}
