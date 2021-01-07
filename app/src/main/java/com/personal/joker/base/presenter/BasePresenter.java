package com.personal.joker.base.presenter;

import com.personal.joker.common.contract.BaseContract;

/**
 * Created by Xiao_Wen.
 * User: Admin
 * Date: 2021/1/7
 * Time: 22:27
 *
 * @description: 抽取Presenter的基类
 */
public abstract class BasePresenter<V extends BaseContract.View> implements BaseContract.Presenter {
    public V mView;

    public BasePresenter(V view) {
        setView(view);
    }

    /**
     * 给子类提供一个获取到View的方法
     * final ：该方法不允许被重写
     *
     * @return view
     */
    public final V getView() {
        return mView;
    }

    @SuppressWarnings("unchecked")
    private void setView(V view) {
        this.mView = view;
        this.mView.setPresenter(this);
    }


    @Override
    public void start() {
        V view = mView;
        if (view != null) {
            view.showLoading();
        }
    }

    /**
     * 需要销毁的时候，解绑 V 和 P
     * 避免造成内存泄漏
     */
    @SuppressWarnings("unchecked")
    @Override
    public void destroy() {
        V view = mView;//todo 没有看懂为什么重新赋值了一次
        mView = null;
        if (view != null) {
            view.setPresenter(null);
        }
    }
}
