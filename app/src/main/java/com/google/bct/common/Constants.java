package com.google.bct.common;

import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Base64;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

import java.util.Locale;

public class Constants {
    public interface ACTION {
        public static String MAIN_ACTION = "com.google.bct.ui.MainActivity";
        public static String MAIN_ACTION2 = "com.google.bct";
        public static String STARTFOREGROUND_ACTION = "com.marothiatechs.foregroundservice.action.startforeground";
        public static String STOPFOREGROUND_ACTION = "com.marothiatechs.foregroundservice.action.stopforeground";
    }

    public static final String KEY_HIDE_FROM_SYSTEM = "hide_from_system";
    public static final String KEY_SHOW_SYSTEM_APP = "show_system_app";
    public static final String KEY_PACKAGE_NAME = "PackageName";
    public static final int OVERRIDE_MODE_WORLD_READABLE = 0x0001;
    public static final String KEY_SHOW_PACKAGE_NAME = "show_package_name";

    public interface NOTIFICATION_ID {
        public static int FOREGROUND_SERVICE = 101;
    }

    public static String h(String str) {
        try {
            return new String(Base64.decode(str, 0), "UTF-8");
        } catch (Throwable th) {
            return "";
        }
    }

    public static int getBearing(int i, int i2) {
        return (int) Math.round((Math.toDegrees(Math.atan2((double) i, (double) (i2 * -1))) + 360.0d) % 360.0d);
    }

    public static LatLng getDestinationPoint(double d, double d2, double d3, double d4) {
        double d5 = d4 / 6371.0d;
        double d6 = (3.141592653589793d * d3) / 180.0d;
        double d7 = (3.141592653589793d * d) / 180.0d;
        double asin = Math.asin((Math.sin(d7) * Math.cos(d5)) + (Math.cos(d7) * Math.sin(d5) * Math.cos(d6)));
        LatLng latLng = new LatLng((180.0d * asin) / 3.141592653589793d, (180.0d * (((3.141592653589793d * d2) / 180.0d) + Math.atan2((Math.sin(d6) * Math.sin(d5)) * Math.cos(d7), Math.cos(d5) - (Math.sin(d7) * Math.sin(asin))))) / 3.141592653589793d);
        return latLng;
    }

    public static boolean isKoara() {
        return Locale.getDefault().equals(Locale.KOREA);
    }

    public static void setTextViewColorPartial(TextView textView, String str, int i) {
        String charSequence = textView.getText().toString();
        int indexOf = charSequence.indexOf(str);
        if (indexOf != -1) {
            SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(charSequence);
            spannableStringBuilder.setSpan(new ForegroundColorSpan(i), indexOf, str.length() + indexOf, 33);
            textView.setText(spannableStringBuilder);
        }
    }

}