package com.personal.joker.db;

import android.os.Handler;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Xiao_Wen.
 * User: Admin
 * Date: 2021/1/8
 * Time: 23:26
 *
 * @description: 初始化OkHttp
 * 在这里才是真正的调用OkHttp访问网络
 */
public class OkHttpManager {
    private static OkHttpManager manager;
    private OkHttpClient mClient;
    private Handler mHandler;
    private Gson mGson;

    private OkHttpManager() {
        initOkHttp();
        mHandler = new Handler();
        mGson = new Gson();
    }

    public static synchronized OkHttpManager getManager() {
        if (manager == null) {
            manager = new OkHttpManager();
        }
        return manager;
    }

    public static OkHttpManager getInstance() {
        if (manager == null) {
            synchronized (OkHttpManager.class) {
                if (manager == null) {
                    manager = new OkHttpManager();
                }
            }
        }
        return manager;
    }

    private void initOkHttp() {
        mClient = new OkHttpClient.Builder()
                .readTimeout(10, TimeUnit.SECONDS)
                .connectTimeout(10, TimeUnit.SECONDS)
                .build();
    }

    /**
     * 使用
     * 因为我们的request信息都是在封装的OkHttpUtil中的
     * 所以将OkhttpUtil的Request传入，方便OkHttpClient调用
     * <p>
     * 传入的BaseCallback，是为了方便返回的数据的回调
     *
     * @param okHttpUtil
     * @param callBack
     */
    public void request(OkHttpUtil okHttpUtil, final BaseCallBack callBack) {
        if (callBack == null) {
            throw new NullPointerException("callback is null");
        }
        mClient.newCall(okHttpUtil.buildRequest()).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                sendFailureMessage(callBack, call, e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String result = response.body().string();
                    //判断callback的泛型类型,如果是String类型的话，直接返回
                    if (callBack.mType == null || callBack.mType == String.class) {
                        sendOnSuccessMessage(callBack, result);
                    } else {
                        //如果不是String类型的话，我们进行解析，通过Gson
                        try {
                            Object o = mGson.fromJson(result, callBack.mType);
                            Log.e("0000",o.toString());
                            sendOnSuccessMessage(callBack, o);
                        } catch (JsonSyntaxException e) {
                            Log.e("error", response.code() + "");
                            sendOnErrorMessage(callBack, response.code());
                            e.printStackTrace();
                        }
                    }

                    if (response.body() != null) {
                        response.body().close();
                    }
                } else {
                    sendOnErrorMessage(callBack, response.code());
                }
            }
        });

    }

    private void sendOnSuccessMessage(final BaseCallBack callBack, final Object result) {
        mHandler.post(new Runnable() {
            @SuppressWarnings("unchecked")
            @Override
            public void run() {
                callBack.onSuccess(result);
            }
        });

    }


    private void sendOnErrorMessage(final BaseCallBack callBack, final int code) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                callBack.onError(code);
            }
        });
    }

    /**
     * 因为回调是在子线程中的，我们要在主线程中去处理，这里使用handler
     *
     * @param call 回调
     * @param e    错误信息
     */
    private void sendFailureMessage(final BaseCallBack callBack, final Call call, final IOException e) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                callBack.onFailure(call, e);
            }
        });
    }
}
