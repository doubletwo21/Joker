package com.personal.joker.mvp.presenter;

import android.util.Log;

import com.google.gson.Gson;
import com.personal.joker.base.presenter.BasePresenter;
import com.personal.joker.common.contract.IJokerContract;
import com.personal.joker.db.model.JokerModel;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

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
        OkHttpUtils.get().url("http://v.juhe.cn/joke/content/list.php")
                .addParams("sort", sort)
                .addParams("page", page)
                .addParams("pagesize", pageSize)
                .addParams("time", time)
                .addParams("key", "5c56e4e89ee9e8cfa686bffd489b1402")
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {

                mView.onDataFailed();
            }

            @Override
            public void onResponse(String response, int id) {
                JokerModel jokerModel = new Gson().fromJson(response, JokerModel.class);
                int error_code = jokerModel.getError_code();
                if (error_code == 0) {
                    JokerModel.ResultBean result = jokerModel.getResult();
                    List<JokerModel.ResultBean.DataBean> data = result.getData();
                    if (data != null && data.size() != 0)
                        mView.onDataSuccess(data);
                    else
                        mView.onDataNoMore("没有更多数据了");
                }
            }
        });
    }
}
