package com.example.hoollyzhang.recorddemo;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import com.google.android.exoplayer2.ExoPlayer;

import org.wysaid.myUtils.FileUtil;

import im.ene.toro.Toro;
import im.ene.toro.ToroAdapter;
import im.ene.toro.exoplayer2.ExoVideoView;
import im.ene.toro.exoplayer2.ExoVideoViewHolder;
import im.ene.toro.exoplayer2.PlayerCallback;

public class RecyclerViewVideoActivity extends Activity {

    private static final String TAG = "RecyclerViewVideo";


    RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toro.attach(this);
        setContentView(R.layout.activity_recycler_view_video);
        mRecyclerView = (RecyclerView) findViewById(R.id.activity_recycler_view_video);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(new MyToroAdapter());
    }

    @Override
    protected void onResume() {
        super.onResume();
        Toro.register(mRecyclerView);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Toro.unregister(mRecyclerView);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        Toro.detach(this);
    }

    class MyToroAdapter extends ToroAdapter<ToroAdapter.ViewHolder> {

        private LayoutInflater inflater;

        int TYPE_VIDEO = 1;

        int TYPE_NORMAL = 2;

        @Override
        public int getItemViewType(int position) {
            return position % 3 == 0 ? TYPE_VIDEO : TYPE_NORMAL;
        }

        @Nullable
        @Override
        protected Object getItem(int position) {
            if (position % 3 == 0) {
                return new SimpleVideoObject("http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4");
            } else {
                return new SimpleObject("这里是大段文本信息/n，这里是大段文本信息/n这里是大段文本信息/n");
            }
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            final View view;
            final ToroAdapter.ViewHolder viewHolder;
            if (inflater == null) {
                inflater = LayoutInflater.from(parent.getContext());
            }

            if (viewType == TYPE_VIDEO) {
                view = inflater.inflate(Basic1VideoViewHolder.LAYOUT_RES, parent, false);
                viewHolder = new Basic1VideoViewHolder(view);
            } else {
                view = inflater.inflate(Basic1NormalViewHolder.LAYOUT_RES, parent, false);
                viewHolder = new Basic1NormalViewHolder(view);
            }

            return viewHolder;
        }

        @Override
        public int getItemCount() {
            return 20;
        }
    }

    class Basic1VideoViewHolder extends ExoVideoViewHolder {

        public static final int LAYOUT_RES = R.layout.vh_toro_video_basic;

        private SimpleVideoObject videoItem;

        private SeekBar seekBar;

        private Handler handler = new Handler(Looper.getMainLooper());
        public Basic1VideoViewHolder(View itemView) {
            super(itemView);
            seekBar = (SeekBar) itemView.findViewById(R.id.seek_bar);
        }


        private void showProgress(){
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    seekBar.setMax((int) videoView.getDuration());
                    seekBar.setProgress((int) videoView.getCurrentPosition());
                    Log.e(TAG, "run() called");
                    showProgress();
                }
            }, 1000);
        }
        @Override
        public void bind(RecyclerView.Adapter adapter, Object item) {
            if (!(item instanceof SimpleVideoObject)) {
                throw new IllegalArgumentException("Invalid Object: " + item);
            }
            this.videoItem = (SimpleVideoObject) item;
            if (this.videoItem.getVideoPath().startsWith("http") || this.videoItem.getVideoPath().startsWith("https")){
                CacheUtils.downLoadVideo(this.videoItem.getVideoPath(), FileUtil.getCachePath(getApplicationContext()), new CacheUtils.DownloadListener() {
                    @Override
                    public void onsuccess(String localPath) {
                        long currentPosition = videoView.getCurrentPosition();
                        videoView.setMedia(Uri.parse(localPath));
                        videoView.seekTo(currentPosition);
                    }
                });
            }
            this.videoView.setMedia(Uri.parse(this.videoItem.getVideoPath()));
            showProgress();
            this.seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });
            //videoView.getCurrentPosition();
        }

        @Override
        public int getBufferPercentage() {
            Log.e(TAG, "getBufferPercentage() called");
            return super.getBufferPercentage();
        }

        @Override
        public void onVideoPreparing() {
            super.onVideoPreparing();
            Log.d(TAG, "onVideoPreparing() called");
        }

        @Override
        protected ExoVideoView findVideoView(View itemView) {
            return (ExoVideoView) itemView.findViewById(R.id.video);
        }

        @Nullable
        @Override
        public String getMediaId() {
            return this.videoItem != null ? this.videoItem.getVideoPath() + "@" + getAdapterPosition() : null;
        }
    }

    class Basic1NormalViewHolder extends ToroAdapter.ViewHolder {

        public static final int LAYOUT_RES = R.layout.vh_normal_view;

        public Basic1NormalViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void bind(RecyclerView.Adapter adapter, Object item) {

        }

        @Override
        public void onAttachedToWindow() {

        }

        @Override
        public void onDetachedFromWindow() {

        }
    }
}
