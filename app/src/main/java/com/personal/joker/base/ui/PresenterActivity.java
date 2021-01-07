package com.personal.joker.base.ui;


import com.personal.joker.common.contract.BaseContract;

public abstract class PresenterActivity<Presenter extends BaseContract.Presenter>
        extends BaseActivity implements BaseContract.View<Presenter> {

    protected Presenter mPresenter;

    public Presenter getPresenter() {
        return mPresenter;
    }

    public PresenterActivity() {
    }

    public PresenterActivity(Presenter presenter) {
        setPresenter(presenter);
    }

    @Override
    public void initWindows() {
        super.initWindows();
        initPresenter();
    }

    protected abstract Presenter initPresenter();

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
    public void setPresenter(Presenter presenter) {
        this.mPresenter = presenter;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter!=null){
            mPresenter = null;
        }
    }
}
