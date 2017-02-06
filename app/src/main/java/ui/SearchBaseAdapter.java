package ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.City;
import com.example.u.myweather.R;

import java.util.List;

/**
 * Created by u on 2016/12/16.
 */
public class SearchBaseAdapter extends MyBaseAdapter {
    private List<City> data;
    private TextView cityZh,provinceZh,leaderZh;
    private LayoutInflater inflater;

    public SearchBaseAdapter(Context context) {

        this.inflater = LayoutInflater.from(context);
    }

    public void setSearchData(List<City> data)
    {
        this.data=data;
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return data.size();
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
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView==null)
        {
            convertView=inflater.inflate(R.layout.simple_list_item_search,null);
            cityZh= (TextView) convertView.findViewById(R.id.cityZh);
            provinceZh= (TextView) convertView.findViewById(R.id.provinceZh);
            leaderZh= (TextView) convertView.findViewById(R.id.countryZh);
        }



        cityZh.setText(data.get(position).getCityZh());
        provinceZh.setText(data.get(position).getProvinceZh());
        leaderZh.setText(data.get(position).getLeaderZh());

        return convertView;
    }
}
