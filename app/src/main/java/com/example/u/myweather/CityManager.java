package com.example.u.myweather;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;

import java.util.ArrayList;
import java.util.Map;

import ui.MyBaseAdapter;

/**
 * Created by u on 2016/10/28.
 */
public class CityManager extends Activity{
    private ImageView back,add;
    private ListView listView;
    private MyBaseAdapter adapter;
    private View root;
    private PopupWindow popupWindow;
    private int flag;
    private Map<String,String>  city_list;
    private ArrayList<String> citys;
    private SQLiteDatabase mSqLiteDatabase;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
//    private


    private void initFindView()
    {

        citys=new ArrayList<>();
        sharedPreferences=getSharedPreferences("citylist", MODE_PRIVATE);
        editor=sharedPreferences.edit();
        city_list= (Map<String, String>) sharedPreferences.getAll();

        for (int i=0;i<city_list.size();i++)
        {
            citys.add(city_list.get(String.valueOf(i)));
        }


        back= (ImageView) findViewById(R.id.drawer);

        add= (ImageView) findViewById(R.id.image_add);

        listView= (ListView) findViewById(R.id.slide_listview);

        adapter=new MyBaseAdapter(getApplicationContext());

        adapter.setData(citys);

        listView.setAdapter(adapter);

//popwindow
        root=getLayoutInflater().inflate(R.layout.dialog_delete,null);
        WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        popupWindow=new PopupWindow(root,width-100,100);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
       popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.alpha = 1f;
                getWindow().setAttributes(lp);
            }
        });


    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_citymanager);

        initFindView();


        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CityManager.this, AddCityActivity.class);
                CityManager.this.startActivity(intent);
                CityManager.this.finish();
                CityManager.this.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(CityManager.this, MainActivity.class);
                CityManager.this.startActivity(intent);
                CityManager.this.finish();
                CityManager.this.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });


       listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
           @Override
           public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
               WindowManager.LayoutParams lp = getWindow().getAttributes();
               lp.alpha = 0.3f;
               getWindow().setAttributes(lp);
               popupWindow.showAsDropDown(view, 30, 30);
               popupWindow.showAtLocation(findViewById(R.id.slide_listview), Gravity.CENTER, 0, 0);
               flag = position;
               Log.i("info", "long click able");
               return true;
           }
       });

        root.findViewById(R.id.delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                citys.remove(flag);
                adapter.setData(citys);
                editor.clear();
                for (int i = 0; i < citys.size(); i++) {
                    editor.putString("" + i, citys.get(i));
                }

                editor.commit();
                adapter.notifyDataSetChanged();
                popupWindow.dismiss();

            }
        });

    }





}