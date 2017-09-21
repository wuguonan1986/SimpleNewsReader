package wu.victor.snr.business.viewmodels;

import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;

import com.google.gson.Gson;
import com.kelin.mvvmlight.base.ViewModel;
import com.kelin.mvvmlight.command.ReplyCommand;
import com.raizlabs.android.dbflow.sql.language.Select;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.List;

import cn.sharesdk.onekeyshare.OnekeyShare;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.http.GET;
import retrofit2.http.Url;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import wu.victor.snr.app.SnrApplication;
import wu.victor.snr.business.models.NewsFavorites;
import wu.victor.snr.business.models.NewsFavorites_Table;
import wu.victor.snr.business.models.NewsHistories;
import wu.victor.snr.business.models.NewsModel;

/**
 * Created by wuguonan on 2017/3/14 0014.
 */

public class NewsDetailViewModel implements ViewModel {

  NewsModel model;

  public String title;

  public final ObservableField<String> html = new ObservableField<>();

  public final ObservableBoolean favorited = new ObservableBoolean(false);

  public NewsDetailViewModel(NewsModel model) {
    this.model = model;
    title = model.getTitle();
  }

  public interface NewsDetailCssService {
    @GET
    Observable<String> getNewsDetail(@Url String url);
  }

  public void loadData() {
    Retrofit retrofit = new Retrofit.Builder()
        .baseUrl("http://m.baidu.com/")
        .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
        .addConverterFactory(new Converter.Factory() {
          @Override
          public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
            return new Converter<ResponseBody, String>() {
              @Override
              public String convert(ResponseBody value) throws IOException {
                return value.string();
              }
            };
          }
        }).build();
    retrofit.create(NewsDetailCssService.class).getNewsDetail(model.getUrl()).subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<String>() {
      @Override
      public void call(String s) {
        html.set(s);
      }
    });

    Observable.just(model).map(new Func1<NewsModel, Boolean>() {
      @Override
      public Boolean call(NewsModel newsModel) {
        List<NewsFavorites> list = new Select().from(NewsFavorites.class)
            .where(NewsFavorites_Table.uniquekey.eq(model.getUniquekey()))
            .orderBy(NewsFavorites_Table.date, false).queryList();
        return list != null && list.size() > 0;
      }
    }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Action1<Boolean>() {
          @Override
          public void call(Boolean aBoolean) {
            favorited.set(aBoolean);
          }
    });
  }

  public void saveHistory() {
    Observable.just(model).map(new Func1<NewsModel, NewsHistories>() {
      @Override
      public NewsHistories call(NewsModel newsModel) {
        Gson gson = new Gson();
        return gson.fromJson(gson.toJson(newsModel), NewsHistories.class);
      }
    }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Action1<NewsHistories>() {
          @Override
          public void call(NewsHistories newsModel) {
            newsModel.save();
          }
        });
  }

  public ReplyCommand onFavoriteClicked = new ReplyCommand(new Action0() {
    @Override
    public void call() {
      Gson gson = new Gson();
      NewsFavorites target = gson.fromJson(gson.toJson(model), NewsFavorites.class);
      if (favorited.get()) {
        target.delete();
        favorited.set(false);
      } else {
        target.save();
        favorited.set(true);
      }
    }
  });

  public ReplyCommand onShareClicked = new ReplyCommand(new Action0() {
    @Override
    public void call() {
      showShare();
    }
  });

  private void showShare() {
    OnekeyShare oks = new OnekeyShare();
    //关闭sso授权
    oks.disableSSOWhenAuthorize();
    // title标题，印象笔记、邮箱、信息、微信、人人网、QQ和QQ空间使用
    oks.setTitle(model.getTitle());
    // titleUrl是标题的网络链接，仅在Linked-in,QQ和QQ空间使用
//    oks.setTitleUrl("http://sharesdk.cn");
    // text是分享文本，所有平台都需要这个字段
    oks.setText(model.getTitle());
    //分享网络图片，新浪微博分享网络图片需要通过审核后申请高级写入接口，否则请注释掉测试新浪微博
//    oks.setImageUrl("http://f1.sharesdk.cn/imgs/2014/02/26/owWpLZo_638x960.jpg");
    // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
    //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
    // url仅在微信（包括好友和朋友圈）中使用
    oks.setUrl(model.getUrl());
    // comment是我对这条分享的评论，仅在人人网和QQ空间使用
//    oks.setComment("我是测试评论文本");
    // site是分享此内容的网站名称，仅在QQ空间使用
//    oks.setSite("ShareSDK");
    // siteUrl是分享此内容的网站地址，仅在QQ空间使用
//    oks.setSiteUrl("http://sharesdk.cn");

// 启动分享GUI
    oks.show(SnrApplication.getInstance());

//
//    Wechat.ShareParams wechatSp = new Wechat.ShareParams();
//    wechatSp.setTitle(model.getTitle());
//    wechatSp.setImageUrl(model.getThumbnail_pic_s());
//    wechatSp.setUrl(model.getUrl());
//    wechatSp.setTitle("text");
//    wechatSp.setShareType(Platform.SHARE_WEBPAGE);
//    Platform platform = ShareSDK.getPlatform(Wechat.NAME);
//    platform.share(wechatSp);
  }
}
