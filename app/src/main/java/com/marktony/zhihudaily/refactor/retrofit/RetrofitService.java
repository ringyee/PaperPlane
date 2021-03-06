package com.marktony.zhihudaily.refactor.retrofit;

import com.marktony.zhihudaily.refactor.data.DoubanMomentContent;
import com.marktony.zhihudaily.refactor.data.DoubanMomentNews;
import com.marktony.zhihudaily.refactor.data.GuokrHandpickNews;
import com.marktony.zhihudaily.refactor.data.ZhihuDailyContent;
import com.marktony.zhihudaily.refactor.data.ZhihuDailyNews;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by lizhaotailang on 2017/5/21.
 */

public interface RetrofitService {

    String ZHIHU_DAILY_BASE = "http://news.at.zhihu.com/api/4/news/";

    String DOUBAN_MOMENT_BASE = "https://moment.douban.com/api/";

    String GUOKR_HANDPICK_BASE = "http://apis.guokr.com/minisite/";

    interface ZhihuDailyService {

        @GET("before/{date}")
        Call<ZhihuDailyNews> getZhihuList(@Path("date") String date);

        @GET("{id}")
        Call<ZhihuDailyContent> getZhihuContent(@Path("id") String id);

    }

    interface DoubanMomentService {

        @GET("stream/date/{date}")
        Call<DoubanMomentNews> getDoubanList(@Path("date") String date);

        @GET("post/{id}")
        Call<DoubanMomentContent> getDoubanContent(@Path("id") String id);

    }

    interface GuokrHandpickService {

        @GET("article.json?retrieve_type=by_minisite&channel_key=")
        Call<GuokrHandpickNews> getGuokrHandpick(@Query("offset")int offset, @Query("limit")int limit );

    }

}
