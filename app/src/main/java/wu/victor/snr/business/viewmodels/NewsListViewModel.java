package wu.victor.snr.business.viewmodels;

import android.databinding.ObservableArrayList;
import android.databinding.ObservableBoolean;
import android.util.Log;

import com.foxframe.segment.core.Segment;
import com.kelin.mvvmlight.base.ViewModel;
import com.kelin.mvvmlight.command.ReplyCommand;
import com.raizlabs.android.dbflow.sql.language.Select;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import wu.victor.snr.BR;
import wu.victor.snr.R;
import wu.victor.snr.business.segment.MyHistorySegment;
import wu.victor.snr.frameworks.database.store.DatabaseUtils;
import wu.victor.snr.frameworks.retrofit.BaseResponseModel;
import wu.victor.snr.frameworks.retrofit.NewsDataService;
import wu.victor.snr.frameworks.retrofit.RetrofitProvider;
import wu.victor.snr.business.constants.NewsTypes;
import wu.victor.snr.business.models.NewsModel;
import wu.victor.snr.business.models.NewsModel_Table;
import wu.victor.snr.business.segment.MyFavoriteSegment;
import wu.victor.snr.frameworks.utils.ToastUtils;

/**
 * Created by wuguonan on 2017/3/3 0003
 */

public class NewsListViewModel {

  public ObservableArrayList<ViewModel> newsList = new ObservableArrayList<ViewModel>();

  public ArrayList<String> keyList = new ArrayList<>();

  public NewsTypes.Types type;

  private Segment segment;

  public int itemRes = R.layout.news_list_item_view;

  public int itemViewModelId = BR.itemViewModel;

  public final ObservableBoolean isRefreshing = new ObservableBoolean(false);

  public NewsListViewModel(NewsTypes.Types type, Segment segment) {
    this.type = type;
    this.segment = segment;
  }

  private ObservableArrayList<ViewModel> getNewsList() {
    if (newsList == null) {
      newsList = new ObservableArrayList<ViewModel>();
    }
    return newsList;
  }

  public final ReplyCommand onRefreshCommand = new ReplyCommand(new Action0() {
    @Override
    public void call() {
      loadData();
    }
  });
  public final ReplyCommand<Integer> onLoadMoreCommand =
      new ReplyCommand<Integer>(new Action1<Integer>() {
    @Override
    public void call(Integer integer) {
        loadData();
    }
  });

  public void loadDefaultData() {
    if (getNewsList().size() > 0) {
      return;
    }
    Observable.create(new Observable.OnSubscribe<List<NewsItemViewModel>>() {
      @Override
      public void call(Subscriber<? super List<NewsItemViewModel>> subscriber) {
        List<NewsModel> list =
            new Select().from(NewsModel.class).where(NewsModel_Table.type.eq(type.getQueryParam()))
                .orderBy(NewsModel_Table.date, false).queryList();
        List<NewsItemViewModel> items = new ArrayList<NewsItemViewModel>();
        List<String> keys = new ArrayList<String>();
        for (NewsModel newsModel : list) {
          Log.e("wgn", newsModel.toString());
          items.add(new NewsItemViewModel(newsModel, segment));
          if (!keyList.contains(newsModel.getUniquekey())) {
            newsModel.setType(type.getQueryParam());
            keys.add(newsModel.getUniquekey());
          }
        }
        keyList.addAll(keys);
        subscriber.onNext(items);
        subscriber.onCompleted();
      }
    }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(
        new Action1<List<NewsItemViewModel>>() {
          @Override
          public void call(List<NewsItemViewModel> newsModels) {
            if (newsModels.size() > 0) {
              getNewsList().clear();
              getNewsList().addAll(newsModels);
            } else {
              loadData();
            }
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

  public void loadData() {
    try {
      isRefreshing.set(true);
      Observable<BaseResponseModel<NewsModel>> observable
          = RetrofitProvider.getInstance().create(NewsDataService.class).getNewsList(
          type.getQueryParam(), NewsDataService.KEY);

      observable.subscribeOn(Schedulers.io())
          .map(new Func1<BaseResponseModel<NewsModel>,
              List<NewsItemViewModel>>() {
            @Override
            public List<NewsItemViewModel> call(
                BaseResponseModel<NewsModel> newsModelBaseResponseModel) {
              List<NewsModel> list = newsModelBaseResponseModel.getResult().getData();
              List<NewsItemViewModel> items = new ArrayList<NewsItemViewModel>();
              List<String> keys = new ArrayList<String>();
              List<NewsModel> filtered = new ArrayList<NewsModel>();
              for (NewsModel newsModel : list) {
                Log.e("wgn", newsModel.toString());
                if (!keyList.contains(newsModel.getUniquekey())) {
                  newsModel.setType(type.getQueryParam());
                  items.add(new NewsItemViewModel(newsModel, segment));
                  keys.add(newsModel.getUniquekey());
                  filtered.add(newsModel);
                }
              }
              DatabaseUtils.saveToDb(filtered);
              keyList.addAll(keys);
              for (int i = 0; i < getNewsList().size(); i++) {
                items.add((NewsItemViewModel) getNewsList().get(i));
              }
              return items;
            }
          }).observeOn(AndroidSchedulers.mainThread()).subscribe(
          new Action1<List<NewsItemViewModel>>() {
            @Override
            public void call(List<NewsItemViewModel> items) {
              getNewsList().clear();
              getNewsList().addAll(items);
              isRefreshing.set(false);
            }
          },
          new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
              throwable.printStackTrace();
              isRefreshing.set(false);
              ToastUtils.showToast("网络连接失败！");
            }
          });
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public ReplyCommand onFavoriteClicked = new ReplyCommand(new Action0() {
    @Override
    public void call() {
      MyFavoriteSegment target = new MyFavoriteSegment();
      MyFavoriteSegment.attachToCurrentStack(segment, target);
    }
  });

  public ReplyCommand onHistoryClicked = new ReplyCommand(new Action0() {
    @Override
    public void call() {
      MyHistorySegment target = new MyHistorySegment();
      MyHistorySegment.attachToCurrentStack(segment, target);
    }
  });

}
