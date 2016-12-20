package com.andrey.tasktesthandh;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.andrey.tasktesthandh.video.Task;
import com.andrey.tasktesthandh.video.TaskHelper;
import com.andrey.tasktesthandh.video.VideoPlay;

import java.util.ArrayList;
import java.util.List;

public class VideoActivity extends Activity {

    private ListView lView;

    private static final String url1="https://www.dropbox.com/s/by70xabe667bmf2/VIDEO0005.zip?dl=1";
    private static final String url2="https://www.dropbox.com/s/yk0pcwy46fyacjv/VIDEO0006.zip?dl=1";
    private static final String url3="https://www.dropbox.com/s/fjfzo4j5r1offz0/VIDEO0007.zip?dl=1";
    private final String videoName1="VIDEO0005.mp4";
    private final String videoName2="VIDEO0006.mp4";
    private final String videoName3="VIDEO0007.mp4";

    public static final String APP_PREFERECES="mysettings";
    private SharedPreferences mSettings;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        mSettings=getSharedPreferences(APP_PREFERECES, Context.MODE_PRIVATE);
        lView=(ListView) findViewById(R.id.lView);

        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,initData());
        lView.setAdapter(adapter);

        lView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String items=String.valueOf(parent.getItemAtPosition(position));
                if (position==0){
                    if(!loadUrlFileName(url1).equals(videoName1)) {
                        Task t = new Task();
                        TaskHelper.getInstance().addTask("task", t, VideoActivity.this);
                        t.execute(url1);
                        saveUrlFileName(url1, videoName1);
                    }
                        else {
                        String filename=videoName1;
                        Intent intent = new Intent(view.getContext(), VideoPlay.class);
                        intent.putExtra("videofilename",filename);
                        startActivity(intent);
                    }
                }
                if (position==1) {
                    if(!loadUrlFileName(url2).equals(videoName2)) {
                        Task t = new Task();
                        TaskHelper.getInstance().addTask("task", t, VideoActivity.this);
                        t.execute(url2);
                        saveUrlFileName(url2,videoName2);
                    }
                    else {
                        String filename = videoName2;
                        Intent intent = new Intent(view.getContext(), VideoPlay.class);
                        intent.putExtra("videofilename", filename);
                        startActivity(intent);
                    }
                }
                if (position==2) {
                    if(!loadUrlFileName(url3).equals(videoName3)) {
                        Task t = new Task();
                        TaskHelper.getInstance().addTask("task", t, VideoActivity.this);
                        t.execute(url3);
                        saveUrlFileName(url3,videoName3);
                    }
                        else {
                        String filename = videoName3;
                        Intent intent = new Intent(view.getContext(), VideoPlay.class);
                        intent.putExtra("videofilename", filename);
                        startActivity(intent);
                    }
                }
            }
        });
        TaskHelper.getInstance().attach("task", this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        TaskHelper.getInstance().detach("task");
    }

    private void saveUrlFileName(String url, String fileName){
        SharedPreferences.Editor editor=mSettings.edit();
        editor.putString(url,fileName);
        editor.apply();
    }

    private String loadUrlFileName(String url){
        if (mSettings.contains(url)){
            return(mSettings.getString(url,""));
        }
        return "";

    }

    private List<String> initData(){
        java.util.List<java.lang.String> list=new ArrayList<String>();
        list.add(videoName1);
        list.add(videoName2);
        list.add(videoName3);

        return list;
    }
}
