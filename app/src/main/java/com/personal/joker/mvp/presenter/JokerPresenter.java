package com.personal.joker.mvp.presenter;

import android.util.Log;

import com.google.gson.Gson;
import com.personal.joker.base.presenter.BasePresenter;
import com.personal.joker.common.contract.IJokerContract;
import com.personal.joker.db.BaseCallBack;
import com.personal.joker.db.OkHttpUtil;
import com.personal.joker.db.model.JokerModel;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;

/**
 * Created by Xiao_Wen.
 * User: Admin
 * Date: 2021/1/7
 * Time: 23:25
 *
 * @description:
 */
public class JokerPresenter extends BasePresenter<IJokerContract.View<List<JokerModel.ResultBean.DataBean>>> implements IJokerContract.Presenter {

    public JokerPresenter(IJokerContract.View view) {
        super(view);
    }

    @Override
    public void getData(String time, String page, String pageSize, String sort) {

        OkHttpUtil.newBuilder().get().url("http://v.juhe.cn/joke/content/list.php")
                .addParams("sort", sort)
                .addParams("page", page)
                .addParams("pagesize", pageSize)
                .addParams("time", time)
                .addParams("key", "5c56e4e89ee9e8cfa686bffd489b1402")
                .build().enqueue(new BaseCallBack<JokerModel>() {
            @Override
            public void onSuccess(JokerModel jokerModel) {
                if (jokerModel.getError_code() == 0) {
                    List<JokerModel.ResultBean.DataBean> data = jokerModel.getResult().getData();
                    mView.onDataSuccess(data);
                } else
                    mView.onDataFailed();
            }

            @Override
            public void onError(int code) {
                super.onError(code);
                Log.e("error", code + "");
                mView.onDataFailed();
            }

            @Override
            public void onFailure(Call call, IOException e) {
                super.onFailure(call, e);
                mView.onDataFailed();
            }
        });
    }
}
