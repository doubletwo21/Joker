package com.personal.joker.common.contract;

/**
 * Created by Xiao_Wen.
 * User: Admin
 * Date: 2021/1/7
 * Time: 23:12
 *
 * @description:
 */
public interface IJokerContract {

    interface View<T> extends BaseContract.View<Presenter> {
        /**
         * 数据成功返回
         *
         * @param t 泛型
         */
        void onDataSuccess(T t);

        /**
         * 数据获取失败
         */
        void onDataFailed();

        /**
         * 没有更多数据
         */
        void onDataNoMore(String str);
    }

    interface Presenter extends BaseContract.Presenter {
        /**
         * 获取笑话数据
         *
         * @param time     时间戳
         * @param page     第几页
         * @param pageSize 每页返回的数据条数
         * @param sort     升降序
         */
        void getData(String time, String page, String pageSize, String sort);
    }
}
