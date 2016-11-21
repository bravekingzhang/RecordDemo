package com.example.hoollyzhang.recorddemo;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.app.Activity;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class VideoCatchFrameActivity extends Activity {


    public final static String COM_EXAMPLE_HOOLLYZHANG_RECORDDEMO_EXTRA = "com_example_hoollyzhang_recorddemo_extra";

    TextView info;

    String mVideoPath;

    LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_catch_frame);

        info = (TextView) findViewById(R.id.info);
        linearLayout = (LinearLayout) findViewById(R.id.container);
        mVideoPath = getIntent().getStringExtra(COM_EXAMPLE_HOOLLYZHANG_RECORDDEMO_EXTRA);

        info.setText("视频的长度是： " + Utils.getDuration(mVideoPath) + "ms");
    }

    @Override
    protected void onResume() {
        super.onResume();
        List<Bitmap> bitmaps = Utils.getBitMap(mVideoPath);
        linearLayout.removeAllViews();
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT - 200);
        layoutParams.gravity = Gravity.CENTER_HORIZONTAL;
        for (int i = 0; i < bitmaps.size(); i++) {
            ImageView imageView = new ImageView(this);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            layoutParams.width = bitmaps.get(i).getWidth();
            layoutParams.height = bitmaps.get(i).getHeight();
            imageView.setLayoutParams(layoutParams);
            imageView.setImageBitmap(bitmaps.get(i));
            linearLayout.addView(imageView);
        }
    }
}
