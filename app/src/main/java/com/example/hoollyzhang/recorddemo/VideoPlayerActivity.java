package com.example.hoollyzhang.recorddemo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;

import java.io.File;

public class VideoPlayerActivity extends AppCompatActivity {

    private VideoView vv_videoview;
    private MediaController mController;
    private Button btn_play;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);

        btn_play = (Button) findViewById(R.id.btn_play);
        vv_videoview = (VideoView) findViewById(R.id.vv_videoview);
        mController = new MediaController(this);

        // Button响应事件，播放本地视频
        btn_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri videoUri = Uri.parse("http://www.androidbegin.com/tutorial/AndroidCommercial.3gp");
                vv_videoview.setVideoURI(videoUri);
               /* File videoFile = new File("/sdcard/DCIM/Camera/test.mp4");
                if (videoFile.exists()) {
                    vv_videoview.setVideoPath(videoFile.getAbsolutePath());
                    vv_videoview.setMediaController(mController);
                    vv_videoview.start();
                    vv_videoview.requestFocus();
                }*/
                vv_videoview.setMediaController(mController);
                vv_videoview.start();
                vv_videoview.requestFocus();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
