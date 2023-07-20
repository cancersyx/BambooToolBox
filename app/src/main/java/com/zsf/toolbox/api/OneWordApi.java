package com.zsf.toolbox.api;

import com.zsf.toolbox.bean.OneWord;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by EWorld
 * 2022/6/9
 */
public interface OneWordApi {
    @GET("?c=d")
    Call<OneWord> getOneWord();
}
