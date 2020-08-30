package com.mxian.smartcity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

public class SelectionActivity extends AppCompatActivity {


    Button water,sanitory,electricity,road,back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selection);

        water=(Button)findViewById(R.id.water);
        sanitory=(Button)findViewById(R.id.sanitory);
        electricity=(Button)findViewById(R.id.electricity);
        road=(Button)findViewById(R.id.road);
        back=(Button)findViewById(R.id.back);


        final SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        final SharedPreferences.Editor editor = pref.edit();



        final ArrayList<String> myList = new ArrayList<String>();


        water.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                myList.add("Contamination");
                myList.add("Leakage");
                myList.add("Lack of water");

                editor.putString("category", "water");
                editor.commit();

                Intent i=new Intent(getApplicationContext(),ReportActivity.class);
                i.putExtra("mylist", myList);
                startActivity(i);
            }
        });
        sanitory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myList.add("Garbage");
                myList.add("Sewage");


                editor.putString("category", "sanitory");
                editor.commit();

                Intent i=new Intent(getApplicationContext(),ReportActivity.class);
                i.putExtra("mylist", myList);
                startActivity(i);
            }
        });
        electricity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                editor.putString("category", "electricity");
                editor.commit();

                myList.add("Power Failure");
                myList.add("Powerline cut");
                myList.add("Transformer issue");

                Intent i=new Intent(getApplicationContext(),ReportActivity.class);
                i.putExtra("mylist", myList);
                startActivity(i);
            }
        });
        road.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                editor.putString("category", "road");
                editor.commit();

                myList.add("Pot holes");
                myList.add("Accident");

                Intent i=new Intent(getApplicationContext(),ReportActivity.class);
                i.putExtra("mylist", myList);
                startActivity(i);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*
                Intent i=new Intent(getApplicationContext(),MainActivity.class);
                startActivity(i);
            */

            }
        });

    }
}
