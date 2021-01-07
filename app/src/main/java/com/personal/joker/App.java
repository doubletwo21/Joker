package com.personal.joker;

import android.app.Application;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.https.HttpsUtils;

import java.net.Proxy;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

/**
 * Created by Xiao_Wen.
 * User: Admin
 * Date: 2021/1/8
 * Time: 1:07
 *
 * @description:
 */
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        initOkhttp();
    }
    private void initOkhttp() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(25000L, TimeUnit.MILLISECONDS)
                .readTimeout(25000L, TimeUnit.MILLISECONDS)
                //其他配置
                .build();

        OkHttpUtils.initClient(okHttpClient);
    }
}
