package com.google.bct.db;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;
import android.view.View;
import android.widget.EditText;

import com.google.bct.R;

public class AddLocationHistoryDialog extends Dialog {
    SharedPreferences sharedPreferences;
    public AddLocationHistoryDialog(final Context context, final String latitude, final String longitude) {
        super(context);

        setTitle(R.string.s_save_location);
        setContentView(R.layout.dialog_add_location);


        final EditText et_Latitude = (EditText) findViewById(R.id.dialog_add_location__EditText_Latitude);
        final EditText et_Longitude = (EditText) findViewById(R.id.dialog_add_location__EditText_Longitude);
        et_Latitude.setText(latitude);
        et_Longitude.setText(longitude);

        final EditText et_Remark = (EditText) findViewById(R.id.dialog_add_location__EditText_Remark);

        findViewById(R.id.but1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String remark = et_Remark.getText().toString();
                final String a = et_Latitude.getText().toString();
                final String b = et_Longitude.getText().toString();
                Location location = new Location(LocationManager.GPS_PROVIDER);
                location.setLatitude(Double.parseDouble(a));
                location.setLongitude(Double.parseDouble(b));
                DAO_History.addLocationHistory(context, location, remark);
                dismiss();
            }
        });
        findViewById(R.id.but2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dismiss();
            }
        });

    }


}
