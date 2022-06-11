package com.zsf.toolbox.api;

import com.zsf.toolbox.bean.IpBean;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by EWorld
 * 2022/6/10
 */
public interface IpApi {
    @GET("?format=json")
    Call<IpBean> getIp();
}
