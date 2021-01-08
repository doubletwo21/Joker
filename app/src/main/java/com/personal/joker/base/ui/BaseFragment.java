package com.personal.joker.base.ui;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.personal.joker.R;

/**
 * A simple {@link Fragment} subclass.
 */
public abstract class BaseFragment extends Fragment {
    public Context mContext;
    public Activity mActivity;
    protected View mRoot;
    private Unbinder mBind;

    public BaseFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        initArgs(getActivity().getIntent().getExtras());
    }

    private void initArgs(Bundle bundle) {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        int contentLayoutId = getContentLayoutId();
        if (mRoot == null) {
            View view = inflater.inflate(contentLayoutId, container, false);
            initWidget(view);
            mContext = mActivity = getActivity();
            mRoot = view;
        } else {
            if (mRoot.getParent() != null) {
                ((ViewGroup) mRoot.getParent()).removeView(mRoot);
            }
        }
        return mRoot;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //当view创建完成后，初始化数据
        initData();
    }

    /**
     * 初始化数据
     */
    protected void initData() {

    }

    /**
     * 初始化控件
     *
     * @param view 根布局
     */
    private void initWidget(View view) {
        mBind = ButterKnife.bind(this, view);
    }

    /**
     * 返回按键调用
     *
     * @return 返回true表示finish我已处理返回逻辑，activity不用自己finish。
     * 如果返回false，代表我么有自己处理，activity自己走自己的逻辑
     */
    public boolean onBackPressed() {
        return false;
    }

    /**
     * 得到当前界面的资源文件ID
     *
     * @return 资源文件id
     */
    protected abstract int getContentLayoutId();
}
