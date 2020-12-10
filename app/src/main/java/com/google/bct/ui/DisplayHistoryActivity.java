package com.google.bct.ui;


import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.google.bct.R;
import com.google.bct.db.DAO_History;
import com.google.bct.db.HistoryBean;
import com.google.bct.init.AppBase;

import java.util.ArrayList;

public class DisplayHistoryActivity extends ListActivity {

    AppBase app;
    SharedPreferences pref;
    private String lat;
    private String lng;
    public void la()
    {
        AlertDialog.Builder localBuilder = new AlertDialog.Builder(this);
        localBuilder.setTitle("History");
        localBuilder.setMessage("History");
        localBuilder.setPositiveButton("Menuju Lokasi", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt)
            {
                Intent intent = new Intent(DisplayHistoryActivity.this, MainActivity.class);
                startActivity(intent);
                finish();

            }
        });
        localBuilder.setNegativeButton("Hapus Lokasi", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt)
            {
                DAO_History.deletefav(DisplayHistoryActivity.this.getApplicationContext(),DisplayHistoryActivity.this.lat, DisplayHistoryActivity.this.lng);
                Intent intent = new Intent(DisplayHistoryActivity.this,DisplayHistoryActivity.class);
                startActivity(intent);
                finish();
            }
        });
        localBuilder.create().show();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        app = AppBase.obtainApp(this);
        app.addActivity(this);
        pref = getSharedPreferences("bct", Context.MODE_PRIVATE);
        setListAdapter(new HistoryAdapter(this,
                DAO_History.getLocationHistories(this)));

        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HistoryBean history = (HistoryBean) parent.getAdapter().getItem(position);
                Double a = Double.valueOf(history.latitude);
                Double b = Double.valueOf(history.longitude);
                lat = history.latitude;
                lng = history.longitude;
                SharedPreferences.Editor e = pref.edit();
                e.putString("lat", Double.toString(a));
                e.putString("lng", Double.toString(b));
                e.apply();
                la();



            }
        });


    }
    @Override
    public void onBackPressed() {
    super.onBackPressed();
        Intent intent = new Intent(DisplayHistoryActivity.this,MainActivity.class);
        startActivity(intent);
        finish();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        app.removeActivity(this);
    }



    private static final class HistoryAdapter extends BaseAdapter {

        private Context context;
        private ArrayList<HistoryBean> historyBeans;

        private HistoryAdapter(Context context, ArrayList<HistoryBean> historyBeans) {
            this.context = context;
            this.historyBeans = historyBeans;
        }

        @Override
        public int getCount() {
            return historyBeans.size();
        }

        @Override
        public Object getItem(int position) {
            return historyBeans.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.item_history, parent, false);
                holder = new ViewHolder();
                holder.tv_Latitude = (TextView) convertView.findViewById(R.id.item_history__TextView_Latitude);
                holder.tv_Longitude = (TextView) convertView.findViewById(R.id.item_history__TextView_Longitude);
                holder.tv_Remark = (TextView) convertView.findViewById(R.id.item_history__TextView_Remark);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            HistoryBean history = historyBeans.get(position);
            holder.tv_Remark.setText(history.remark);
            holder.tv_Longitude.setText(history.longitude);
            holder.tv_Latitude.setText(history.latitude);
            return convertView;
        }
    }

    private static final class ViewHolder {
        public TextView tv_Longitude, tv_Latitude, tv_Remark;
    }


}
