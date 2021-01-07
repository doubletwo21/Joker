package com.personal.joker.mvp.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.personal.joker.R;
import com.personal.joker.base.ui.PresenterActivity;
import com.personal.joker.common.contract.IJokerContract;
import com.personal.joker.db.model.JokerModel;
import com.personal.joker.mvp.presenter.JokerPresenter;
import com.personal.joker.widget.ItemConfig;
import com.personal.joker.widget.ItemTouchHelperCallback;
import com.personal.joker.widget.OnSlideListener;
import com.personal.joker.widget.SlideLayoutManager;
import com.personal.joker.widget.SmileView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import jp.wasabeef.glide.transformations.BlurTransformation;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;

public class JokerActivity extends PresenterActivity<IJokerContract.Presenter>
        implements IJokerContract.View<List<JokerModel.ResultBean.DataBean>> {

    int[] bgs = {
            R.mipmap.img_slide_1,
            R.mipmap.img_slide_2,
            R.mipmap.img_slide_3,
            R.mipmap.img_slide_4,
            R.mipmap.img_slide_5,
            R.mipmap.skid_right_1,
            R.mipmap.skid_right_2,
            R.mipmap.skid_right_3,
            R.mipmap.skid_right_4,
            R.mipmap.skid_right_5,
            R.mipmap.skid_right_6,
            R.mipmap.skid_right_7,
            R.mipmap.img_slide_6
    };
    int page = 1;
    private RecyclerView mRecyclerView;
    private SmileView mSmileView;
    private List<JokerModel.ResultBean.DataBean> dataList = new ArrayList();
    private ItemTouchHelper mItemTouchHelper;
    private ItemTouchHelperCallback mItemTouchHelperCallback;
    private MyAdapter mAdapter;
    private int mLikeCount = 0;
    private int mDislikeCount = 0;

    @Override
    public int getContentLayoutId() {
        return R.layout.activity_joker;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void initView() {
        mRecyclerView = findViewById(R.id.recycler_view);
        mSmileView = findViewById(R.id.smile_view);
        mSmileView.setLike(mLikeCount);
        mSmileView.setDisLike(mDislikeCount);
        mAdapter = new MyAdapter();
        mRecyclerView.setAdapter(mAdapter);
        mItemTouchHelperCallback = new ItemTouchHelperCallback(mRecyclerView.getAdapter(), dataList);
        mItemTouchHelper = new ItemTouchHelper(mItemTouchHelperCallback);
        SlideLayoutManager slideLayoutManager = new SlideLayoutManager(mRecyclerView, mItemTouchHelper);
        mItemTouchHelper.attachToRecyclerView(mRecyclerView);
        mRecyclerView.setLayoutManager(slideLayoutManager);
        mItemTouchHelperCallback.setOnSlideListener(new OnSlideListener() {
            @Override
            public void onSliding(RecyclerView.ViewHolder viewHolder, float ratio, int direction) {

            }

            @Override
            public void onSlided(RecyclerView.ViewHolder viewHolder, Object o, int direction) {
                if (direction == ItemConfig.SLIDED_LEFT) {
                    mDislikeCount++;
                    mSmileView.setDisLike(mDislikeCount);
                    mSmileView.disLikeAnimation();
                } else if (direction == ItemConfig.SLIDED_RIGHT) {
                    mLikeCount++;
                    mSmileView.setLike(mLikeCount);
                    mSmileView.likeAnimation();
                }
            }

            @Override
            public void onClear() {
                page++;
                String time = System.currentTimeMillis() + "";
                String substring = time.substring(0, 10);
                mPresenter.getData(substring, page + "1", "20", "desc");
            }
        });
    }

    @Override
    public void initData() {
        super.initData();
        String time = System.currentTimeMillis() + "";
        String substring = time.substring(0, 10);
        mPresenter.getData(substring, page + "", "20", "desc");
    }


    @Override
    public void onDataSuccess(List<JokerModel.ResultBean.DataBean> dataBeans) {
        dataList.addAll(dataBeans);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDataFailed() {
        showShortToast("数据获取失败");
    }

    @Override
    public void onDataNoMore(String str) {
        showShortToast(str);
    }


    @Override
    protected IJokerContract.Presenter initPresenter() {
        return new JokerPresenter(this);
    }


    class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_slide, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            JokerModel.ResultBean.DataBean bean = dataList.get(position);
            int i1 = (int) (Math.random() * 100);
            int i = i1 / 13;
            Glide.with(mContext)
                    .load(bgs[i])
                    .apply(bitmapTransform(new BlurTransformation(10, 8)).centerCrop())
                    .into(holder.imgBg);
            holder.tvTitle.setText(bean.getContent());
        }

        @Override
        public int getItemCount() {
            return dataList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            ImageView imgBg;
            TextView tvTitle;

            public ViewHolder(View itemView) {
                super(itemView);
                imgBg = itemView.findViewById(R.id.img_bg);
                tvTitle = itemView.findViewById(R.id.tv_title);
            }
        }
    }
}
