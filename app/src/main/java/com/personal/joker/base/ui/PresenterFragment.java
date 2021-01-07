package com.personal.joker.base.ui;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.personal.joker.common.contract.BaseContract;

/**
 * A simple {@link Fragment} subclass.
 */
public abstract class PresenterFragment<Presenter extends BaseContract.Presenter>
        extends BaseFragment implements BaseContract.View<Presenter> {

    public Presenter mPresenter;

    public PresenterFragment(Presenter presenter) {
        setPresenter(presenter);
    }

    public PresenterFragment() {
    }

    public Presenter getPresenter() {
        return mPresenter;
    }

    /**
     * 将本身这个View和Presenter进行绑定
     *
     * @param presenter presenter
     */
    @Override
    public void setPresenter(Presenter presenter) {
        this.mPresenter = presenter;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        initPresenter();
    }

    /**
     * 所有的子类，都会实现这个方法，在这个方法里会进行
     * new Presenter(View view);
     * 此时，new Presenter(View view)的时候，
     * BasePresenter构造函数中的SetView()又将view和presenter绑定了
     *
     * @return presenter
     */
    public abstract Presenter initPresenter();

    @Override
    public void showLoading() {

    }

    @Override
    public void showError() {

    }

    @Override
    public void dismissLoading() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter = null;
        }
    }
}
