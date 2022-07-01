package com.zsf.toolbox.api;

import com.zsf.toolbox.bean.DogBean;
import com.zsf.toolbox.bean.Garbage;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by EWorld
 * 2022/6/10
 */
public interface GarbageApi {
    @GET("AppPrivacy/GarbageClassification.json")
    Call<Garbage> getGarbage();
}
