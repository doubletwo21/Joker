package com.personal.joker.mvp.view;

import android.util.Log;
import android.widget.TextView;

import com.personal.joker.R;
import com.personal.joker.base.ui.BaseActivity;
import com.personal.joker.db.BaseCallBack;
import com.personal.joker.db.OkHttpUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 测试Okhttp的Activity
 */
public class TestOkHttpActivity extends BaseActivity {

    @BindView(R.id.tv_okhttp)
    TextView tvOkhttp;

    @Override
    public int getContentLayoutId() {
        return R.layout.activity_test_okhttp_actviity;
    }

    @Override
    public void initData() {
        super.initData();
    }


    @OnClick(R.id.btn_send)
    public void onViewClicked() {
//        showTextPostForm();
//        showTextPostJson();
        OkHttpUtilPostForm();

    }

    private void showTextPostJson() {
        String url = "http://v.juhe.cn/chengyu/query";
        OkHttpClient okHttpClient = new OkHttpClient();

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("key", "fbe1a4fa9821ffe92777bc22774d7980");
            jsonObject.put("word", "瓮中捉鳖");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String jsonstr = jsonObject.toString();

        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset = utf-8"), jsonstr);

        Request request = new Request.Builder()
                .post(requestBody)
                .url(url)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("log == ", "error");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String string = response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tvOkhttp.setText(string);
                    }
                });
            }
        });

    }

    /**
     * OkHttp显示数据
     */
    private void showTextPostForm() {
        String url = "http://v.juhe.cn/chengyu/query";
        OkHttpClient okHttpClient = new OkHttpClient();

        RequestBody build = new FormBody.Builder()
                .add("key", "fbe1a4fa9821ffe92777bc22774d7980")
                .add("word", "瓮中捉鳖")
                .build();

        Request request = new Request.Builder()
                .post(build)
                .url(url)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("log == ", "error");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String string = response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tvOkhttp.setText(string);
                    }
                });
            }
        });
    }

    /**
     * 自己封装的OkHttpUtil进行请求网络
     */
    private void OkHttpUtilPostForm() {
        String url = "http://v.juhe.cn/chengyu/query";
        OkHttpUtil.newBuilder()
                .postForm()
                .url(url)
                .addParams("key", "fbe1a4fa9821ffe92777bc22774d7980")
                .addParams("word", "趁人之危")
                .build().enqueue(new BaseCallBack<String>() {
            @Override
            public void onSuccess(final String string) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tvOkhttp.setText(string);
                    }
                });
            }

            @Override
            public void onError(int code) {
                super.onError(code);
            }

            @Override
            public void onFailure(Call call, IOException e) {
                super.onFailure(call, e);
            }
        });

    }
}
