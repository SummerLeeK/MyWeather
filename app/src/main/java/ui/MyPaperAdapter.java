package ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.u.myweather.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;

import DataResource.Data_weather;

/**
 * Created by u on 2016/10/17.
 */
public class MyPaperAdapter extends PagerAdapter
{
    private List<Map<String,Object>> data;
    private TextView city,temperature,weather,windscale,pm;
    private ImageView weatherImg;
    private GridView daily_forecast,suggtion;
    private static String[] suggtion_name={"感冒","穿衣","舒适","运动","旅游","洗车","紫外线","湿度"};
    private static String[] suggtion_name_txt={"flu","drsg","comf","sport","trav","cw","uv","hum"};
    private int img_suggtion[]={R.drawable.flu,R.drawable.drsg,R.drawable.comf,R.drawable.sport,R.drawable.trav,R.drawable.cw,R.drawable.uv,R.drawable.hum};
    private Context context;

    private List<Map<String,Object>> now;
    private List<Map<String,Map>> daily_map;
    private List<Map<String,Object>> suggtion_map;
    private List<View> papers;
    public MyPaperAdapter(Context context,List<View> papers) {
        this.context=context;
        this.papers=papers;
    }

    public void setData(List<Map<String,Object>> now,List<Map<String,Map>> daily_map,List<Map<String,Object>> suggtion_map)
    {
        this.now=now;
        this.daily_map=daily_map;
        this.suggtion_map=suggtion_map;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return papers.size();
    }


    public void clear()
    {
        this.now.clear();
        this.daily_map.clear();
        this.suggtion_map.clear();

    }
    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(papers.get(position));
    }


    @Override
    public Object instantiateItem(ViewGroup container, final int position) {



//        LayoutInflater inflater = (LayoutInflater) container.getContext()
//                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//
//        View view=inflater.inflate(R.layout.weather_list,null);

        city= (TextView)papers.get(position).findViewById(R.id.txtcity);
        temperature= (TextView)papers.get(position).findViewById(R.id.temp);
        weather= (TextView) papers.get(position).findViewById(R.id.weather);
        windscale= (TextView) papers.get(position).findViewById(R.id.windscale);
        pm= (TextView) papers.get(position).findViewById(R.id.pm);
        weatherImg= (ImageView) papers.get(position).findViewById(R.id.weatherImg);
        daily_forecast= (GridView) papers.get(position).findViewById(R.id.daily_forecast);
        suggtion= (GridView) papers.get(position).findViewById(R.id.suggtion);


        Log.i("info","MypaperAdapter now"+now);

        //设置数据if
        if (now.isEmpty()==true)
        {
            Log.i("info","MypaperAdapter now");
            city.setText("null");
            temperature.setText("null");
            weather.setText("null");
            windscale.setText("null");
            pm.setText("null");
            weatherImg.setImageResource(R.drawable.wu);

        }
        else
        {
            Log.i("info",position+"postion");
            city.setText(now.get(position).get("qlty").toString());
            temperature.setText(now.get(position).get("tmp").toString()+"°");
            weather.setText(now.get(position).get("weatherTxt").toString());
            windscale.setText(now.get(position).get("sc").toString());
            pm.setText(now.get(position).get("pm25").toString());
            try {
                File file = new File(Data_weather.FileImage + "/"+daily_map.get(position).get("0").get("date").toString() + daily_map.get(position).get("0").get("code_d").toString() + ".png");
                FileInputStream inputStream = new FileInputStream(file);
                Bitmap image = BitmapFactory.decodeStream(inputStream);
                weatherImg.setImageBitmap(image);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                weatherImg.setImageResource(R.drawable.wu);
            }


        }




        daily_forecast.setAdapter(new BaseAdapter() {


            @Override
            public int getCount() {
                return 7;
            }

            @Override
            public Object getItem(int position) {
                return position;
            }

            @Override
            public long getItemId(int position) {
                return 0;
            }

            @Override
            public View getView(int positions, View convertView, ViewGroup parent) {

                View view1 = View.inflate(context, R.layout.grid_weather, null);
                TextView date = (TextView) view1.findViewById(R.id.date);
                ImageView image_date = (ImageView) view1.findViewById(R.id.img_date);
                TextView date_weather = (TextView) view1.findViewById(R.id.date_weather);

                if (daily_map.isEmpty()==true)
                {
                    date.setText("null");
                    image_date.setImageResource(R.drawable.wu);
                    date_weather.setText("null");
                }

                else
                {
                    String dating=daily_map.get(position).get(String.valueOf(positions)).get("date").toString().substring(5);
                    date.setText(dating);
                    File file = new File(Data_weather.FileImage + "/"+daily_map.get(position).get(String.valueOf(positions)).get("date").toString() + daily_map.get(position).get(String.valueOf(positions)).get("code_d").toString() + ".png");
                    FileInputStream inputStream = null;
                    try {
                        inputStream = new FileInputStream(file);
                        Bitmap image = BitmapFactory.decodeStream(inputStream);
                        image_date.setImageBitmap(image);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                        image_date.setImageResource(R.drawable.wu);
                    }


                    date_weather.setText(daily_map.get(position).get(String.valueOf(positions)).get("txt_d").toString());
                }


                return view1;
            }
        });



        suggtion.setAdapter(new BaseAdapter() {
                                @Override
                                public int getCount() {
                                    return 8;
                                }

                                @Override
                                public Object getItem(int position) {
                                    return null;
                                }

                                @Override
                                public long getItemId(int position) {
                                    return 0;
                                }

                                @Override
                                public View getView(int positions, View convertView, ViewGroup parent) {
                                    View suggtion_view = View.inflate(context, R.layout.grid_suggtion, null);

                                    ImageView image_suggtion = (ImageView) suggtion_view.findViewById(R.id.img_suggtion);

                                    TextView suggtion_brf = (TextView) suggtion_view.findViewById(R.id.suggtion_brf);

                                    TextView suggtion_txt = (TextView) suggtion_view.findViewById(R.id.suggtion_txt);


                                    suggtion_txt.setText(suggtion_name[positions]);

                                    if (suggtion_map.isEmpty()==true) {
                                        suggtion_brf.setText("null");
                                    } else {
                                        String key = suggtion_name_txt[positions] + "_txt";
                                        suggtion_brf.setText(suggtion_map.get(position).get(key).toString());
                                    }

                                    image_suggtion.setImageResource(img_suggtion[positions]);
                                    return suggtion_view;
                                }
                            });

       // Log.i("info", position + "postion");
        container.addView(papers.get(position));
        return papers.get(position);

    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
}


