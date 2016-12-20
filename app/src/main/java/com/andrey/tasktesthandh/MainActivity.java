package com.andrey.tasktesthandh;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {

    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.listView);
        String[] Sections = {"ПОГОДА", "ВИДЕО"};

        ListAdapter sectionsAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,Sections);
        listView.setAdapter(sectionsAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String sections=String.valueOf(parent.getItemAtPosition(position));

                if (position==0) {
                    Intent intent = new Intent(view.getContext(), WeatherActivity.class);
                    startActivity(intent);
                }

                if (position==1){
                    Intent intent=new Intent(view.getContext(),VideoActivity.class);
                    startActivity(intent);
                }
            }
        });
    }
}
