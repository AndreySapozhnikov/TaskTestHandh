package com.andrey.tasktesthandh.video;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.widget.MediaController;
import android.widget.VideoView;

import com.andrey.tasktesthandh.R;


public class VideoPlay extends AppCompatActivity {

    private VideoView videoView;
    String filename;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.play_video);

        videoView=(VideoView)findViewById(R.id.videoView);

        Intent i=getIntent();
        Bundle extras=i.getExtras();
        filename=extras.getString("videofilename");
        String videoSource= Environment.getExternalStorageDirectory() + "/unzipFolder/"+filename;
        videoView.setVideoPath(videoSource);
        videoView.setMediaController(new MediaController(this));
        videoView.requestFocus();
        videoView.start();
    }
}
