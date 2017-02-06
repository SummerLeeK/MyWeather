package ui;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.example.u.myweather.R;
import java.util.List;


/**
 * Created by u on 2016/12/13.
 */
public class MyBaseAdapter extends BaseAdapter {
    private List<String>data;
    private TextView cityname;
    private LayoutInflater inflater;

    public MyBaseAdapter(Context context)
    {
        this.inflater = LayoutInflater.from(context);
    }

    public MyBaseAdapter() {
    }

    public void setData(List<String> data)
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

        convertView=inflater.inflate(R.layout.listview_citymanager_item,null);
        cityname= (TextView) convertView.findViewById(R.id.cityname);
        cityname.setText(data.get(position).toString());

        return convertView;
    }
}
