package com.personal.joker.base.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.personal.joker.R;
import com.zackratos.ultimatebarx.library.UltimateBarX;

import java.util.List;

public abstract class BaseActivity extends AppCompatActivity {

    public Context mContext;
    private Unbinder mBind;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initWindows();
        //如果传入的参数正确，进行布局配置
        if (initArgs(getIntent().getExtras())) {
            initStatusBars();
            setContentView(getContentLayoutId());
            mContext = this;
            initView();
            initWidgets();
            initData();

        } else
            finish();
    }

    public void initStatusBars() {
        UltimateBarX.with(this)
                .fitWindow(false)
                .color(Color.RED)
                .color(R.color.black)
                .light(false)
                .applyStatusBar();
    }

    public void initView() {

    }

    /**
     * 初始化数据
     */
    public void initData() {

    }

    /**
     * 初始化控件
     */
    private void initWidgets() {
        mBind = ButterKnife.bind(this);
    }

    public abstract int getContentLayoutId();

    /**
     * 初始化相关参数
     *
     * @param bundle 参数bundle
     * @return 如果参数正确，返回true ；错误返回false；
     */
    private boolean initArgs(Bundle bundle) {
        return true;
    }

    public void initWindows() {

    }

    /**
     * 当点击界面导航返回时，finish当前界面
     *
     * @return
     */
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //得到所有的fragment
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        if (fragments != null && fragments.size() > 0) {
            //遍历判断是否
            for (Fragment fragment : fragments) {
                //如果这个fragment是我们自己定义的类型
                if (fragment instanceof BaseFragment) {
                    //判断，是否处理了返回按钮,如果已经拦截了，直接return，不用处理
                    if (((BaseFragment) fragment).onBackPressed()) {
                        return;
                    }
                }
            }
        }
        super.onBackPressed();
        //没有拦截的话，finish；
        finish();
    }

    /**
     * 重写dispatchTouchEvent
     * 点击软键盘外面的区域关闭软键盘
     *
     * @param ev
     * @return
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            // 获得当前得到焦点的View，
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) {
                //根据判断关闭软键盘
                InputMethodManager imm = (InputMethodManager) getSystemService(
                        Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 判断用户点击的区域是否是输入框
     *
     * @param v
     * @param event
     * @return
     */
    private boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0], top = l[1], bottom = top + v.getHeight(), right = left
                    + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击EditText的事件，忽略它。
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

    @Override
    public Resources getResources() {
        Resources resources = super.getResources();
        if (resources.getConfiguration().fontScale != 1) { //fontScale不为1，需要强制设置为1
            Configuration newConfig = new Configuration();
            newConfig.setToDefaults();//设置成默认值，即fontScale为1
            resources.updateConfiguration(newConfig, resources.getDisplayMetrics());
        }
        return resources;
    }

    public void showShortToast(final String msg) {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
