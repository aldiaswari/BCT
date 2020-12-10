package com.google.bct.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.bct.R;
import com.google.bct.common.Constants;
import com.google.bct.model.Member;
import com.google.bct.ui.main;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.valdesekamdem.library.mdtoast.MDToast;

import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Prof extends Fragment {

    private FirebaseAuth mAuth;
    private SharedPreferences prefs;
    private FirebaseDatabase db;
    FirebaseUser firebaseUser;
    private DatabaseReference databaseReference;
    private EditText edtname;
    private Button btnprof;
    private ProgressBar progbar;
    private EditText edtphone;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.activity_create_profile_new, container, false);
        mAuth = FirebaseAuth.getInstance();
        prefs = getActivity().getSharedPreferences(Constants.h("YmN0"), 0);
        if (mAuth.getCurrentUser() == null) {
            getActivity().finish();
            startActivity(new Intent(getActivity(), main.class));
        }
        db = FirebaseDatabase.getInstance();
        databaseReference = db.getReference(Constants.h("bWVtYmVy"));
        db.getReference(Constants.h("YXBw")).setValue((Object) Constants.h("TG9naW4gQmFjb3Q="));
        db.getReference(Constants.h("YXBw")).addListenerForSingleValueEvent(new app());
        edtname = (EditText) inflate.findViewById(R.id.create_profile_name_editText);
        btnprof = (Button) inflate.findViewById(R.id.create_profile_button);
        edtphone = inflate.findViewById(R.id.create_profile_phone_editText);
        progbar = (ProgressBar) inflate.findViewById(R.id.create_profile_progressBar);
        btnprof.setOnClickListener(new goprofi());
        return inflate;

    }

    private class app implements ValueEventListener {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            Log.e(Constants.h("YmN0"), Constants.h("QXBwIHRpdGxlIHVwZGF0ZWQ="));

        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            Log.e(Constants.h("YmN0"),  Constants.h("RmFpbGVkIHRvIHJlYWQgYXBwIHRpdGxlIHZhbHVlLg=="), databaseError.toException());

        }
    }

    private class goprofi implements View.OnClickListener {
        @Override
        public void onClick(View v) {
             progbar.setVisibility(View.VISIBLE);
            aa(this);
            progbar.setVisibility(View.GONE);

            startActivity(new Intent(getActivity(), main.class));
            getActivity().finish();

        }
    }

    private void aa(goprofi goprofi) {
        String ina = "+62";
        String name = edtname.getText().toString().trim();
        String phone = edtphone.getText().toString().trim();
        String phoneIdn= ina + phone;
        if (name.isEmpty() || phone.isEmpty()) {
            MDToast.makeText(getActivity(), Constants.h("VGlkYWsgQm9sZWggS29zb25n"), Toast.LENGTH_SHORT, MDToast.TYPE_INFO).show();
            return;
        }
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        Calendar instance = Calendar.getInstance();
        PrintStream printStream = System.out;
        printStream.println("Before " + instance.getTime());
        String string = Settings.Secure.getString(getActivity().getContentResolver(), Constants.h("YW5kcm9pZF9pZA=="));
        Date time = instance.getTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String format = simpleDateFormat.format(time);
        instance.add(2, 1);
        String format2 = simpleDateFormat.format(time);
        PrintStream printStream2 = System.out;
        printStream2.println(Constants.h("QWZ0ZXI= ") + instance.getTime());
        if (firebaseUser != null) {
            Member member = new Member(0,name,format,firebaseUser.getEmail(),format2,phoneIdn,string);
      //      apv.g.a(firebaseUser.a()).a((Object) member);
            databaseReference.child(firebaseUser.getUid()).setValue(member);
       //     bjh.a(apv.getActivity(), apn.a("UHJvZmlsZSBjcmVhdGUgc3VjY2VzZnVsbHkuLi4="), bjh.b, 1).show();
            MDToast.makeText(getActivity(), Constants.h("UHJvZmlsZSBjcmVhdGUgc3VjY2VzZnVsbHkuLi4="), Toast.LENGTH_SHORT, MDToast.TYPE_SUCCESS).show();
        }

    }
}
