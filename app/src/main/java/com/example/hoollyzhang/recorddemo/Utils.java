package com.example.hoollyzhang.recorddemo;

import android.graphics.Bitmap;
import android.os.Build;
import android.provider.SyncStateContract;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import wseemann.media.FFmpegMediaMetadataRetriever;

/**
 * Created by hoollyzhang on 16/11/21.
 */

public class Utils {

    public static long getDuration(String videoUrl) {
        FFmpegMediaMetadataRetriever mmr = new FFmpegMediaMetadataRetriever();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            mmr.setDataSource(videoUrl, new HashMap<String, String>());
        } else {
            mmr.setDataSource(videoUrl);
        }
        return Long.parseLong(mmr.extractMetadata(FFmpegMediaMetadataRetriever.METADATA_KEY_DURATION));
    }

    public static List<Bitmap> getBitMap(String videoUrl) {

        List<Bitmap> bitmaps =  new ArrayList<>();
        FFmpegMediaMetadataRetriever mmr = new FFmpegMediaMetadataRetriever();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            mmr.setDataSource(videoUrl, new HashMap<String, String>());
        } else {
            mmr.setDataSource(videoUrl);
        }
        long duration = Long.parseLong(mmr.extractMetadata(FFmpegMediaMetadataRetriever.METADATA_KEY_DURATION));
        mmr.extractMetadata(FFmpegMediaMetadataRetriever.METADATA_KEY_ALBUM);
        mmr.extractMetadata(FFmpegMediaMetadataRetriever.METADATA_KEY_ARTIST);
        int maxTimes = (int) (duration / 1000);
        for (int i = 0; i < maxTimes; i++) {
            Bitmap b = mmr.getFrameAtTime(1000000 * i, FFmpegMediaMetadataRetriever.OPTION_CLOSEST);//1000000这里是是1s的意思。
            //byte[] artwork = mmr.getEmbeddedPicture();
            bitmaps.add(b);
        }
        mmr.release();
        return bitmaps;

    }

}
