package com.zsf.toolbox.api;

import com.zsf.toolbox.bean.SimpDog;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by EWorld
 * 2022/6/9
 */
public interface SimpApi {
    @GET("api/dog")
    Call<SimpDog> getSimpDiary();
}
