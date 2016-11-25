package com.example.hoollyzhang.recorddemo;

import android.os.AsyncTask;

/**
 * Created by hoollyzhang on 16/11/22.
 */

class CacheUtils {
    public static void downLoadVideo(String videoPath, String cachePath , final DownloadListener downloadListener) {
        new AsyncTask<String,Integer,String>(){

            @Override
            protected String doInBackground(String... strings) {
                return null;
            }

            @Override
            protected void onProgressUpdate(Integer... values) {
                super.onProgressUpdate(values);
            }

            @Override
            protected void onPostExecute(String path) {
                super.onPostExecute(path);
                if (downloadListener!=null){
                    downloadListener.onsuccess(path);
                }
            }
        }.execute(videoPath);
    }

    public interface DownloadListener{
        void onsuccess(String localPath);
    }
}
