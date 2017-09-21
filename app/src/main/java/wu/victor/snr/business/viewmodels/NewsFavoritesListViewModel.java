package wu.victor.snr.business.viewmodels;

import android.databinding.ObservableArrayList;
import android.util.Log;

import com.foxframe.segment.core.Segment;
import com.google.gson.Gson;
import com.kelin.mvvmlight.base.ViewModel;
import com.raizlabs.android.dbflow.sql.language.Select;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import wu.victor.snr.BR;
import wu.victor.snr.R;
import wu.victor.snr.business.models.NewsFavorites;
import wu.victor.snr.business.models.NewsFavorites_Table;
import wu.victor.snr.business.models.NewsModel;

/**
 * Created by wuguonan on 2017/3/15 0015.
 */

public class NewsFavoritesListViewModel {

  public ObservableArrayList<ViewModel> newsList = new ObservableArrayList<ViewModel>();

  private Segment segment;

  public int itemRes = R.layout.news_list_item_view;

  public int itemViewModelId = BR.itemViewModel;

  public NewsFavoritesListViewModel(Segment segment) {
    this.segment = segment;
  }

  private ObservableArrayList<ViewModel> getNewsList() {
    if (newsList == null) {
      newsList = new ObservableArrayList<ViewModel>();
    }
    return newsList;
  }

  public void loadDefaultData() {
    Observable.create(new Observable.OnSubscribe<List<NewsItemViewModel>>() {
      @Override
      public void call(Subscriber<? super List<NewsItemViewModel>> subscriber) {
        List<NewsFavorites> list =
            new Select().from(NewsFavorites.class)
                .orderBy(NewsFavorites_Table.date, false).queryList();
        List<NewsItemViewModel> items = new ArrayList<NewsItemViewModel>();
        for (NewsFavorites newsModel : list) {
          Log.e("wgn", newsModel.toString());
          Gson gson = new Gson();
          NewsModel model = gson.fromJson(gson.toJson(newsModel), NewsModel.class);
          items.add(new NewsItemViewModel(model, segment));
        }
        subscriber.onNext(items);
        subscriber.onCompleted();
      }
    }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(
        new Action1<List<NewsItemViewModel>>() {
          @Override
          public void call(List<NewsItemViewModel> newsModels) {
            getNewsList().clear();
            getNewsList().addAll(newsModels);
          }
        },
        new Action1<Throwable>() {
          @Override
          public void call(Throwable throwable) {
            throwable.printStackTrace();
          }
        }
    );
  }
}
