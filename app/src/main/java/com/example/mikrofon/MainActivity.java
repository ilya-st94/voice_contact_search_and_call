package com.example.mikrofon;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.media.Session2Command;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private TextView textt;
ArrayList<String> t = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        f();
    }

    private void f(){
        textt =findViewById(R.id.te);
    }

    public void knopka(View view) {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        startActivityForResult(intent, 1);


    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            switch (requestCode) {
                case 1:
                    t =  data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    String s ="";
                    s+=t.get(0);
                    getcontackt(s.toUpperCase());


                    break;

            }
        }
    }


    private void getcontackt(String s){
        String numberphone = "";
        Uri CONTENT_URI = ContactsContract.Contacts.CONTENT_URI;
        String id = ContactsContract.Contacts._ID;
        String display = ContactsContract.Contacts.DISPLAY_NAME;
        String phone_nomer = ContactsContract.Contacts.HAS_PHONE_NUMBER;

        Uri PhoneCONTENT_URI = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String Phone_CONTACT_ID = ContactsContract.CommonDataKinds.Phone.CONTACT_ID;
        String NUMBER = ContactsContract.CommonDataKinds.Phone.NUMBER;

        StringBuffer aray = new StringBuffer();
        ContentResolver contentResolver = getContentResolver();
        Cursor cursor = contentResolver.query(CONTENT_URI, null, null, null, null);

        if (cursor.getCount()>0){
            while (cursor.moveToNext()){
                String contakt_id = cursor.getString(cursor.getColumnIndex(id));
                String name = cursor.getString(cursor.getColumnIndex(display));
                int hasnumber = Integer.parseInt(cursor.getString(cursor.getColumnIndex(phone_nomer)));

                if(s.equals("СПИСОК")){

                    textt.setText(aray.append(name.toUpperCase()));
                }

                if (hasnumber>0){
                    if(name.toUpperCase().contains(s)){
                            Cursor phonecurcor = contentResolver.query(PhoneCONTENT_URI, null, Phone_CONTACT_ID + " = ?", new String[]{contakt_id}, null );
                            while (phonecurcor.moveToNext()){
                                numberphone = phonecurcor.getString(phonecurcor.getColumnIndex(NUMBER));
                                String col = "tel:" +numberphone;
                                Intent intent = new Intent(Intent.ACTION_CALL);
                                intent.setData(Uri.parse(col));
                                startActivity(intent);

                        }
                    }
                    aray.append("\n");

                }

            }
        }


    }

}