package com.personal.joker.db;

import android.util.Log;

import com.google.gson.internal.$Gson$Types;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import okhttp3.Call;

/**
 * Created by Xiao_Wen.
 * User: Admin
 * Date: 2021/1/8
 * Time: 23:03
 *
 * @description:
 */
public abstract class BaseCallBack<T> {

    public Type mType;

    public BaseCallBack() {
        mType = getSuperClassTypeParameter(this.getClass());
        Log.e("type == ", mType.toString());
    }

    //Gson得到泛型的类型
    static Type getSuperClassTypeParameter(Class<?> subclass) {
        Type superclass = subclass.getGenericSuperclass();
        if (superclass instanceof Class) {
            return null;
        }
        ParameterizedType par = (ParameterizedType) superclass;
        return $Gson$Types.canonicalize(par.getActualTypeArguments()[0]);

    }

    public void onSuccess(T t) {

    }

    public void onError(int code) {

    }

    public void onFailure(Call call, IOException e) {

    }
}
