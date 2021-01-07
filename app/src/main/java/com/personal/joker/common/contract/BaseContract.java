package com.personal.joker.common.contract;

/**
 * Created by Xiao_Wen.
 * User: Admin
 * Date: 2021/1/7
 * Time: 22:22
 *
 * @description:
 */
public interface BaseContract {
    interface View<T extends Presenter> {
        void showLoading();

        void showError();

        void dismissLoading();

        void setPresenter(T t);
    }

    interface Presenter {
        void start();

        void destroy();
    }
}
