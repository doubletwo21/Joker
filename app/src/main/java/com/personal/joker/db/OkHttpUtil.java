package com.personal.joker.db;

import android.net.Uri;
import android.util.Log;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by Xiao_Wen.
 * User: Admin
 * Date: 2021/1/8
 * Time: 22:38
 *
 * @description: 自定义的OkHttpUtil
 * <p>
 * 主类的构造方法是私有的，
 * 通过new Builder创建一个Builder，
 * 然后通过Builder的build方法返回一个OkHttpUtil
 * <p>
 * <p>
 * 这里我们不想让每次访问网络请求都实例化一次Okhttp，
 * 只想让他实例化一次，所以就用到了单例模式
 * 所以单例模式的话，在哪里单例？
 * 在OkHttpUtil中单例？
 * 还是在Builder中单例？
 * 还是新建类单例？
 * <p>
 * 因为初始化OkHttp的时候 需要一些参数，
 * OkHttpUtil这个类只是使用这些参数
 * 初始化放在一个管理类里会比较优雅
 */
public class OkHttpUtil {
    private Builder mBuilder;

    private OkHttpUtil(Builder builder) {
        this.mBuilder = builder;
    }

    /**
     * 因为 构造函数是私有的，
     * 所以通过newBuilder()方法返回自身（OkhttpUtil）
     *
     * @return Builder，这里返回Builder之后，
     * 尧通过他的build()方法
     * 返回一个OkhttpUtil
     */
    public static Builder newBuilder() {
        return new Builder();
    }

    /**
     * 构建Request 给OkHttpManger
     *
     * @return
     */
    public Request buildRequest() {
        Request.Builder builder = new Request.Builder();
        if (mBuilder.method == "GET") {
            builder.get();
            builder.url(buildGetRequestParams());
        } else if (mBuilder.method == "POST") {
            builder.post(buildRequestBody());
            builder.url(mBuilder.url);
        }

        return builder.build();
    }

    /**
     * 这里处理的是如果get请求方法如果有参数传进来的话，
     * 需要拼接到原来的url后
     *
     * @return
     */
    private String buildGetRequestParams() {
        if (mBuilder.mParms.size() <= 0) {
            return mBuilder.url;
        } else {
            Uri.Builder builder = Uri.parse(mBuilder.url).buildUpon();
            for (RequestParm param : mBuilder.mParms) {
                builder.appendQueryParameter(param.getKey(), param.getValue() == null ? "" : param.getValue().toString());
            }
            String url = builder.build().toString();
            Log.e("OkHttpUtil url =", "OkHttpUrl url is ---" + url);
            return url;
        }
    }

    private RequestBody buildRequestBody() {
        //Json参数还是Form表单参数
        // 如果是json参数
        if (mBuilder.isJsonParam) {
            JSONObject jsonObject = new JSONObject();
            for (RequestParm param : mBuilder.mParms) {
                try {
                    jsonObject.put(param.getKey(), param.getValue());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            String json = jsonObject.toString();
            return RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        } else {
            //如果是Form表单post请求的话，直接将参数添加到FormBody中就可以了
            FormBody.Builder builder = new FormBody.Builder();
            for (RequestParm param : mBuilder.mParms) {
                builder.add(param.getKey(), param.getValue() == null ? "" : param.getValue().toString());
            }
            return builder.build();

        }

    }

    /**
     * 发起网络请求
     * 这里需要用到OkhttpClient
     * 所以需要OkhttpManager
     *
     * @param callBack 访问网络的回调接口
     */
    public void enqueue(BaseCallBack callBack) {
        OkHttpManager.getInstance().request(this, callBack);
    }

    public static class Builder {

        private String url;
        private String method;
        private List<RequestParm> mParms;
        private boolean isJsonParam;//是否是提交json字符串，默认是false

        /**
         * 构造函数，默认请求方式为get
         */
        public Builder() {
            method = "GET";
        }

        /**
         * build方法返回一个工具类
         *
         * @return
         */
        public OkHttpUtil build() {
            return new OkHttpUtil(this);
        }

        /**
         * get请求
         *
         * @return Builder
         */
        public Builder get() {
            method = "GET";
            return this;
        }

        /**
         * url路径
         *
         * @return Builder
         */
        public Builder url(String url) {
            this.url = url;
            return this;
        }

        /**
         * post form表单
         *
         * @return Builder
         */
        public Builder postForm() {
            method = "POST";
            return this;
        }

        /**
         * post Json
         *
         * @return Builder
         */
        public Builder postJson() {
            isJsonParam = true;
            return postForm();
        }

        /**
         * 添加参数
         *
         * @param key   参数名
         * @param value 参数值
         * @return Builder
         */
        public Builder addParams(String key, String value) {
            if (mParms == null) {
                mParms = new ArrayList<>();
            }
            mParms.add(new RequestParm(key, value));
            return this;
        }
    }
}
