package com.zsf.toolbox.api;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by EWorld
 * 2022/6/9
 */
public interface SimpApi {
    @GET("tgrj/index.php")
    Call<String> getSimpDiary();
}
