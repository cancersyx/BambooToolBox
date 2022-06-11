package com.zsf.toolbox;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.zsf.toolbox.api.ComicApi;
import com.zsf.toolbox.api.TaoBaoApi;
import com.zsf.toolbox.constant.Constant;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by EWorld
 * 2022/6/10
 */
public class ComicImgActivity extends AppCompatActivity {
    private static final String TAG = "TaoBaoActivity";
    private ImageView mBackIv;
    private TextView mTitleTv;
    private ImageView mComicPic;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comic_img);

        mBackIv = findViewById(R.id.iv_back);
        mTitleTv = findViewById(R.id.tv_title);
        mComicPic = findViewById(R.id.iv_pic);
        mProgressBar = findViewById(R.id.progress_bar);
        mProgressBar.setVisibility(View.VISIBLE);

        mTitleTv.setText("二次元");

        mBackIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        initData();
    }

    private void initData() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.API_COMIC_IMG)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ComicApi comicApi = retrofit.create(ComicApi.class);
        Call call = comicApi.getComicPic();
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                try {
                    ResponseBody responseBody = (ResponseBody) response.body();
                    if (responseBody == null) return;
                    Bitmap bitmap = BitmapFactory.decodeStream(responseBody.byteStream());
                    mComicPic.setImageBitmap(bitmap);
                    mProgressBar.setVisibility(View.GONE);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {

            }
        });
    }

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, ComicImgActivity.class);
        context.startActivity(intent);
    }
}
