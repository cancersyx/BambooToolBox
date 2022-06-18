package com.zsf.toolbox.api;

import com.zsf.toolbox.bean.CommonExchangeRateBean;
import com.zsf.toolbox.bean.Currency;
import com.zsf.toolbox.constant.Constant;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by EWorld
 * 2022/5/28
 */
public interface CommonExchangeRateAPI {
    @GET("exchange/query?key="+Constant.KEY)
    Call<CommonExchangeRateBean> getCommonRateList();

}
