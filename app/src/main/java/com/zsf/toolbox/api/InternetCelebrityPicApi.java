package com.zsf.toolbox.api;


import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by EWorld
 * 2022/6/10
 */
public interface InternetCelebrityPicApi {
    @GET("/beibei_images.php")
    Call<ResponseBody> getInternetCelebrityPic();
}
