package com.example.u.myweather;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.City;
import com.example.CityDao;
import com.example.DaoMaster;
import com.example.DaoSession;

import java.util.ArrayList;
import java.util.List;

import DataResource.DBManager;
import ui.MyListView;
import ui.SearchBaseAdapter;

/**
 * Created by u on 2016/12/14.
 */
public class AddCityActivity extends Activity {

    private ImageView back;
    private EditText et_search;
    private MyListView listView;
    private SearchBaseAdapter adapter;
    private SQLiteDatabase db;
    private List<City> search_data;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;


    private DaoSession session;
    private DaoMaster master;
    private CityDao cityDao;
    private void initView() {

        openDb();
        back= (ImageView) findViewById(R.id.back);
        et_search = (EditText) findViewById(R.id.et_search);
        listView = (MyListView) findViewById(R.id.listView);
        adapter=new SearchBaseAdapter(this);
        search_data=new ArrayList<City>();
    }


    private void openDb(){
        db=new DaoMaster.DevOpenHelper(AddCityActivity.this, DBManager.DB_PATH + "/"+ DBManager.DB_NAME,null).getWritableDatabase();

        master=new DaoMaster(db);

        session=master.newSession();

        cityDao=session.getCityDao();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_add);

        initView();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddCityActivity.this, MainActivity.class);
                AddCityActivity.this.startActivity(intent);
                AddCityActivity.this.finish();
                AddCityActivity.this.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });
        // 搜索框的文本变化实时监听
        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().trim().length() == 0) {
                    search_data.clear();
                    adapter.notifyDataSetChanged();

                } else {

                    String tempName = et_search.getText().toString();
                    // 根据tempName去模糊查询数据库中有没有数据
                    queryData(tempName);
                }


            }
        });




        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView textView = (TextView) view.findViewById(R.id.cityZh);
                String name = textView.getText().toString();
//                et_search.setText(name);

//                // TODO 获取到item上面的文字，根据该关键字跳转到另一个页面查询，由你自己去实现

                sharedPreferences=getSharedPreferences("citylist",MODE_PRIVATE);
                editor=sharedPreferences.edit();
                editor.putString(String.valueOf(sharedPreferences.getAll().size()),name);
                editor.commit();
                Toast.makeText(getApplicationContext(), "添加成功", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(AddCityActivity.this, MainActivity.class);
                AddCityActivity.this.startActivity(intent);
                AddCityActivity.this.finish();
                AddCityActivity.this.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });


    }







    /**
     * 模糊查询数据
     */
    private void queryData(String key) {

        // 创建adapter适配器对象

        search_data=cityDao.queryBuilder().where(CityDao.Properties.CityZh.like(key+"%")).list();

        Log.i("info",search_data.toString());

        adapter.setSearchData(search_data);

        listView.setAdapter(adapter);

        adapter.notifyDataSetChanged();


    }
}
