package wu.victor.snr.frameworks.retrofit;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;
import wu.victor.snr.business.models.NewsModel;

/**
 * Created by wuguonan on 2017/2/28 0028.
 */

public interface NewsDataService {

    public static final String KEY = "9596a121817c2c349f55e1014c954dff";

    @GET("/toutiao/index")
    public Observable<BaseResponseModel<NewsModel>> getNewsList(
        @Query("type") String type, @Query("key") String key);

}
