package com.zsf.toolbox.api;

import com.zsf.toolbox.bean.CarBrandBean;
import com.zsf.toolbox.constant.Constant;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by EWorld
 * 2022/11/21
 */
public interface CarBrandApi {
    @GET("brand?key=" + Constant.CAR_KEY)
    Call<CarBrandBean> getCarBrand(@Query("first_letter") String first_letter);
}
