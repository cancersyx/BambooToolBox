package com.zsf.toolbox.api;

import com.zsf.toolbox.bean.DogBean;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by EWorld
 * 2022/6/10
 */
public interface DogApi {
    @GET("breeds/image/random")
    Call<DogBean> getDog();
}
