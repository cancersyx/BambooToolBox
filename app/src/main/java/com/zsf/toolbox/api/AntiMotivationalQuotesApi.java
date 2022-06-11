package com.zsf.toolbox.api;

import com.zsf.toolbox.bean.AntiMotivationalQuotesBean;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by EWorld
 * 2022/6/9
 */
public interface AntiMotivationalQuotesApi {
    @GET("ws/api.php")
    Call<AntiMotivationalQuotesBean> getAntiMotivationalQuotes();
}
