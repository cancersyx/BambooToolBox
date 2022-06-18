package com.zsf.toolbox.api;

import com.zsf.toolbox.bean.Currency;
import com.zsf.toolbox.bean.CurrentRate;
import com.zsf.toolbox.constant.Constant;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by EWorld
 * 2022/6/18
 * 实时汇率换算
 */
public interface CurrentRateApi {
    @GET("exchange/currency?key=" + Constant.KEY)
    Call<CurrentRate> getCurrentRate(@Query("from") String from, @Query("to") String to);
}
